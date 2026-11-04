package vip.xiaonuo.mobile.modular.content.video.service.impl;

import com.mybatisflex.core.paginate.Page;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.content.api.ContentApi;
import vip.xiaonuo.content.api.dto.VideoByActionDTO;
import vip.xiaonuo.content.api.dto.VideoByActionPageDTO;
import vip.xiaonuo.mobile.modular.content.video.service.VideoService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/6 14:29
 */
@Slf4j
@Component
public class VideoServiceImpl implements VideoService {

    @Inject
    private ContentApi contentApi;

    @Override
    public Page<VideoByActionDTO> page(VideoByActionPageDTO dto) {
        return contentApi.videoByActionPage(dto);
    }
}
