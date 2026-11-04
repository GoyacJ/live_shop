package vip.xiaonuo.content.modular.action.service;

import com.mybatisflex.core.service.IService;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.content.api.dto.ContentStatsDTO;
import vip.xiaonuo.content.api.enums.ContentActionType;
import vip.xiaonuo.content.api.enums.ContentType;
import vip.xiaonuo.content.modular.action.entity.ContentStatCounter;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 11:20
 */
public interface ContentStatCounterService extends IService<ContentStatCounter> {

    /**
     * 获取内容统计信息
     *
     * @param contentId   内容Id
     * @param contentType 内容类型
     * @return 内容统计信息
     */
    ContentStatsDTO getContentStats(String contentId, ContentType contentType);

    /**
     * 添加视频统计信息
     *
     * @param videoId    视频Id
     * @param actionType 操作类型
     * @param active
     */
    void addVideoStats(String videoId, ContentActionType actionType, SimpleStatus active);

    /**
     * 添加追剧统计信息
     *
     * @param shopPlayId 追剧Id
     * @param active     active
     */
    void addFollowerStats(String shopPlayId, SimpleStatus active);

    /**
     * 添加访客统计信息
     *
     * @param contentId   内容Id
     * @param contentType 内容类型
     * @param episodeId   剧集Id
     */
    void addVisitorStats(String contentId, ContentType contentType, String episodeId);
}
