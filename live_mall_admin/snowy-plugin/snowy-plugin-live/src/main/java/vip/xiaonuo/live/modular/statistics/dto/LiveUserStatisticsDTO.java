package vip.xiaonuo.live.modular.statistics.dto;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/30 09:43
 */
@Data
public class LiveUserStatisticsDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4766444287730354685L;

    /**
     * h5访问人数
     */
    private Integer h5VisitsNo;

    /**
     * app下载人数
     */
    private Integer appDownloadNo;

    /**
     * app注册人数
     */
    private Integer appRegisterNo;

    /**
     * app访问人数
     */
    private Integer appVisitsNo;

    /**
     * 直播信息
     */
    private List<LiveInfoDTO> liveInfos;

    public LiveUserStatisticsDTO() {
        this.h5VisitsNo = 0;
        this.appDownloadNo = 0;
        this.appRegisterNo = 0;
        this.appVisitsNo = 0;
        this.liveInfos = Lists.newArrayList();
    }

    @Data
    public static class LiveInfoDTO implements Serializable {

        @Serial
        private static final long serialVersionUID = 5815867656748026710L;

        /**
         * 直播间Id
         */
        private String liveId;

        /**
         * 直播间名称
         */
        private String liveName;

        /**
         * 直播场次
         */
        private Integer liveBroadcasts;

        /**
         * 直播间在线人数
         */
        private Integer liveOnlineNo;

        /**
         * 直播间累计观看人数
         */
        private Integer liveVisitNo;

        /**
         * 开播时长(暂时拿不到)
         */
        private Integer liveTime;

        /**
         * 用户平均观看时长
         */
        private Integer visitTime;

        public LiveInfoDTO() {
            this.liveOnlineNo = 0;
            this.liveVisitNo = 0;
            this.liveTime = 0;
            this.visitTime = 0;
        }
    }

}
