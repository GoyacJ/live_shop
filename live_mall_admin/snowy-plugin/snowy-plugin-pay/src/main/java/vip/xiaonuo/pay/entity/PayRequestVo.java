package vip.xiaonuo.pay.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付方式 请求对象
 * Created by GMQ on 2022/2/17 14:59
 */
@Data
public class PayRequestVo implements Serializable {

    private static final long serialVersionUID = -7618721077184817164L;

    /**
     * 支付方式，1 余额，2 微信支付,3微信H5支付
     */
    private String payType;


    /**
     * 总价
     **/
    private BigDecimal totalPrice;

    /**
     * 其他
     **/
    private String other;

}