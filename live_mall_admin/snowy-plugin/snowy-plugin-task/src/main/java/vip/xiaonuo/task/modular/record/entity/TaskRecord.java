package vip.xiaonuo.task.modular.record.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.common.enums.RefreshTimeType;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.common.pojo.CommonEntity;
import vip.xiaonuo.task.api.enums.TaskBusinessCategory;
import vip.xiaonuo.task.api.enums.TaskCategory;
import vip.xiaonuo.task.api.enums.TaskTriggerCondition;
import vip.xiaonuo.task.api.enums.TaskType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>任务</p>
 *
 * @author goya
 * @since 2025/5/21 10:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("task_record")
public class TaskRecord extends CommonEntity {

    @Id
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty("任务ID")
    private String taskId;

    @ApiModelProperty("操作时间")
    private LocalDateTime operateTime;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "任务类型")
    private TaskType taskType;

    @ApiModelProperty(value = "任务分类")
    private TaskCategory taskCategory;

    @ApiModelProperty(value = "任务业务分类")
    private TaskBusinessCategory taskBusinessCategory;

    @ApiModelProperty(value = "触发条件")
    private TaskTriggerCondition triggerCondition;

    @ApiModelProperty(value = "奖励积分")
    private BigDecimal rewardIntegral;

    @ApiModelProperty(value = "次数限制")
    private Integer frequencyLimit;

    @ApiModelProperty(value = "刷新时间")
    private RefreshTimeType refreshTime;

    @ApiModelProperty("是否刷新")
    private SimpleStatus isRefresh;

    @ApiModelProperty("是否限时")
    private SimpleStatus isLimitTime;

    @ApiModelProperty("任务有效期开始")
    private LocalDateTime validityPeriodStart;

    @ApiModelProperty("任务有效期截止")
    private LocalDateTime validityPeriodEnd;

    @ApiModelProperty("数据类型参数")
    private Integer numberParms;

    @ApiModelProperty("积分有效期")
    private LocalDateTime validityPeriod;

    @ApiModelProperty("执行结果")
    private SimpleStatus executeStatus;

    @ApiModelProperty("执行异常原因")
    private String errReason;
}
