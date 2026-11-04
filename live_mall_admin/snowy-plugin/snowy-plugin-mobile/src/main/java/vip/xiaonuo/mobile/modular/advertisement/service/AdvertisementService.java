package vip.xiaonuo.mobile.modular.advertisement.service;

import vip.xiaonuo.mobile.modular.advertisement.dto.AdvertisementVisitDTO;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 19:36
 */
public interface AdvertisementService {

    /**
     * 看广告
     * @param userId 用户id
     */
    void visit(AdvertisementVisitDTO dto);
}
