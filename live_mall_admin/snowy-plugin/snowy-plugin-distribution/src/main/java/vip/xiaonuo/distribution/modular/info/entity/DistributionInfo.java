package vip.xiaonuo.distribution.modular.info.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.common.pojo.CommonEntity;
import vip.xiaonuo.distribution.api.enums.*;

import java.math.BigDecimal;

/**
 * <p>推广信息</p>
 *
 * @author goya
 * @since 2025/5/30 10:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("distribution_info")
public class DistributionInfo extends CommonEntity {

    @Id
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "推广名称")
    private String name;

    @ApiModelProperty(value = "推广类型")
    private DistributionType type;

    @ApiModelProperty(value = "推广简介")
    private String introduction;

    @ApiModelProperty(value = "推广LOGO")
    private String logo;

    @ApiModelProperty(value = "推广状态")
    private SimpleStatus status;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "积分返佣方式")
    private RecommendCommission recommendCommission;

    @ApiModelProperty(value = "返佣积分/比例")
    private BigDecimal commissionIntegral;

    @ApiModelProperty(value = "积分结算方式")
    private SettlementType settlementType;

    @ApiModelProperty(value = "积分结算时间类型")
    private SettlementTimeType settlementTimeType;

    @ApiModelProperty(value = "积分发放时间类型")
    private CommissionTimeType commissionTimeType;

    @ApiModelProperty(value = "积分发放时间")
    private String commissionFixTime;
}
