package vip.xiaonuo.shop.modular.provider;

import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import manage.UserApi;
import org.noear.dami.bus.Payload;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.solon.annotation.DamiTopic;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.enums.OrderTypeMapperEnum;
import vip.xiaonuo.distribution.api.DistributionApi;
import vip.xiaonuo.distribution.api.enums.DistributionType;
import vip.xiaonuo.pay.handle.PayHandle;
import vip.xiaonuo.shop.modular.mallorder.entity.MallOrder;
import vip.xiaonuo.shop.modular.mallorder.mapper.MallOrderMapper;
import vip.xiaonuo.shop.modular.mallordergoods.entity.MallOrderGoods;
import vip.xiaonuo.shop.modular.mallordergoods.mapper.MallOrderGoodsMapper;
import vip.xiaonuo.shop.modular.mallthirdpaymen.entity.MallThirdPayment;
import vip.xiaonuo.shop.modular.mallthirdpaymen.mapper.MallThirdPaymentMapper;
import vip.xiaonuo.task.api.TaskApi;
import vip.xiaonuo.task.api.enums.TaskBusinessCategory;

@Slf4j
@Component
public class PurchaseGoodsPayImpl extends PayHandle {

    @Inject
    private MallOrderMapper mallOrderMapper;
    @Inject
    private MallThirdPaymentMapper mallThirdPaymentMapper;
    @Inject
    private MallOrderGoodsMapper mallOrderGoodsMapper;
    @Inject
    private UserApi userApi;
    @Inject
    private DistributionApi distributionApi;
    @Inject
    private TaskApi taskApi;

    @Override
    public OrderTypeMapperEnum getPayClassify() {
        return OrderTypeMapperEnum.ZBJ;
    }

    @DamiTopic("payBusinessBy2")
    public class UserEventListener implements TopicListener<Payload<String, String>> {
        @Override
        // public void onEvent(Payload<String, String> payload) throws Throwable {
        public void onEvent(Payload<String, String> payload) throws Throwable {
            log.info("购买商品支付回调");
            log.info("收到消息：{}", payload.getContent());
            // JSONObject attach = JSONUtil.parseObj(payload.getContent());
            // String orderId = attach.getStr("orderId");
            String orderNum = payload.getContent();
            QueryWrapper queryWrapper = QueryWrapper.create();
            queryWrapper.eq(MallOrder::getOrderNumber, orderNum);
            MallOrder mallOrder = mallOrderMapper.selectOneByQuery(queryWrapper);
            if (ObjectUtil.isEmpty(mallOrder)) {
                log.warn("订单不存在, order_num:{}", orderNum);
                return;
            }

            //订单状态，0待支付，1已支付待发货，2已发货待收货，3已收货，4已完成，5售后
            if (!"0".equals(mallOrder.getStatus())) {
                log.warn("订单已支付");
                return;
            }
            MallOrder mallOrderUpdate = new MallOrder();
            mallOrderUpdate.setId(mallOrder.getId());
            mallOrderUpdate.setStatus("1");
            mallOrderMapper.update(mallOrderUpdate);
            MallThirdPayment mallThirdPayment = new MallThirdPayment();
            mallThirdPayment.setStatus("1");
            queryWrapper = QueryWrapper.create();
            queryWrapper.eq(MallThirdPayment::getOrderId, mallOrder.getId());
            mallThirdPaymentMapper.updateByQuery(mallThirdPayment, queryWrapper);
            // 返积分
            MallOrderGoods mallOrderGoods = mallOrderGoodsMapper.selectOneByQuery(QueryWrapper.create().eq(MallOrderGoods::getOrderId, mallOrder.getId()));
            if (ObjectUtil.isNotEmpty(mallOrderGoods)) {
                distributionApi.promotion(mallOrder.getCreateUser(), DistributionType.SHOPPING, mallOrder.getId(), "购买商品");
                taskApi.task(mallOrder.getCreateUser(), TaskBusinessCategory.LIVE_BUY);
            }
        }
    }

    @DamiTopic("payBusinessBy1")
    public class payBusinessBy1 implements TopicListener<Payload<String, String>> {
        @Override
        public void onEvent(Payload<String, String> payload) throws Throwable {
            log.info("购买商品支付回调");
            log.info("收到消息：{}", payload.getContent());
            String orderNum = payload.getContent();
            QueryWrapper queryWrapper = QueryWrapper.create();
            queryWrapper.eq(MallOrder::getOrderNumber, orderNum);
            MallOrder mallOrder = mallOrderMapper.selectOneByQuery(queryWrapper);
            if (ObjectUtil.isEmpty(mallOrder)) {
                log.warn("订单不存在, order_num:{}", orderNum);
                return;
            }

            //订单状态，0待支付，1已支付待发货，2已发货待收货，3已收货，4已完成，5售后
            if (!"0".equals(mallOrder.getStatus())) {
                log.warn("订单已支付");
                return;
            }
            MallOrder mallOrderUpdate = new MallOrder();
            mallOrderUpdate.setId(mallOrder.getId());
            mallOrderUpdate.setStatus("1");
            mallOrderMapper.update(mallOrderUpdate);
            MallThirdPayment mallThirdPayment = new MallThirdPayment();
            mallThirdPayment.setStatus("1");
            queryWrapper = QueryWrapper.create();
            queryWrapper.eq(MallThirdPayment::getOrderId, mallOrder.getId());
            mallThirdPaymentMapper.updateByQuery(mallThirdPayment, queryWrapper);
            // 返积分
            MallOrderGoods mallOrderGoods = mallOrderGoodsMapper.selectOneByQuery(QueryWrapper.create().eq(MallOrderGoods::getOrderId, mallOrder.getId()));
            if (ObjectUtil.isNotEmpty(mallOrderGoods)) {
                distributionApi.promotion(mallOrder.getCreateUser(), DistributionType.SHOPPING, mallOrder.getId(), "购买商品");
                taskApi.task(mallOrder.getCreateUser(), TaskBusinessCategory.SHOP_BUY);
            }
        }
    }

    @DamiTopic("h5PayBusinessBy")
    public class h5PayBusinessBy implements TopicListener<Payload<String, String>> {
        @Override
        public void onEvent(Payload<String, String> payload) throws Throwable {
            log.info("购买商品支付回调");
            log.info("收到消息：{}", payload.getContent());
            String orderNum = payload.getContent();
            QueryWrapper queryWrapper = QueryWrapper.create();
            queryWrapper.eq(MallOrder::getOrderNumber, orderNum);
            MallOrder mallOrder = mallOrderMapper.selectOneByQuery(queryWrapper);
            if (ObjectUtil.isEmpty(mallOrder)) {
                log.warn("订单不存在, order_num:{}", orderNum);
                return;
            }

            //订单状态，0待支付，1已支付待发货，2已发货待收货，3已收货，4已完成，5售后
            if (!"0".equals(mallOrder.getStatus())) {
                log.warn("订单已支付");
                return;
            }
            MallOrder mallOrderUpdate = new MallOrder();
            mallOrderUpdate.setId(mallOrder.getId());
            mallOrderUpdate.setStatus("1");
            mallOrderMapper.update(mallOrderUpdate);
            MallThirdPayment mallThirdPayment = new MallThirdPayment();
            mallThirdPayment.setStatus("1");
            queryWrapper = QueryWrapper.create();
            queryWrapper.eq(MallThirdPayment::getOrderId, mallOrder.getId());
            mallThirdPaymentMapper.updateByQuery(mallThirdPayment, queryWrapper);
            // 返积分
            MallOrderGoods mallOrderGoods = mallOrderGoodsMapper.selectOneByQuery(QueryWrapper.create().eq(MallOrderGoods::getOrderId, mallOrder.getId()));
            if (ObjectUtil.isNotEmpty(mallOrderGoods)) {
                distributionApi.promotion(mallOrder.getCreateUser(), DistributionType.SHOPPING, mallOrder.getId(), "H5购买商品");
                taskApi.task(mallOrder.getCreateUser(), TaskBusinessCategory.SHOP_BUY);
            }
        }
    }
}
