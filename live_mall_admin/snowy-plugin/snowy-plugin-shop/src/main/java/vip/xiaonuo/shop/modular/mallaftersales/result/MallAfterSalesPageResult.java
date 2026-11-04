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
package vip.xiaonuo.shop.modular.mallaftersales.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 商城-订单添加参数
 *
 **/
@Getter
@Setter
public class MallAfterSalesPageResult {

    /** 订单id */
    @ApiModelProperty(value = "订单id", position = 2)
    private String orderId;

    @ApiModelProperty(value = "订单类型", position = 2)
    private String orderType;

    /** 订单id */
    @ApiModelProperty(value = "订单编号", position = 2)
    private String orderNumber;

    /** 售后类型 */
    @ApiModelProperty(value = "售后类型", position = 2)
    private String afterSaleType;

    /** 处理状态 */
    @ApiModelProperty(value = "处理状态", position = 3)
    private String afterSaleStatus;

    /** 反馈信息 */
    @ApiModelProperty(value = "反馈信息", position = 4)
    private String feedbackMessage;

    /** 处理信息 */
    @ApiModelProperty(value = "处理信息", position = 5)
    private String replyMessage;

}
