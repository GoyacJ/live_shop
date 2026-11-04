package vip.xiaonuo.distribution.api;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.distribution.api.dto.UserOwnPromotionDTO;
import vip.xiaonuo.distribution.api.dto.UserOwnPromotionParamDTO;
import vip.xiaonuo.distribution.api.enums.DistributionType;
import vip.xiaonuo.distribution.modular.distribution.service.DistributionService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/30 17:49
 */
@Slf4j
@Component
public class DistributionApiImpl implements DistributionApi {

    @Inject
    private DistributionService distributionService;

    @Override
    public void promotion(String userId, DistributionType type, String businessId, String remark) {
        distributionService.promotion(userId, type, businessId, remark);
    }

    @Override
    public void promotion(String userId, String promoterId, DistributionType type, String businessId, String remark) {
        distributionService.promotion(userId, promoterId, type, businessId, remark);
    }

    @Override
    public UserOwnPromotionDTO userOwnPromotion(UserOwnPromotionParamDTO param) {
        return distributionService.userOwnPromotion(param);
    }
}
