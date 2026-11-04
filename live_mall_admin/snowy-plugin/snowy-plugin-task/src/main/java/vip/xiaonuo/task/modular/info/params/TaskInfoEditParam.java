package vip.xiaonuo.task.modular.info.params;

import io.swagger.annotations.ApiModelProperty;
import vip.xiaonuo.common.enums.RefreshTimeType;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.task.api.enums.TaskBusinessCategory;
import vip.xiaonuo.task.api.enums.TaskCategory;
import vip.xiaonuo.task.api.enums.TaskTriggerCondition;
import vip.xiaonuo.task.api.enums.TaskType;
import vip.xiaonuo.task.modular.info.entity.TaskInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 15:24
 */
public record TaskInfoEditParam(
        @ApiModelProperty(value = "ID")
        String id,

        @ApiModelProperty("排序")
        Integer sort,

        @ApiModelProperty("图标")
        String icon,

        @ApiModelProperty(value = "任务名称")
        String taskName,

        @ApiModelProperty(value = "任务介绍")
        String taskIntroduction,

        @ApiModelProperty(value = "任务类型")
        TaskType taskType,

        @ApiModelProperty(value = "任务分类")
        TaskCategory taskCategory,

        @ApiModelProperty(value = "任务业务分类")
        TaskBusinessCategory taskBusinessCategory,

        @ApiModelProperty(value = "触发条件")
        TaskTriggerCondition triggerCondition,

        @ApiModelProperty(value = "奖励积分")
        BigDecimal rewardIntegral,

        @ApiModelProperty(value = "次数限制")
        Integer frequencyLimit,

        @ApiModelProperty(value = "刷新时间")
        RefreshTimeType refreshTime,

        @ApiModelProperty("是否刷新")
        SimpleStatus isRefresh,

        @ApiModelProperty("是否限时")
        SimpleStatus isLimitTime,

        @ApiModelProperty("任务有效期开始")
        LocalDateTime validityPeriodStart,

        @ApiModelProperty("任务有效期截止")
        LocalDateTime validityPeriodEnd,

        @ApiModelProperty("数据类型参数")
        Integer numberParms,

        @ApiModelProperty("积分有效期")
        LocalDateTime validityPeriod,

        @ApiModelProperty("跳转路由")
        String jumpRouting,

        @ApiModelProperty("状态")
        SimpleStatus status
) {
    public TaskInfo toBean() {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setId(id);
        taskInfo.setSort(sort);
        taskInfo.setIcon(icon);
        taskInfo.setTaskName(taskName);
        taskInfo.setTaskIntroduction(taskIntroduction);
        taskInfo.setTaskType(taskType);
        taskInfo.setTaskCategory(taskCategory);
        taskInfo.setTaskBusinessCategory(taskBusinessCategory);
        taskInfo.setTriggerCondition(triggerCondition);
        taskInfo.setRewardIntegral(rewardIntegral);
        taskInfo.setFrequencyLimit(frequencyLimit);
        taskInfo.setRefreshTime(refreshTime);
        taskInfo.setIsRefresh(isRefresh);
        taskInfo.setIsLimitTime(isLimitTime);
        taskInfo.setValidityPeriodStart(validityPeriodStart);
        taskInfo.setValidityPeriodEnd(validityPeriodEnd);
        taskInfo.setNumberParms(numberParms);
        taskInfo.setValidityPeriod(validityPeriod);
        taskInfo.setJumpRouting(jumpRouting);
        taskInfo.setStatus(status);
        return taskInfo;
    }
}
