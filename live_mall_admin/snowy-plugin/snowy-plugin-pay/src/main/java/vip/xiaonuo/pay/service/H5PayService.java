package vip.xiaonuo.pay.service;

import org.noear.solon.core.handle.Context;
import vip.xiaonuo.pay.dto.WechatPayCustomResultDTO;
import vip.xiaonuo.pay.dto.WechatPayOrderResultDTO;
import vip.xiaonuo.pay.entity.CreateH5PayOrderDTO;

import java.util.Map;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/23 09:20
 */
public interface H5PayService {

    /**
     * H5支付
     * @param dto 支付参数
     * @return H5支付url
     */
    Map<String,String> h5Pay(CreateH5PayOrderDTO dto);

    /**
     * H5支付回调
     * @param ctx
     */
    void h5PayNotify(Context ctx);

    /**
     * 商户订单号查询订单
     * @param outTradeNo 订单号
     * @return
     */
    WechatPayCustomResultDTO queryCustomOrder(String outTradeNo);

    /**
     * 微信支付订单号查询订单
     * @param transactionId 【微信支付订单号】 微信支付侧订单的唯一标识，订单支付成功后，支付成功回调通知和商户订单号查询订单会返回该参数。
     * @return
     */
    WechatPayOrderResultDTO queryOrderDetail(String transactionId);
}
