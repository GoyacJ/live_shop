package vip.xiaonuo.content.modular.content.service.impl;

import api.shop.MallGoodsApi;
import api.shop.dto.MallGoodsDetailsDTO;
import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.paginate.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.pojo.dto.CommonIdParam;
import vip.xiaonuo.content.api.dto.*;
import vip.xiaonuo.content.api.enums.ContentType;
import vip.xiaonuo.content.modular.action.service.ContentShopPlayFollowService;
import vip.xiaonuo.content.modular.action.service.ContentStatCounterService;
import vip.xiaonuo.content.modular.action.service.ContentUserActionService;
import vip.xiaonuo.content.modular.action.service.ContentWatchProgressService;
import vip.xiaonuo.content.modular.content.service.ContentService;
import vip.xiaonuo.content.modular.shortplay.entity.ContentShortPlay;
import vip.xiaonuo.content.modular.shortplay.entity.ContentShortPlayEpisode;
import vip.xiaonuo.content.modular.shortplay.service.ContentShortPlayEpisodeService;
import vip.xiaonuo.content.modular.shortplay.service.ContentShortPlayService;
import vip.xiaonuo.content.modular.video.dto.ContentVideoDTO;
import vip.xiaonuo.content.modular.video.service.ContentVideoService;
import vip.xiaonuo.task.api.TaskApi;
import vip.xiaonuo.task.api.enums.TaskBusinessCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 11:23
 */
@Tran
@Slf4j
@Component
public class ContentServiceImpl implements ContentService {

    @Inject
    private ContentShortPlayService contentShortPlayService;

    @Inject
    private ContentShortPlayEpisodeService contentShortPlayEpisodeService;

    @Inject
    private ContentVideoService contentVideoService;

    @Inject
    private ContentShopPlayFollowService contentShopPlayFollowService;

    @Inject
    private ContentWatchProgressService contentWatchProgressService;

    @Inject
    private ContentStatCounterService contentStatCounterService;

    @Inject
    private ContentUserActionService contentUserActionService;

    @Inject
    private TaskApi taskApi;

    @Inject
    private MallGoodsApi mallGoodsApi;

    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Override
    public Page<ShortPlayByActionDTO> shortPlayByActionPage(ShortPlayByActionPageDTO dto) {
        return contentShortPlayService.shortPlayByActionPage(dto)
                .map(shortPlay -> getShortPlayByActionDTO(dto.userId(), shortPlay));
    }

    /**
     * 获取短剧信息
     *
     * @param userId    用户Id
     * @param shortPlay 短剧
     * @return 结果
     */
    private ShortPlayByActionDTO getShortPlayByActionDTO(String userId, ContentShortPlay shortPlay) {
        ShortPlayByActionDTO res = BeanUtil.copyProperties(shortPlay, ShortPlayByActionDTO.class);
        if (StringUtils.isNotEmpty(userId)) {
            boolean isFollow = contentShopPlayFollowService.isFollow(userId, shortPlay.getId());
            res.setUserId(userId);
            res.setIsFollow(isFollow);
            res.setLastWatchEpisodeNo(getLastWatchEpisodeNo(userId, shortPlay.getId()));
        }
        return res;
    }

    /**
     * 获取最后观看的集数
     *
     * @param userId      用户Id
     * @param shortPlayId 短剧Id
     * @return 集数
     */
    private Integer getLastWatchEpisodeNo(String userId, String shortPlayId) {
        return contentWatchProgressService.getLastWatchEpisodeNoByUserIdAndShortPlayId(userId, shortPlayId);
    }

    @Override
    public Page<ShortPlayByActionDTO> shortPlayIndexByActionPage(ShortPlayByActionPageDTO dto) {
        return contentShortPlayService.shortPlayIndexByActionPage(dto)
                .map(shortPlay -> getShortPlayByActionDTO(dto.userId(), shortPlay));
    }

    @Override
    public Page<ShortPlayEpisodeByActionDTO> shortPlayEpisodeByActionPage(ShortPlayEpisodeByActionPageDTO dto) {

        ContentShortPlay shortPlay = this.contentShortPlayService.getById(dto.shortPlayId());
        if (Objects.isNull(shortPlay)) {
            return new Page<>();
        }

        Page<ContentShortPlayEpisode> page = contentShortPlayEpisodeService.shortPlayEpisodeByActionPage(dto);
        return page.map(shortPlayEpisode -> {
            ShortPlayEpisodeByActionDTO res = BeanUtil.copyProperties(shortPlayEpisode, ShortPlayEpisodeByActionDTO.class);
            res.setEpisodeCount(shortPlay.getEpisodeCount());
            ContentVideoDTO contentVideo = this.contentVideoService.getVideoByAction(shortPlayEpisode.getVideoId());
            res.setVideoByAction(getVideoByActionDTO(dto.userId(), contentVideo, ContentType.SHORT_PLAY, shortPlayEpisode.getShortPlayId(), shortPlayEpisode.getId()));
            return res;
        });
    }

    @Override
    public Page<VideoByActionDTO> videoByActionPage(VideoByActionPageDTO dto) {
        Page<ContentVideoDTO> page = contentVideoService.videoByActionPage(dto);
        return page.map(video -> getVideoByActionDTO(dto.userId(), video, ContentType.VIDEO, null, null));
    }

    /**
     * 获取视频信息
     *
     * @param userId      用户Id
     * @param video       视频
     * @param contentType
     * @param shortPlayId 短剧Id
     * @param episodeId   剧集Id
     * @return 结果
     */
    private VideoByActionDTO getVideoByActionDTO(String userId, ContentVideoDTO video, ContentType contentType, String shortPlayId, String episodeId) {
        VideoByActionDTO res = BeanUtil.copyProperties(video, VideoByActionDTO.class);

        // 设置商品样例
        res.setGoodsSampleInfo(getGoodsSampleInfo(video));

        if (StringUtils.isNotEmpty(userId)) {

            // 设置用户动作
            res.setUserAction(getUserActionInfo(userId, video.getId()));

            // 设置观看进度
            res.setUserWatchProgress(getUserWatchProgress(userId, video.getId(), contentType, shortPlayId, episodeId));
        }
        return res;
    }

    /**
     * 获取商品样例信息
     *
     * @param res 视频信息
     * @return 商品样例信息
     */
    private GoodsSampleInfoDTO getGoodsSampleInfo(ContentVideoDTO res) {

        if (StringUtils.isEmpty(res.getGoodsId())) {
            return null;
        }

        MallGoodsDetailsDTO detail = mallGoodsApi.detail(res.getGoodsId());
        if (Objects.isNull(detail)) {
            return null;
        }

        GoodsSampleInfoDTO goodsSampleInfoDTO = new GoodsSampleInfoDTO();
        BeanUtil.copyProperties(res, goodsSampleInfoDTO);
        goodsSampleInfoDTO.setGoodsId(detail.getId());
        goodsSampleInfoDTO.setGoodsName(detail.getTitle());
        goodsSampleInfoDTO.setGoodsPrice(BigDecimal.valueOf(detail.getPrice()));
        goodsSampleInfoDTO.setHostImage(detail.getHostImage());
        goodsSampleInfoDTO.setStartShowGoodsSecond(res.getStartShowGoodsSecond());
        goodsSampleInfoDTO.setShowGoodsSecond(res.getShowGoodsSecond());
        return goodsSampleInfoDTO;
    }

    /**
     * 获取用户观看进度
     *
     * @param userId  用户Id
     * @param videoId 视频Id
     * @return 结果
     */
    private ContentUserWatchProgressDTO getUserWatchProgress(String userId, String videoId, ContentType contentType, String shortPlayId, String episodeId) {
        return contentWatchProgressService.getUserWatchProgress(userId, videoId, contentType, shortPlayId, episodeId);
    }

    /**
     * 获取用户行为信息
     *
     * @param userId  用户Id
     * @param videoId 视频Id
     * @return 结果
     */
    private ContentUserActionInfoDTO getUserActionInfo(String userId, String videoId) {
        return contentUserActionService.getUserActionInfo(userId, videoId);
    }

    @Override
    public void action(ContentUserActionDTO dto) {
        this.contentUserActionService.action(dto);
    }

    @Override
    public void shorPlayFollow(ShopPlayFollowDTO dto) {
        contentShopPlayFollowService.follow(dto.userId(), dto.shopPlayId(), dto.isFollow());
    }

    @Override
    public Page<ContentWatchHistoryDTO> contentWatchHistory(ContentWatchHistoryPageDTO dto) {
        return contentWatchProgressService.contentWatchHistory(dto);
    }

    @Override
    public void visit(ContentWatchProgressDTO dto) {
        contentWatchProgressService.visit(dto);

        if (dto.progressSeconds() < 0) {
            throw new IllegalArgumentException("观看时长必须为正值");
        }

        LocalDate today = LocalDate.now();
        String day = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String timeKey = String.format("video:watch:time:%s:%s:%s", dto.userId(), dto.contentId(), day);

        // 日维度
        commonCacheOperator.incrBy(timeKey, dto.progressSeconds());


        // 保留90天数据
        commonCacheOperator.expire(timeKey, 3600 * 24 * 90);
        taskApi.task(dto.userId(), dto.contentId(), TaskBusinessCategory.VIDEO_VISIT);
    }

    @Override
    public void deleteRecord(CommonIdParam dto) {
        contentWatchProgressService.deleteRecord(dto);
    }
}
