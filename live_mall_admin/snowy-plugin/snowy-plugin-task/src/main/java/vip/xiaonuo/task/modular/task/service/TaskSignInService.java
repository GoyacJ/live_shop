package vip.xiaonuo.task.modular.task.service;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/12 21:31
 */
public interface TaskSignInService {

    /**
     * 签到
     *
     * @param userId   用户Id
     * @return 是否签到
     */
    boolean signIn(String userId);

    /**
     * 获取连续签到天数
     *
     * @param userId 用户Id
     * @return 连续签到天数
     */
    int getContinuousSignDays(String userId);

    /**
     * 连续签到领取奖励
     *
     * @param userId 用户Id
     * @return 是否领取奖励
     */
    boolean signInContinuous(String userId);
}
