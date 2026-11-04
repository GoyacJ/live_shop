package vip.xiaonuo.distribution.modular.settlement.entity;

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
 * <p>推广结算</p>
 *
 * @author goya
 * @since 2025/5/30 10:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("distribution_settlement")
public class DistributionSettlement extends CommonEntity {

    @Id
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "推广类型")
    private DistributionType type;

    @ApiModelProperty(value = "推广信息Id")
    private String infoId;

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty(value = "推广人Id")
    private String promoterId;

    @ApiModelProperty(value = "积分返佣方式")
    private RecommendCommission recommendCommission;

    @ApiModelProperty(value = "返佣积分/比例")
    private BigDecimal commissionIntegral;

    @ApiModelProperty(value = "积分结算方式")
    private SettlementType settlementType;

    @ApiModelProperty(value = "结算状态")
    private SettlementStatus settlementStatus;

    @ApiModelProperty(value = "积分结算时间类型")
    private SettlementTimeType settlementTimeType;

    @ApiModelProperty(value = "积分发放时间类型")
    private CommissionTimeType commissionTimeType;

    @ApiModelProperty(value = "积分发放时间")
    private String commissionFixTime;

    @ApiModelProperty(value = "积分发放状态")
    private CommissionTimeStatus commissionTimeStatus;

    @ApiModelProperty(value = "积分发放时间")
    private LocalDateTime commissionTime;

    @ApiModelProperty(value = "结算时间")
    private LocalDateTime settlementTime;

    @ApiModelProperty(value = "结算积分")
    private BigDecimal integral;

    @ApiModelProperty(value = "业务ID")
    private String businessId;

    @ApiModelProperty(value = "备注")
    private String remark;
}
