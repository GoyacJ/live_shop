package vip.xiaonuo.mobile.modular.content.video.service;

import com.mybatisflex.core.paginate.Page;
import vip.xiaonuo.content.api.dto.VideoByActionDTO;
import vip.xiaonuo.content.api.dto.VideoByActionPageDTO;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/6 14:28
 */
public interface VideoService {

    /**
     * 分页查询
     *
     * @param dto 查询条件
     * @return 结果
     */
    Page<VideoByActionDTO> page(VideoByActionPageDTO dto);
}
