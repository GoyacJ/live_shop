package vip.xiaonuo.task.modular.task.service;

import vip.xiaonuo.task.modular.info.entity.TaskInfo;

import java.math.BigDecimal;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/12 23:32
 */
public interface TaskVideoService {

    /**
     * 视频观看
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    BigDecimal videoVisit(String userId, String businessId, TaskInfo taskInfo);

    /**
     * 视频分享
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    BigDecimal videoShare(String userId, String businessId, TaskInfo taskInfo);

    /**
     * 视频订阅
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    BigDecimal videoSubscribe(String userId, String businessId, TaskInfo taskInfo);

    /**
     * 视频评论
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    BigDecimal videoComment(String userId, String businessId, TaskInfo taskInfo);

    /**
     * 视频广告
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    BigDecimal videoAdvertisement(String userId, String businessId, TaskInfo taskInfo);
}
