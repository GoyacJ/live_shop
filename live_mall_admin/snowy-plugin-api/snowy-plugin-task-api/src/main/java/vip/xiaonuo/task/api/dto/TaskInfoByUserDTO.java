package vip.xiaonuo.task.api.dto;

import vip.xiaonuo.common.enums.RefreshTimeType;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.task.api.enums.TaskBusinessCategory;
import vip.xiaonuo.task.api.enums.TaskCategory;
import vip.xiaonuo.task.api.enums.TaskType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/6 11:07
 */
public record TaskInfoByUserDTO(
        String taskId,
        String taskName,
        String taskDesc,
        String icon,
        Integer sort,
        TaskType taskType,
        TaskCategory taskCategory,
        TaskBusinessCategory taskBusinessCategory,
        BigDecimal rewardIntegral,
        Integer frequencyLimit,
        RefreshTimeType refreshTime,
        SimpleStatus isLimitTime,
        LocalDateTime validityPeriodStart,
        LocalDateTime validityPeriodEnd,
        String jumpRouting,
        String userId,
        SimpleStatus isComplete,
        Integer completeCount
) {
}
