package vip.xiaonuo.mobile.modular.content.history.service.impl;

import com.mybatisflex.core.paginate.Page;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.pojo.dto.CommonIdParam;
import vip.xiaonuo.content.api.ContentApi;
import vip.xiaonuo.content.api.dto.ContentWatchHistoryDTO;
import vip.xiaonuo.content.api.dto.ContentWatchHistoryPageDTO;
import vip.xiaonuo.mobile.modular.content.history.service.ContentHistoryService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 14:45
 */
@Slf4j
@Component
public class ContentHistoryServiceImpl implements ContentHistoryService {

    @Inject
    private ContentApi contentApi;

    @Override
    public Page<ContentWatchHistoryDTO> record(ContentWatchHistoryPageDTO dto) {
        return contentApi.contentWatchHistory(dto);
    }

    @Override
    public void deleteRecord(CommonIdParam dto) {
        contentApi.deleteRecord(dto);
    }
}
