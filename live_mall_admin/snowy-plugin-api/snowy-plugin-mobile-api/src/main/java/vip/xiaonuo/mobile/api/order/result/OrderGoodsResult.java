package vip.xiaonuo.mobile.api.order.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Author: shaojiang
 * CreateTime: 2025/4/9 16:19
 * Description: 订单关联商品
 */
@Getter
@Setter
public class OrderGoodsResult {
    /** ID */
    @ApiModelProperty(value = "主键", position = 1)
    private String goodsId;
    /** 商品标题 */
    @ApiModelProperty(value = "商品标题", position = 2)
    private String goodsTitle;
    /** 商品封面 */
    @ApiModelProperty(value = "商品封面", position = 3)
    private String goodsCover;
    /** 商品价格 */
    @ApiModelProperty(value = "商品价格", position = 4)
    private BigDecimal goodsPrice;
    @ApiModelProperty(value = "商品数量", position = 5)
    private int goodsCount;
}
