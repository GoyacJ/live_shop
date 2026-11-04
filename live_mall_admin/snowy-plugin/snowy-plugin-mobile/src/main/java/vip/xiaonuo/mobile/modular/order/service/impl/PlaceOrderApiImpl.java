package vip.xiaonuo.mobile.modular.order.service.impl;

import api.evaluate.MallEvaluateApi;
import api.order.MallOrderApi;
import api.order.dto.CreateMallOrderDTO;
import api.sales.MallSalesTypeApi;
import api.shop.MallGoodsApi;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import manage.UserApi;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.enums.OrderStatusEnum;
import vip.xiaonuo.common.enums.OrderTypeMapperEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.util.ResponseUtil;
import vip.xiaonuo.live.api.live.LiveApi;
import vip.xiaonuo.live.api.live.dto.CreateShoppingCarOrderDTO;
import vip.xiaonuo.manage.modular.useraddress.entity.UserAddress;
import vip.xiaonuo.manage.modular.useraddress.service.UserAddressService;
import vip.xiaonuo.mobile.api.order.PlaceOrderApi;
import vip.xiaonuo.mobile.api.order.result.OrderGoodsResult;
import vip.xiaonuo.mobile.api.order.result.OrderListResult;
import vip.xiaonuo.mobile.api.order.result.OrderResult;
import vip.xiaonuo.mobile.modular.manage.result.UserInfoResult;
import vip.xiaonuo.mobile.modular.manage.service.UserInfoService;

import java.util.*;
import java.util.stream.Collectors;

import static vip.xiaonuo.common.util.CommonServletUtil.getRequest;

@Component
@Slf4j
public class PlaceOrderApiImpl implements PlaceOrderApi {

    @Inject
    private CommonCacheOperator commonCacheOperator;
    @Inject
    private MallGoodsApi mallGoodsApi;
    @Inject
    private MallOrderApi mallOrderApi;
    @Inject
    private MallEvaluateApi mallEvaluateApi;
    @Inject
    private MallSalesTypeApi mallSalesTypeApi;
    @Inject
    private UserAddressService userAddressService;
    @Inject
    private UserInfoService userInfoService;
    @Inject
    private LiveApi liveApi;
    @Inject
    private UserApi userApi;

    @Tran
    @Override
    public CommonResult<String> playOrder(String userId, String productId, Integer goodsCount, String addressId, String orderType, String sourceId, String remark) {

        OrderTypeMapperEnum orderTypeEnum = OrderTypeMapperEnum.getByKey(orderType);
        if (Objects.isNull(orderTypeEnum)) {
            throw new CommonException("未找到订单类型");
        }

        // 获取邀请人
        String pid = "";
        UserInfoResult userInfo = userInfoService.getUserinfo(userId);
        if (ObjectUtil.isNotEmpty(userInfo)) {
            pid = userInfo.getPId();
        }

        // 获取收货地址
        UserAddress consignee = userAddressService.getById(addressId);

        String orderId;

        // 订单分发
        switch (orderTypeEnum) {
            case SC -> orderId = playScOrder(userId, productId, goodsCount, sourceId, remark, pid, consignee);
            case ZBJ -> orderId = playZbjOrder(userId, productId, goodsCount, sourceId, remark, pid, consignee);
            case JF, FD -> throw new CommonException("暂未开放此订单模式");
            default -> throw new CommonException("未找到订单类型");
        }

        return CommonResult.data(orderId);
    }

    /**
     * 直播间订单
     *
     * @param userId     下单用户Id
     * @param productId  商品Id
     * @param goodsCount 商品数量
     * @param sourceId   来源信息
     * @param remark     备注
     * @param pid        邀请人信息
     * @param consignee  收货人信息
     * @return 订单号
     */
    private String playZbjOrder(String userId, String productId, Integer goodsCount, String sourceId, String remark, String pid, UserAddress consignee) {
        return liveApi.createShoppingCarOrder(new CreateShoppingCarOrderDTO(
                userId,
                pid,
                sourceId,
                remark,
                productId,
                goodsCount,
                consignee.getMobile(),
                consignee.getNickname(),
                consignee.getProvinceName(),
                consignee.getCityName(),
                consignee.getCountiesName(),
                consignee.getAddress()
        ));
    }

    /**
     * 商城订单
     *
     * @param userId     下单用户Id
     * @param productId  商品Id
     * @param goodsCount 商品数量
     * @param sourceId   来源信息
     * @param remark     备注
     * @param pid        邀请人信息
     * @param consignee  收货人信息
     * @return 订单号
     */
    private String playScOrder(String userId, String productId, Integer goodsCount, String sourceId, String remark, String pid, UserAddress consignee) {
        return mallOrderApi.createMallOrder(new CreateMallOrderDTO(
                userId,
                pid,
                sourceId,
                remark,
                productId,
                goodsCount,
                consignee.getMobile(),
                consignee.getNickname(),
                consignee.getProvinceName(),
                consignee.getCityName(),
                consignee.getCountiesName(),
                consignee.getAddress()
        ));
    }

    @Override
    public CommonResult<Map<String, String>> orderPay(String userId, String orderId, String payType) {
        Map<String, String> payR = mallOrderApi.orderPay(userId, orderId, payType);
        return CommonResult.data(payR);
    }

    @Override
    public CommonResult<String> h5PayUrl(String userId, String orderId, String payType) {
        Map<String, String> payR = mallOrderApi.orderPay(userId, orderId, payType);
        String h5Url = payR.get("h5Url");
        return CommonResult.data(h5Url);
    }

    @Override
    public boolean cancelOrder(String uid, String orderId) {
        //检查订单是否存在： 如果订单不存在，返回 "订单不存在"。
        JSONObject orderObj = mallOrderApi.getOrderObj(orderId);
        if (ObjectUtil.isEmpty(orderObj)) {
            throw new CommonException("未找到订单");
        }

        return mallOrderApi.updateOrderStatus(orderId, String.valueOf(OrderStatusEnum.CANCEL.getStatus()));
    }

    @Tran
    @Override
    public boolean confirmReceipt(String userId, String orderId) {
        return mallOrderApi.confirmOrder(userId, orderId);
    }

    @Tran
    @Override
    public Map<String, String> evaluateOrder(String userId, String productId, String evaluationContent) {
        //检查订单是否存在： 如果订单不存在，返回 "订单不存在"。
        List<OrderResult> orderListResult = checkoutOrderStatus(userId);
        if (orderListResult != null && !orderListResult.isEmpty()) {
            for (OrderResult orderResult : orderListResult) {
                //检查订单状态是否为已收货： 如果订单状态不正确，返回 "订单状态不正确，无法进行评价"。
                if (orderResult.getStatus().equals(String.valueOf(OrderStatusEnum.COMPLETED.getStatus()))) {
                    mallEvaluateApi.addEvaluate(userId, productId, evaluationContent);
                } else {
                    return ResponseUtil.errorResponse(-1, "订单状态不正确，无法进行评价", null);
                }
            }
        } else {
            return ResponseUtil.errorResponse(-1, "订单不存在", null);
        }
        return ResponseUtil.successResponse(userId);
    }

    @Tran(readOnly = true)
    @Override
    public List<OrderListResult> getOrderList(String userId, String status) {
        //根据用户ID查询订单列表： 该方法会返回符合用户ID和订单状态的所有订单。
        List<JSONObject> orderStatusList = mallOrderApi.getOrderStatusList(userId, status);
        if (ObjectUtil.isEmpty(orderStatusList)) {
            return new ArrayList<>();
        }
        List<OrderListResult> orderList = orderStatusList.stream()
                .map(jsonObject -> JSONUtil.toBean(jsonObject, OrderListResult.class))
                .collect(Collectors.toList());
        if (orderList.isEmpty()) {
            throw new IllegalArgumentException("用户没有+ " + status + "的订单");
        }

        List<String> ids = orderList.stream().map(OrderListResult::getId).collect(Collectors.toList());

        List<JSONObject> orderGoodsList = mallOrderApi.getOrderGoodsList(ids);
        orderList.forEach(orderListResult -> {
            orderListResult.setGoods(orderGoodsList.stream()
                    .filter(jsonObject -> jsonObject.getStr("orderId").equals(orderListResult.getId()))
                    .map(jsonObject -> JSONUtil.toBean(jsonObject, OrderGoodsResult.class))
                    .collect(Collectors.toList()));
        });

        return orderList;
    }

    @Override
    public JSONObject getOrderDetail(String orderId) {
        Object uid = getRequest().attr("uid");
        if (ObjectUtil.isEmpty(uid)) {
            throw new CommonException("用户id不能为空");
        }
        //根据订单ID查询订单详情： 该方法会返回符合订单ID的订单详情。
        JSONObject orderObj = mallOrderApi.getOrderObj(orderId);
        System.out.println(orderObj);
        if (ObjectUtil.isNotEmpty(orderObj)
                && Convert.toStr(uid).equals(orderObj.getStr("createUser"))
        ) {
            orderObj.set("goods", mallOrderApi.getOrderGoods(orderObj.getStr("id")));

            return orderObj;
        } else {
            throw new CommonException("订单不存在");
        }
    }

    @Tran
    @Override
    public Map<String, String> afterSale(String userId, String orderId, String afterSaleType, String afterSaleDescription) {
        //检查订单是否存在： 如果订单不存在，记录日志并返回 "订单不存在"。
        JSONObject orderObj = mallOrderApi.getOrderObj(orderId);
        if (!orderObj.isEmpty()) {
            //检查订单状态是否为已完成： 如果订单状态不正确，记录日志并返回 "订单状态不正确，无法进行售后处理"。
            if (orderObj.getStr("status").equals(String.valueOf(OrderStatusEnum.COMPLETED.getStatus()))) {
                int i = mallSalesTypeApi.addMallSalesType(orderId, afterSaleType, afterSaleDescription);
                if (i == 1) {
                    //更新订单状态
                    mallOrderApi.updateOrderStatus(orderId, String.valueOf(OrderStatusEnum.PROCESSING.getStatus()));
                    return ResponseUtil.successResponse(orderId);
                }
                return ResponseUtil.errorResponse(-1, "售后处理失败", orderId);
            }
            return ResponseUtil.errorResponse(-1, "订单状态不正确，无法进行售后处理 ", orderId);
        }
        return ResponseUtil.errorResponse(-1, "订单不存在，无法进行售后处理", orderId);
    }

    @Tran
    @Override
    public Map<String, String> recordShare(String shareCode, String userId) {
        return new HashMap<>();
    }

    /**
     * 检查订单状态
     *
     * @param userId
     * @return
     */
    @Tran(readOnly = true)
    private List<OrderResult> checkoutOrderStatus(String userId) {
        List<JSONObject> orderList = mallOrderApi.getUserIdGetOrderLists(userId);
        if (orderList != null && !orderList.isEmpty()) {
            return orderList.stream()
                    .map(jsonObject -> JSONUtil.toBean(jsonObject, OrderResult.class))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
