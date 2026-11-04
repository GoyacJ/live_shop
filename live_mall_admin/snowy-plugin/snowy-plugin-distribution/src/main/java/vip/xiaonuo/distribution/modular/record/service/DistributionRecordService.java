package vip.xiaonuo.distribution.modular.record.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.common.pojo.dto.CommonPageParam;
import vip.xiaonuo.distribution.modular.record.entity.DistributionRecord;
import vip.xiaonuo.distribution.modular.settlement.entity.DistributionSettlement;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/30 11:00
 */
public interface DistributionRecordService extends IService<DistributionRecord> {

    /**
     * 分页
     *
     * @param param 分页参数
     * @return 结果
     */
    Page<DistributionRecord> page(CommonPageParam param);

    /**
     * 创建推广记录
     *
     * @param settlement 结算单
     */
    void createRecord(DistributionSettlement settlement);

    /**
     * 详情
     *
     * @param id id
     * @return 结果
     */
    DistributionRecord detail(String id);
}
