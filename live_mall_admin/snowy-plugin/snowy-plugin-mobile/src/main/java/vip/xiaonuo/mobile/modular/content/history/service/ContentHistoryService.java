package vip.xiaonuo.mobile.modular.content.history.service;

import com.mybatisflex.core.paginate.Page;
import vip.xiaonuo.common.pojo.dto.CommonIdParam;
import vip.xiaonuo.content.api.dto.ContentWatchHistoryDTO;
import vip.xiaonuo.content.api.dto.ContentWatchHistoryPageDTO;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 14:44
 */
public interface ContentHistoryService {

    /**
     * 历史记录
     *
     * @param dto 条件
     * @return 结果
     */
    Page<ContentWatchHistoryDTO> record(ContentWatchHistoryPageDTO dto);

    /**
     * 删除历史记录
     *
     * @param dto dto
     */
    void deleteRecord(CommonIdParam dto);
}
