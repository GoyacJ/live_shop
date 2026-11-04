package vip.xiaonuo.content.modular.action.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.common.pojo.dto.CommonIdParam;
import vip.xiaonuo.content.api.dto.ContentUserWatchProgressDTO;
import vip.xiaonuo.content.api.dto.ContentWatchHistoryDTO;
import vip.xiaonuo.content.api.dto.ContentWatchHistoryPageDTO;
import vip.xiaonuo.content.api.dto.ContentWatchProgressDTO;
import vip.xiaonuo.content.api.enums.ContentType;
import vip.xiaonuo.content.modular.action.entity.ContentWatchProgress;
import vip.xiaonuo.content.modular.action.mapper.ContentWatchProgressMapper;
import vip.xiaonuo.content.modular.action.service.ContentShopPlayFollowService;
import vip.xiaonuo.content.modular.action.service.ContentStatCounterService;
import vip.xiaonuo.content.modular.action.service.ContentWatchProgressService;
import vip.xiaonuo.content.modular.action.service.ContentWatchRecordService;
import vip.xiaonuo.content.modular.shortplay.entity.ContentShortPlayEpisode;
import vip.xiaonuo.content.modular.shortplay.service.ContentShortPlayEpisodeService;
import vip.xiaonuo.content.modular.video.entity.ContentVideo;
import vip.xiaonuo.content.modular.video.service.ContentVideoService;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 11:21
 */
@Tran
@Slf4j
@Component
public class ContentWatchProgressServiceImpl extends ServiceImpl<ContentWatchProgressMapper, ContentWatchProgress> implements ContentWatchProgressService {

    @Inject
    private ContentShortPlayEpisodeService contentShortPlayEpisodeService;

    @Inject
    private ContentVideoService contentVideoService;

    @Inject
    private ContentShopPlayFollowService contentShopPlayFollowService;

    @Inject
    private ContentWatchRecordService contentWatchRecordService;

    @Inject
    private ContentStatCounterService contentStatCounterService;

    @Override
    public Integer getLastWatchEpisodeNoByUserIdAndShortPlayId(String userId, String shortPlayId) {
        ContentWatchProgress contentWatchProgress = this.getOne(new QueryWrapper()
                .eq(ContentWatchProgress::getUserId, userId)
                .eq(ContentWatchProgress::getContentType, ContentType.SHORT_PLAY.getCode())
                .eq(ContentWatchProgress::getContentId, shortPlayId)
                .orderBy(ContentWatchProgress::getStartWatchTime)
                .desc().limit(1));

        if (Objects.isNull(contentWatchProgress)) {
            return 1;
        }
        String episodeId = contentWatchProgress.getEpisodeId();
        if (StringUtils.isNotEmpty(episodeId)) {
            ContentShortPlayEpisode episode = contentShortPlayEpisodeService.getById(episodeId);
            if (Objects.nonNull(episode)) {
                return episode.getEpisodeNumber();
            }
        }
        return 1;
    }

    @Override
    public void visit(ContentWatchProgressDTO dto) {
        QueryWrapper queryWrapper = new QueryWrapper()
                .eq(ContentWatchProgress::getUserId, dto.userId())
                .eq(ContentWatchProgress::getContentId, dto.contentId());

        if (ContentType.SHORT_PLAY.equals(dto.contentType())
                && StringUtils.isNotEmpty(dto.episodeId())) {
            queryWrapper.eq(ContentWatchProgress::getContentType, ContentType.SHORT_PLAY.getCode())
                    .eq(ContentWatchProgress::getEpisodeId, dto.episodeId());
        } else {
            queryWrapper.eq(ContentWatchProgress::getContentType, ContentType.VIDEO.getCode());
        }

        ContentWatchProgress contentWatchProgress = this.getOne(queryWrapper);
        if (Objects.nonNull(contentWatchProgress)) {
            contentWatchProgress.setProgressSeconds(dto.progressSeconds());
            contentWatchProgress.setViewProgress(dto.viewProgress());
            contentWatchProgress.setLastWatchTime(LocalDateTime.now());
            this.updateById(contentWatchProgress);
        } else {
            contentWatchProgress = new ContentWatchProgress();
            contentWatchProgress.setUserId(dto.userId());
            contentWatchProgress.setContentId(dto.contentId());
            contentWatchProgress.setContentType(dto.contentType());
            contentWatchProgress.setEpisodeId(dto.episodeId());
            contentWatchProgress.setProgressSeconds(dto.progressSeconds());
            contentWatchProgress.setViewProgress(dto.viewProgress());
            contentWatchProgress.setLastWatchTime(LocalDateTime.now());
            contentWatchProgress.setStartWatchTime(contentWatchProgress.getLastWatchTime().plusSeconds(contentWatchProgress.getProgressSeconds()));
            this.save(contentWatchProgress);
        }

        contentWatchRecordService.add(contentWatchProgress);
        contentStatCounterService.addVisitorStats(dto.contentId(), dto.contentType(), dto.episodeId());
    }

    @Override
    public Page<ContentWatchHistoryDTO> contentWatchHistory(ContentWatchHistoryPageDTO dto) {
        QueryWrapper queryWrapper = new QueryWrapper();

        queryWrapper.eq(ContentWatchProgress::getUserId, dto.userId());
        queryWrapper.eq(ContentWatchProgress::getContentType, dto.contentType());

        return this.page(CommonPageRequest.defaultPage(), queryWrapper).map(contentWatchProgress -> {
            ContentWatchHistoryDTO res = new ContentWatchHistoryDTO();
            res.setId(contentWatchProgress.getId());
            res.setUserId(contentWatchProgress.getUserId());
            res.setContentId(contentWatchProgress.getContentId());
            res.setContentType(contentWatchProgress.getContentType());
            res.setEpisodeId(contentWatchProgress.getEpisodeId());
            res.setViewProgress(contentWatchProgress.getViewProgress());
            res.setLastWatchTime(contentWatchProgress.getLastWatchTime());

            if (StringUtils.equals(ContentType.VIDEO.getCode(), dto.contentType())) {
                ContentVideo contentVideo = contentVideoService.getById(contentWatchProgress.getContentId());
                if (Objects.nonNull(contentVideo)) {
                    res.setTitle(contentVideo.getTitle());
                    res.setCover(contentVideo.getCover());
                    res.setDuration(contentVideo.getDuration());
                    res.setVideoUrl(contentVideo.getVideoUrl());
                }
            } else if (StringUtils.equals(ContentType.SHORT_PLAY.getCode(), dto.contentType()) && StringUtils.isNotEmpty(res.getEpisodeId())) {
                ContentShortPlayEpisode episode = contentShortPlayEpisodeService.getById(res.getEpisodeId());
                if (Objects.nonNull(episode)) {
                    res.setTitle(episode.getTitle());
                    res.setCover(episode.getCover());
                    res.setDuration(episode.getDuration());
                    res.setVideoUrl(episode.getVideoUrl());
                    res.setEpisodeNumber(episode.getEpisodeNumber());
                    boolean follow = contentShopPlayFollowService.isFollow(res.getUserId(), contentWatchProgress.getContentId());
                    res.setIsFollow(follow);
                }
            }
            return res;
        });
    }

    @Override
    public void deleteRecord(CommonIdParam dto) {
        this.removeById(dto.id());
    }

    @Override
    public ContentUserWatchProgressDTO getUserWatchProgress(String userId, String videoId, ContentType contentType, String shortPlayId, String episodeId) {

        QueryWrapper queryWrapper = new QueryWrapper()
                .eq(ContentWatchProgress::getUserId, userId)
                .eq(ContentWatchProgress::getContentType, contentType);

        if (ContentType.SHORT_PLAY.equals(contentType)) {
            queryWrapper.eq(ContentWatchProgress::getContentId, shortPlayId).eq(ContentWatchProgress::getEpisodeId, episodeId);
        } else if (ContentType.VIDEO.equals(contentType)) {
            queryWrapper.eq(ContentWatchProgress::getUserId, userId)
                    .eq(ContentWatchProgress::getContentId, videoId);
        }

        ContentWatchProgress contentWatchProgress = this.getOne(queryWrapper);

        if (Objects.nonNull(contentWatchProgress)) {
            ContentUserWatchProgressDTO dto = new ContentUserWatchProgressDTO();
            dto.setViewProgress(contentWatchProgress.getViewProgress());
            dto.setLastWatchTime(contentWatchProgress.getLastWatchTime());
            return dto;
        }
        return null;
    }
}
