package vip.xiaonuo.content.modular.action.service;

import com.mybatisflex.core.service.IService;
import vip.xiaonuo.content.api.dto.ContentUserActionDTO;
import vip.xiaonuo.content.api.dto.ContentUserActionInfoDTO;
import vip.xiaonuo.content.modular.action.entity.ContentUserAction;

/**
 * <p></p>
 * @author goya
 * @since 2025/6/11 11:20
 */
public interface ContentUserActionService extends IService<ContentUserAction> {

    /**
     * 获取用户行为信息
     *
     * @param userId  用户Id
     * @param videoId 视频Id
     * @return 结果
     */
    ContentUserActionInfoDTO getUserActionInfo(String userId, String videoId);

    /**
     * 用户行为
     *
     * @param dto dto
     */
    void action(ContentUserActionDTO dto);
}
