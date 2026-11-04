package vip.xiaonuo.mobile.modular.task.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.mobile.modular.task.service.TaskService;
import vip.xiaonuo.mobile.modular.task.vo.TaskInfoVO;
import vip.xiaonuo.task.api.TaskApi;
import vip.xiaonuo.task.api.dto.TaskInfoByUserDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/6 11:03
 */
@Slf4j
@Component
public class TaskServiceImpl implements TaskService {

    @Inject
    private TaskApi taskApi;

    @Override
    public List<TaskInfoVO> getTaskListByUserId(String userId) {
        List<TaskInfoByUserDTO> taskListByUser = taskApi.getTaskListByUser(userId);
        return taskListByUser.stream().map(dto -> {
            TaskInfoVO taskInfoVO = new TaskInfoVO();
            taskInfoVO.setTaskId(dto.taskId());
            taskInfoVO.setTaskName(dto.taskName());
            taskInfoVO.setTaskDesc(dto.taskDesc());
            taskInfoVO.setIcon(dto.icon());
            taskInfoVO.setSort(dto.sort());
            taskInfoVO.setTaskType(dto.taskType());
            taskInfoVO.setTaskCategory(dto.taskCategory());
            taskInfoVO.setTaskBusinessCategory(dto.taskBusinessCategory());
            taskInfoVO.setRewardIntegral(dto.rewardIntegral());
            taskInfoVO.setFrequencyLimit(dto.frequencyLimit());
            taskInfoVO.setRefreshTime(dto.refreshTime());
            taskInfoVO.setIsLimitTime(dto.isLimitTime());
            taskInfoVO.setValidityPeriodStart(dto.validityPeriodStart());
            taskInfoVO.setValidityPeriodEnd(dto.validityPeriodEnd());
            taskInfoVO.setJumpRouting(dto.jumpRouting());
            taskInfoVO.setIsComplete(dto.isComplete());
            taskInfoVO.setCompleteCount(dto.completeCount());
            return taskInfoVO;
        }).collect(Collectors.toList());
    }

    @Override
    public void signInByDay(String userId) {
        taskApi.signInByDay(userId);
    }
}
