package vip.xiaonuo.task.api;

import vip.xiaonuo.task.api.dto.TaskInfoByUserDTO;
import vip.xiaonuo.task.api.enums.TaskBusinessCategory;

import java.util.List;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/31 18:12
 */
public interface TaskApi {

    /**
     * 任务
     * @param userId 用户Id
     * @param taskBusinessCategory 任务类型
     */
    void task(String userId, TaskBusinessCategory taskBusinessCategory);

    /**
     * 任务
     *
     * @param userId               用户Id
     * @param businessId           业务Id
     * @param taskBusinessCategory 任务类型
     */
    void task(String userId, String businessId, TaskBusinessCategory taskBusinessCategory);

    /**
     * 获取用户任务列表
     * @param userId 用户Id
     * @return 任务列表
     */
    List<TaskInfoByUserDTO> getTaskListByUser(String userId);

    /**
     * 每日签到
     * @param userId 用户Id
     */
    void signInByDay(String userId);
}
