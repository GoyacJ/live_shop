package vip.xiaonuo.content.api;

import com.mybatisflex.core.paginate.Page;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.pojo.dto.CommonIdParam;
import vip.xiaonuo.content.api.dto.*;
import vip.xiaonuo.content.modular.content.service.ContentService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 11:24
 */
@Slf4j
@Component
public class ContentApiImpl implements ContentApi{

    @Inject
    private ContentService contentService;

    @Override
    public Page<ShortPlayByActionDTO> shortPlayByActionPage(ShortPlayByActionPageDTO dto) {
        return contentService.shortPlayByActionPage(dto);
    }

    @Override
    public Page<ShortPlayByActionDTO> shortPlayIndexByActionPage(ShortPlayByActionPageDTO dto) {
        return contentService.shortPlayIndexByActionPage(dto);
    }

    @Override
    public Page<ShortPlayEpisodeByActionDTO> shortPlayEpisodeByActionPage(ShortPlayEpisodeByActionPageDTO dto) {
        return contentService.shortPlayEpisodeByActionPage(dto);
    }

    @Override
    public Page<VideoByActionDTO> videoByActionPage(VideoByActionPageDTO dto) {
        return contentService.videoByActionPage(dto);
    }

    @Override
    public void action(ContentUserActionDTO dto) {
        contentService.action(dto);
    }

    @Override
    public void shorPlayFollow(ShopPlayFollowDTO dto) {
        contentService.shorPlayFollow(dto);
    }

    @Override
    public Page<ContentWatchHistoryDTO> contentWatchHistory(ContentWatchHistoryPageDTO dto) {
        return contentService.contentWatchHistory(dto);
    }

    @Override
    public void visit(ContentWatchProgressDTO dto) {
        contentService.visit(dto);
    }

    @Override
    public void deleteRecord(CommonIdParam dto) {
        contentService.deleteRecord(dto);
    }
}
