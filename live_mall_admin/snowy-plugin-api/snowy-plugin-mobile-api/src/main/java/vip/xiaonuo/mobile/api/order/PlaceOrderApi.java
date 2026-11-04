package vip.xiaonuo.mobile.api.order;

import cn.hutool.json.JSONObject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.mobile.api.order.result.OrderListResult;

import java.util.List;
import java.util.Map;

public interface PlaceOrderApi {

    /**
     * 商品下单接口。
     * 该方法接收用户ID、商品ID和数量，创建订单并处理支付。
     * @param userId 用户ID
     * @param goodsCount 商品数量
     * @param productId  商品ID
     * @param addressId  收货人
     * @return 订单创建的结果，包括订单ID和状态信息
     */
    @Tran
    CommonResult<String> playOrder(String userId, String productId, Integer goodsCount, String addressId, String orderType, String sourceId, String remark);

    CommonResult<Map<String, String>> orderPay(String userId, String orderId, String payType);

    CommonResult<String> h5PayUrl(String str, String orderId, String payType);


    boolean cancelOrder(String uid, String orderId);

    /**
     * 确认收货接口。
     * 该方法接收订单ID，更新订单状态为已收货，并记录确认收货的时间。
     *
     * @param userId 用户ID
     * @return 确认收货的结果，包括订单ID和状态信息
     */
    boolean confirmReceipt(String userId, String orderId);

    /**
     * 评价订单接口。
     * 该方法接收订单ID、评价内容和评分，更新订单的评价信息。
     *
     * @param orderId 订单ID
     * @param evaluationContent 评价内容
     * @return 评价的结果，包括订单ID和状态信息
     */
    Map<String, String> evaluateOrder(String userId,String orderId, String evaluationContent);

    /**
     * 获取订单列表。
     * 该方法根据用户ID和订单状态获取相应的订单列表。
     * @param userId 用户ID
     * @param status 订单状态，例如: "CREATED", "PAID", "SHIPPED", "RECEIVED", "COMPLETED", "AFTER_SALE"
     * @return 订单列表
     */
    List<OrderListResult> getOrderList(String userId, String status);

    JSONObject getOrderDetail(String orderId);

    /**
     * 售后处理接口。
     * 该方法接收订单ID、售后类型和描述，更新订单的售后信息。
     *
     * @param orderId 订单ID
     * @param afterSaleType 售后类型，例如: "RETURN", "EXCHANGE", "REPAIR"
     * @param afterSaleDescription 售后描述
     * @return 售后处理的结果，包括订单ID和状态信息
     */
    Map<String, String> afterSale(String userId,String orderId, String afterSaleType, String afterSaleDescription);

    /**
     * 记录分享行为并发放奖励
     * 该方法记录新用户通过分享链接注册的行为。
     *
     * @param shareCode 分享码
     * @param userId 用户ID
     * @return 记录结果
     */
    Map<String, String> recordShare(String shareCode, String userId);
}
