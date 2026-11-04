package vip.xiaonuo.mobile.modular.content.shortplay.service;

import com.mybatisflex.core.paginate.Page;
import vip.xiaonuo.content.api.dto.*;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/14 15:18
 */
public interface ShortPlayService {

    /**
     * 短剧分页查询
     *
     * @param dto 条件
     * @return 结果
     */
    Page<ShortPlayByActionDTO> page(ShortPlayByActionPageDTO dto);

    /**
     * 剧集分页查询
     *
     * @param dto 条件
     * @return 结果
     */
    Page<ShortPlayEpisodeByActionDTO> episodePage(ShortPlayEpisodeByActionPageDTO dto);

    /**
     * 短剧首页推荐
     *
     * @param dto 条件
     * @return 结果
     */
    Page<ShortPlayByActionDTO> index(ShortPlayByActionPageDTO dto);

    /**
     * 关注
     *
     * @param dto dto
     */
    void follow(ShopPlayFollowDTO dto);
}
