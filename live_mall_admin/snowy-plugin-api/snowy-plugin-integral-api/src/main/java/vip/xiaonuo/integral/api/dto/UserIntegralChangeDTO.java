package vip.xiaonuo.integral.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.integral.api.enums.IntegralCategory;
import vip.xiaonuo.integral.api.enums.IntegralChangeType;
import vip.xiaonuo.integral.api.enums.IntegralOperationSource;
import vip.xiaonuo.integral.api.enums.IntegralSource;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/16 15:12
 */
@Data
public class UserIntegralChangeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户Id")
    private String userId;

    @ApiModelProperty(value = "积分Id")
    private String integralId;

    @ApiModelProperty(value = "积分记录Id")
    private String integralRecordId;

    @ApiModelProperty(value = "积分来源")
    private IntegralSource integralSource;

    @ApiModelProperty(value = "积分来源Id")
    private String integralSourceId;

    @ApiModelProperty("变动类型")
    private IntegralChangeType changeType;

    @ApiModelProperty("积分分类")
    private IntegralCategory integralCategory;

    @ApiModelProperty("操作时间")
    private LocalDateTime operationTime;

    @ApiModelProperty(value = "积分")
    private BigDecimal integral;

    @ApiModelProperty(value = "操作来源")
    private IntegralOperationSource operationSource;

    @ApiModelProperty(value = "操作人ID")
    private String operationUserId;

    @ApiModelProperty(value = "积分余额")
    private BigDecimal integralBalance;

    @ApiModelProperty(value = "冻结积分余额")
    private BigDecimal frozenIntegralBalance;

    @ApiModelProperty(value = "过期积分余额")
    private BigDecimal expireIntegralBalance;

    @ApiModelProperty(value = "过期积分清除状态")
    private SimpleStatus clearExpireStatus;

    @ApiModelProperty("积分有效期")
    private LocalDateTime validityPeriod;

    @ApiModelProperty(value = "备注")
    private String remark;
}
