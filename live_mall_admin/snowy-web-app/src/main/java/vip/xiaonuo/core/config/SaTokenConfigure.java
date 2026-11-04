/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.core.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.solon.dao.SaTokenDaoOfRedisBase64;
import cn.dev33.satoken.solon.integration.SaTokenInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.core.handler.GlobalExceptionUtil;
import vip.xiaonuo.sys.core.enums.SysBuildInEnum;

/**
 * Snowy配置
 *
 * @author xuyuxiang
 * @date 2021/10/9 14:24
 **/
@Configuration
public class SaTokenConfigure {

    /**
     * 无需登录的接口地址集合
     */
    private static final String[] NO_LOGIN_PATH_ARR = {
            /* 主入口 */
            "/",

            /* 静态资源 */
            "/favicon.ico",
            "/doc.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/v2/api-docs-ext",
            "/configuration/ui",
            "/configuration/security",
            "/ureport/**",
            "/druid/**",
            "/images/**",

            /* 认证相关 */
            "/auth/c/getPicCaptcha",
            "/auth/c/getPhoneValidCode",
            "/auth/c/doLogin",
            "/auth/c/doLoginByPhone",

            "/auth/b/getPicCaptcha",
            "/auth/b/getPhoneValidCode",
            "/auth/b/doLogin",
            "/auth/b/doLoginByPhone",

            /* 三方登录相关 */
            "/auth/third/render",
            "/auth/third/callback",

            /* 系统基础配置 */
            "/dev/config/sysBaseList",

            /* 系统字典树 */
            "/dev/dict/tree",

            /* 文件下载 */
            "/dev/file/download",

            /* 用户个人中心相关 */
            "/sys/userCenter/getPicCaptcha",
            "/sys/userCenter/findPasswordGetPhoneValidCode",
            "/sys/userCenter/findPasswordGetEmailValidCode",
            "/sys/userCenter/findPasswordByPhone",
            "/sys/userCenter/findPasswordByEmail",

            /* 快递鸟回调地址 */
            "/kdniao/trajectory/callback"
    };

    /**
     * 仅超管使用的接口地址集合
     */
    private static final String[] SUPER_PERMISSION_PATH_ARR = {
            "/auth/session/**",
            "/auth/third/page",
            "/client/user/**",
            "/sys/org/**",
            "/sys/group/**",
            "/sys/position/**",
            "/sys/button/**",
            "/sys/menu/**",
            "/sys/module/**",
            "/sys/role/**",
            "/sys/user/**",
            "/sys/index/bizDataCount",
            "/sys/index/opDataCount",
            "/sys/index/toolDataCount",
            "/dev/config/**",
            "/dev/dict/**",
            "/dev/email/**",
            "/dev/file/**",
            "/dev/job/**",
            "/dev/log/**",
            "/dev/message/**",
            "/dev/monitor/**",
            "/dev/sms/**",
            "/dev/slideshow/**",
            "/gen/basic/**",
            "/gen/config/**",
            "/mobile/menu/**",
            "/mobile/module/**",
    };

    /**
     * 注册跨域过滤器
     */
    @Bean(index = -100)
    public SaTokenInterceptor saTokenInit() {
        return new SaTokenInterceptor()
                // 指定拦截路由
                .addInclude("/**")
                .addExclude("/swagger-resources", "/swagger/*") //todo: 用于支持 swagger-resouces

                // 设置鉴权的接口
                .setAuth(r -> {
                    SaRouter.match("/**")
                            .notMatch(CollectionUtil.newArrayList(NO_LOGIN_PATH_ARR))
                            .check(r1 -> StpUtil.checkLogin());
                    SaRouter.match(CollectionUtil.newArrayList(SUPER_PERMISSION_PATH_ARR))
                            .notMatch(CollectionUtil.newArrayList(NO_LOGIN_PATH_ARR))
                            .check(r1 -> StpUtil.checkRole(SysBuildInEnum.BUILD_IN_ROLE_CODE.getValue()));
                })
                // 前置函数：在每次认证函数之前执行
                .setBeforeAuth(obj -> {

                    // ---------- 设置跨域响应头 ----------
                    SaHolder.getResponse()

                            // 是否可以在iframe显示视图： DENY=不可以 | SAMEORIGIN=同域下可以 | ALLOW-FROM uri=指定域名下可以
                            // .setHeader("X-Frame-Options", "SAMEORIGIN")

                            // 是否启用浏览器默认XSS防护： 0=禁用 | 1=启用 | 1; mode=block 启用, 并在检查到XSS攻击时，停止渲染页面
                            .setHeader("X-XSS-Protection", "1; mode=block")
                            // 禁用浏览器内容嗅探
                            .setHeader("X-Content-Type-Options", "nosniff")
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", "*")
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
                            // 有效时间
                            .setHeader("Access-Control-Max-Age", "3600")
                            // 允许的header参数
                            .setHeader("Access-Control-Allow-Headers", "*");

                    // 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            // OPTIONS预检请求，不做处理
                            .free(r -> {
                            })
                            .back();
                }).setError(e -> {
                    // 由于过滤器中抛出的异常不进入全局异常处理，所以必须提供[异常处理函数]来处理[认证函数]里抛出的异常
                    // 在[异常处理函数]里的返回值，将作为字符串输出到前端，此处统一转为JSON输出前端
                    SaResponse saResponse = SaHolder.getResponse();
                    saResponse.setHeader(Header.CONTENT_TYPE.getValue(), ContentType.JSON + ";charset=" + CharsetUtil.UTF_8);
                    return GlobalExceptionUtil.getCommonResult((Exception) e);
                });
    }

    @Bean
    public SaTokenDao saTokenDaoInit(@Inject("${sa-token.redis}") SaTokenDaoOfRedisBase64 saTokenDao) {
        //todo: 如果 SaTokenDaoOfRedisJson 有问题，就换成 SaTokenDaoOfRedisBase64。by noear 202301
        return saTokenDao;
    }
}
