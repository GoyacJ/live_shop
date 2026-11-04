package vip.xiaonuo.auth.modular.mobile.auth;

import cn.hutool.json.JSONObject;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.auth.modular.mobile.param.MobileAuthLoginParam;
import vip.xiaonuo.auth.modular.mobile.service.MobileAuthService;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.mobile.api.auth.api.MobileAuthApi;


/**
 * 移动端登录实现类
 *
 */
@Component("mobileAuthApi")
public class MobileAuthApiProvider implements MobileAuthApi {

    @Inject
    private MobileAuthService mobileAuthService;

    @Override
    public CommonResult loginOrRegistUser(JSONObject jsonObject) {
        MobileAuthLoginParam mobileAuthPasswordLoginParam = jsonObject.toBean(MobileAuthLoginParam.class);
        return mobileAuthService.MobileLogin(mobileAuthPasswordLoginParam);
    }

    @Override
    public CommonResult manageUser(JSONObject param) {
        return CommonResult.ok();
    }

    @Override
    public CommonResult getPhoneValidCode(JSONObject param) {
        MobileAuthLoginParam mobileAuthPasswordLoginParam = param.toBean(MobileAuthLoginParam.class);
        return mobileAuthService.getPhoneValidCode(mobileAuthPasswordLoginParam);
    }
}
