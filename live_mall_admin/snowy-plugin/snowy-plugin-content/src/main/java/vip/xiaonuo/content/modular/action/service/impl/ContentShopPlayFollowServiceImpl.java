package vip.xiaonuo.content.modular.action.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.content.modular.action.entity.ContentShopPlayFollow;
import vip.xiaonuo.content.modular.action.mapper.ContentShopPlayFollowMapper;
import vip.xiaonuo.content.modular.action.service.ContentShopPlayFollowService;
import vip.xiaonuo.content.modular.action.service.ContentStatCounterService;

import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 11:19
 */
@Slf4j
@Component
public class ContentShopPlayFollowServiceImpl extends ServiceImpl<ContentShopPlayFollowMapper, ContentShopPlayFollow> implements ContentShopPlayFollowService {

    @Inject
    private ContentStatCounterService contentStatCounterService;

    @Override
    public boolean isFollow(String userId, String shopPlayId) {
        return this.exists(
                new QueryWrapper()
                        .eq(ContentShopPlayFollow::getUserId, userId)
                        .eq(ContentShopPlayFollow::getShopPlayId, shopPlayId)
                        .eq(ContentShopPlayFollow::getIsActive, SimpleStatus.ENABLE)
        );
    }

    @Override
    public void follow(String userId, String shopPlayId, Boolean follow) {
        ContentShopPlayFollow contentShopPlayFollow = new ContentShopPlayFollow();
        contentShopPlayFollow.setUserId(userId);
        contentShopPlayFollow.setShopPlayId(shopPlayId);
        contentShopPlayFollow.setIsActive(Boolean.TRUE.equals(follow) ? SimpleStatus.ENABLE : SimpleStatus.DISABLE);
        contentShopPlayFollow.setOperationTime(LocalDateTime.now());
        this.save(contentShopPlayFollow);

        // 添加追剧统计信息
        contentStatCounterService.addFollowerStats(shopPlayId, contentShopPlayFollow.getIsActive());
    }
}
