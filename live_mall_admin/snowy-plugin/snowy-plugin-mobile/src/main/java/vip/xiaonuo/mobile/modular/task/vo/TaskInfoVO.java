package vip.xiaonuo.mobile.modular.task.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import vip.xiaonuo.common.enums.RefreshTimeType;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.task.api.enums.TaskBusinessCategory;
import vip.xiaonuo.task.api.enums.TaskCategory;
import vip.xiaonuo.task.api.enums.TaskType;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/6 11:05
 */
@Data
public class TaskInfoVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务ID")
    private String taskId;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "任务介绍")
    private String taskDesc;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty(value = "任务类型")
    private TaskType taskType;

    @ApiModelProperty(value = "任务分类")
    private TaskCategory taskCategory;

    @ApiModelProperty(value = "任务业务分类")
    private TaskBusinessCategory taskBusinessCategory;

    @ApiModelProperty(value = "奖励积分")
    private BigDecimal rewardIntegral;

    @ApiModelProperty(value = "次数限制")
    private Integer frequencyLimit;

    @ApiModelProperty(value = "刷新时间")
    private RefreshTimeType refreshTime;

    @ApiModelProperty("是否限时")
    private SimpleStatus isLimitTime;

    @ApiModelProperty("任务有效期开始")
    private LocalDateTime validityPeriodStart;

    @ApiModelProperty("任务有效期截止")
    private LocalDateTime validityPeriodEnd;

    @ApiModelProperty("跳转路由")
    private String jumpRouting;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("是否完成")
    private SimpleStatus isComplete;

    @ApiModelProperty("完成次数")
    private Integer completeCount;
}
