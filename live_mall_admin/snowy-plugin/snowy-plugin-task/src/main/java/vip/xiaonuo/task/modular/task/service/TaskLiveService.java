package vip.xiaonuo.task.modular.task.service;

import vip.xiaonuo.task.modular.info.entity.TaskInfo;

import java.math.BigDecimal;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/12 23:26
 */
public interface TaskLiveService {

    /**
     * 直播购买
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    BigDecimal liveBuy(String userId, String businessId, TaskInfo taskInfo);

    /**
     * 直播观看
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    BigDecimal liveVisit(String userId, String businessId, TaskInfo taskInfo);

    /**
     * 直播礼物
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    BigDecimal liveGift(String userId, String businessId, TaskInfo taskInfo);

    /**
     * 直播分享
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    BigDecimal liveShare(String userId, String businessId, TaskInfo taskInfo);

    /**
     * 直播打赏
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    BigDecimal liveReward(String userId, String businessId, TaskInfo taskInfo);

    /**
     * 直播订阅
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    BigDecimal liveSubscribe(String userId, String businessId, TaskInfo taskInfo);

    /**
     * 直播评论
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    BigDecimal liveComment(String userId, String businessId, TaskInfo taskInfo);

    /**
     * 直播活动
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    BigDecimal liveActivity(String userId, String businessId, TaskInfo taskInfo);
}
