package api.order;

import api.order.dto.CreateIntegralOrderDTO;
import api.order.dto.CreateMallOrderDTO;
import api.order.dto.CreateOrderDTO;
import api.order.dto.WechatPayCustomResultDTO;
import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONObject;
import org.noear.solon.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface MallOrderApi {

    /**
     * 保存订单
     *
     * @param userId    用户id
     * @param price     订单价格
     * @param consignee 收货人
     * @param address   地址
     * @param phone     手机
     * @return
     */
    String saveOrder(String userId, String orderType, Dict goods, Dict address);

    Map<String, String> orderPay(String userId, String orderId, String payType);

    /**
     * 根据userId查询订单信息
     *
     * @param userId
     * @return
     */
    List<JSONObject> getUserIdGetOrderLists(String userId);

    JSONObject getOrderGoods(String orderId);

    List<JSONObject> getOrderGoodsList(List<String> ids);

    /**
     * 根据orderId查询订单信息
     *
     * @param orderId
     * @return
     */
    JSONObject getOrderObj(String orderId);

    /**
     * 根据userId,status查询订单信息
     */
    List<JSONObject> getOrderStatusList(String userId, String status);

    /**
     * 更新订单状态
     *
     * @param orderId
     * @param status
     * @return
     */
    Boolean updateOrderStatus(String orderId, String status);

    /**
     * 获取用户推广订单总价
     *
     * @return
     */
    BigDecimal getUserPromotionOrderPrice();

    /**
     * 创建订单
     *
     * @param param 创建订单参数
     */
    String createOrder(@Validated CreateOrderDTO param);

    /**
     * 创建商城订单
     *
     * @param param 创建订单参数
     * @return 订单Id
     */
    String createMallOrder(@Validated CreateMallOrderDTO param);

    /**
     * 创建积分订单
     *
     * @param dto 参数
     * @return 订单Id
     */
    String createIntegralOrder(CreateIntegralOrderDTO dto);

    /**
     * 确认订单
     *
     * @param userId  用户Id
     * @param orderId 订单Id
     * @return 结果
     */
    boolean confirmOrder(String userId, String orderId);

    /**
     * 订单支付结果
     *
     * @param outTradeNo 订单号
     * @return
     */
    WechatPayCustomResultDTO orderPayResult(String outTradeNo);
}
