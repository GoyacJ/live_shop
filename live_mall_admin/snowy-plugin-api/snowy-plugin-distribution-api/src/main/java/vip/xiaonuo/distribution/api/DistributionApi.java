package vip.xiaonuo.distribution.api;

import vip.xiaonuo.distribution.api.dto.UserOwnPromotionDTO;
import vip.xiaonuo.distribution.api.dto.UserOwnPromotionParamDTO;
import vip.xiaonuo.distribution.api.enums.DistributionType;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/21 14:44
 */
public interface DistributionApi {

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
     * 我的推广
     * @param param 参数
     * @return 结果
     */
    UserOwnPromotionDTO userOwnPromotion(UserOwnPromotionParamDTO param);

}
