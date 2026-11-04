package vip.xiaonuo.task.modular.task.service.impl;

import cn.hutool.core.collection.IterUtil;
import lombok.extern.slf4j.Slf4j;
import manage.UserApi;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.enums.RefreshTimeType;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.integral.api.IntegralApi;
import vip.xiaonuo.integral.api.dto.ChangeIntegralDTO;
import vip.xiaonuo.integral.api.enums.IntegralOperationSource;
import vip.xiaonuo.integral.api.enums.IntegralSource;
import vip.xiaonuo.task.api.TaskException;
import vip.xiaonuo.task.api.dto.TaskInfoByUserDTO;
import vip.xiaonuo.task.api.enums.TaskBusinessCategory;
import vip.xiaonuo.task.modular.info.entity.TaskInfo;
import vip.xiaonuo.task.modular.info.service.TaskInfoService;
import vip.xiaonuo.task.modular.record.entity.TaskRecord;
import vip.xiaonuo.task.modular.record.service.TaskRecordService;
import vip.xiaonuo.task.modular.task.service.*;
import vip.xiaonuo.task.modular.task.service.dto.TaskCacheDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static vip.xiaonuo.task.api.constants.TaskConstants.TASK_CACHE_PREFIX;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/31 18:41
 */
@Tran
@Slf4j
@Component
public class TaskServiceImpl implements TaskService {

    @Inject
    private TaskInfoService taskInfoService;

    @Inject
    private TaskRecordService taskRecordService;

    @Inject
    private IntegralApi integralApi;

    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Inject
    private UserApi userApi;

    @Inject
    private TaskSignInService signInService;

    @Inject
    private TaskShopService taskShopService;

    @Inject
    private TaskAuthService taskAuthService;

    @Inject
    private TaskLiveService taskLiveService;

    @Inject
    private TaskVideoService taskVideoService;

    @Override
    public void task(String userId, String businessId, TaskBusinessCategory taskBusinessCategory) {

        if (StringUtils.isBlank(userId)) {
            return;
        }

        List<TaskInfo> taskInfos = taskInfoService.getTaskInfoByBusinessCategory(taskBusinessCategory, businessId);

        if (IterUtil.isEmpty(taskInfos)) {
            return;
        }

        taskInfos.forEach(taskInfo -> {
            TaskRecord record;
            try {
                // 执行任务
                BigDecimal integral = executeTask(userId, businessId, taskInfo);

                // 更改奖励积分
                taskInfo.setRewardIntegral(integral);

                // 记录日志
                record = taskRecordService.createRecord(userId, taskInfo);
            } catch (TaskException e) {
                // 记录日志
                record = taskRecordService.createRecord(userId, taskInfo, SimpleStatus.DISABLE, e.getMessage());
            }

            if (Objects.nonNull(record)
                    && record.getRewardIntegral().compareTo(BigDecimal.ZERO) > 0
                    && SimpleStatus.ENABLE.equals(record.getExecuteStatus())) {
                // 增加积分
                addTaskIntegral(userId, record);
            }
        });
    }

    @Override
    public List<TaskInfoByUserDTO> getTaskListByUserId(String userId) {
        List<TaskInfo> taskInfoList = taskInfoService.getTaskInfoList();
        return taskInfoList.stream().map(taskInfo -> {
            int completeCount = userTaskCompleteCount(userId, taskInfo, LocalDateTime.now());
            SimpleStatus completeStatus;
            if (SimpleStatus.ENABLE.equals(taskInfo.getIsRefresh())
                    && Objects.nonNull(taskInfo.getFrequencyLimit())
                    && completeCount < taskInfo.getFrequencyLimit()) {
                completeStatus = SimpleStatus.DISABLE;
            } else {
                completeStatus = SimpleStatus.ENABLE;
            }

            return new TaskInfoByUserDTO(
                    taskInfo.getId(),
                    taskInfo.getTaskName(),
                    taskInfo.getTaskIntroduction(),
                    taskInfo.getIcon(),
                    taskInfo.getSort(),
                    taskInfo.getTaskType(),
                    taskInfo.getTaskCategory(),
                    taskInfo.getTaskBusinessCategory(),
                    taskInfo.getRewardIntegral(),
                    taskInfo.getFrequencyLimit(),
                    taskInfo.getRefreshTime(),
                    taskInfo.getIsLimitTime(),
                    taskInfo.getValidityPeriodStart(),
                    taskInfo.getValidityPeriodEnd(),
                    taskInfo.getJumpRouting(),
                    userId,
                    completeStatus,
                    completeCount
            );
        }).collect(Collectors.toList());
    }

    @Override
    public void signInByDay(String userId) {
        this.task(userId, TaskBusinessCategory.SIGN_IN_DAY);
    }

    /**
     * 用户任务次数完成
     *
     * @param userId   用户Id
     * @param taskInfo 任务信息
     * @return 完成此书
     */
    private int userTaskCompleteCount(String userId, TaskInfo taskInfo, LocalDateTime time) {
        // 获取任务记录
        if (SimpleStatus.ENABLE.equals(taskInfo.getIsRefresh())) {
            int size;
            switch (taskInfo.getRefreshTime()) {
                case DAY -> size = this.taskRecordService.getSuccessTaskRecordByUser(
                        userId,
                        taskInfo.getId(),
                        RefreshTimeType.DAY.getStartTime(),
                        time
                ).size();
                case WEEK -> size = this.taskRecordService.getSuccessTaskRecordByUser(
                        userId,
                        taskInfo.getId(),
                        RefreshTimeType.WEEK.getStartTime(),
                        time
                ).size();
                case MONTH -> size = this.taskRecordService.getSuccessTaskRecordByUser(
                        userId,
                        taskInfo.getId(),
                        RefreshTimeType.MONTH.getStartTime(),
                        time
                ).size();
                case YEAR -> size = this.taskRecordService.getSuccessTaskRecordByUser(
                        userId,
                        taskInfo.getId(),
                        RefreshTimeType.YEAR.getStartTime(),
                        time
                ).size();
                default -> size = this.taskRecordService.getSuccessTaskRecordByUser(userId, taskInfo.getId()).size();
            }
            return size;
        } else {
            return this.taskRecordService.getSuccessTaskRecordByUser(userId, taskInfo.getId()).size();
        }
    }


    private void addTaskIntegral(String userId, TaskRecord record) {
        integralApi.changeIntegral(ChangeIntegralDTO.increaseIntegral(
                userId,
                record.getRewardIntegral(),
                userId,
                IntegralSource.TASK,
                record.getId(),
                IntegralOperationSource.SYSTEM,
                record.getValidityPeriod(),
                "任务积分,任务类型: " + record.getTaskType() + ",任务分类: " + record.getTaskCategory() + "任务业务分类: " + record.getTaskBusinessCategory() + ",任务,用户Id: " + userId
        ));
    }

    /**
     * 执行任务
     *
     * @param userId     用户id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     */
    private BigDecimal executeTask(String userId, String businessId, TaskInfo taskInfo) {
        BigDecimal integral = taskInfo.getRewardIntegral();

        // 限时校验
        checkLimitTime(taskInfo);

        String key;
        if (StringUtils.isEmpty(businessId)) {
            key = TASK_CACHE_PREFIX + taskInfo.getTaskBusinessCategory().name() + ":" + userId;
        } else {
            key = TASK_CACHE_PREFIX + taskInfo.getTaskBusinessCategory().name() + ":" + userId + ":" + businessId;
        }

        // 校验刷新次数
        checkRefreshTime(userId, businessId, key, taskInfo);

        switch (taskInfo.getTaskBusinessCategory()) {
            case AUTH_REGISTER -> {
                // 注册
                return authRegister(userId, taskInfo);
            }
            case ADD_CUSTOMER_WECHAT -> {
                // 注册
                return addCustomerWechat(userId, taskInfo);
            }
            case SIGN_IN_DAY -> {
                // 每日签到
                return signInDay(userId, taskInfo);
            }
            case SIGN_IN_CONTINUOUS -> {
                // 连续签到
                return signInContinuous(userId, taskInfo);
            }
            case SHOP_VISIT -> {
                // 商品浏览
                return shopVisit(userId, businessId, taskInfo);
            }
            case SHOP_BUY -> {
                // 商品购买
                return shopBuy(userId, businessId, taskInfo);
            }
            case SHOP_SHARE -> {
                // 商品分享
                return shopShare(userId, businessId, taskInfo);
            }
            case SHOP_COMMENT -> {
                // 商品评论
                return shopComment(userId, businessId, taskInfo);
            }
            case LIVE_VISIT -> {
                // 直播观看
                return liveVisit(userId, businessId, taskInfo);
            }
            case LIVE_SHARE -> {
                // 直播分享
                return liveShare(userId, businessId, taskInfo);
            }
            case LIVE_SUBSCRIBE -> {
                // 直播订阅
                return liveSubscribe(userId, businessId, taskInfo);
            }
            case LIVE_COMMENT -> {
                // 直播评论
                return liveComment(userId, businessId, taskInfo);
            }
            case LIVE_REWARD -> {
                // 直播打赏
                return liveReward(userId, businessId, taskInfo);
            }
            case LIVE_BUY -> {
                // 直播商品购买
                return liveBuy(userId, businessId, taskInfo);
            }
            case LIVE_GIFT -> {
                // 直播送礼
                return liveGift(userId, businessId, taskInfo);
            }
            case LIVE_ACTIVITY -> {
                // 直播活动
                return liveActivity(userId, businessId, taskInfo);
            }
            case VIDEO_VISIT -> {
                // 视频观看
                return videoVisit(userId, businessId, taskInfo);
            }
            case VIDEO_SHARE -> {
                // 视频分享
                return videoShare(userId, businessId, taskInfo);
            }
            case VIDEO_SUBSCRIBE -> {
                // 视频订阅
                return videoSubscribe(userId, businessId, taskInfo);
            }
            case VIDEO_COMMENT -> {
                // 视频评论
                return videoComment(userId, businessId, taskInfo);
            }
            case VIDEO_ADVERTISEMENT -> {
                // 视频广告
                return videoAdvertisement(userId, businessId, taskInfo);
            }
            case ADVERTISEMENT_VISIT -> {
                // 广告观看
                return advertisementVisit(userId, businessId, taskInfo);
            }
            default -> throw new TaskException("不支持的任务");
        }
    }

    /**
     * 视频广告
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    private BigDecimal videoAdvertisement(String userId, String businessId, TaskInfo taskInfo) {
        return taskVideoService.videoAdvertisement(userId, businessId, taskInfo);
    }

    /**
     * 视频评论
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    private BigDecimal videoComment(String userId, String businessId, TaskInfo taskInfo) {
        return taskVideoService.videoComment(userId, businessId, taskInfo);
    }

    /**
     * 视频订阅
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    private BigDecimal videoSubscribe(String userId, String businessId, TaskInfo taskInfo) {
        return taskVideoService.videoSubscribe(userId, businessId, taskInfo);
    }

    /**
     * 视频分享
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    private BigDecimal videoShare(String userId, String businessId, TaskInfo taskInfo) {
        return taskVideoService.videoShare(userId, businessId, taskInfo);
    }

    /**
     * 视频观看
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    private BigDecimal videoVisit(String userId, String businessId, TaskInfo taskInfo) {
        return taskVideoService.videoVisit(userId, businessId, taskInfo);
    }

    /**
     * 直播商品购买
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    private BigDecimal liveBuy(String userId, String businessId, TaskInfo taskInfo) {
        return taskLiveService.liveBuy(userId, businessId, taskInfo);
    }

    /**
     * 直播观看
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    private BigDecimal liveVisit(String userId, String businessId, TaskInfo taskInfo) {
        return taskLiveService.liveVisit(userId, businessId, taskInfo);
    }

    /**
     * 直播礼物
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    private BigDecimal liveGift(String userId, String businessId, TaskInfo taskInfo) {
        return taskLiveService.liveGift(userId, businessId, taskInfo);
    }

    /**
     * 直播活动
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    private BigDecimal liveActivity(String userId, String businessId, TaskInfo taskInfo) {
        return taskLiveService.liveActivity(userId, businessId, taskInfo);
    }

    /**
     * 直播打赏
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    private BigDecimal liveReward(String userId, String businessId, TaskInfo taskInfo) {
        return taskLiveService.liveReward(userId, businessId, taskInfo);
    }

    /**
     * 评论直播
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    private BigDecimal liveComment(String userId, String businessId, TaskInfo taskInfo) {
        return taskLiveService.liveComment(userId, businessId, taskInfo);
    }

    /**
     * 订阅直播
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    private BigDecimal liveSubscribe(String userId, String businessId, TaskInfo taskInfo) {
        return taskLiveService.liveSubscribe(userId, businessId, taskInfo);
    }

    /**
     * 分享直播
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    private BigDecimal liveShare(String userId, String businessId, TaskInfo taskInfo) {
        return taskLiveService.liveShare(userId, businessId, taskInfo);
    }

    /**
     * 注册
     *
     * @param userId   用户id
     * @param taskInfo 任务信息
     * @return 积分
     */
    private BigDecimal authRegister(String userId, TaskInfo taskInfo) {
        return taskAuthService.authRegister(userId, taskInfo);
    }

    /**
     * 添加客户微信
     *
     * @param userId   用户Id
     * @param taskInfo 任务信息
     * @return 积分
     */
    private BigDecimal addCustomerWechat(String userId, TaskInfo taskInfo) {
        return taskAuthService.addCustomerWechat(userId, taskInfo);
    }

    /**
     * 商品评论
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    private BigDecimal shopComment(String userId, String businessId, TaskInfo taskInfo) {
        return taskShopService.comment(userId, businessId, taskInfo);
    }

    /**
     * 商品浏览
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    private BigDecimal shopVisit(String userId, String businessId, TaskInfo taskInfo) {
        return taskShopService.visit(userId, businessId, taskInfo);
    }

    /**
     * 商品分享
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    private BigDecimal shopShare(String userId, String businessId, TaskInfo taskInfo) {
        return taskShopService.shopShare(userId, businessId, taskInfo);
    }

    /**
     * 商品购买
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    private BigDecimal shopBuy(String userId, String businessId, TaskInfo taskInfo) {
        return taskShopService.shopBuy(userId, businessId, taskInfo);
    }

    /**
     * 广告观看
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param taskInfo   任务信息
     * @return 积分
     */
    private BigDecimal advertisementVisit(String userId, String businessId, TaskInfo taskInfo) {
        BigDecimal rewardIntegral = taskInfo.getRewardIntegral();
        return rewardIntegral;
    }

    /**
     * 每日签到
     *
     * @param userId   用户Id
     * @param taskInfo 任务信息
     * @return 积分
     */
    private BigDecimal signInDay(String userId, TaskInfo taskInfo) {

        boolean signed = signInService.signIn(userId);
        if (!signed) {
            throw new TaskException("今日已签到");
        }

        // 连续签到
        task(userId, TaskBusinessCategory.SIGN_IN_CONTINUOUS);
        return taskInfo.getRewardIntegral();
    }


    /**
     * 连续签到
     *
     * @param userId   用户Id
     * @param taskInfo 任务信息
     * @return 积分
     */
    private BigDecimal signInContinuous(String userId, TaskInfo taskInfo) {
        BigDecimal rewardIntegral = taskInfo.getRewardIntegral();
        boolean signed = signInService.signInContinuous(userId);
        if (signed) {
            return rewardIntegral;
        }
        return BigDecimal.ZERO;
    }

    /**
     * 校验刷新次数
     *
     * @param userId     用户Id
     * @param businessId 业务Id
     * @param key        key
     * @param taskInfo   任务信息
     */
    private void checkRefreshTime(String userId, String businessId, String key, TaskInfo taskInfo) {

        if (SimpleStatus.DISABLE.equals(taskInfo.getIsRefresh())) {
            return;
        }

        TaskCacheDTO dto = new TaskCacheDTO();
        dto.setLastOperationTime(LocalDateTime.now());
        dto.setUserId(userId);
        dto.setIntegral(taskInfo.getRewardIntegral());
        dto.setBusinessId(businessId);
        if (Boolean.TRUE.equals(commonCacheOperator.exists(key))) {
            TaskCacheDTO cache = commonCacheOperator.getCache(key, TaskCacheDTO.class);
            dto.setCount(cache.getCount() + 1);
        } else {
            dto.setCount(1);
        }
        try {
            // 判断是否达到次数限制
            if (dto.getCount() > taskInfo.getFrequencyLimit()) {
                log.warn("用户{}的{}任务达到次数限制", userId, taskInfo.getTaskBusinessCategory().getName());
                throw new TaskException("任务达到次数限制");
            }
        } finally {
            commonCacheOperator.put(key, dto, taskInfo.getRefreshTime().toCalculateTimeOfIntegralLimit());
        }
    }


    /**
     * 校验限时
     *
     * @param taskInfo 任务信息
     */
    private void checkLimitTime(TaskInfo taskInfo) {
        SimpleStatus isLimitTime = taskInfo.getIsLimitTime();
        if (Objects.nonNull(isLimitTime) && SimpleStatus.ENABLE.equals(isLimitTime)) {
            LocalDateTime validityPeriodStart = taskInfo.getValidityPeriodStart();
            LocalDateTime validityPeriodEnd = taskInfo.getValidityPeriodEnd();
            LocalDateTime now = LocalDateTime.now();

            // 校验时间区间有效性
            if (Objects.nonNull(validityPeriodStart) && now.isBefore(validityPeriodStart)) {
                throw new TaskException("任务尚未开始，有效起始时间: " + validityPeriodStart);
            }

            if (Objects.nonNull(validityPeriodEnd) && now.isAfter(validityPeriodEnd)) {
                throw new TaskException("任务已过期，有效结束时间: " + validityPeriodEnd);
            }
        }
    }
}
