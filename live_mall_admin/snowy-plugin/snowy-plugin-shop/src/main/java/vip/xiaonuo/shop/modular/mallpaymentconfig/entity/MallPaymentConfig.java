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
package vip.xiaonuo.shop.modular.mallpaymentconfig.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import vip.xiaonuo.common.pojo.CommonEntity;

/**
 * 商城-支付配置实体
 *
 **/
@Getter
@Setter
@Table(value = "mall_payment_config")
public class MallPaymentConfig extends CommonEntity {

    /** ID */
    @Id
    @ApiModelProperty(value = "ID", position = 1)
    private String id;

    /** 支付名称 */
    @ApiModelProperty(value = "支付名称", position = 2)
    private String paymentName;


    /** 支付标识 */
    @ApiModelProperty(value = "支付标识", position = 2)
    private String paymentValue;

    /** 商户id */
    @ApiModelProperty(value = "商户id", position = 3)
    private String mchid;

    /** 应用id */
    @ApiModelProperty(value = "应用id", position = 4)
    private String appId;

    /** 支付平台公钥 */
    @ApiModelProperty(value = "支付平台公钥", position = 5)
    private String publicKey;

    /** 支付平台私钥 */
    @ApiModelProperty(value = "支付平台私钥", position = 6)
    private String privateKey;

    /** 异步回调地址 */
    @ApiModelProperty(value = "异步回调地址", position = 7)
    private String notifyUrl;

    /** 同步回调地址 */
    @ApiModelProperty(value = "同步回调地址", position = 8)
    private String returnUrl;

    /** 收款账号 */
    @ApiModelProperty(value = "收款账号(针对支付宝)", position = 9)
    private String seller;

    /** 签名 */
    @ApiModelProperty(value = "签名", position = 10)
    private String sign;

    /** 签名类型 */
    @ApiModelProperty(value = "签名类型", position = 11)
    private String signType;

    /** 字符类型 */
    @ApiModelProperty(value = "字符类型", position = 12)
    private String inputCharset;

    /** 消息类型 */
    @ApiModelProperty(value = "消息类型", position = 12)
    private String msgType;

    /** 证书地址 */
    @ApiModelProperty(value = "证书地址", position = 12)
    private String keystorePath;

    /** 证书密码 */
    @ApiModelProperty(value = "证书密码", position = 12)
    private String storePassword;

    /** 差异字段 */
    @ApiModelProperty(value = "差异字段", position = 12)
    private String fieldJson;

}
