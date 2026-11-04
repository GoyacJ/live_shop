package vip.xiaonuo.content.core.config;

import com.github.xiaoymin.knife4j.solon.extension.OpenApiExtensionResolver;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.solon.docs.DocDocket;
import org.noear.solon.docs.models.ApiInfo;
import vip.xiaonuo.common.pojo.CommonHttpCodes;

/**
 * <p>内容管理配置类</p>
 *
 * @author goya
 * @since 2025/5/13 10:37
 */
@Configuration
public class ContentConfig {

    @Inject
    private OpenApiExtensionResolver openApiExtensionResolver;

    /**
     * API文档分组配置
     *
     * @author xuyuxiang
     * @date 2022/7/7 16:18
     **/
    @Bean("contentDocApi")
    public DocDocket contentDocApi() {
        return new DocDocket()
                .info(new ApiInfo()
                        .title("内容管理")
                        .description("内容管理")
                        .termsOfService("https://www.xiaonuo.vip")
                        .contact("SNOWY_TEAM", "https://www.xiaonuo.vip", "xuyuxiang29@foxmail.com")
                        .version("3.0.0"))
                .globalResponseCodes(new CommonHttpCodes())
                .groupName("内容管理")
                .apis("vip.xiaonuo.content")
                .basicAuth(openApiExtensionResolver.getSetting().getBasic())
                .vendorExtensions(openApiExtensionResolver.buildExtensions());
    }
}
