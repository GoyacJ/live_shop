package vip.xiaonuo.mobile.modular.content.shortplay.service.impl;

import com.mybatisflex.core.paginate.Page;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.content.api.ContentApi;
import vip.xiaonuo.content.api.dto.*;
import vip.xiaonuo.mobile.modular.content.shortplay.service.ShortPlayService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/14 15:18
 */
@Slf4j
@Component
public class ShortPlayServiceImpl implements ShortPlayService {

    @Inject
    private ContentApi contentApi;


    @Override
    public Page<ShortPlayByActionDTO> page(ShortPlayByActionPageDTO dto) {
        return contentApi.shortPlayByActionPage(dto);
    }

    @Override
    public Page<ShortPlayEpisodeByActionDTO> episodePage(ShortPlayEpisodeByActionPageDTO dto) {
        return contentApi.shortPlayEpisodeByActionPage(dto);
    }

    @Override
    public Page<ShortPlayByActionDTO> index(ShortPlayByActionPageDTO dto) {
        return contentApi.shortPlayIndexByActionPage(dto);
    }

    @Override
    public void follow(ShopPlayFollowDTO dto) {
        contentApi.shorPlayFollow(dto);
    }
}
