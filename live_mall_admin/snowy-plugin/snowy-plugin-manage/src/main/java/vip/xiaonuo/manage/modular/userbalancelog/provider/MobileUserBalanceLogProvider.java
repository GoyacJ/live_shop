package vip.xiaonuo.manage.modular.userbalancelog.provider;

import cn.hutool.json.JSONObject;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.manage.modular.userbalancelog.param.MobileUserBalanceLogUserIdParam;
import vip.xiaonuo.manage.modular.userbalancelog.service.UserBalanceLogService;
import vip.xiaonuo.manage.modular.userintegral.service.UserIntegralService;
import vip.xiaonuo.mobile.api.auth.api.MobileUserBalanceLogApi;


/**
 * 移动端余额变更记录实现类
 *
 */
@Component("mobileUserBalanceLogApi")
public class MobileUserBalanceLogProvider implements MobileUserBalanceLogApi {

    @Inject
    private UserBalanceLogService userBalanceLogService;

    @Inject
    private UserIntegralService userIntegralService;

    @Override
    public CommonResult getUserBalanceLogByUserId(JSONObject jSONObject) {
        MobileUserBalanceLogUserIdParam userBalanceLogReasonIdParam = new MobileUserBalanceLogUserIdParam();
        userBalanceLogReasonIdParam.setCreateUser(jSONObject.getStr("createUser"));
        userBalanceLogReasonIdParam.setBalanceType(jSONObject.getStr("balanceType"));
        return CommonResult.data(userBalanceLogService.getdetailByUserId(userBalanceLogReasonIdParam));
    }
}
