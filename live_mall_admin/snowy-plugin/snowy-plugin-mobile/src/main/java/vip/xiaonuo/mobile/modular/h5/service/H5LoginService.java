package vip.xiaonuo.mobile.modular.h5.service;

import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.mobile.modular.h5.param.H5LoginParam;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/12 14:56
 */
public interface H5LoginService {

    String getCode(String phone);

    CommonResult login(H5LoginParam loginParam);
}
