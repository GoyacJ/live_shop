package vip.xiaonuo.integral.core.config;

import com.github.xiaoymin.knife4j.solon.extension.OpenApiExtensionResolver;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.solon.docs.DocDocket;
import org.noear.solon.docs.models.ApiInfo;
import org.noear.solon.scheduling.annotation.EnableScheduling;
import vip.xiaonuo.common.pojo.CommonHttpCodes;

/**
 * <p>积分管理配置类</p>
 *
 * @author goya
 * @since 2025/5/28 15:36
 */
@Configuration
@EnableScheduling
public class IntegralConfig {


    @Inject
    private OpenApiExtensionResolver openApiExtensionResolver;

    /**
     * API文档分组配置
     *
     * @author xuyuxiang
     * @date 2022/7/7 16:18
     **/
    @Bean("integralDocApi")
    public DocDocket integralDocApi() {
        return new DocDocket()
                .info(new ApiInfo()
                        .title("积分管理")
                        .description("积分管理")
                        .termsOfService("https://www.xiaonuo.vip")
                        .contact("SNOWY_TEAM", "https://www.xiaonuo.vip", "xuyuxiang29@foxmail.com")
                        .version("3.0.0"))
                .globalResponseCodes(new CommonHttpCodes())
                .groupName("积分管理")
                .apis("vip.xiaonuo.integral")
                .basicAuth(openApiExtensionResolver.getSetting().getBasic())
                .vendorExtensions(openApiExtensionResolver.buildExtensions());
    }
}
