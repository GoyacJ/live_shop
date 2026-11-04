package vip.xiaonuo.task.modular.task.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.content.api.ContentApi;
import vip.xiaonuo.task.modular.info.entity.TaskInfo;
import vip.xiaonuo.task.modular.task.service.TaskVideoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/12 23:32
 */
@Slf4j
@Component
public class TaskVideoServiceImpl implements TaskVideoService {

    @Inject
    private ContentApi contentApi;

    @Inject
    private CommonCacheOperator commonCacheOperator;

    // 1分钟
    private static final int UNIT_SECONDS = 60;


    @Override
    public BigDecimal videoVisit(String userId, String businessId, TaskInfo taskInfo) {
        LocalDate today = LocalDate.now();
        String day = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String timeKey = String.format("video:watch:time:%s:%s:%s", userId, businessId, day);
        String rewardedKey = String.format("video:watch:rewarded:%s:%s:%s", userId, businessId, day);

        // 累加总观看时长
        Long watchTime = commonCacheOperator.getCache(timeKey, Long.class);

        if (SimpleStatus.ENABLE.equals(taskInfo.getIsRefresh())) {
            // 当前达到多少整分钟
            long totalMinutes = watchTime / UNIT_SECONDS;

            if (totalMinutes > taskInfo.getFrequencyLimit()) {
                totalMinutes = taskInfo.getFrequencyLimit();
            }

            // 获取已奖励的分钟数
            long rewardedMinutes = 0;
            String rewardedStr = commonCacheOperator.getCache(rewardedKey);
            if (rewardedStr != null) {
                rewardedMinutes = Long.parseLong(rewardedStr);
            }

            // 新增的可奖励分钟数
            long rewardableMinutes = totalMinutes - rewardedMinutes;

            if (rewardableMinutes > 0) {
                BigDecimal totalReward = taskInfo.getRewardIntegral().multiply(BigDecimal.valueOf(rewardableMinutes));
                commonCacheOperator.put(rewardedKey, String.valueOf(totalMinutes), 3600 * 24 * 2);
                return totalReward;
            }
        } else {
            // 当前达到多少整分钟
            long totalMinutes = watchTime / UNIT_SECONDS;

            // 获取已奖励的分钟数
            long rewardedMinutes = 0;
            String rewardedStr = commonCacheOperator.getCache(rewardedKey);
            if (rewardedStr != null) {
                rewardedMinutes = Long.parseLong(rewardedStr);
            }

            // 新增的可奖励分钟数
            long rewardableMinutes = totalMinutes - rewardedMinutes;

            if (rewardableMinutes > 0) {
                BigDecimal totalReward = taskInfo.getRewardIntegral().multiply(BigDecimal.valueOf(rewardableMinutes));
                commonCacheOperator.put(rewardedKey, String.valueOf(totalMinutes), 3600 * 24 * 2);
                return totalReward;
            }
        }
        return BigDecimal.ZERO;

    }

    @Override
    public BigDecimal videoShare(String userId, String businessId, TaskInfo taskInfo) {
        return taskInfo.getRewardIntegral();
    }

    @Override
    public BigDecimal videoSubscribe(String userId, String businessId, TaskInfo taskInfo) {
        return taskInfo.getRewardIntegral();
    }

    @Override
    public BigDecimal videoComment(String userId, String businessId, TaskInfo taskInfo) {
        return taskInfo.getRewardIntegral();
    }

    @Override
    public BigDecimal videoAdvertisement(String userId, String businessId, TaskInfo taskInfo) {
        return taskInfo.getRewardIntegral();
    }
}
