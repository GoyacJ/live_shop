package vip.xiaonuo.distribution.core.config;

import com.github.xiaoymin.knife4j.solon.extension.OpenApiExtensionResolver;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;
import org.noear.solon.docs.DocDocket;
import org.noear.solon.docs.models.ApiInfo;
import org.noear.solon.scheduling.annotation.EnableScheduling;
import vip.xiaonuo.common.pojo.CommonHttpCodes;

/**
 * <p>分销管理配置类</p>
 *
 * @author goya
 * @since 2025/5/13 10:37
 */
@Slf4j
@Configuration
@EnableScheduling
public class DistributionConfig {

    @Inject
    private OpenApiExtensionResolver openApiExtensionResolver;

    /**
     * API文档分组配置
     *
     * @author xuyuxiang
     * @date 2022/7/7 16:18
     **/
    @Bean("distributionDocApi")
    public DocDocket distributionDocApi() {
        return new DocDocket()
                .info(new ApiInfo()
                        .title("分销管理")
                        .description("分销管理")
                        .termsOfService("https://www.xiaonuo.vip")
                        .contact("SNOWY_TEAM", "https://www.xiaonuo.vip", "xuyuxiang29@foxmail.com")
                        .version("3.0.0"))
                .globalResponseCodes(new CommonHttpCodes())
                .groupName("分销管理")
                .apis("vip.xiaonuo.distribution")
                .basicAuth(openApiExtensionResolver.getSetting().getBasic())
                .vendorExtensions(openApiExtensionResolver.buildExtensions());
    }
}
