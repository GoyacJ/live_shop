package vip.xiaonuo.shop.modular.logistics.service.impl;

import api.logistics.common.constants.LogisticsConstants;
import api.logistics.dto.InstantQueryDTO;
import api.logistics.dto.OrderLogisticsInfoDTO;
import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.shop.modular.logistics.dto.ImportLogisticsDTO;
import vip.xiaonuo.shop.modular.logistics.entity.OrderLogistics;
import vip.xiaonuo.shop.modular.logistics.excel.ImportLogisticsListener;
import vip.xiaonuo.shop.modular.logistics.mapper.OrderLogisticsMapper;
import vip.xiaonuo.shop.modular.logistics.param.OrderLogisticsSettingParam;
import vip.xiaonuo.shop.modular.logistics.service.LogisticsService;
import vip.xiaonuo.shop.modular.logistics.service.OrderLogisticsService;
import vip.xiaonuo.shop.modular.mallorder.entity.MallOrder;
import vip.xiaonuo.shop.modular.mallorder.service.MallOrderService;

import java.util.Objects;

import static api.logistics.common.constants.LogisticsConstants.Logistics_CACHE_PREFIX;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/27 09:55
 */
@Slf4j
@Component
public class OrderLogisticsServiceImpl extends ServiceImpl<OrderLogisticsMapper, OrderLogistics> implements OrderLogisticsService {

    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Inject
    private LogisticsService logisticsService;

    @Inject
    private MallOrderService mallOrderService;

    @Override
    @Tran
    public boolean setting(OrderLogisticsSettingParam param) {

        // 先实时查询 如果已签收则使用实时查询,如果未完成则进行轨迹订阅
        if (instantQuery(param) || subscription(param)) {
            /*
                更新订单信息
            */
            return updateOrderInfo(param);
        }
        return false;
    }

    /**
     * 实时查询
     *
     * @param param 参数
     * @return 结果
     */
    private boolean instantQuery(OrderLogisticsSettingParam param) {
        InstantQueryDTO dto = logisticsService.instantQuery(param.shipper(), param.logisticCode(), param.customerName(), param.orderCode());

        // 已完成则使用此方式
        if (dto.success()) {
            commonCacheOperator.put(LogisticsConstants.Logistics_CACHE_PREFIX + dto.orderCode() + dto.logisticCode(), OrderLogisticsInfoDTO.of(dto), 2592000);

            boolean res;
            // 更新
            if (this.exists(QueryWrapper.create().eq(OrderLogistics::getOrderCode, param.orderCode()))) {
                res = UpdateChain.of(OrderLogistics.class)
                        .set(OrderLogistics::getLogisticCode, param.logisticCode())
                        .set(OrderLogistics::getShipper, param.shipper())
                        .set(OrderLogistics::getCustomerName, param.customerName())
                        .set(OrderLogistics::getState, dto.state())
                        .set(OrderLogistics::getStateEx, dto.stateEx())
                        .set(OrderLogistics::getLocation, dto.location())
                        .set(OrderLogistics::getNextCity, dto.nextCity())
                        .set(OrderLogistics::getDeliveryMan, dto.deliveryMan())
                        .set(OrderLogistics::getDeliveryManTel, dto.deliveryManTel())
                        .where(OrderLogistics::getOrderCode).eq(param.orderCode())
                        .update();
            } else {
                OrderLogistics orderLogistics = new OrderLogistics();
                orderLogistics.setId(IdUtil.getSnowflakeNextIdStr());
                orderLogistics.setOrderCode(param.orderCode());
                orderLogistics.setLogisticCode(param.logisticCode());
                orderLogistics.setShipper(param.shipper());
                orderLogistics.setCustomerName(param.customerName());
                orderLogistics.setState(dto.state());
                orderLogistics.setStateEx(dto.stateEx());
                orderLogistics.setLocation(dto.location());
                orderLogistics.setNextCity(dto.nextCity());
                orderLogistics.setDeliveryMan(dto.deliveryMan());
                orderLogistics.setDeliveryManTel(dto.deliveryManTel());
                res = this.save(orderLogistics);
            }
            return res;
        } else {
            return false;
        }
    }

    @Override
    public OrderLogisticsInfoDTO detail(String orderNumber) {
        OrderLogistics orderLogistics = this.getOne(QueryWrapper.create().eq(OrderLogistics::getOrderCode, orderNumber));
        if (Objects.isNull(orderLogistics)) {
            return null;
        }

        OrderLogisticsInfoDTO cache = commonCacheOperator
                .getCache(Logistics_CACHE_PREFIX + orderLogistics.getOrderCode() + orderLogistics.getLogisticCode(), OrderLogisticsInfoDTO.class);
        log.info("[物流信息] --- |物流| --- 从缓存中获取快递信息:{}", cache);

        if (Objects.isNull(cache)) {
            OrderLogisticsInfoDTO dto = new OrderLogisticsInfoDTO();
            dto.setSuccess(true);
            dto.setOrderCode(orderNumber);
            dto.setLogisticCode(orderLogistics.getLogisticCode());
            dto.setShipperCode(orderLogistics.getShipper());
            dto.setState(orderLogistics.getState());
            dto.setStateEx(orderLogistics.getStateEx());
            dto.setLocation(orderLogistics.getLocation());
            dto.setNextCity(orderLogistics.getNextCity());
            dto.setDeliveryMan(orderLogistics.getDeliveryMan());
            dto.setDeliveryManTel(orderLogistics.getDeliveryManTel());
            return dto;
        }

        return cache;
    }

    @Override
    public boolean importLogistics(UploadedFile file) {
        try {
            EasyExcelFactory.read(file.getContent(), ImportLogisticsDTO.class, new ImportLogisticsListener())
                    .sheet()
                    .headRowNumber(2)
                    .doRead();
            return true;
        } catch (Exception e) {
            log.error("物流订单信息导入失败", e);
            return false;
        }
    }

    /**
     * 订阅方式
     *
     * @param param 请求参数
     * @return 结果
     */
    private boolean subscription(OrderLogisticsSettingParam param) {
        // 订阅
        if (!logisticsService.trajectorySubscription(param.shipper(), param.logisticCode(), param.customerName(), param.orderCode())) {
            log.error("物流订阅失败,快递公司:{},快递单号:{},收件人手机号:{},订单编号:{}",
                    param.shipper(),
                    param.logisticCode(),
                    param.customerName(),
                    param.orderCode());
            return false;
        }

        boolean res;

        // 更新
        if (this.exists(QueryWrapper.create().eq(OrderLogistics::getOrderCode, param.orderCode()))) {
            res = UpdateChain.of(OrderLogistics.class)
                    .set(OrderLogistics::getLogisticCode, param.logisticCode())
                    .set(OrderLogistics::getShipper, param.shipper())
                    .set(OrderLogistics::getCustomerName, param.customerName())
                    .where(OrderLogistics::getOrderCode).eq(param.orderCode())
                    .update();
        } else {
            OrderLogistics orderLogistics = new OrderLogistics();
            orderLogistics.setId(IdUtil.getSnowflakeNextIdStr());
            orderLogistics.setOrderCode(param.orderCode());
            orderLogistics.setLogisticCode(param.logisticCode());
            orderLogistics.setShipper(param.shipper());
            orderLogistics.setCustomerName(param.customerName());
            res = this.save(orderLogistics);
        }
        return res;
    }

    private boolean updateOrderInfo(OrderLogisticsSettingParam param) {
        return UpdateChain.of(MallOrder.class)
                .set(MallOrder::getStatus, "2")
                .set(MallOrder::getLogisticsNumber, param.logisticCode())
                .where(MallOrder::getOrderNumber).eq(param.orderCode())
                .update();
    }
}
