package api.shop.dto;

import api.order.enums.SettlementStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class SettleManagePageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("UUID")
    private String uuid;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("上级Id")
    private String parentId;

    @ApiModelProperty("积分比例")
    private String commissionRatio;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("订单账户")
    private String orderAccount;

    @ApiModelProperty("订单手机号")
    private String orderPhone;

    @ApiModelProperty("订单时间")
    private LocalDateTime orderTime;

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    @ApiModelProperty("结算状态")
    private SettlementStatus settleStatus;

    @ApiModelProperty("积分")
    private String commission;

    @ApiModelProperty("结算时间")
    private LocalDateTime settleTime;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("邀请码")
    private String invitationCode;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
