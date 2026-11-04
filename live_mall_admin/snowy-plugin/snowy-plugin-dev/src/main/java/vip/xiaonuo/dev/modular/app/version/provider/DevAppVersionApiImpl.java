package vip.xiaonuo.dev.modular.app.version.provider;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.dev.api.DevAppVersionApi;
import vip.xiaonuo.dev.dto.LatestAppVersionDTO;
import vip.xiaonuo.dev.modular.app.version.service.AppVersionService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/27 16:09
 */
@Component
@Slf4j
public class DevAppVersionApiImpl implements DevAppVersionApi {

    @Inject
    private AppVersionService appVersionService;

    @Override
    public LatestAppVersionDTO getLatestAppVersion() {
        return appVersionService.getLatestAppVersion();
    }
}
