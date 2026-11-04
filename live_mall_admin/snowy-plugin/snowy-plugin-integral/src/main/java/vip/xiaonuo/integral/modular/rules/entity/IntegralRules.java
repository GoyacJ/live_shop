package vip.xiaonuo.integral.modular.rules.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import manage.enums.UserLimitType;
import vip.xiaonuo.common.enums.RefreshTimeType;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.common.pojo.CommonEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>积分规则</p>
 *
 * @author goya
 * @since 2025/5/21 10:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("integral_rules")
public class IntegralRules extends CommonEntity {

    @Id
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("限制用户类型")
    private UserLimitType userLimitType;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("积分有效期")
    private LocalDateTime validityPeriod;

    @ApiModelProperty("积分增加限制时间类型")
    private RefreshTimeType limitTimeType;

    @ApiModelProperty("积分增加限制")
    private BigDecimal limitIntegral;

    @ApiModelProperty("积分提现限制时间类型")
    private RefreshTimeType withdrawalTimeType;

    @ApiModelProperty("积分提现限制")
    private BigDecimal withdrawalIntegral;

    @ApiModelProperty("积分换现金比例")
    private BigDecimal pointsToCashRatio;

    @ApiModelProperty("状态")
    private SimpleStatus status;

    @ApiModelProperty("备注")
    private String remark;

}
