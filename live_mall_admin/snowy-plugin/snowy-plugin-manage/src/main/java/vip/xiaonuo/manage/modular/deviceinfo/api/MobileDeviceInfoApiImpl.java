package vip.xiaonuo.manage.modular.deviceinfo.api;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import lombok.extern.slf4j.Slf4j;
import manage.dto.MobileDeviceAccessDTO;
import manage.MobileDeviceInfoApi;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.manage.modular.deviceinfo.entity.MobileDeviceInfo;
import vip.xiaonuo.manage.modular.deviceinfo.service.MobileDeviceInfoService;

import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/30 11:10
 */
@Slf4j
@Component
public class MobileDeviceInfoApiImpl implements MobileDeviceInfoApi {

    @Inject
    private MobileDeviceInfoService mobileDeviceInfoService;

    @Override
    public boolean saveDeviceInfo(MobileDeviceAccessDTO bean) {
        if (mobileDeviceInfoService.exists(
                new QueryWrapper()
                        .eq(MobileDeviceInfo::getClientId, bean.clientId())
        )) {
            return UpdateChain.of(MobileDeviceInfo.class)
                    .set(MobileDeviceInfo::getLastAccessTime, LocalDateTime.now())
                    .where(MobileDeviceInfo::getClientId)
                    .eq(bean.clientId()).update();
        } else {
            MobileDeviceInfo info = new MobileDeviceInfo();
            info.setClientId(bean.clientId());
            info.setBrand(bean.brand());
            info.setModel(bean.model());
            info.setVersion(bean.version());
            info.setSimulator(bean.simulator());
            info.setFirstAccessTime(LocalDateTime.now());
            info.setLastAccessTime(info.getFirstAccessTime());
            return mobileDeviceInfoService.save(info);
        }
    }

    @Override
    public void updateMobileDeviceInfo(String userId, String clientId) {
        if (StringUtils.isAnyEmpty(userId, clientId)) {
            return;
        }
        UpdateChain.of(MobileDeviceInfo.class)
                .set(MobileDeviceInfo::getLastAccessTime, LocalDateTime.now())
                .set(MobileDeviceInfo::getUserId, userId)
                .where(MobileDeviceInfo::getClientId)
                .eq(clientId).update();
    }
}
