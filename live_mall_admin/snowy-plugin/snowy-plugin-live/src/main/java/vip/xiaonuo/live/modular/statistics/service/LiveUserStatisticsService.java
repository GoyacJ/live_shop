package vip.xiaonuo.live.modular.statistics.service;

import vip.xiaonuo.live.modular.statistics.dto.LiveUserStatisticsDTO;
import vip.xiaonuo.live.modular.statistics.param.LiveUserStatisticsParam;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/30 09:41
 */
public interface LiveUserStatisticsService {

    /**
     * 获取直播用户信息统计
     * @param param 参数
     * @return 结果
     */
    LiveUserStatisticsDTO info(LiveUserStatisticsParam param);
}
