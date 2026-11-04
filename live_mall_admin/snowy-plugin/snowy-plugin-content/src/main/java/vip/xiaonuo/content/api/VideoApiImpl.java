package vip.xiaonuo.content.api;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.task.api.TaskApi;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/6 14:33
 */
@Slf4j
@Component
public class VideoApiImpl {

    @Inject
    private TaskApi taskApi;

    @Inject
    private CommonCacheOperator commonCacheOperator;

    public static final String VIDEO_VIEW_DURATION_PREFIX = "video:view_duration:";


}
