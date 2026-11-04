package vip.xiaonuo.mobile.core.aop;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.solon.core.route.PathRule;
import org.noear.solon.core.route.RouterInterceptor;
import org.noear.solon.core.route.RouterInterceptorChain;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.mobile.core.conts.MobileRedisConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Author: shaojiang
 * CreateTime: 2025/3/17 14:49
 * Description: 移动端-非登录授权接口拦截器
 */
@Slf4j
@Component
public class ApiInterceptor implements RouterInterceptor {

    @Inject("${mobile.request.allowed-time-difference}")
    private long allowedTimeDifference;

    @Inject("${mobile.request.sign-secret}")
    private String apiSecret;

    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Override
    public PathRule pathPatterns() {
        return new PathRule().include("/mapi/**");
    }

    @Override
    public void doIntercept(Context ctx, Handler mainHandler, RouterInterceptorChain chain) throws Throwable {
        boolean isAuthApi = ctx.pathAsLower().startsWith("/mapi/auth/");

        log.info("请求路径:" + ctx.pathAsLower());
        log.info("请求头:" + ctx.headerMap());
        // 检查时间戳
        String timestamp = ctx.headerOrDefault("timestamp", "0");
        log.info("时间戳：" + timestamp);
        long currentTime = System.currentTimeMillis();
        long requestTime = Convert.toLong(timestamp);
        if (Math.abs(currentTime - requestTime) > allowedTimeDifference) {
            ctx.render(CommonResult.error("请求已过期"));
            return;
        }

        if (null == ctx.header("nonce") || null != commonCacheOperator.get(MobileRedisConstants.API_REQUEST_NONCE + ctx.header("nonce"))) {
            ctx.render(CommonResult.error("请勿重复发送请求"));
            return;
        } else {
            commonCacheOperator.put(MobileRedisConstants.API_REQUEST_NONCE + ctx.header("nonce"), "1", allowedTimeDifference / 1000);
        }

        // 参与签名的参数
        Map<String, String> signMap = new HashMap<>();
        if (ctx.method().equalsIgnoreCase("POST") && ObjectUtil.isNotEmpty(ctx.contentType()) && ctx.contentType().equals("application/json") && ObjectUtil.isNotEmpty(ctx.body())) {
            String body = ctx.body();
            // if (body != null && !body.isEmpty()) {
            signMap = JSONUtil.toBean(body, Map.class);
            // }

            // System.out.println("body-type: " +ctx.contentType());
            // if (body != null && !body.isEmpty() && ctx.contentType().equals("application/json")) {
            //     signMap = JSONUtil.toBean(body, Map.class);
            // }
        } else if (ctx.method().equalsIgnoreCase("GET") && ObjectUtil.isNotEmpty(ctx.paramMap())) {
            signMap = ctx.paramMap().toValueMap();
        }

        signMap.put("device-type", ctx.headerOrDefault("device-type", "-"));
        signMap.put("device-id", ctx.headerOrDefault("device-id", "-"));
        signMap.put("app-version", ctx.headerOrDefault("app-version", "-"));
        signMap.put("timestamp", ctx.headerOrDefault("timestamp", "-"));
        signMap.put("nonce", ctx.headerOrDefault("nonce", "-"));

        String token = ctx.header("authorization");
        if (null != token && !token.isEmpty()) {
            signMap.put("authorization", token);
        }

        Object uid = commonCacheOperator.get(MobileRedisConstants.USER_TOKEN_PREFIX + token);
        if (Objects.isNull(uid)){
            uid = commonCacheOperator.get(MobileRedisConstants.USER_TOKEN_H5_PREFIX + token);
        }
        if (isAuthApi && (null == token || token.isEmpty() || null == uid || ObjectUtil.isEmpty(uid))) {
            ctx.render(CommonResult.unauthorized());
            return;
        } else if (isAuthApi) {
            ctx.attrSet("uid", Convert.toStr(uid));
        }

        String signStr = MapUtil.sortJoin(signMap, "&", "=", true);
        log.info("签名字符串:" + signStr);
        String sign = new Digester(DigestAlgorithm.MD5).digestHex(signStr + apiSecret);
        if (!sign.equals(ctx.header("signature"))) {
            ctx.render(CommonResult.error("参数校验失败" + sign));
            return;
        }

        chain.doIntercept(ctx, mainHandler);
        log.info("离开拦截器");
    }
}

