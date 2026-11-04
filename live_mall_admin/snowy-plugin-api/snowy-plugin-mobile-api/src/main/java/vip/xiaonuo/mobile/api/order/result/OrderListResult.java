package vip.xiaonuo.mobile.api.order.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Author: shaojiang
 * CreateTime: 2025/4/9 16:15
 * Description: 订单列表
 */
@Getter
@Setter
public class OrderListResult {
    /** ID */
    @ApiModelProperty(value = "主键", position = 1)
    private String id;
    /** 订单编号 */
    @ApiModelProperty(value = "订单编号", position = 2)
    private String orderNumber;
    /** 订单价格 */
    @ApiModelProperty(value = "订单价格", position = 3)
    private BigDecimal orderFee;
    /** 状态 */
    @ApiModelProperty(value = "状态", position = 4)
    private String status;
    @ApiModelProperty(value = "商品", position = 5)
    private List<OrderGoodsResult> goods;
}
