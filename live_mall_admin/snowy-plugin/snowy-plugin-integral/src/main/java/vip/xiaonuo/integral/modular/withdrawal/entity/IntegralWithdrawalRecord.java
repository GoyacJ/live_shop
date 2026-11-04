package vip.xiaonuo.integral.modular.withdrawal.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.common.pojo.CommonEntity;
import vip.xiaonuo.integral.api.enums.IntegralWithdrawalStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>积分提现记录</p>
 *
 * @author goya
 * @since 2025/5/21 10:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("integral_withdrawal_record")
public class IntegralWithdrawalRecord extends CommonEntity {

    @Id
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "用户Id")
    private String userId;

    @ApiModelProperty(value = "提现积分")
    private BigDecimal integral;

    @ApiModelProperty(value = "提现状态")
    private IntegralWithdrawalStatus withdrawalStatus;

    @ApiModelProperty(value = "提现时间")
    private LocalDateTime withdrawalTime;

    @ApiModelProperty(value = "提现账号")
    private String withdrawalAccount;

    @ApiModelProperty(value = "积分兑换比例")
    private BigDecimal integralExchangeRate;

    @ApiModelProperty(value = "备注")
    private String remark;

}
