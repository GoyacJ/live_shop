package vip.xiaonuo.task.api;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.task.api.dto.TaskInfoByUserDTO;
import vip.xiaonuo.task.api.enums.TaskBusinessCategory;
import vip.xiaonuo.task.modular.task.service.TaskService;

import java.util.List;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/31 18:12
 */
@Tran
@Slf4j
@Component
public class TaskApiImpl implements TaskApi {

    @Inject
    private TaskService taskService;

    @Override
    public void task(String userId, TaskBusinessCategory taskBusinessCategory) {
        taskService.task(userId, taskBusinessCategory);
    }

    @Override
    public void task(String userId, String businessId, TaskBusinessCategory taskBusinessCategory) {
        taskService.task(userId, businessId, taskBusinessCategory);
    }

    @Override
    public List<TaskInfoByUserDTO> getTaskListByUser(String userId) {
        return taskService.getTaskListByUserId(userId);
    }

    @Override
    public void signInByDay(String userId) {
        taskService.signInByDay(userId);
    }
}
