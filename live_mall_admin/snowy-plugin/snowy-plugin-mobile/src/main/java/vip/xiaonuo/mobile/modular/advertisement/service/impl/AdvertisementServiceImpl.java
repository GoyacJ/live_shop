package vip.xiaonuo.mobile.modular.advertisement.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.mobile.modular.advertisement.dto.AdvertisementVisitDTO;
import vip.xiaonuo.mobile.modular.advertisement.service.AdvertisementService;
import vip.xiaonuo.task.api.TaskApi;
import vip.xiaonuo.task.api.enums.TaskBusinessCategory;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 19:36
 */
@Slf4j
@Component
public class AdvertisementServiceImpl implements AdvertisementService {

    @Inject
    private TaskApi taskApi;

    @Override
    public void visit(AdvertisementVisitDTO dto) {
        taskApi.task(dto.userId(), dto.taskId(), TaskBusinessCategory.ADVERTISEMENT_VISIT);
    }
}
