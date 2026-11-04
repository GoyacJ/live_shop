package vip.xiaonuo.content.modular.action.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.content.api.dto.ContentStatsDTO;
import vip.xiaonuo.content.api.enums.ContentActionType;
import vip.xiaonuo.content.api.enums.ContentType;
import vip.xiaonuo.content.modular.action.entity.ContentStatCounter;
import vip.xiaonuo.content.modular.action.mapper.ContentStatCounterMapper;
import vip.xiaonuo.content.modular.action.service.ContentStatCounterService;
import vip.xiaonuo.content.modular.shortplay.entity.ContentShortPlayEpisode;
import vip.xiaonuo.content.modular.shortplay.service.ContentShortPlayEpisodeService;
import vip.xiaonuo.content.modular.shortplay.service.ContentShortPlayService;

import java.util.List;
import java.util.Objects;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 11:20
 */
@Tran
@Slf4j
@Component
public class ContentStatCounterServiceImpl extends ServiceImpl<ContentStatCounterMapper, ContentStatCounter> implements ContentStatCounterService {


    @Inject
    private ContentShortPlayService contentShortPlayService;

    @Inject
    private ContentShortPlayEpisodeService contentShortPlayEpisodeService;

    @Override
    public ContentStatsDTO getContentStats(String contentId, ContentType contentType) {
        ContentStatCounter contentStatCounter = this.getOne(
                new QueryWrapper()
                        .eq(ContentStatCounter::getContentId, contentId)
                        .eq(ContentStatCounter::getContentType, contentType.getCode())
        );

        if (Objects.nonNull(contentStatCounter)) {
            return BeanUtil.copyProperties(contentStatCounter, ContentStatsDTO.class);
        }

        return new ContentStatsDTO();
    }

    @Override
    public void addVideoStats(String videoId, ContentActionType actionType, SimpleStatus active) {
        ContentStatCounter contentStatCounter = this.getOne(
                new QueryWrapper()
                        .eq(ContentStatCounter::getContentId, videoId)
                        .eq(ContentStatCounter::getContentType, ContentType.VIDEO.getCode())
        );

        if (Objects.nonNull(contentStatCounter)) {
            if (SimpleStatus.ENABLE.equals(active)) {
                if (ContentActionType.LIKE.equals(actionType)) {
                    contentStatCounter.setLikeCount(contentStatCounter.getLikeCount() + 1);
                } else if (ContentActionType.COLLECT.equals(actionType)) {
                    contentStatCounter.setCollectCount(contentStatCounter.getCollectCount() + 1);
                }
            } else {
                if (ContentActionType.LIKE.equals(actionType)) {
                    contentStatCounter.setLikeCount(contentStatCounter.getLikeCount() - 1);
                } else if (ContentActionType.COLLECT.equals(actionType)) {
                    contentStatCounter.setCollectCount(contentStatCounter.getCollectCount() - 1);
                }
            }
            if (ContentActionType.SHARE.equals(actionType)) {
                contentStatCounter.setShareCount(contentStatCounter.getShareCount() + 1);
            } else if (ContentActionType.COMMENT.equals(actionType)) {
                contentStatCounter.setCommentCount(contentStatCounter.getCommentCount() + 1);
            }
            this.updateById(contentStatCounter);
        } else {
            ContentStatCounter bean = new ContentStatCounter();
            bean.setContentId(videoId);
            bean.setContentType(ContentType.VIDEO);
            if (ContentActionType.LIKE.equals(actionType)) {
                bean.setLikeCount(1);
            } else if (ContentActionType.COLLECT.equals(actionType)) {
                bean.setCollectCount(1);
            } else if (ContentActionType.SHARE.equals(actionType)) {
                bean.setShareCount(1);
            } else if (ContentActionType.COMMENT.equals(actionType)) {
                bean.setCommentCount(1);
            }
            this.save(bean);
        }

        addShortPlayStats(videoId, actionType, active);
    }

    @Override
    public void addFollowerStats(String shopPlayId, SimpleStatus active) {
        ContentStatCounter contentStatCounter = this.getOne(
                new QueryWrapper()
                        .eq(ContentStatCounter::getContentId, shopPlayId)
                        .eq(ContentStatCounter::getContentType, ContentType.SHORT_PLAY.getCode())
        );
        if (Objects.nonNull(contentStatCounter)) {
            if (SimpleStatus.ENABLE.equals(active)) {
                contentStatCounter.setFollowCount(contentStatCounter.getFollowCount() + 1);
            } else {
                contentStatCounter.setFollowCount(contentStatCounter.getFollowCount() - 1);
            }
            this.updateById(contentStatCounter);
        } else {
            ContentStatCounter bean = new ContentStatCounter();
            bean.setContentId(shopPlayId);
            bean.setContentType(ContentType.SHORT_PLAY);
            if (SimpleStatus.ENABLE.equals(active)) {
                bean.setFollowCount(1);
            }
            this.save(bean);
        }
    }

    @Override
    public void addVisitorStats(String contentId, ContentType contentType, String episodeId) {

        ContentStatCounter contentStatCounter = this.getOne(
                new QueryWrapper()
                        .eq(ContentStatCounter::getContentId, contentId)
                        .eq(ContentStatCounter::getContentType, contentType.getCode())
        );
        if (Objects.nonNull(contentStatCounter)) {
            contentStatCounter.setViewCount(contentStatCounter.getViewCount() + 1);
            this.updateById(contentStatCounter);
        } else {
            ContentStatCounter bean = new ContentStatCounter();
            bean.setContentId(contentId);
            bean.setContentType(contentType);
            bean.setViewCount(1);
            this.save(bean);
        }

        if (ContentType.SHORT_PLAY.equals(contentType)) {
            ContentShortPlayEpisode episode = contentShortPlayEpisodeService.getById(episodeId);
            if (Objects.nonNull(episode)) {
                String videoId = episode.getVideoId();
                ContentStatCounter videoCounter = this.getOne(
                        new QueryWrapper()
                                .eq(ContentStatCounter::getContentId, videoId)
                                .eq(ContentStatCounter::getContentType, ContentType.VIDEO)
                );
                if (Objects.nonNull(videoCounter)) {
                    videoCounter.setViewCount(videoCounter.getViewCount() + 1);
                    this.updateById(videoCounter);
                } else {
                    ContentStatCounter bean = new ContentStatCounter();
                    bean.setContentId(videoId);
                    bean.setContentType(ContentType.VIDEO);
                    bean.setViewCount(1);
                    this.save(bean);
                }
            }
        }
    }

    /**
     * 添加内容统计信息
     *
     * @param videoId    视频Id
     * @param actionType 操作类型
     */
    private void addShortPlayStats(String videoId, ContentActionType actionType, SimpleStatus active) {
        List<ContentShortPlayEpisode> episodeList = contentShortPlayEpisodeService.list(
                new QueryWrapper()
                        .eq(ContentShortPlayEpisode::getVideoId, videoId));

        episodeList.forEach(episode -> {
            ContentStatCounter contentStatCounter = this.getOne(new QueryWrapper()
                    .eq(ContentStatCounter::getContentId, episode.getShortPlayId())
                    .eq(ContentStatCounter::getContentType, ContentType.SHORT_PLAY.getCode()));
            if (Objects.nonNull(contentStatCounter)) {
                if (SimpleStatus.ENABLE.equals(active)) {
                    if (ContentActionType.LIKE.equals(actionType)) {
                        contentStatCounter.setLikeCount(contentStatCounter.getLikeCount() + 1);
                    } else if (ContentActionType.COLLECT.equals(actionType)) {
                        contentStatCounter.setCollectCount(contentStatCounter.getCollectCount() + 1);
                    }
                } else {
                    if (ContentActionType.LIKE.equals(actionType)) {
                        contentStatCounter.setLikeCount(contentStatCounter.getLikeCount() - 1);
                    } else if (ContentActionType.COLLECT.equals(actionType)) {
                        contentStatCounter.setCollectCount(contentStatCounter.getCollectCount() - 1);
                    }
                }
                if (ContentActionType.SHARE.equals(actionType)) {
                    contentStatCounter.setShareCount(contentStatCounter.getShareCount() + 1);
                } else if (ContentActionType.COMMENT.equals(actionType)) {
                    contentStatCounter.setCommentCount(contentStatCounter.getCommentCount() + 1);
                }
                this.updateById(contentStatCounter);
            } else {
                ContentStatCounter bean = new ContentStatCounter();
                bean.setContentId(episode.getShortPlayId());
                bean.setContentType(ContentType.SHORT_PLAY);
                if (ContentActionType.LIKE.equals(actionType)) {
                    bean.setLikeCount(1);
                } else if (ContentActionType.COLLECT.equals(actionType)) {
                    bean.setCollectCount(1);
                } else if (ContentActionType.SHARE.equals(actionType)) {
                    bean.setShareCount(1);
                } else if (ContentActionType.COMMENT.equals(actionType)) {
                    bean.setCommentCount(1);
                }
                this.save(bean);
            }
        });
    }
}
