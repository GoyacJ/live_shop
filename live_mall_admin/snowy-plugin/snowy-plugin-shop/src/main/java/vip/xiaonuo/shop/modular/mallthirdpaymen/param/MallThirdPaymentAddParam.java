/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.shop.modular.mallthirdpaymen.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 商城-订单添加参数
 *
 **/
@Getter
@Setter
public class MallThirdPaymentAddParam {

    /** 订单id */
    @ApiModelProperty(value = "订单id", position = 2)
    private String orderId;

    /** 调用接口提交的公众号id */
    @ApiModelProperty(value = "调用接口提交的公众号id", position = 2)
    private String appid;

    /** 分配的商户id */
    @ApiModelProperty(value = "分配的商户id", position = 3)
    private String mchid;

    /** 调用接口时提交的商户服务订单号 */
    @ApiModelProperty(value = "物流状态", position = 4)
    private String outOrderNo;

    /** 支付金额 */
    @ApiModelProperty(value = "支付金额", position = 5)
    private Double totalFee;

    /** 支付类型 */
    @ApiModelProperty(value = "支付类型", position = 6)
    private String payType;

    /** 支付状态 */
    @ApiModelProperty(value = "支付状态", position = 11)
    private String status;
}
