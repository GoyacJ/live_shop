package vip.xiaonuo.content.modular.action.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.common.pojo.dto.CommonIdParam;
import vip.xiaonuo.content.api.dto.ContentUserWatchProgressDTO;
import vip.xiaonuo.content.api.dto.ContentWatchHistoryDTO;
import vip.xiaonuo.content.api.dto.ContentWatchHistoryPageDTO;
import vip.xiaonuo.content.api.dto.ContentWatchProgressDTO;
import vip.xiaonuo.content.api.enums.ContentType;
import vip.xiaonuo.content.modular.action.entity.ContentWatchProgress;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 11:21
 */
public interface ContentWatchProgressService extends IService<ContentWatchProgress> {

    /**
     * 获取最后观看的集数
     *
     * @param userId      用户Id
     * @param shortPlayId 短剧Id
     * @return 集数
     */
    Integer getLastWatchEpisodeNoByUserIdAndShortPlayId(String userId, String shortPlayId);

    /**
     * 用户观看
     *
     * @param dto dto
     */
    void visit(ContentWatchProgressDTO dto);

    /**
     * 观看记录
     *
     * @param dto dto
     * @return 结果
     */
    Page<ContentWatchHistoryDTO> contentWatchHistory(ContentWatchHistoryPageDTO dto);

    /**
     * 删除历史记录
     *
     * @param dto dto
     */
    void deleteRecord(CommonIdParam dto);

    /**
     * 获取用户观看进度
     *
     * @param userId      用户Id
     * @param videoId     视频Id
     * @param contentType 内容类型
     * @param shortPlayId 短剧Id
     * @param episodeId   剧集Id
     * @return 结果
     */
    ContentUserWatchProgressDTO getUserWatchProgress(String userId, String videoId, ContentType contentType, String shortPlayId, String episodeId);
}
