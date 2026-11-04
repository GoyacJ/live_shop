package vip.xiaonuo.pay.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 企业付款对象
 * Created by GMQ on 2022/3/2 12:25
 */
@Data
public class CorporatePaymentVo implements Serializable {

    private static final long serialVersionUID = 4308051800117907614L;

    // 商户订单号
    private String partnerTradeNo;

    // 收款人，用户在商户下的唯一标识，如微信openid
    private String payer;

    // 描述
    private String description;

    // 订单总金额 元
    private BigDecimal total;

}