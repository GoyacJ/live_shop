package vip.xiaonuo.distribution.modular.info.params;

import io.swagger.annotations.ApiModelProperty;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.distribution.api.enums.*;
import vip.xiaonuo.distribution.modular.info.entity.DistributionInfo;

import java.math.BigDecimal;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 14:09
 */
public record DistributionInfoAddParam(

        @ApiModelProperty(value = "推广名称")
        String name,

        @ApiModelProperty(value = "推广类型")
        DistributionType type,

        @ApiModelProperty(value = "推广简介")
        String introduction,

        @ApiModelProperty(value = "推广LOGO")
        String logo,

        @ApiModelProperty(value = "推广状态")
        SimpleStatus status,

        @ApiModelProperty(value = "排序")
        Integer sort,

        @ApiModelProperty(value = "积分返佣方式")
        RecommendCommission recommendCommission,

        @ApiModelProperty(value = "返佣积分/比例")
        BigDecimal commissionIntegral,

        @ApiModelProperty(value = "积分结算方式")
        SettlementType settlementType,

        @ApiModelProperty(value = "积分结算时间类型")
        SettlementTimeType settlementTimeType,

        @ApiModelProperty(value = "积分发放时间类型")
        CommissionTimeType commissionTimeType,

        @ApiModelProperty(value = "积分发放时间")
        String commissionFixTime
) {
    public DistributionInfo toBean() {
        DistributionInfo distributionInfo = new DistributionInfo();
        distributionInfo.setName(name);
        distributionInfo.setType(type);
        distributionInfo.setIntroduction(introduction);
        distributionInfo.setLogo(logo);
        distributionInfo.setStatus(status);
        distributionInfo.setSort(sort);
        distributionInfo.setRecommendCommission(recommendCommission);
        distributionInfo.setCommissionIntegral(commissionIntegral);
        distributionInfo.setSettlementType(settlementType);
        distributionInfo.setSettlementTimeType(settlementTimeType);
        distributionInfo.setCommissionTimeType(commissionTimeType);
        distributionInfo.setCommissionFixTime(commissionFixTime);
        return distributionInfo;
    }
}
