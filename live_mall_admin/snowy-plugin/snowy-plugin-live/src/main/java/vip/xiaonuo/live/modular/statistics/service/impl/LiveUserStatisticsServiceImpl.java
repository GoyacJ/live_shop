package vip.xiaonuo.live.modular.statistics.service.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import manage.MobileDeviceInfoApi;
import manage.UserApi;
import manage.dto.UserStatisticsInfoDTO;
import org.apache.commons.lang3.StringUtils;
import org.noear.redisx.plus.RedisHash;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.live.api.openim.OpenImApi;
import vip.xiaonuo.live.api.openim.dto.OpenImGroupInfoDTO;
import vip.xiaonuo.live.api.openim.dto.OpenImGroupMemberInfoDTO;
import vip.xiaonuo.live.modular.livebiz.entity.LiveBiz;
import vip.xiaonuo.live.modular.livebiz.service.LiveBizService;
import vip.xiaonuo.live.modular.statistics.dto.LiveUserStatisticsDTO;
import vip.xiaonuo.live.modular.statistics.param.LiveUserStatisticsParam;
import vip.xiaonuo.live.modular.statistics.service.LiveUserStatisticsService;

import java.util.List;
import java.util.Objects;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/30 09:41
 */
@Slf4j
@Component
public class LiveUserStatisticsServiceImpl implements LiveUserStatisticsService {

    @Inject
    private UserApi userApi;

    @Inject
    private LiveBizService liveBizService;

    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Inject
    private OpenImApi openImApi;

    @Inject("${openim.ip}")
    private String ip;

    @Inject
    private MobileDeviceInfoApi mobileDeviceInfoApi;

    @Override
    public LiveUserStatisticsDTO info(LiveUserStatisticsParam param) {

        LiveUserStatisticsDTO dto = new LiveUserStatisticsDTO();

        // H5访问人数 && app注册人数
        UserStatisticsInfoDTO userStatisticsInfo = userApi.statisticsInfo(param.beginTime(), param.endTime());

        dto.setH5VisitsNo(userStatisticsInfo.getH5VisitsNo());
        dto.setAppRegisterNo(userStatisticsInfo.getAppRegisterNo());
        dto.setAppVisitsNo(userStatisticsInfo.getAppVisitsNo());

        // app下载人数
        dto.setAppDownloadNo(userStatisticsInfo.getAppVisitsNo());

        dto.setLiveInfos(getLiveInfos(param.liveId()));
        return dto;
    }

    private List<LiveUserStatisticsDTO.LiveInfoDTO> getLiveInfos(String liveId) {
        List<OpenImGroupInfoDTO> groupsInfos;

        // 直播间在线人数 、直播间累计观看人数、观看时长
        if (StringUtils.isEmpty(liveId)) {
            List<String> ids = this.liveBizService.list().stream().map(LiveBiz::getId).toList();
            groupsInfos = openImApi.getGroupsInfo(ids);
        } else {
            groupsInfos = openImApi.getGroupsInfo(Lists.newArrayList(liveId));
        }

        return groupsInfos.stream().map(groupsInfo -> {
            LiveUserStatisticsDTO.LiveInfoDTO dto = new LiveUserStatisticsDTO.LiveInfoDTO();
            String groupId = groupsInfo.groupID();
            LiveBiz liveBiz = this.liveBizService.getById(groupId);
            if (Objects.isNull(liveBiz)) {
                return null;
            }
            dto.setLiveId(groupId);
            dto.setLiveName(liveBiz.getMainTitle());
            dto.setLiveBroadcasts(liveBiz.getLiveBroadcasts());
            dto.setLiveVisitNo(groupsInfo.memberCount());

            List<String> userIds = openImApi.getMembersInfo(groupId, 1, Lists.newArrayList()).stream().map(OpenImGroupMemberInfoDTO::userID).toList();
            long liveOnlineNo = openImApi.checkUserOnLine(userIds).stream().filter(user -> user.status() == 1).count();
            dto.setLiveOnlineNo((int) liveOnlineNo);
            dto.setLiveTime(calculateAverageWatchTime(groupId));
            return dto;
        }).filter(Objects::nonNull).toList();
    }

    /**
     * 计算平均观看时长
     */
    public Integer calculateAverageWatchTime(String liveId) {
        String redisKey = "watch_time:" + liveId;
        // 用户平均观看时长
        RedisHash hash = commonCacheOperator.getHash(redisKey);

        if (hash.isEmpty()) {
            return 0;
        }

        long total = 0;
        for (String time : hash.values()) {
            total += Long.parseLong(time);
        }
        return Math.toIntExact(total / hash.size());
    }
}
