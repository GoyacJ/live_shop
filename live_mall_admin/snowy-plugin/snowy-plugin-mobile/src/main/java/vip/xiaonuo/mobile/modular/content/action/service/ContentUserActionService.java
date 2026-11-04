package vip.xiaonuo.mobile.modular.content.action.service;

import vip.xiaonuo.content.api.dto.ContentUserActionDTO;
import vip.xiaonuo.content.api.dto.ContentWatchProgressDTO;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 13:32
 */
public interface ContentUserActionService {

    /**
     * 用户行为
     *
     * @param dto dto
     */
    void action(ContentUserActionDTO dto);

    /**
     * 用户观看记录
     *
     * @param dto dto
     */
    void visit(ContentWatchProgressDTO dto);
}
