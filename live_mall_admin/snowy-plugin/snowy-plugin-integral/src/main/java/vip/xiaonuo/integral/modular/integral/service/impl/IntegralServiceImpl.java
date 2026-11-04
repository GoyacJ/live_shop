package vip.xiaonuo.integral.modular.integral.service.impl;

import com.mybatisflex.core.paginate.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.integral.api.dto.ChangeIntegralDTO;
import vip.xiaonuo.integral.api.dto.IntegralInfoDTO;
import vip.xiaonuo.integral.api.dto.UserIntegralChangeDTO;
import vip.xiaonuo.integral.api.dto.UserIntegralChangePageDTO;
import vip.xiaonuo.integral.api.enums.IntegralCategory;
import vip.xiaonuo.integral.api.enums.IntegralChangeType;
import vip.xiaonuo.integral.api.enums.IntegralOperationSource;
import vip.xiaonuo.integral.api.enums.IntegralSource;
import vip.xiaonuo.integral.api.exception.IntegralException;
import vip.xiaonuo.integral.modular.info.entity.IntegralInfo;
import vip.xiaonuo.integral.modular.info.service.IntegralInfoService;
import vip.xiaonuo.integral.modular.integral.service.IntegralService;
import vip.xiaonuo.integral.modular.record.entity.IntegralRecord;
import vip.xiaonuo.integral.modular.record.service.IntegralRecordService;
import vip.xiaonuo.integral.modular.rules.service.IntegralRulesService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static vip.xiaonuo.integral.api.enums.IntegralCategory.*;
import static vip.xiaonuo.integral.api.enums.IntegralChangeType.*;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/30 16:38
 */
@Tran
@Slf4j
@Component
public class IntegralServiceImpl implements IntegralService {

    @Inject
    private IntegralInfoService integralInfoService;

    @Inject
    private IntegralRecordService integralRecordService;

    @Inject
    private IntegralRulesService integralRulesService;


    @Override
    public boolean changeIntegral(ChangeIntegralDTO dto) {
        IntegralInfo integralInfo = this.integralInfoService.getIntegralInfoByUserId(dto.userId());
        if (Objects.isNull(integralInfo)) {
            throw new IntegralException("积分信息错误");
        }

        boolean res;

        switch (dto.changeType()) {
            case INCREASE -> res = increaseIntegral(dto.userId(),
                    dto.integral(),
                    dto.integralCategory(),
                    dto.operationUserId(),
                    dto.integralSource(),
                    dto.integralSourceId(),
                    dto.operationSource(),
                    dto.validityPeriod(),
                    dto.remark());
            case DECREASE -> res = decreaseIntegral(dto.userId(),
                    dto.integral(),
                    dto.integralCategory(),
                    dto.operationUserId(),
                    dto.integralSource(),
                    dto.integralSourceId(),
                    dto.operationSource(),
                    dto.remark());
            case ADJUST -> res = adjustmentIntegral(dto.userId(),
                    dto.integral(),
                    dto.integralCategory(),
                    dto.operationUserId(),
                    dto.integralSource(),
                    dto.integralSourceId(),
                    dto.operationSource(),
                    dto.remark());
            case UNLOCK -> res = unlockFrozenIntegral(dto.userId(),
                    dto.integral(),
                    dto.operationUserId(),
                    dto.integralSource(),
                    dto.integralSourceId(),
                    dto.operationSource(),
                    dto.remark());
            case CHANGE_STATUS -> res = changeIntegralStatus(dto.userId(),
                    dto.status(),
                    dto.operationUserId(),
                    dto.integralSource(),
                    dto.integralSourceId(),
                    dto.operationSource(),
                    dto.remark());
            case EXPIRE -> res = expireIntegral(dto.userId(),
                    dto.integral(),
                    dto.operationUserId(),
                    dto.integralSource(),
                    dto.integralSourceId(),
                    dto.operationSource(),
                    dto.remark());
            default -> throw new IntegralException("暂不支持的操作");
        }

        return res;
    }

    /**
     * 积分过期
     *
     * @param userId           用户Id
     * @param integral         积分
     * @param modifyUserId     操作用户Id
     * @param integralSource   来源
     * @param integralSourceId 来源Id
     * @param operationSource  操作来源
     * @param remark           备注
     * @return 结果
     */
    private boolean expireIntegral(String userId,
                                   BigDecimal integral,
                                   String modifyUserId,
                                   IntegralSource integralSource,
                                   String integralSourceId,
                                   IntegralOperationSource operationSource,
                                   String remark) {
        if (integralInfoService.decreaseIntegral(userId, integral, modifyUserId) && integralInfoService.increaseExpireIntegral(userId, integral, modifyUserId)) {
            return saveRecord(userId, integral, modifyUserId, EXPIRE, INTEGRAL, integralSource, integralSourceId, operationSource, null, SimpleStatus.ENABLE, remark);
        }
        return false;
    }

    /**
     * 清除过期积分
     *
     * @param userId           用户Id
     * @param modifyUserId     操作用户Id
     * @param integralSource   来源
     * @param integralSourceId 来源Id
     * @param operationSource  操作来源
     * @param remark           备注
     * @return 结果
     */
    public boolean clearExpiredIntegral(String userId,
                                        String modifyUserId,
                                        IntegralSource integralSource,
                                        String integralSourceId,
                                        IntegralOperationSource operationSource,
                                        String remark) {
        IntegralInfo integralInfo = integralInfoService.getIntegralInfoByUserId(userId);
        if (integralInfoService.decreaseExpireIntegral(userId, integralInfo.getExpireIntegral(), modifyUserId)) {
            return saveRecord(userId, integralInfo.getExpireIntegral(), modifyUserId, ADJUST, EXPIRE_INTEGRAL,
                    integralSource, integralSourceId, operationSource, null, SimpleStatus.ENABLE, remark);
        }
        return false;
    }

    @Override
    public boolean withdrawalPoints(String userId,
                                    String modifyUserId,
                                    BigDecimal integral,
                                    IntegralOperationSource operationSource,
                                    String remark) {
        return false;
    }

    @Override
    public BigDecimal getIntegralByUserId(String userId) {
        return getIntegralInfoByUserId(userId).integral();
    }

    @Override
    public IntegralInfoDTO getIntegralInfoByUserId(String userId) {
        IntegralInfo info = integralInfoService.getIntegralInfoByUserId(userId);
        return new IntegralInfoDTO(
                info.getId(),
                info.getUserId(),
                info.getIntegral(),
                info.getFrozenIntegral(),
                info.getExpireIntegral(),
                info.getLastModifyTime(),
                info.getLastModifyUserId(),
                info.getStatus()
        );
    }

    @Override
    public String createIntegral(String userId) {
        return createIntegral(userId, userId, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    @Override
    public String createIntegral(String userId, String modifyUserId, BigDecimal integral, BigDecimal frozenIntegral) {
        String integralId = integralInfoService.createIntegral(userId, modifyUserId, integral, frozenIntegral);
        IntegralOperationSource operationSource;
        if (StringUtils.equals(userId, modifyUserId)) {
            operationSource = IntegralOperationSource.USER;
        } else {
            operationSource = IntegralOperationSource.ADMIN;
        }
        saveRecord(userId,
                integral,
                modifyUserId,
                ADJUST,
                INTEGRAL,
                IntegralSource.SYSTEM,
                null,
                operationSource,
                null,
                SimpleStatus.ENABLE,
                "积分首次创建");

        return integralId;
    }

    @Override
    public boolean deleteIntegralByUserId(String userId) {
        if (integralInfoService.deleteIntegralInfoByUserId(userId)) {
            return integralRecordService.deleteIntegralRecordByUserId(userId);
        }
        return false;
    }

    @Override
    public Page<UserIntegralChangeDTO> userIntegralChangePage(UserIntegralChangePageDTO param) {
        return integralRecordService.userIntegralChangePage(param);
    }

    /**
     * 修改积分状态
     *
     * @param userId           用户Id
     * @param status           积分状态
     * @param modifyUserId     操作用户Id
     * @param integralSource   来源
     * @param integralSourceId 来源Id
     * @param operationSource  操作来源
     * @param remark           备注
     * @return 结果
     */
    private boolean changeIntegralStatus(String userId,
                                         SimpleStatus status,
                                         String modifyUserId,
                                         IntegralSource integralSource,
                                         String integralSourceId,
                                         IntegralOperationSource operationSource,
                                         String remark) {
        IntegralInfo integralInfo = integralInfoService.getIntegralInfoByUserId(userId);
        if (integralInfoService.updateIntegralStatus(userId, status, modifyUserId)) {
            return saveRecord(userId, integralInfo.getExpireIntegral(), modifyUserId, IntegralChangeType.CHANGE_STATUS,
                    null, integralSource, integralSourceId, operationSource, null, null, remark);
        }
        return false;
    }

    /**
     * 解锁积分
     *
     * @param userId           用户Id
     * @param integral         积分
     * @param modifyUserId     操作用户Id
     * @param integralSource   来源
     * @param integralSourceId 来源Id
     * @param operationSource  操作来源
     * @param remark           备注
     * @return 结果
     */
    private boolean unlockFrozenIntegral(String userId,
                                         BigDecimal integral,
                                         String modifyUserId,
                                         IntegralSource integralSource,
                                         String integralSourceId,
                                         IntegralOperationSource operationSource,
                                         String remark) {
        if (integralInfoService.unLockFrozenIntegral(userId, integral, modifyUserId)) {
            return saveRecord(userId, integral, modifyUserId, IntegralChangeType.UNLOCK, FROZEN_INTEGRAL,
                    integralSource, integralSourceId, operationSource, null, null, remark);
        }
        return false;
    }

    /**
     * 调整积分
     *
     * @param userId           用户Id
     * @param integral         积分
     * @param category         积分分类
     * @param modifyUserId     操作用户Id
     * @param integralSource   来源
     * @param integralSourceId 来源Id
     * @param operationSource  操作来源
     * @param remark           备注
     * @return 结果
     */
    private boolean adjustmentIntegral(String userId,
                                       BigDecimal integral,
                                       IntegralCategory category,
                                       String modifyUserId,
                                       IntegralSource integralSource,
                                       String integralSourceId,
                                       IntegralOperationSource operationSource,
                                       String remark) {
        switch (category) {
            case INTEGRAL -> {
                if (integralInfoService.updateIntegral(userId, integral, modifyUserId)) {
                    return saveRecord(userId, integral, modifyUserId, ADJUST, INTEGRAL,
                            integralSource, integralSourceId, operationSource, null, null, remark);
                }
                return false;
            }
            case FROZEN_INTEGRAL -> {
                if (integralInfoService.updateFrozenIntegral(userId, integral, modifyUserId)) {
                    return saveRecord(userId, integral, modifyUserId, ADJUST, FROZEN_INTEGRAL,
                            integralSource, integralSourceId, operationSource, null, null, remark);
                }
                return false;
            }
            case EXPIRE_INTEGRAL -> {
                if (integralInfoService.updateExpireIntegral(userId, integral, modifyUserId)) {
                    return saveRecord(userId, integral, modifyUserId, ADJUST, EXPIRE_INTEGRAL,
                            integralSource, integralSourceId, operationSource, null, null, remark);
                }
                return false;
            }
            default -> throw new IntegralException("调整积分失败,非法类型");
        }
    }

    /**
     * 增加积分
     *
     * @param userId           用户Id
     * @param integral         积分
     * @param category         积分分类
     * @param modifyUserId     操作用户Id
     * @param integralSource   来源
     * @param integralSourceId 来源Id
     * @param operationSource  操作来源
     * @param validityPeriod   积分有效期
     * @param remark           备注
     * @return 结果
     */
    private boolean increaseIntegral(String userId,
                                     BigDecimal integral,
                                     IntegralCategory category,
                                     String modifyUserId,
                                     IntegralSource integralSource,
                                     String integralSourceId,
                                     IntegralOperationSource operationSource,
                                     LocalDateTime validityPeriod,
                                     String remark) {
        switch (category) {
            case INTEGRAL -> {
                if (integralRulesService.checkIncreaseRules(userId, integral)) {
                    if (integralInfoService.increaseIntegral(userId, integral, modifyUserId)) {
                        return saveRecord(userId, integral, modifyUserId, INCREASE, INTEGRAL,
                                integralSource, integralSourceId, operationSource, validityPeriod, SimpleStatus.DISABLE, remark);
                    }
                    return false;
                }
                log.info("增加积分失败,增加积分不符合增加规则");
                return false;
            }
            case FROZEN_INTEGRAL -> {
                if (integralInfoService.increaseFrozenIntegral(userId, integral, modifyUserId)) {
                    return saveRecord(userId, integral, modifyUserId, INCREASE, FROZEN_INTEGRAL,
                            integralSource, integralSourceId, operationSource, null, null, remark);
                }
                return false;
            }
            case EXPIRE_INTEGRAL -> {
                if (integralInfoService.increaseExpireIntegral(userId, integral, modifyUserId)) {
                    return saveRecord(userId, integral, modifyUserId, INCREASE, EXPIRE_INTEGRAL,
                            integralSource, integralSourceId, operationSource, null, null, remark);
                }
                return false;
            }
            default -> throw new IntegralException("增加积分失败,非法类型");
        }
    }

    /**
     * 减少积分
     *
     * @param userId           用户Id
     * @param integral         积分
     * @param category         积分分类
     * @param modifyUserId     操作用户Id
     * @param integralSource   来源
     * @param integralSourceId 来源Id
     * @param operationSource  操作来源
     * @param remark           备注
     * @return 结果
     */
    private boolean decreaseIntegral(String userId,
                                     BigDecimal integral,
                                     IntegralCategory category,
                                     String modifyUserId,
                                     IntegralSource integralSource,
                                     String integralSourceId,
                                     IntegralOperationSource operationSource,
                                     String remark) {
        switch (category) {
            case INTEGRAL -> {
                if (integralInfoService.decreaseIntegral(userId, integral, modifyUserId)) {
                    return saveRecord(
                            userId,
                            integral,
                            modifyUserId,
                            DECREASE,
                            INTEGRAL,
                            integralSource,
                            integralSourceId,
                            operationSource,
                            null,
                            null,
                            remark);
                }
                return false;
            }
            case FROZEN_INTEGRAL -> {
                if (integralInfoService.decreaseFrozenIntegral(userId, integral, modifyUserId)) {
                    return saveRecord(userId, integral, modifyUserId, DECREASE, FROZEN_INTEGRAL,
                            integralSource, integralSourceId, operationSource, null, null, remark);
                }
                return false;
            }
            case EXPIRE_INTEGRAL -> {
                if (integralInfoService.decreaseExpireIntegral(userId, integral, modifyUserId)) {
                    return saveRecord(userId, integral, modifyUserId, DECREASE, EXPIRE_INTEGRAL,
                            integralSource, integralSourceId, operationSource, null, null, remark);
                }
                return false;
            }
            default -> throw new IntegralException("减少积分失败,非法类型");
        }
    }


    /**
     * 日志保存
     *
     * @param userId            用户Id
     * @param integral          积分
     * @param modifyUserId      操作用户Id
     * @param changeType        积分变动类型
     * @param category          积分分类
     * @param integralSource    来源
     * @param integralSourceId  来源Id
     * @param operationSource   操作来源
     * @param validityPeriod    积分有效期
     * @param clearExpireStatus 过期积分清除状态
     * @param remark            备注
     * @return 结果
     */
    private boolean saveRecord(String userId,
                               BigDecimal integral,
                               String modifyUserId,
                               IntegralChangeType changeType,
                               IntegralCategory category,
                               IntegralSource integralSource,
                               String integralSourceId,
                               IntegralOperationSource operationSource,
                               LocalDateTime validityPeriod,
                               SimpleStatus clearExpireStatus,
                               String remark) {
        IntegralInfo integralInfo = integralInfoService.getIntegralInfoByUserId(userId);
        IntegralRecord integralRecord = new IntegralRecord();
        integralRecord.setUserId(userId);
        integralRecord.setIntegralInfoId(integralInfo.getId());
        integralRecord.setIntegralSource(integralSource);
        integralRecord.setIntegralSourceId(integralSourceId);
        integralRecord.setChangeType(changeType);
        integralRecord.setIntegralCategory(category);
        integralRecord.setOperationTime(integralInfo.getLastModifyTime());
        integralRecord.setIntegral(integral);
        integralRecord.setIntegralBalance(integralInfo.getIntegral());
        integralRecord.setFrozenIntegralBalance(integralInfo.getFrozenIntegral());
        integralRecord.setExpireIntegralBalance(integralInfo.getExpireIntegral());
        integralRecord.setOperationSource(operationSource);
        integralRecord.setOperationUserId(modifyUserId);
        integralRecord.setRemark(changeType.getName() + (Objects.nonNull(remark) ? ": " + remark : ""));

        // 设置积分有效期
        if (Objects.nonNull(validityPeriod)) {
            integralRecord.setValidityPeriod(validityPeriod);
        } else {
            LocalDateTime validityTime = this.integralRulesService.getValidityTime(userId);
            if (Objects.nonNull(validityTime)) {
                integralRecord.setValidityPeriod(validityTime);
            }
        }
        integralRecord.setClearExpireStatus(clearExpireStatus);
        return integralRecordService.addIntegralRecord(integralRecord);
    }
}
