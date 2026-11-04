package vip.xiaonuo.shop.modular.provider;

import api.order.MallOrderApi;
import api.order.dto.CreateIntegralOrderDTO;
import api.order.dto.CreateMallOrderDTO;
import api.order.dto.CreateOrderDTO;
import api.order.dto.WechatPayCustomResultDTO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import manage.UserApi;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.OrderTypeMapperEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.pay.entity.PayRequestVo;
import vip.xiaonuo.shop.modular.mallorder.entity.MallOrder;
import vip.xiaonuo.shop.modular.mallorder.mapper.MallOrderMapper;
import vip.xiaonuo.shop.modular.mallorder.service.MallOrderService;
import vip.xiaonuo.shop.modular.mallordergoods.entity.MallOrderGoods;
import vip.xiaonuo.shop.modular.mallordergoods.mapper.MallOrderGoodsMapper;
import vip.xiaonuo.shop.modular.mallthirdpaymen.entity.MallThirdPayment;
import vip.xiaonuo.shop.modular.mallthirdpaymen.mapper.MallThirdPaymentMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static vip.xiaonuo.common.util.CommonServletUtil.getRequest;

@Slf4j
@Component
public class MallOrderApiImpl implements MallOrderApi {

    @Inject
    private MallOrderService mallOrderService;
    @Inject
    private MallOrderMapper mallOrderMapper;
    @Inject
    private MallOrderGoodsMapper mallOrderGoodsMapper;
    @Inject
    private MallThirdPaymentMapper mallThirdPaymentMapper;

    @Inject
    private PurchaseGoodsPayImpl purchaseGoodsPay;
    @Inject
    private UserApi userApi;

    @Tran
    @Override
    public String saveOrder(String userId, String orderType, Dict goods, Dict order) {
        MallOrder addParam = Convert.convert(MallOrder.class, order);
        addParam.setId(IdUtil.getSnowflakeNextIdStr());
        addParam.setCreateUser(userId);
        addParam.setOrderType(orderType);
        addParam.setOrderFee(order.getBigDecimal("orderTotalPrice"));
        List<Object> keyList = EnumUtil.getFieldValues(OrderTypeMapperEnum.class, "key");
        List<Object> abbrList = EnumUtil.getFieldValues(OrderTypeMapperEnum.class, "abbr");
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < keyList.size(); i++) {
            map.put(keyList.get(i).toString(), abbrList.get(i).toString());
        }
        addParam.setOrderNumber(map.get(orderType) + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss") + IdUtil.nanoId(6));
        addParam.setStatus("0");
        Dict payRequestVo = new Dict();
        payRequestVo.put("orderType", orderType);
        addParam.setAttach(JSONUtil.toJsonStr(payRequestVo));
        mallOrderMapper.insert(addParam);
        //保存订单商品
        MallOrderGoods mallOrderGoods = Convert.convert(MallOrderGoods.class, goods);
        mallOrderGoods.setCreateUser(userId);
        mallOrderGoods.setOrderId(addParam.getId());
        mallOrderGoodsMapper.insert(mallOrderGoods);

        return addParam.getId();
    }

    @Override
    public Map<String, String> orderPay(String userId, String orderId, String payType) {
        MallOrder mallOrder = mallOrderMapper.selectOneById(orderId);
        if (ObjectUtil.isEmpty(mallOrder)) {
            throw new CommonException("订单不存在");
        }
        //生成预支付
        JSONObject payRequestVo = JSONUtil.parseObj(mallOrder.getAttach());
        //保存第三方支付
        MallThirdPayment mallThirdPayment = new MallThirdPayment();
        mallThirdPayment.setCreateUser(userId);
        mallThirdPayment.setOrderId(mallOrder.getId());
        mallThirdPayment.setPayType(payType);
        mallThirdPayment.setTotalFee(mallOrder.getOrderFee());
        mallThirdPayment.setOutOrderNo(mallOrder.getOrderNumber());
        mallThirdPayment.setStatus("0");
        mallThirdPaymentMapper.insert(mallThirdPayment);

        PayRequestVo payRequestVo1 = new PayRequestVo();
        payRequestVo1.setPayType(payType);
        payRequestVo1.setTotalPrice(mallOrder.getOrderFee());
        Map<String, String> payR = purchaseGoodsPay.pay(userId, mallOrder.getOrderNumber(), JSONUtil.toJsonStr(payRequestVo), payRequestVo1);

        return payR;
    }

    @Override
    public WechatPayCustomResultDTO orderPayResult(String outTradeNo) {
        return BeanUtil.copyProperties(purchaseGoodsPay.queryCustomOrder(outTradeNo), WechatPayCustomResultDTO.class);
    }

    @Override
    public List<JSONObject> getUserIdGetOrderLists(String userId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        List<JSONObject> collect = mallOrderService.list(queryWrapper).stream().map(JSONUtil::parseObj).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            return collect;
        }
        return null;
    }

    @Override
    public JSONObject getOrderGoods(String orderId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_id", orderId);
        return new JSONObject(mallOrderGoodsMapper.selectOneByQuery(queryWrapper));
    }

    @Override
    public JSONObject getOrderObj(String orderId) {
        // QueryWrapper queryWrapper = new QueryWrapper();
        // queryWrapper.eq("order_number",orderId);
        // MallOrder mallOrder = mallOrderMapper.selectOneByQuery(queryWrapper);
        MallOrder mallOrder = mallOrderMapper.selectOneById(orderId);
        return new JSONObject(mallOrder);
    }

    @Override
    public List<JSONObject> getOrderStatusList(String userId, String status) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(MallOrder::getCreateUser, userId);
        if (!status.isEmpty() && !status.equals("-1")) {
            queryWrapper.eq(MallOrder::getStatus, status);
        }
        queryWrapper.ne(MallOrder::getOrderType, "3");
        queryWrapper.orderBy(MallOrder::getCreateTime, false);
        List<MallOrder> list = mallOrderService.list(queryWrapper);
        if (ObjectUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<JSONObject> collect = list.stream().map(JSONUtil::parseObj).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            return collect;
        }
        return null;
    }

    @Override
    public List<JSONObject> getOrderGoodsList(List<String> ids) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in(MallOrderGoods::getOrderId, ids);
        queryWrapper.orderBy(MallOrderGoods::getCreateTime, false);
        List<MallOrderGoods> mallOrderGoods = mallOrderGoodsMapper.selectListByQuery(queryWrapper);
        if (ObjectUtil.isEmpty(mallOrderGoods)) {
            return new ArrayList<>();
        }

        return mallOrderGoods.stream().map(JSONUtil::parseObj).collect(Collectors.toList());
    }

    @Override
    public Boolean updateOrderStatus(String orderId, String status) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", orderId);
        MallOrder mallOrder = new MallOrder();
        mallOrder.setStatus(status);
        mallOrderMapper.updateByQuery(mallOrder, queryWrapper);
        return true;
    }

    @Override
    public BigDecimal getUserPromotionOrderPrice() {
        Object uid = getRequest().attr("uid");
        if (ObjectUtil.isEmpty(uid)) {
            throw new CommonException("用户id不能为空");
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(MallOrder::getPid, Convert.toStr(uid));
        queryWrapper.select(QueryMethods.sum(MallOrder::getOrderFee).as("total"));
        Object total = mallOrderMapper.selectObjectByQuery(queryWrapper);
        return Convert.toBigDecimal(total);
    }

    @Override
    @Tran
    public String createOrder(CreateOrderDTO param) {
        return mallOrderService.createOrder(param);
    }

    @Override
    public String createMallOrder(CreateMallOrderDTO param) {
        return mallOrderService.createMallOrder(param);
    }

    @Override
    public String createIntegralOrder(CreateIntegralOrderDTO dto) {
        return mallOrderService.createIntegralOrder(dto);
    }

    @Tran
    @Override
    public boolean confirmOrder(String userId, String orderId) {
        return mallOrderService.confirmOrder(userId, orderId);
    }
}
