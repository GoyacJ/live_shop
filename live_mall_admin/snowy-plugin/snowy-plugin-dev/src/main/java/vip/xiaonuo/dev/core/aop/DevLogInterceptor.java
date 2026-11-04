package vip.xiaonuo.dev.core.aop;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import org.apache.ibatis.cache.CacheException;
import org.noear.solon.core.aspect.Interceptor;
import org.noear.solon.core.aspect.Invocation;
import vip.xiaonuo.auth.core.pojo.SaBaseLoginUser;
import vip.xiaonuo.auth.core.util.StpLoginUserUtil;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.dev.modular.log.util.DevLogUtil;

/**
 * @author noear 2022/10/21 created
 */
public class DevLogInterceptor implements Interceptor {
    @Override
    public Object doIntercept(Invocation inv) throws Throwable {
        CommonLog commonLog = inv.method().getAnnotation(CommonLog.class);

        Object result = null;

        try {
            result = inv.invoke();

            String userName = "未知";
            try {
                SaBaseLoginUser loginUser = StpLoginUserUtil.getLoginUser();
                if (ObjectUtil.isNotNull(loginUser)) {
                    userName = loginUser.getName();
                }
            } catch (Exception ignored) {
            }
            // 异步记录日志
            DevLogUtil.executeOperationLog(commonLog, userName, inv, JSONUtil.toJsonStr(result));

        } catch (CommonException e) {
            String userName = "未知";
            try {
                SaBaseLoginUser loginUser = StpLoginUserUtil.getLoginUser();
                if (ObjectUtil.isNotNull(loginUser)) {
                    userName = loginUser.getName();
                }
            } catch (Exception ignored) {
            }
            //异步记录日志
            DevLogUtil.executeExceptionLog(commonLog, userName, inv, e);
            throw new CommonException(e.getMsg());
        }

        return result;
    }
}
