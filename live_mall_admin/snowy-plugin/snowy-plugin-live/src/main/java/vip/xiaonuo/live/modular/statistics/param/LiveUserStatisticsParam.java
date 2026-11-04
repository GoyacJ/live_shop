package vip.xiaonuo.live.modular.statistics.param;

import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/30 09:53
 */
public record LiveUserStatisticsParam(

        /*
          开始时间
         */
        LocalDateTime beginTime,

        /*
          结束时间
         */
        LocalDateTime endTime,

        /*
          直播间Id
         */
        String liveId,

        /*
          直播场次
          暂无场次功能
         */
        String liveBroadcasts
) {
}
