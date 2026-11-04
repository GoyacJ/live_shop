package vip.xiaonuo.content.modular.action.service;

import com.mybatisflex.core.service.IService;
import vip.xiaonuo.content.modular.action.entity.ContentShopPlayFollow;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 11:19
 */
public interface ContentShopPlayFollowService extends IService<ContentShopPlayFollow> {

    /**
     * 是否追剧
     *
     * @param userId     用户Id
     * @param shopPlayId 短剧Id
     * @return 是否追剧
     */
    boolean isFollow(String userId, String shopPlayId);

    /**
     * 追剧
     *
     * @param userId     用户Id
     * @param shopPlayId 短剧Id
     * @param follow     是否追剧
     */
    void follow(String userId, String shopPlayId, Boolean follow);
}
