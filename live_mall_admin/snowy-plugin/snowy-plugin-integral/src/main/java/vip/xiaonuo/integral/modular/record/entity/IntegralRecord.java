package vip.xiaonuo.integral.modular.record.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.common.pojo.CommonEntity;
import vip.xiaonuo.integral.api.enums.IntegralCategory;
import vip.xiaonuo.integral.api.enums.IntegralChangeType;
import vip.xiaonuo.integral.api.enums.IntegralOperationSource;
import vip.xiaonuo.integral.api.enums.IntegralSource;

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
@Table("integral_record")
public class IntegralRecord extends CommonEntity {

    @Id
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "积分信息Id")
    private String integralInfoId;

    @ApiModelProperty(value = "用户Id")
    private String userId;

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
