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
package vip.xiaonuo.shop.modular.mallordergoods.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.noear.solon.validation.annotation.NotBlank;

/**
 * 商城-订单商品编辑参数
 *
 **/
@Getter
@Setter
public class MallOrderGoodsEditParam {

    /** ID */
    @ApiModelProperty(value = "ID", required = true, position = 1)
    @NotBlank(message = "id不能为空")
    private String id;

    /** 订单编号 */
    @ApiModelProperty(value = "商品id", position = 2)
    private String goodsId;

    /** 订单价格 */
    @ApiModelProperty(value = "订单id", position = 2)
    private String orderId;

    /** 物流编号 */
    @ApiModelProperty(value = "商品名称", position = 3)
    private String goodsTitle;

    /** 物流状态 */
    @ApiModelProperty(value = "商品价格", position = 4)
    private Double goodsPrice;

    /** 手机号 */
    @ApiModelProperty(value = "商品数量", position = 5)
    private String goodsCount;

    /** 状态 */
    @ApiModelProperty(value = "状态", position = 11)
    private String status;

    /** 排序 */
    @ApiModelProperty(value = "排序", position = 12)
    private String sort;

}
