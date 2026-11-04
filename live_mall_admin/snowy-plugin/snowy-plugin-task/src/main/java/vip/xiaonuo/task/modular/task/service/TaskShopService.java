package vip.xiaonuo.task.modular.task.service;

import vip.xiaonuo.task.modular.info.entity.TaskInfo;

import java.math.BigDecimal;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/12 22:13
 */
public interface TaskShopService {

    /**
     * 商品浏览
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    BigDecimal visit(String userId, String businessId, TaskInfo taskInfo);

    /**
     * 商品分享
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    BigDecimal shopShare(String userId, String businessId, TaskInfo taskInfo);

    /**
     * 商品评论
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    BigDecimal comment(String userId, String businessId, TaskInfo taskInfo);

    /**
     * 商品购买
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    BigDecimal shopBuy(String userId, String businessId, TaskInfo taskInfo);
}
