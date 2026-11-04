package vip.xiaonuo.mobile.modular.task.service;

import vip.xiaonuo.mobile.modular.task.vo.TaskInfoVO;

import java.util.List;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/6 11:03
 */
public interface TaskService {

    /**
     * 获取任务列表
     *
     * @param userId 用户Id
     * @return 任务列表
     */
    List<TaskInfoVO> getTaskListByUserId(String userId);

    /**
     * 每日签到
     *
     * @param userId 用户Id
     */
    void signInByDay(String userId);
}
