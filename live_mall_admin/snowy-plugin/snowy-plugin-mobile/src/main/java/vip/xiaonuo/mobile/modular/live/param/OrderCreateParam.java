package vip.xiaonuo.mobile.modular.live.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.NotNull;

/**
 * Author: shaojiang
 * CreateTime: 2025/4/8 09:06
 * Description: 下单入参
 */
@Getter
@Setter
public class OrderCreateParam {
    @ApiModelProperty(value = "购买的商品ID", required = true, position = 1)
    @NotBlank(message = "请选择购买商品")
    private String goodsId;

    @ApiModelProperty(value = "商品数量", required = true, position = 1)
    @NotNull(message = "商品数量不能为空")
    private Integer goodsCount;

    @ApiModelProperty(value = "收货地址", required = true, position = 2)
    @NotBlank(message = "收货地址不能为空")
    private String addressId;

    @ApiModelProperty(value = "支付场景", position = 3)
    @NotBlank(message = "支付场景不能为空")
    private String orderType;

    @ApiModelProperty(value = "支付类型", position = 3)
    @NotBlank(message = "支付类型")
    private String payType;

    @ApiModelProperty(value = "订单来源ID", position = 3)
    private String sourceId;

    @ApiModelProperty(value = "订单备注", position = 3)
    private String remark;
}
