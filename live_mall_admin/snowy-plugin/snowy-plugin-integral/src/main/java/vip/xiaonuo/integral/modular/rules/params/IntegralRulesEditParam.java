package vip.xiaonuo.integral.modular.rules.params;

import io.swagger.annotations.ApiModelProperty;
import manage.enums.UserLimitType;
import vip.xiaonuo.common.enums.RefreshTimeType;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.integral.modular.rules.entity.IntegralRules;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 14:09
 */
public record IntegralRulesEditParam(
        @ApiModelProperty("ID")
        String id,

        @ApiModelProperty("排序")
        Integer sort,

        @ApiModelProperty("限制用户类型")
        UserLimitType userLimitType,

        @ApiModelProperty("用户ID")
        String userId,

        @ApiModelProperty("积分有效期")
        LocalDateTime validityPeriod,

        @ApiModelProperty("积分增加限制时间类型")
        RefreshTimeType limitTimeType,

        @ApiModelProperty("积分增加限制")
        BigDecimal limitIntegral,

        @ApiModelProperty("积分提现限制时间类型")
        RefreshTimeType withdrawalTimeType,

        @ApiModelProperty("积分提现限制")
        BigDecimal withdrawalIntegral,

        @ApiModelProperty("积分换现金比例")
        BigDecimal pointsToCashRatio,

        @ApiModelProperty("状态")
        SimpleStatus status,

        @ApiModelProperty("备注")
        String remark
) {
    public IntegralRules toBean() {
        IntegralRules integralRules = new IntegralRules();
        integralRules.setId(id);
        integralRules.setSort(sort);
        integralRules.setUserLimitType(userLimitType);
        integralRules.setUserId(userId);
        integralRules.setValidityPeriod(validityPeriod);
        integralRules.setLimitTimeType(limitTimeType);
        integralRules.setLimitIntegral(limitIntegral);
        integralRules.setWithdrawalTimeType(withdrawalTimeType);
        integralRules.setWithdrawalIntegral(withdrawalIntegral);
        integralRules.setPointsToCashRatio(pointsToCashRatio);
        integralRules.setStatus(status);
        integralRules.setRemark(remark);
        return integralRules;
    }
}
