package vip.xiaonuo.task.modular.task.service.impl;

import lombok.extern.slf4j.Slf4j;
import manage.UserApi;
import manage.dto.UserInfoDTO;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.enums.RefreshTimeType;
import vip.xiaonuo.task.api.TaskException;
import vip.xiaonuo.task.api.enums.TaskTriggerCondition;
import vip.xiaonuo.task.modular.info.entity.TaskInfo;
import vip.xiaonuo.task.modular.task.service.TaskAuthService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/12 22:42
 */
@Slf4j
@Component
public class TaskAuthServiceImpl implements TaskAuthService {

    @Inject
    private UserApi userApi;


    @Inject
    private CommonCacheOperator commonCacheOperator;

    private String key = "task:auth:register:invitation:";

    @Override
    public BigDecimal authRegister(String userId, TaskInfo taskInfo) {
        if (TaskTriggerCondition.INVITATION.equals(taskInfo.getTriggerCondition())) {
            return invitationAuth(userId, taskInfo);
        }

        UserInfoDTO user = userApi.getUserById(userId);
        if (Objects.isNull(user)) {
            throw new TaskException("用户注册失败");
        }
        return taskInfo.getRewardIntegral();
    }

    @Override
    public BigDecimal addCustomerWechat(String userId, TaskInfo taskInfo) {
        return taskInfo.getRewardIntegral();
    }

    private BigDecimal invitationAuth(String userId, TaskInfo taskInfo) {
        LocalDateTime now = LocalDateTime.now();
        long promotionCount = userApi.getPromotionCount(userId, RefreshTimeType.MONTH.getStartTime(), now);
        if (promotionCount >= taskInfo.getNumberParms()) {

            // 检查次数
            if (Boolean.TRUE.equals(commonCacheOperator.exists(key + userId))) {
                return BigDecimal.ZERO;
            }

            commonCacheOperator.put(key + userId, userId, taskInfo.getRefreshTime().toCalculateTimeOfIntegralLimit());
            return taskInfo.getRewardIntegral();
        }
        return BigDecimal.ZERO;
    }
}
