package vip.xiaonuo.mobile.modular.content.action.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.content.api.ContentApi;
import vip.xiaonuo.content.api.dto.ContentUserActionDTO;
import vip.xiaonuo.content.api.dto.ContentWatchProgressDTO;
import vip.xiaonuo.mobile.modular.content.action.service.ContentUserActionService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 13:33
 */
@Slf4j
@Component
public class ContentUserActionServiceImpl implements ContentUserActionService {

    @Inject
    private ContentApi contentApi;

    @Override
    public void action(ContentUserActionDTO dto) {
        contentApi.action(dto);
    }

    @Override
    public void visit(ContentWatchProgressDTO dto) {
        contentApi.visit(dto);
    }
}
