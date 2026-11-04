package vip.xiaonuo.task.modular.task.service;

import vip.xiaonuo.task.modular.info.entity.TaskInfo;

import java.math.BigDecimal;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/12 22:42
 */
public interface TaskAuthService {

    /**
     * 注册
     *
     * @param userId   用户id
     * @param taskInfo 任务信息
     * @return 积分
     */
    BigDecimal authRegister(String userId, TaskInfo taskInfo);

    /**
     * 添加客服微信
     *
     * @param userId   用户id
     * @param taskInfo 任务信息
     * @return 积分
     */
    BigDecimal addCustomerWechat(String userId, TaskInfo taskInfo);
}
