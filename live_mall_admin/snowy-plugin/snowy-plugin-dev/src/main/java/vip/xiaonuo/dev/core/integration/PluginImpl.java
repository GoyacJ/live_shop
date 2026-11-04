package vip.xiaonuo.dev.core.integration;

import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.dev.core.aop.DevLogInterceptor;

/**
 * @author noear 2022/10/21 created
 */
public class PluginImpl implements Plugin {
    @Override
    public void start(AppContext context) {
        context.beanInterceptorAdd(CommonLog.class, new DevLogInterceptor());
    }
}
