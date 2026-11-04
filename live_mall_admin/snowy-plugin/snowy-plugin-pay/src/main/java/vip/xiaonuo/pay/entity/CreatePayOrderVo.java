package vip.xiaonuo.pay.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 创建支付订单对象
 * Created by GMQ on 2022/2/28 13:28
 */
@Data
public class CreatePayOrderVo implements Serializable {

    private static final long serialVersionUID = -1746710197153115469L;

    // 商户系统内部订单号
    private String outTradeNo;

    // 商品描述
    private String description;

    // 订单总金额 元
    private BigDecimal total;

    // 用户在商户下的唯一标识，如微信openid
    private String payer;

    // 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用
    private String attach;

}