package vip.xiaonuo.content.api;

import com.mybatisflex.core.paginate.Page;
import vip.xiaonuo.common.pojo.dto.CommonIdParam;
import vip.xiaonuo.content.api.dto.*;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 11:24
 */
public interface ContentApi {

    /**
     * 短剧分页查询
     * @param dto 条件
     * @return 结果
     */
    Page<ShortPlayByActionDTO> shortPlayByActionPage(ShortPlayByActionPageDTO dto);

    /**
     * 短剧分页查询
     * @param dto 条件
     * @return 结果
     */
    Page<ShortPlayByActionDTO> shortPlayIndexByActionPage(ShortPlayByActionPageDTO dto);

    /**
     * 短剧剧集分页查询
     * @param dto 条件
     * @return 结果
     */
    Page<ShortPlayEpisodeByActionDTO> shortPlayEpisodeByActionPage(ShortPlayEpisodeByActionPageDTO dto);

    /**
     * 视频分页查询
     * @param dto 条件
     * @return 结果
     */
    Page<VideoByActionDTO> videoByActionPage(VideoByActionPageDTO dto);

    /**
     * 用户行为
     * @param dto dto
     */
    void action(ContentUserActionDTO dto);

    /**
     * 追剧
     * @param dto dto
     */
    void shorPlayFollow(ShopPlayFollowDTO dto);

    /**
     * 观看记录
     * @param dto dto
     * @return 结果
     */
    Page<ContentWatchHistoryDTO> contentWatchHistory(ContentWatchHistoryPageDTO dto);

    /**
     * 用户观看
     * @param dto dto
     */
    void visit(ContentWatchProgressDTO dto);

    /**
     * 删除历史记录
     * @param dto dto
     */
    void deleteRecord(CommonIdParam dto);
}
