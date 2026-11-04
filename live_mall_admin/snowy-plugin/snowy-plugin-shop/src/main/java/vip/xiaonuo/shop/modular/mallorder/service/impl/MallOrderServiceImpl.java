/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.shop.modular.mallorder.service.impl;

import api.order.dto.CreateIntegralOrderDTO;
import api.order.dto.CreateMallOrderDTO;
import api.order.dto.CreateOrderDTO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.*;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import manage.UserApi;
import manage.dto.UserInfoDTO;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.OrderStatusEnum;
import vip.xiaonuo.common.enums.OrderTypeEnum;
import vip.xiaonuo.common.enums.OrderTypeMapperEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.common.util.CommonDownloadUtil;
import vip.xiaonuo.integral.api.IntegralApi;
import vip.xiaonuo.integral.api.dto.ChangeIntegralDTO;
import vip.xiaonuo.integral.api.enums.IntegralOperationSource;
import vip.xiaonuo.integral.api.enums.IntegralSource;
import vip.xiaonuo.live.api.live.LiveApi;
import vip.xiaonuo.live.api.live.dto.LiveBizDTO;
import vip.xiaonuo.live.api.live.dto.ShoppingCarDTO;
import vip.xiaonuo.shop.modular.logistics.entity.OrderLogistics;
import vip.xiaonuo.shop.modular.logistics.service.OrderLogisticsService;
import vip.xiaonuo.shop.modular.mallgoods.entity.MallGoods;
import vip.xiaonuo.shop.modular.mallgoods.service.MallGoodsService;
import vip.xiaonuo.shop.modular.mallintegralgoods.entity.MallIntegralGoods;
import vip.xiaonuo.shop.modular.mallintegralgoods.service.MallIntegralGoodsService;
import vip.xiaonuo.shop.modular.mallorder.entity.MallOrder;
import vip.xiaonuo.shop.modular.mallorder.excel.ExportOrderDTO;
import vip.xiaonuo.shop.modular.mallorder.excel.OrderReportStyleStrategy;
import vip.xiaonuo.shop.modular.mallorder.excel.OrderSummaryWriteHandler;
import vip.xiaonuo.shop.modular.mallorder.mapper.MallOrderMapper;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderAddParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderEditParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderIdParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderPageParam;
import vip.xiaonuo.shop.modular.mallorder.service.MallOrderService;
import vip.xiaonuo.shop.modular.mallordergoods.entity.MallOrderGoods;
import vip.xiaonuo.shop.modular.mallordergoods.param.MallOrderGoodsIdsParam;
import vip.xiaonuo.shop.modular.mallordergoods.service.MallOrderGoodsService;
import vip.xiaonuo.shop.modular.mallthirdpaymen.entity.MallThirdPayment;
import vip.xiaonuo.shop.modular.mallthirdpaymen.service.MallThirdPaymentService;
import vip.xiaonuo.task.api.TaskApi;
import vip.xiaonuo.task.api.enums.TaskBusinessCategory;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 商城-订单Service接口实现类
 **/
@Tran
@Slf4j
@Component
public class MallOrderServiceImpl extends ServiceImpl<MallOrderMapper, MallOrder> implements MallOrderService {
    @Inject
    private MallOrderGoodsService mallOrderGoodsService;

    @Inject
    private MallGoodsService mallGoodsService;

    @Inject
    private LiveApi liveApi;

    @Inject
    private UserApi userApi;

    @Inject
    private MallThirdPaymentService mallThirdPaymentService;

    @Inject
    private OrderLogisticsService orderLogisticsService;

    @Inject
    private MallIntegralGoodsService mallIntegralGoodsService;

    @Inject
    private IntegralApi integralApi;

    @Inject
    private TaskApi taskApi;

    @Override
    public Page<MallOrder> page(MallOrderPageParam mallOrderPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(mallOrderPageParam.getOrderNumber())) {
            queryWrapper.like(MallOrder::getOrderNumber, mallOrderPageParam.getOrderNumber());
        }

        if (ObjectUtil.isNotEmpty(mallOrderPageParam.getOrderType())) {
            queryWrapper.eq(MallOrder::getOrderType, mallOrderPageParam.getOrderType());
        }

        if (ObjectUtil.isNotEmpty(mallOrderPageParam.getCreateUser())) {
            queryWrapper.eq(MallOrder::getCreateUser, mallOrderPageParam.getCreateUser());
        }

        if (StrUtil.isNotBlank(mallOrderPageParam.getLiveName())) {
            queryWrapper.and(q -> {
                q.where(MallOrder::getOrderSourceId).in(
                        new QueryWrapper().select("live_biz.id").from("live_biz").like("main_title", mallOrderPageParam.getLiveName())
                );
            });
        }

        if (ObjectUtil.isNotEmpty(mallOrderPageParam.getStartCreateTime()) && ObjectUtil.isNotEmpty(mallOrderPageParam.getEndCreateTime())) {
            queryWrapper.between(MallOrder::getCreateTime, mallOrderPageParam.getStartCreateTime(), mallOrderPageParam.getEndCreateTime());
        } else {
            queryWrapper.orderBy(MallOrder::getCreateTime, false);
        }

        /*
            增加筛选条件: 订单状态、下单手机号
         */
        if (StringUtils.isNotEmpty(mallOrderPageParam.getStatus())) {
            queryWrapper.eq(MallOrder::getStatus, mallOrderPageParam.getStatus());
        }

        if (StringUtils.isNotEmpty(mallOrderPageParam.getMobile())) {
            queryWrapper.eq(MallOrder::getMobile, mallOrderPageParam.getMobile());
        }

        Page<MallOrder> pageList = this.page(CommonPageRequest.defaultPage(), queryWrapper);
/*        List<MallOrder>  list = pageList.getRecords();
        MallOrderGoodsIdsParam mallOrderGoodsIdsParam = new  MallOrderGoodsIdsParam();
        for (MallOrder  mallOrder : list) {
            //查询mall_order_goods表中的数据进行拼接
            mallOrderGoodsIdsParam.setOrderId(mallOrder.getId());
            List<MallOrderGoods> mallOrderGoodsList = mallOrderGoodsService.getOrderGoodsListByOrderId(mallOrderGoodsIdsParam);
            BigDecimal  orderFee = new BigDecimal(0);
            for (MallOrderGoods mallOrderGoods : mallOrderGoodsList) {
                orderFee = orderFee.add(BigDecimal.valueOf(mallOrderGoods.getGoodsPrice()));
            }
            mallOrder.setOrderFee(orderFee.toString());
        }*/
        return pageList;
    }

    @Tran
    @Override
    public void add(MallOrderAddParam mallOrderAddParam) {
        if (ObjectUtil.isEmpty(mallOrderAddParam)) {
            throw new CommonException("商城-订单信息不能为空");
        }
        try {
            MallOrder mallOrder = BeanUtil.toBean(mallOrderAddParam, MallOrder.class);
            this.save(mallOrder);
        } catch (Exception e) {
            throw new CommonException("商城-订单信息添加失败");
        }
    }

    @Override
    @Tran
    public String createMallOrder(CreateMallOrderDTO param) {
        // 1. 扣减库存
        if (!mallGoodsService.deductionMallGoodsStock(param.goodsId(), param.goodsNum())) {
            throw new CommonException("库存不足，无法下单");
        }

        CreateOrderDTO createParam = new CreateOrderDTO();
        createParam.setCreateUserId(param.createUserId());
        createParam.setPid(param.pid());
        createParam.setSourceId(param.sourceId());
        createParam.setRemark(param.remark());
        createParam.setGoodsId(param.goodsId());
        createParam.setGoodsNum(param.goodsNum());
        createParam.setMobile(param.mobile());
        createParam.setNickname(param.nickname());
        createParam.setProvinceName(param.provinceName());
        createParam.setCityName(param.cityName());
        createParam.setCountiesName(param.countiesName());
        createParam.setAddress(param.address());

        createParam.setOrderType(OrderTypeMapperEnum.SC);
        MallGoods mallGoods = mallGoodsService.getById(param.goodsId());
        if (Objects.isNull(mallGoods)) {
            throw new CommonException("商城-商品不存在");
        }

        // 获取商品价格
        Double price = mallGoods.getPrice();

        // 计算订单总价
        BigDecimal orderTotalPrice = NumberUtil.round(NumberUtil.mul(price, param.goodsNum()), 2);
        createParam.setOrderFee(orderTotalPrice);

        // 2. 创建订单
        return createOrder(createParam);
    }

    @Override
    public String createIntegralOrder(CreateIntegralOrderDTO param) {
        // 校验限制
        MallIntegralGoods mallIntegralGoods = mallIntegralGoodsService.getById(param.goodsId());
        if (Objects.isNull(mallIntegralGoods)) {
            throw new CommonException("商城-积分商品不存在");
        }

        List<MallOrderGoods> mallOrderGoods = this.mallOrderGoodsService.list(
                new QueryWrapper().eq(MallOrderGoods::getCreateUser, param.createUserId())
                        .eq(MallOrderGoods::getGoodsId, param.goodsId())
        );

        long count = mallOrderGoods.stream().filter(orderGoods -> this.exists(new QueryWrapper()
                .eq(MallOrder::getId, orderGoods.getOrderId())
                .eq(MallOrder::getOrderType, OrderTypeMapperEnum.JF.getKey())
                .eq(MallOrder::getStatus, OrderStatusEnum.COMPLETED.getStatus()))).count();

        if (Objects.nonNull(mallIntegralGoods.getUserLimit())
                && mallIntegralGoods.getUserLimit() > 0
                && mallIntegralGoods.getUserLimit() < (count + param.goodsNum())) {
            throw new CommonException("已达到最大兑换次数");
        }

        // 扣减库存
        if (!mallIntegralGoodsService.deductionMallGoodsStock(param.goodsId(), param.goodsNum())) {
            throw new CommonException("库存不足，无法下单");
        }

        CreateOrderDTO createParam = new CreateOrderDTO();
        createParam.setCreateUserId(param.createUserId());
        createParam.setPid(param.pid());
        createParam.setSourceId(param.sourceId());
        createParam.setRemark(param.remark());
        createParam.setGoodsId(param.goodsId());
        createParam.setGoodsNum(param.goodsNum());
        createParam.setMobile(param.mobile());
        createParam.setNickname(param.nickname());
        createParam.setProvinceName(param.provinceName());
        createParam.setCityName(param.cityName());
        createParam.setCountiesName(param.countiesName());
        createParam.setAddress(param.address());

        createParam.setOrderType(OrderTypeMapperEnum.JF);
        MallIntegralGoods integralGoods = mallIntegralGoodsService.getById(param.goodsId());
        if (Objects.isNull(integralGoods)) {
            throw new CommonException("商城-商品不存在");
        }

        // 获取商品价格
        Integer price = integralGoods.getIntegral();

        // 计算订单总价
        BigDecimal orderTotalPrice = NumberUtil.round(NumberUtil.mul(price, param.goodsNum()), 2);
        createParam.setOrderFee(orderTotalPrice);

        // 2. 创建订单
        String orderId = createOrder(createParam);

        integralApi.changeIntegral(
                ChangeIntegralDTO.decreaseIntegral(
                        param.createUserId(),
                        orderTotalPrice,
                        param.createUserId(),
                        IntegralSource.EXCHANGE,
                        orderId,
                        IntegralOperationSource.USER,
                        "商城-积分兑换"
                )
        );

        return orderId;
    }

    @Override
    @Tran
    public String createOrder(CreateOrderDTO param) {
        if (Objects.isNull(param)) {
            throw new CommonException("商城-订单信息不能为空");
        }

        MallOrder mallOrder = new MallOrder();
        mallOrder.setId(IdUtil.getSnowflakeNextIdStr());
        mallOrder.setCreateUser(param.getCreateUserId());
        mallOrder.setRemark(param.getRemark());
        mallOrder.setOrderSourceId(param.getSourceId());
        mallOrder.setPid(param.getPid());

        // 默认商城
        mallOrder.setOrderType(param.getOrderType().getKey());

        mallOrder.setOrderFee(param.getOrderFee());
        mallOrder.setOrderNumber(OrderTypeMapperEnum.createOrderNumber(param.getOrderType()));
        if (OrderTypeMapperEnum.JF.equals(param.getOrderType())) {
            mallOrder.setStatus(String.valueOf(OrderStatusEnum.PAID.getStatus()));
        } else {
            mallOrder.setStatus(String.valueOf(OrderStatusEnum.PENDING_PAYMENT.getStatus()));
        }

        Dict payRequestVo = new Dict();
        payRequestVo.put("orderType", mallOrder.getOrderType());
        mallOrder.setAttach(JSONUtil.toJsonStr(payRequestVo));

        // 收货人信息
        mallOrder.setMobile(param.getMobile());
        mallOrder.setNickname(param.getNickname());
        mallOrder.setProvinceName(param.getProvinceName());
        mallOrder.setCityName(param.getCityName());
        mallOrder.setCountiesName(param.getCountiesName());
        mallOrder.setAddress(mallOrder.getProvinceName() + mallOrder.getCityName() + mallOrder.getCountiesName() + param.getAddress());

        if (this.save(mallOrder)) {
            // 创建订单商品
            if (OrderTypeMapperEnum.JF.equals(param.getOrderType())) {
                this.mallOrderGoodsService.createMallOrderIntegralGoods(
                        param.getCreateUserId(),
                        mallOrder.getId(),
                        param.getGoodsNum(),
                        param.getGoodsId()
                );
            } else {
                this.mallOrderGoodsService.createMallOrderGoods(
                        param.getCreateUserId(),
                        mallOrder.getId(),
                        param.getGoodsNum(),
                        param.getGoodsId()
                );


            }
            return mallOrder.getId();
        }

        throw new CommonException("商城-订单信息添加失败");
    }


    @Override
    @Tran
    public void edit(MallOrderEditParam mallOrderEditParam) {
        MallOrder mallOrder = this.getById(mallOrderEditParam.getId());
        if (ObjectUtil.isEmpty(mallOrder)) {
            throw new CommonException("商城-订单不存在，id值为：{}", mallOrderEditParam.getId());
        }
        if (!mallOrder.getStatus().equals("1") && !StrUtil.isEmpty(mallOrderEditParam.getLogisticsNumber())) {
            throw new CommonException("当前状态无法发货");
        }

        //如果状态物流编号不为空则物流状态为已发货
        if (StrUtil.isNotEmpty(mallOrderEditParam.getLogisticsNumber())) {
            System.out.println("物流编号：" + mallOrderEditParam.getLogisticsNumber());
            mallOrderEditParam.setStatus(Convert.toStr(OrderStatusEnum.PROCESSING.getStatus()));
            // mallOrderEditParam.setLogisticsStatus("1"); // 字段已废弃
            // 调用根据物流编号轨迹订阅的方法
            // subscribeLogisticsTrack(mallOrder.getOrderNumber(), mallOrderEditParam.getLogisticsNumber());
        }
        try {
            BeanUtil.copyProperties(mallOrderEditParam, mallOrder);
            this.updateById(mallOrder);
        } catch (Exception e) {
            throw new CommonException("商城-订单信息修改失败");
        }
    }

    @Tran
    @Override
    public void delete(List<MallOrderIdParam> MallOrderIdParamList) {
        try {
            // 执行删除
            this.removeByIds(CollStreamUtil.toList(MallOrderIdParamList, MallOrderIdParam::getId));
        } catch (Exception e) {
            throw new CommonException("商城-订单信息删除失败");
        }
    }

    @Override
    public MallOrder detail(MallOrderIdParam mallOrderIdParam) {
        MallOrder mallOrder = new MallOrder();
        QueryWrapper queryWrapper = new QueryWrapper();
        if (mallOrderIdParam.getId() != null) {
            mallOrder = this.getById(mallOrderIdParam.getId());
        } else if (mallOrderIdParam.getOrderNumber() != null) {
            mallOrder.setOrderNumber(mallOrderIdParam.getOrderNumber());
            queryWrapper.like(MallOrder::getOrderNumber, mallOrderIdParam.getOrderNumber());
            mallOrder = this.getOne(queryWrapper);
        } else if (ObjectUtil.isEmpty(mallOrder)) {
            throw new CommonException("商城-订单不存在，id值为：{}", mallOrderIdParam.getId());
        }
        return mallOrder;
    }

    @Override
    public List<MallOrder> mallOrderList(MallOrderIdParam mallOrderIdParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(mallOrderIdParam.getId())) {
            queryWrapper.eq("id", mallOrderIdParam.getId());
        }
        if (ObjectUtil.isNotEmpty(mallOrderIdParam.getOrderNumber())) {
            queryWrapper.like("order_number", mallOrderIdParam.getOrderNumber());
        }
        List<MallOrder> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public void exportOrders(String orderIds, Context ctx) {
        // 整合数据
        List<ExportOrderDTO> dtoLists = prepareData(orderIds);
        try {
            exportOrdersToExcel(dtoLists, ctx);
        } catch (Exception e) {
            log.error("订单导出失败", e);
            throw new CommonException("订单导出失败");
        }
    }

    @Override
    public boolean confirmOrder(String userId, String orderId) {

        //检查订单是否存在： 如果订单不存在，返回 "订单不存在"。

        MallOrder mallOrder = this.getById(orderId);
        if (Objects.isNull(mallOrder) || !mallOrder.getCreateUser().equals(userId)) {
            throw new CommonException("未找到订单");
        }

        // 只有是已发货待收货的状态才可以确认收货
        if (!StringUtils.equals("2", mallOrder.getStatus())) {
            throw new CommonException("订单状态异常");
        }

        // 判断订单类型是否在1,2,4,6这个数组中
        JSONObject orderGoods = null;


        UpdateChain<MallOrder> updateChain = UpdateChain.of(MallOrder.class)
                .eq(MallOrder::getId, orderId)
                .set(MallOrder::getStatus, OrderStatusEnum.COMPLETED.getStatus());

        if (updateChain.update()) {
            if (ArrayUtil.contains(new String[]{OrderTypeEnum.SHOP_ORDER.getType(), OrderTypeEnum.LIVE_ORDER.getType(), OrderTypeEnum.LIVE_FU_DI_ORDER.getType()}, mallOrder.getOrderType())) {
                taskApi.task(userId, orderId, TaskBusinessCategory.SHOP_BUY);
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 导出订单信息到excel并响应
     *
     * @param dtoLists 订单信息
     * @param ctx      请求上下文
     */
    private void exportOrdersToExcel(List<ExportOrderDTO> dtoLists, Context ctx) {

        /*
            GMV：那些只要下单付款的订单金额，就会被计入到GMV当中，管你取消不取消订单、拒不拒收货物、退不退货呢；
            销售金额：只有买家收到货并确认收货，事后也不退款的订单金额，才能被计入到实际交易额中。
            线上实收:支付账号收到的那些到账的订单金额
         */

        // GMV
//        String gmv = "￥" + dtoLists.stream().mapToDouble(dto -> dto.getOrderFee().doubleValue()).sum();
        String gmv = "￥ 0.0";

        // 销售金额
//        String salesAmount = "￥" + dtoLists.stream().mapToDouble(dto -> dto.getOrderFee().doubleValue()).sum();
        String salesAmount = "￥ 0.0";
        // 线上实收
//        String onlineActualReceipt = "￥" + dtoLists.stream().mapToDouble(dto -> dto.getOrderFee().doubleValue()).sum();
        String onlineActualReceipt = "￥ 0.0";

        String fileName = "导出订单" + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".xlsx";
        File tempFile = FileUtil.file(FileUtil.getTmpDir() + FileUtil.FILE_SEPARATOR + fileName);
        try {

            EasyExcelFactory.write(tempFile, ExportOrderDTO.class)
                    .registerWriteHandler(new OrderSummaryWriteHandler(gmv, salesAmount, onlineActualReceipt))
                    .registerWriteHandler(OrderReportStyleStrategy.getStrategy())
                    .sheet("订单报表")
                    // 表示表头从第4行写，数据从第5行开始
                    .relativeHeadRowIndex(3)
                    .doWrite(dtoLists);

            CommonDownloadUtil.download(tempFile, ctx);
        } catch (Exception e) {
            log.error("导出失败", e);
            throw new CommonException("订单导出失败", e);
        } finally {
            FileUtil.del(tempFile);
        }
    }


    /**
     * 准备导出的订单数据
     *
     * @param orderIds 订单列表
     * @return 订单信息列表
     */
    private List<ExportOrderDTO> prepareData(String orderIds) {
        List<MallOrder> mallOrders = this.listByIds(Arrays.stream(StringUtils.split(orderIds, ",")).toList());
        return mallOrders.parallelStream().map(order -> {
            ExportOrderDTO dto = new ExportOrderDTO();
            dto.setOrderNumber(order.getOrderNumber());
            dto.setOrderFee(order.getOrderFee());

            MallOrderGoodsIdsParam mallOrderGoodsIdsParam = new MallOrderGoodsIdsParam();
            mallOrderGoodsIdsParam.setOrderId(order.getId());
            List<MallOrderGoods> orderGoodsListByOrderId = this.mallOrderGoodsService.getOrderGoodsListByOrderId(mallOrderGoodsIdsParam);
            if (IterUtil.isNotEmpty(orderGoodsListByOrderId)) {
                MallOrderGoods first = orderGoodsListByOrderId.get(0);
                String goodsId = first.getGoodsId();
                MallGoods mallGoods = this.mallGoodsService.getById(goodsId);
                if (Objects.nonNull(mallGoods)) {
                    dto.setCostPrice(BigDecimal.valueOf(mallGoods.getCostPrice()));
                    dto.setGoodsName(mallGoods.getTitle());
                    dto.setGoodsCode(mallGoods.getId());
                    dto.setGoodsAttributes(mallGoods.getDetail());
                }
                dto.setPurchaseQuantity(Integer.valueOf(first.getGoodsCount()));
                dto.setGoodsPrice(first.getGoodsPrice());
                dto.setTransactionAmount(first.getGoodsPrice());

                List<ShoppingCarDTO> shoppingCars = liveApi.getShoppingCarByLiveId(order.getOrderSourceId());
                if (IterUtil.isNotEmpty(shoppingCars)) {
                    shoppingCars.stream()
                            .filter(shoppingCar -> StringUtils.equals(shoppingCar.getGoodsId(), goodsId))
                            .findFirst()
                            .ifPresent(shoppingCar -> dto.setLivePrice(shoppingCar.getPrice()));
                }
            }

            dto.setOrderStatus(OrderStatusEnum.fromStatus(Integer.parseInt(order.getStatus())));
            dto.setConsignee(order.getNickname());
            dto.setPhoneNumber(order.getMobile());
            dto.setDeliveryAddress(order.getProvinceName() + order.getCityName() + order.getCountiesName() + order.getAddress());
            dto.setRemark(order.getRemark());
            dto.setOrderPlacementTime(order.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

            List<MallThirdPayment> payments = mallThirdPaymentService.list(new QueryWrapper().eq(MallThirdPayment::getOrderId, order.getId()).orderBy(MallThirdPayment::getCreateTime, false));
            if (IterUtil.isNotEmpty(payments)) {
                MallThirdPayment payment = payments.get(0);
                dto.setPayStatus(StringUtils.equals(payment.getStatus(), "0") ? "未支付" : "已支付");
                dto.setPaymentTime(payment.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                dto.setPaymentMethod(payment.getPayType());
            }

            OrderLogistics orderLogistics = orderLogisticsService.getOne(new QueryWrapper().eq(OrderLogistics::getOrderCode, order.getOrderNumber()));
            if (Objects.nonNull(orderLogistics)) {
                dto.setDeliveryTime(orderLogistics.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                dto.setShipper(orderLogistics.getShipper());
                dto.setLogisticsNumber(orderLogistics.getLogisticCode());
            }


            LiveBizDTO live = this.liveApi.getLiveByLiveId(order.getOrderSourceId());
            if (Objects.nonNull(live)) {
                dto.setLiveName(live.getMainTitle());
            }

            // 退款金额
            dto.setRefundAmount(new BigDecimal("0.0"));

            dto.setBuyerMessage("");

            String createUser = order.getCreateUser();
            UserInfoDTO user = userApi.getUserById(createUser);
            if (Objects.nonNull(user)) {
                dto.setBuyerId(user.getId());
                dto.setBuyerName(user.getNickname());
            }
            return dto;
        }).toList();
    }
}
