package manage;

import manage.dto.MobileDeviceAccessDTO;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/30 11:10
 */
public interface MobileDeviceInfoApi {
    /**
     * 保存设备信息
     * @param bean 设备信息
     * @return 结果
     */
    boolean saveDeviceInfo(MobileDeviceAccessDTO bean);

    /**
     * 更新用户登录设备
     * @param userId 用户id
     * @param clientId 设备id
     */
    void updateMobileDeviceInfo(String userId, String clientId);
}
