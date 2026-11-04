package api.shop.dto;

import api.order.enums.SettlementStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import vip.xiaonuo.common.enums.OrderStatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/21 14:06
 */
@Data
public class OrderManagePageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单时间")
    private LocalDateTime orderTime;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("订单名称")
    private String orderName;

    @ApiModelProperty("下单账户")
    private String orderAccount;

    @ApiModelProperty("账号Id")
    private String accountId;

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    @ApiModelProperty("订单状态")
    private OrderStatusEnum orderStatus;

    @ApiModelProperty("上级账户")
    private String parentAccount;

    @ApiModelProperty("上级Id")
    private String parentId;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("邀请码")
    private String invitationCode;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "结算状态")
    private SettlementStatus settlementStatus;

    @ApiModelProperty("结算时间")
    private LocalDateTime settleTime;
}
