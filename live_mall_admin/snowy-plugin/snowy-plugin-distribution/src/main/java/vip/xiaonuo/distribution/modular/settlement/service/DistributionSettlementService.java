package vip.xiaonuo.distribution.modular.settlement.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.common.pojo.dto.CommonPageParam;
import vip.xiaonuo.distribution.api.dto.UserOwnPromotionDTO;
import vip.xiaonuo.distribution.api.dto.UserOwnPromotionParamDTO;
import vip.xiaonuo.distribution.api.enums.SettlementType;
import vip.xiaonuo.distribution.modular.info.entity.DistributionInfo;
import vip.xiaonuo.distribution.modular.settlement.entity.DistributionSettlement;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/30 10:59
 */
public interface DistributionSettlementService extends IService<DistributionSettlement> {

    /**
     * 分页
     *
     * @param param 分页参数
     * @return 结果
     */
    Page<DistributionSettlement> page(CommonPageParam param);

    /**
     * 详情
     *
     * @param id id
     * @return 结果
     */
    DistributionSettlement detail(String id);

    /**
     * 创建结算
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param infoId     推广信息
     * @param remark     备注
     * @return 结算信息
     */
    DistributionSettlement createSettlement(String userId,
                                            String businessId,
                                            String infoId,
                                            String remark);

    /**
     * 创建结算
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param info       推广信息
     * @param remark     备注
     * @return 结算信息
     */
    DistributionSettlement createSettlement(String userId,
                                            String businessId,
                                            DistributionInfo info,
                                            String remark);

    /**
     * 创建结算
     *
     * @param userId     用户Id
     * @param promoterId 推广用户Id
     * @param businessId 业务Id
     * @param info       推广信息
     * @param remark     备注
     * @return 结算信息
     */
    DistributionSettlement createSettlement(String userId,
                                            String promoterId,
                                            String businessId,
                                            DistributionInfo info,
                                            String remark);


    /**
     * 手工结算
     *
     * @param id id
     */
    default void manualSettlement(String id) {
        DistributionSettlement detail = detail(id);
        detail.setSettlementType(SettlementType.MANUAL);
        settlement(detail);
    }

    /**
     * 结算
     *
     * @param id             参数
     * @param settlementType 积分结算方式
     */
    default void settlement(String id, SettlementType settlementType) {
        DistributionSettlement detail = detail(id);
        detail.setSettlementType(settlementType);
        settlement(detail);
    }

    /**
     * 结算
     *
     * @param settlement 参数
     */
    void settlement(DistributionSettlement settlement);

    /**
     * 结算正在等待的推广数据
     */
    void settlementWait();

    /**
     * 发放正在等待的积分
     */
    void commissionWait();

    /**
     * 我的推广
     * @param param 参数
     * @return 结果
     */
    UserOwnPromotionDTO userOwnPromotion(UserOwnPromotionParamDTO param);
}
