package vip.xiaonuo.task.modular.task.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.live.api.live.LiveApi;
import vip.xiaonuo.task.modular.info.entity.TaskInfo;
import vip.xiaonuo.task.modular.task.service.TaskLiveService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/12 23:26
 */
@Slf4j
@Component
public class TaskLiveServiceImpl implements TaskLiveService {

    @Inject
    private LiveApi liveApi;

    @Inject
    private CommonCacheOperator commonCacheOperator;

    // 2 小时
    private static final int UNIT_SECONDS = 7200;

    @Override
    public BigDecimal liveBuy(String userId, String businessId, TaskInfo taskInfo) {
        return taskInfo.getRewardIntegral();
    }

    @Override
    public BigDecimal liveVisit(String userId, String businessId, TaskInfo taskInfo) {

        long watchTime = queryWatchTime(userId, businessId, "D", LocalDateTime.now());
        String rewardKey = String.format("live:rewarded:count:%s:%s:%s", userId, businessId, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        if (watchTime <= 0) {
            return BigDecimal.ZERO;
        }

        if (SimpleStatus.ENABLE.equals(taskInfo.getIsRefresh())) {
            // 当前满足的整倍数奖励次数
            long rewardableUnits = watchTime / UNIT_SECONDS;

            if (rewardableUnits > taskInfo.getFrequencyLimit()) {
                rewardableUnits = taskInfo.getFrequencyLimit();
            }

            // 已领取过的奖励次数（默认0）
            long rewardedCount = 0;
            String oldVal = commonCacheOperator.getCache(rewardKey);
            if (oldVal != null) {
                rewardedCount = Long.parseLong(oldVal);
            }

            long newRewardUnits = rewardableUnits - rewardedCount;
            if (newRewardUnits > 0) {

                BigDecimal totalReward = taskInfo.getRewardIntegral().multiply(BigDecimal.valueOf(newRewardUnits));
                // 更新已奖励单位数
                commonCacheOperator.put(rewardKey, String.valueOf(rewardableUnits), 3600 * 24 * 2);
                return totalReward;
            }
        } else {
            // 当前满足的整倍数奖励次数
            long rewardableUnits = watchTime / UNIT_SECONDS;

            // 已领取过的奖励次数（默认0）
            long rewardedCount = 0;
            String oldVal = commonCacheOperator.getCache(rewardKey);
            if (oldVal != null) {
                rewardedCount = Long.parseLong(oldVal);
            }

            long newRewardUnits = rewardableUnits - rewardedCount;
            if (newRewardUnits > 0) {

                BigDecimal totalReward = taskInfo.getRewardIntegral().multiply(BigDecimal.valueOf(newRewardUnits));
                // 更新已奖励单位数
                commonCacheOperator.put(rewardKey, String.valueOf(rewardableUnits), 3600 * 24 * 2);
                return totalReward;
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * 查询某用户某直播维度下的观看时长
     *
     * @param userId 用户ID
     * @param liveId 直播ID
     * @param type   D/W/M
     * @param time   时间
     */
    public long queryWatchTime(String userId, String liveId, String type, LocalDateTime time) {

        String key = "";

        if (StringUtils.equals("D", type)) {
            key = "live:watch:time:" + userId + ":" + liveId + type + time.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        } else if (StringUtils.equals("W", type)) {
            WeekFields weekFields = WeekFields.of(Locale.CHINA);
            int weekNum = time.get(weekFields.weekOfWeekBasedYear());
            key = "live:watch:time:" + userId + ":" + liveId + type + weekNum;
        } else if (StringUtils.equals("M", type)) {
            key = "live:watch:time:" + userId + ":" + liveId + type + time.format(DateTimeFormatter.ofPattern("yyyyMM"));
        }

        if (StringUtils.isEmpty(key)) {
            return 0L;
        }
        Long val = commonCacheOperator.getCache(key, Long.class);
        return val == null ? 0L : val;
    }

    @Override
    public BigDecimal liveGift(String userId, String businessId, TaskInfo taskInfo) {
        return taskInfo.getRewardIntegral();
    }

    @Override
    public BigDecimal liveShare(String userId, String businessId, TaskInfo taskInfo) {
        return taskInfo.getRewardIntegral();
    }

    @Override
    public BigDecimal liveReward(String userId, String businessId, TaskInfo taskInfo) {
        return taskInfo.getRewardIntegral();
    }

    @Override
    public BigDecimal liveSubscribe(String userId, String businessId, TaskInfo taskInfo) {
        return taskInfo.getRewardIntegral();
    }

    @Override
    public BigDecimal liveComment(String userId, String businessId, TaskInfo taskInfo) {
        return taskInfo.getRewardIntegral();
    }

    @Override
    public BigDecimal liveActivity(String userId, String businessId, TaskInfo taskInfo) {
        return taskInfo.getRewardIntegral();
    }
}
