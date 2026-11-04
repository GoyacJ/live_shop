package vip.xiaonuo.distribution.modular.record.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.common.pojo.dto.CommonPageParam;
import vip.xiaonuo.distribution.modular.record.entity.DistributionRecord;
import vip.xiaonuo.distribution.modular.record.mapper.DistributionRecordMapper;
import vip.xiaonuo.distribution.modular.record.service.DistributionRecordService;
import vip.xiaonuo.distribution.modular.settlement.entity.DistributionSettlement;

import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/30 11:00
 */
@Slf4j
@Component
public class DistributionRecordServiceImpl extends ServiceImpl<DistributionRecordMapper, DistributionRecord> implements DistributionRecordService {


    @Override
    public Page<DistributionRecord> page(CommonPageParam param) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(param.startCreateTime()) && ObjectUtil.isNotEmpty(param.endCreateTime())) {
            queryWrapper.between(DistributionRecord::getCreateTime, param.startCreateTime(), param.endCreateTime());
        }
        // 关键词查询
        if (ObjectUtil.isNotEmpty(param.searchKey())) {
            // sql注入处理
            queryWrapper.and(wq -> {
                wq.where(wq1 -> {
                    wq1.or(DistributionRecord::getUserId).like(param.searchKey())
                            .or(DistributionRecord::getSettlementType).like(param.searchKey());

                });
            });
        }
        if (ObjectUtil.isAllNotEmpty(param.sortField(), param.sortOrder())) {
            CommonSortOrderEnum.validate(param.sortOrder());
            queryWrapper.orderBy(CharSequenceUtil.toUnderlineCase(param.sortField()), param.sortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(DistributionRecord::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public void createRecord(DistributionSettlement settlement) {
        // 创建推广记录
        DistributionRecord distributionRecord = new DistributionRecord();
        distributionRecord.setUserId(settlement.getUserId());
        distributionRecord.setPromoterId(settlement.getPromoterId());
        distributionRecord.setType(settlement.getType());
        distributionRecord.setBusinessId(settlement.getBusinessId());
        distributionRecord.setOperationTime(LocalDateTime.now());
        distributionRecord.setInfoId(settlement.getInfoId());
        distributionRecord.setSettlementId(settlement.getId());
        distributionRecord.setSettlementStatus(settlement.getSettlementStatus());
        distributionRecord.setSettlementTime(settlement.getSettlementTime());
        distributionRecord.setRecommendCommission(settlement.getRecommendCommission());
        distributionRecord.setIntegral(settlement.getIntegral());
        distributionRecord.setSettlementType(settlement.getSettlementType());
        distributionRecord.setSettlementTimeType(settlement.getSettlementTimeType());
        distributionRecord.setCommissionTimeType(settlement.getCommissionTimeType());
        distributionRecord.setCommissionTime(settlement.getSettlementTime());
        distributionRecord.setCommissionTimeStatus(settlement.getCommissionTimeStatus());
        distributionRecord.setRemark(settlement.getRemark());
        this.save(distributionRecord);
    }

    @Override
    public DistributionRecord detail(String id) {
        return this.getById(id);
    }
}
