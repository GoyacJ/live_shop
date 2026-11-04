package vip.xiaonuo.content.api;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.content.modular.action.service.ContentStatCounterService;
import vip.xiaonuo.content.modular.shortplay.service.ContentShortPlayEpisodeService;
import vip.xiaonuo.content.modular.shortplay.service.ContentShortPlayService;
import vip.xiaonuo.task.api.TaskApi;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/14 15:22
 */
@Slf4j
@Component
public class ShortPlayApiImpl {

    @Inject
    private ContentShortPlayService contentShortPlayService;

    @Inject
    private ContentShortPlayEpisodeService contentShortPlayEpisodeService;

    @Inject
    private ContentStatCounterService contentShortPlayRecordService;

    @Inject
    private TaskApi taskApi;

}
