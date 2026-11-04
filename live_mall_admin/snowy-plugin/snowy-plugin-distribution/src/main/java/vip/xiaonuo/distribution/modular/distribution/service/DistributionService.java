package vip.xiaonuo.distribution.modular.distribution.service;

import vip.xiaonuo.distribution.api.dto.UserOwnPromotionDTO;
import vip.xiaonuo.distribution.api.dto.UserOwnPromotionParamDTO;
import vip.xiaonuo.distribution.api.enums.DistributionType;
import vip.xiaonuo.distribution.modular.settlement.entity.DistributionSettlement;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/30 17:57
 */
public interface DistributionService {

    /**
     * 推广
     *
     * @param userId     用户Id
     * @param type       推广类型
     * @param businessId 业务Id
     * @param remark     备注
     */
    void promotion(String userId, DistributionType type, String businessId, String remark);

    /**
     * 推广
     *
     * @param userId     用户Id
     * @param promoterId 推广用户Id
     * @param type       推广类型
     * @param businessId 业务Id
     * @param remark     备注
     */
    void promotion(String userId, String promoterId, DistributionType type, String businessId, String remark);

    /**
     * 结算
     *
     * @param settlement 参数
     */
    void settlement(DistributionSettlement settlement);

    /**
     * 我的推广
     * @param param 参数
     * @return 结果
     */
    UserOwnPromotionDTO userOwnPromotion(UserOwnPromotionParamDTO param);

}
