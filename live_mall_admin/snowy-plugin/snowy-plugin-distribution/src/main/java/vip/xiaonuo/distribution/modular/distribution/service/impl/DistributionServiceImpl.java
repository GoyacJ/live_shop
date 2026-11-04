package vip.xiaonuo.distribution.modular.distribution.service.impl;

import lombok.extern.slf4j.Slf4j;
import manage.UserApi;
import manage.dto.UserInfoDTO;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.distribution.api.dto.UserOwnPromotionDTO;
import vip.xiaonuo.distribution.api.dto.UserOwnPromotionParamDTO;
import vip.xiaonuo.distribution.api.enums.DistributionType;
import vip.xiaonuo.distribution.api.exception.DistributionException;
import vip.xiaonuo.distribution.modular.distribution.service.DistributionService;
import vip.xiaonuo.distribution.modular.info.entity.DistributionInfo;
import vip.xiaonuo.distribution.modular.info.service.DistributionInfoService;
import vip.xiaonuo.distribution.modular.record.service.DistributionRecordService;
import vip.xiaonuo.distribution.modular.settlement.entity.DistributionSettlement;
import vip.xiaonuo.distribution.modular.settlement.service.DistributionSettlementService;

import java.util.Objects;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/30 17:57
 */
@Slf4j
@Component
public class DistributionServiceImpl implements DistributionService {

    @Inject
    private DistributionInfoService distributionInfoService;

    @Inject
    private DistributionSettlementService distributionSettlementService;

    @Inject
    private DistributionRecordService distributionRecordService;

    @Inject
    private UserApi userApi;

    @Override
    public void promotion(String userId, DistributionType type, String businessId, String remark) {
        UserInfoDTO user = userApi.getUserById(userId);
        if (Objects.isNull(user)) {
            log.error("创建结算: 用户不存在, userId: {}", userId);
            throw new DistributionException("用户不存在");
        }
        promotion(userId, user.getPId(), type, businessId, remark);
    }


    @Override
    public void promotion(String userId, String promoterId, DistributionType type, String businessId, String remark) {
        if (StringUtils.isNotEmpty(promoterId)) {
            DistributionInfo info = distributionInfoService.getDistributionInfoByType(type);
            if (Objects.isNull(info)) {
                log.error("推广: 推广信息不存在, type: {}", type);
                throw new DistributionException("推广信息不存在");
            }

            // 创建结算信息
            DistributionSettlement settlement = distributionSettlementService.createSettlement(userId, promoterId, businessId, info, remark);

            // 结算
            distributionSettlementService.settlement(settlement);

            distributionRecordService.createRecord(settlement);
        }
    }

    @Override
    public void settlement(DistributionSettlement settlement) {
        // 结算
        distributionSettlementService.settlement(settlement);
        distributionRecordService.createRecord(settlement);
    }

    @Override
    public UserOwnPromotionDTO userOwnPromotion(UserOwnPromotionParamDTO param) {
        return distributionSettlementService.userOwnPromotion(param);
    }

}
