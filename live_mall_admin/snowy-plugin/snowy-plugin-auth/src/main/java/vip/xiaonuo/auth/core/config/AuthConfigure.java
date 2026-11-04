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
package vip.xiaonuo.auth.core.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.github.xiaoymin.knife4j.solon.extension.OpenApiExtensionResolver;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.solon.docs.DocDocket;
import org.noear.solon.docs.models.ApiInfo;
import vip.xiaonuo.auth.core.enums.SaClientTypeEnum;
import vip.xiaonuo.auth.core.util.StpClientUtil;
import vip.xiaonuo.auth.core.util.StpLoginUserUtil;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.consts.CacheConstant;
import vip.xiaonuo.common.pojo.CommonHttpCodes;

import java.util.List;
import java.util.function.Supplier;

/**
 * SaToken鉴权配置
 *
 * @author xuyuxiang
 * @date 2021/10/9 14:24
 **/
@Configuration
public class AuthConfigure {

    /**
     * 注册Sa-Token的注解拦截器，打开注解式鉴权功能
     * <p>
     * 注解的方式有以下几中，注解既可以加在接口方法上，也可加在Controller类上：
     * 1.@SaCheckLogin: 登录认证 —— 只有登录之后才能进入该方法（常用）
     * 2.@SaCheckRole("admin"): 角色认证 —— 必须具有指定角色标识才能进入该方法（常用）
     * 3.@SaCheckPermission("user:add"): 权限认证 —— 必须具有指定权限才能进入该方法（常用）
     * 4.@SaCheckSafe: 二级认证校验 —— 必须二级认证之后才能进入该方法
     * 5.@SaCheckBasic: HttpBasic认证 —— 只有通过 Basic 认证后才能进入该方法
     * <p>
     * 在Controller中创建一个接口，默认不需要登录也不需要任何权限都可以访问的，只有加了上述注解才会校验
     **/
    @Bean
    public StpLogic getStpLogic() {
        // 重写Sa-Token的StpLogic，默认客户端类型为B
        return new StpLogic(SaClientTypeEnum.B.getValue());
    }

    /**
     * 权限认证接口实现类，集成权限认证功能
     *
     * @author xuyuxiang
     * @date 2022/7/7 16:16
     **/
    @Component
    public static class StpInterfaceImpl implements StpInterface {

        @Inject
        private CommonCacheOperator commonCacheOperator;

        /**
         * 返回一个账号所拥有的权限码集合
         */
        @Override
        public List<String> getPermissionList(Object loginId, String loginType) {
            Object permissionListObject;
            if (SaClientTypeEnum.B.getValue().equals(loginType)) {
                permissionListObject = commonCacheOperator.get(CacheConstant.AUTH_B_PERMISSION_LIST_CACHE_KEY + loginId);

            }else{
                permissionListObject = commonCacheOperator.get(CacheConstant.AUTH_C_PERMISSION_LIST_CACHE_KEY + loginId);
            }
            return JSONUtil.parseArray(permissionListObject).toList(String.class);
        }

        /**
         * 返回一个账号所拥有的角色标识集合
         */
        @Override
        public List<String> getRoleList(Object loginId, String loginType) {
            return StpLoginUserUtil.getLoginUser().getRoleCodeList();
        }
    }

    /**
     * 获取当前用户id放到容器里边
     *
     * @author ChengChuanYao
     * @date 2024/06/22 15:30:20
     */
    @Bean("user_id_bean")
    public Supplier<String> userIdBean() {
        return () -> {
            Object loginId = StpUtil.getLoginId();
            if (null == loginId) {
                Object clientLoginId = StpClientUtil.getLoginId();
                return null == clientLoginId ? null : String.valueOf(clientLoginId);
            } else {
                return String.valueOf(loginId);
            }
        };
    }

    @Inject
    private OpenApiExtensionResolver openApiExtensionResolver;

    /**
     * API文档分组配置
     *
     * @author chengchuanyao
     * @date 2024/8/20 9:08
     */
    @Bean("authDocApi")
    public DocDocket authDocApi() {
        return new DocDocket()
                .info(new ApiInfo()
                        .title("验证功能AUTH")
                        .description("验证功能AUTH")
                        .termsOfService("https://www.xiaonuo.vip")
                        .contact("SNOWY_TEAM", "https://www.xiaonuo.vip", "chuzhong1008@gamil.com")
                        .version("3.0.0"))
                .globalResponseCodes(new CommonHttpCodes())
                .groupName("验证功能AUTH")
                .apis("vip.xiaonuo.auth")
                .basicAuth(openApiExtensionResolver.getSetting().getBasic())
                .vendorExtensions(openApiExtensionResolver.buildExtensions());
    }
}
