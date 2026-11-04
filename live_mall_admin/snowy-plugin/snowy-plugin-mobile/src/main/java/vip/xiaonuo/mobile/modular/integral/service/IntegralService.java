package vip.xiaonuo.mobile.modular.integral.service;

import com.mybatisflex.core.paginate.Page;
import vip.xiaonuo.manage.modular.userbalancelog.param.UserIntegralChangePageParam;
import vip.xiaonuo.mobile.modular.integral.dto.SharingLinksDTO;
import vip.xiaonuo.mobile.modular.manage.result.UserIntegralChangeVO;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 19:04
 */
public interface IntegralService {

    /**
     * 分享链接
     * @param dto 参数
     * @return 链接地址
     */
    String sharingLinks(SharingLinksDTO dto);

    /**
     * 获取平台-用户余额变更记录分页
     * @param param 参数
     * @return 分页
     */
    Page<UserIntegralChangeVO> page(UserIntegralChangePageParam param);
}
