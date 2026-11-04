package vip.xiaonuo.distribution.modular.record.entity;

import api.order.enums.SettlementStatus;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.common.pojo.CommonEntity;
import vip.xiaonuo.distribution.api.enums.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>推广记录</p>
 *
 * @author goya
 * @since 2025/5/30 10:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("distribution_record")
public class DistributionRecord extends CommonEntity {

    @Id
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "推广人Id")
    private String promoterId;

    @ApiModelProperty(value = "推广类型")
    private DistributionType type;

    @ApiModelProperty(value = "业务Id")
    private String businessId;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime operationTime;

    @ApiModelProperty("推广信息Id")
    private String infoId;

    @ApiModelProperty("结算Id")
    private String settlementId;

    @ApiModelProperty(value = "结算状态")
    private SettlementStatus settlementStatus;

    @ApiModelProperty(value = "结算时间")
    private LocalDateTime settlementTime;

    @ApiModelProperty(value = "积分返佣方式")
    private RecommendCommission recommendCommission;

    @ApiModelProperty(value = "返佣积分")
    private BigDecimal integral;

    @ApiModelProperty(value = "积分结算方式")
    private SettlementType settlementType;

    @ApiModelProperty(value = "积分结算时间类型")
    private SettlementTimeType settlementTimeType;

    @ApiModelProperty(value = "积分发放时间类型")
    private CommissionTimeType commissionTimeType;

    @ApiModelProperty(value = "积分发放状态")
    private CommissionTimeStatus commissionTimeStatus;

    @ApiModelProperty(value = "积分发放时间")
    private LocalDateTime commissionTime;

    @ApiModelProperty(value = "备注")
    private String remark;

}
