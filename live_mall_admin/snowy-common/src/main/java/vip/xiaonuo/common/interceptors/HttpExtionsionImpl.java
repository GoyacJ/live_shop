package vip.xiaonuo.common.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.net.http.HttpExtension;
import org.noear.solon.net.http.HttpUtils;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.consts.SubConstants;

@Slf4j
@Component
public class HttpExtionsionImpl implements HttpExtension {

    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Inject("${openim.secret}")
    private String secret;

    @Inject("${openim.userId}")
    private String userId;

    @Inject("${openim.ip}")
    private String ip;

    @Override
    public void onInit(HttpUtils httpUtils, String url) {
        httpUtils.header("operationID", String.valueOf(System.currentTimeMillis()));
        httpUtils.header("Content-Type", "application/json");
        httpUtils.header("token",String.valueOf(commonCacheOperator.get(SubConstants.OPENIM_TOKEN_REDIS_KEY)));
    }
}


