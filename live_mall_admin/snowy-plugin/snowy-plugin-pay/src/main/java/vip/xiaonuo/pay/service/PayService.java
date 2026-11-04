package vip.xiaonuo.pay.service;

import org.noear.solon.core.handle.Context;
import vip.xiaonuo.pay.entity.CorporatePaymentVo;
import vip.xiaonuo.pay.entity.CreatePayOrderVo;


public interface PayService {

    /**
     * 创建支付订单
     * @param vo
     * @return T
     * @exception
     * @author GMQ
     * @date 2022/2/28 15:23
     **/
    <T> T  createPayOrder(CreatePayOrderVo vo);

    /**
     * 企业付款
     * @param vo
     * @return T
     * @exception
     * @author GMQ
     * @date 2022/3/2 12:43
     **/
    <T> T corporatePayment(CorporatePaymentVo vo);

    /**
     * 订单异步通知
     *
     * @throws
     * @author GMQ
     * @date 2022/3/3 21:08
     **/
    void orderNotifyResult(Context ctx);
}
