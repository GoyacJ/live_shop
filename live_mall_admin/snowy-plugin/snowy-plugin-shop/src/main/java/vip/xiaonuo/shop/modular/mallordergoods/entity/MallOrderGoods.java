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
package vip.xiaonuo.shop.modular.mallordergoods.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import vip.xiaonuo.common.pojo.CommonEntity;

import java.math.BigDecimal;

/**
 * 商城-订单实体
 *
 **/
@Getter
@Setter
@Table(value = "mall_order_goods")
public class MallOrderGoods extends CommonEntity {

    /** ID */
    @Id
    @ApiModelProperty(value = "ID", position = 1)
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

    /** 商品封面 */
    @ApiModelProperty(value = "商品封面", position = 3)
    private String goodsCover;

    /** 物流状态 */
    @ApiModelProperty(value = "商品价格", position = 4)
    private BigDecimal goodsPrice;

    /** 手机号 */
    @ApiModelProperty(value = "商品数量", position = 5)
    private String goodsCount;

    /** 赠送的积分 */
    @ApiModelProperty(value = "赠送的积分", position = 8)
    private int giftPoints;
}
