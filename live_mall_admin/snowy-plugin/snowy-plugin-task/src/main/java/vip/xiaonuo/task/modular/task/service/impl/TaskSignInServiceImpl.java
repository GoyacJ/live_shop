package vip.xiaonuo.task.modular.task.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.task.modular.task.service.TaskSignInService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/12 21:31
 */
@Slf4j
@Component
public class TaskSignInServiceImpl implements TaskSignInService {

    public static final String SIGN_IN_KEY_PREFIX = "sign_in:";
    // 是否已经领奖的 Redis Key 前缀
    private static final String REWARD_KEY_PREFIX = "sign:rewarded:";
    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Override
    public boolean signIn(String userId) {
        LocalDate today = LocalDate.now();
        int dayIndex = today.getDayOfMonth() - 1;
        String bitmapKey = getSignKey(userId, today);

        boolean alreadySigned = commonCacheOperator.getBit(bitmapKey, dayIndex);
        if (alreadySigned) {
            // 今日已签到
            return false;
        }

        // 设置当天签到位
        commonCacheOperator.setBit(bitmapKey, dayIndex, true);
        // 90天过期
        commonCacheOperator.expire(bitmapKey, 3600 * 24 * 90);
        return true;
    }

    @Override
    public int getContinuousSignDays(String userId) {
        LocalDate today = LocalDate.now();
        int day = today.getDayOfMonth();
        String key = getSignKey(userId, today);

        int count = 0;
        for (int i = day - 1; i >= 0; i--) {
            if (commonCacheOperator.getBit(key, i)) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    @Override
    public boolean signInContinuous(String userId) {
        // 获取用户连续签到天数
        int streak = getContinuousSignDays(userId);

        // 满30天奖励逻辑
        if (streak == 30) {
            boolean reward = commonCacheOperator.exists(REWARD_KEY_PREFIX + userId);
            if (!reward) {
                commonCacheOperator.put(REWARD_KEY_PREFIX + userId, "1", 3600 * 24);
                return true;
            }
        }
        return false;
    }

    /**
     * 获取签到key
     */
    public String getSignKey(String userId, LocalDate date) {
        return SIGN_IN_KEY_PREFIX + userId + ":" + date.format(DateTimeFormatter.ofPattern("yyyyMM"));
    }
}
