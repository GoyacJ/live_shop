package vip.xiaonuo.live.modular.timer;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.consts.SubConstants;
import vip.xiaonuo.common.timer.CommonTimerTaskRunner;
import vip.xiaonuo.live.modular.openim.OpenImImpl;

import java.util.Map;

@Slf4j
@Component
public class FetchTokenTaskRunner implements CommonTimerTaskRunner {

    @Inject
    private OpenImImpl openImImpl;
    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Override
    public void action(String extJson) {
        log.info("获取token定时任务启动。。。。。。。");
        Map<String, String> adminToken = openImImpl.getAdminToken();
        if (!adminToken.isEmpty()) {
            String token = adminToken.get("token");
            String expireTimeSecondsStr = adminToken.get("expireTimeSeconds");
            if (token != null && expireTimeSecondsStr != null) {
                commonCacheOperator.put(SubConstants.OPENIM_TOKEN_REDIS_KEY, token, Long.valueOf(expireTimeSecondsStr));
            }
        }
    }
}
