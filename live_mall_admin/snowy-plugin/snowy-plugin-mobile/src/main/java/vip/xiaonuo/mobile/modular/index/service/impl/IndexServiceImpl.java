package vip.xiaonuo.mobile.modular.index.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.dev.api.DevConfigApi;
import vip.xiaonuo.dev.dto.DevConfigDTO;
import vip.xiaonuo.mobile.modular.index.service.IndexService;
import vip.xiaonuo.mobile.modular.index.vo.PrivacyAgreementVO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/12 11:27
 */
@Slf4j
@Component
public class IndexServiceImpl implements IndexService {

    @Inject
    private DevConfigApi devConfigApi;

    @Override
    public PrivacyAgreementVO privacyAgreement() {
        List<DevConfigDTO> configList = devConfigApi.getConfigByCategory("PRIVACY_AGREEMENT");
        Map<String, String> configMap = configList.stream()
                .collect(Collectors.toMap(DevConfigDTO::getConfigKey, DevConfigDTO::getConfigValue, (a, b) -> b));
        PrivacyAgreementVO vo = new PrivacyAgreementVO();
        vo.setUserAgreement(configMap.get("USER_AGREEMENT"));
        vo.setPrivacyPolicy(configMap.get("PRIVACY_POLICY"));
        return vo;
    }
}
