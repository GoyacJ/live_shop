package vip.xiaonuo.task.modular.task.service;

import vip.xiaonuo.task.api.dto.TaskInfoByUserDTO;
import vip.xiaonuo.task.api.enums.TaskBusinessCategory;

import java.util.List;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/31 18:41
 */
public interface TaskService {

    /**
     * 任务
     *
     * @param userId               用户Id
     * @param taskBusinessCategory 任务类型
     */
    default void task(String userId, TaskBusinessCategory taskBusinessCategory) {
        task(userId, userId, taskBusinessCategory);
    }

    /**
     * 任务
     *
     * @param userId               用户Id
     * @param businessId           业务Id
     * @param taskBusinessCategory 任务类型
     */
    void task(String userId, String businessId, TaskBusinessCategory taskBusinessCategory);

    /**
     * 获取任务列表
     *
     * @param userId 用户Id
     * @return 任务列表
     */
    List<TaskInfoByUserDTO> getTaskListByUserId(String userId);

    /**
     * 每日签到
     *
     * @param userId 用户Id
     */
    void signInByDay(String userId);
}
