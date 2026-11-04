package vip.xiaonuo.task.core.config;

import com.github.xiaoymin.knife4j.solon.extension.OpenApiExtensionResolver;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.solon.docs.DocDocket;
import org.noear.solon.docs.models.ApiInfo;
import org.springframework.scheduling.annotation.EnableScheduling;
import vip.xiaonuo.common.pojo.CommonHttpCodes;

/**
 * <p>积分管理配置类</p>
 *
 * @author goya
 * @since 2025/5/28 15:36
 */
@Slf4j
@EnableScheduling
@Configuration
public class TaskConfig {

    @Inject
    private OpenApiExtensionResolver openApiExtensionResolver;

    /**
     * API文档分组配置
     *
     * @author xuyuxiang
     * @date 2022/7/7 16:18
     **/
    @Bean("taskDocApi")
    public DocDocket taskDocApi() {
        return new DocDocket()
                .info(new ApiInfo()
                        .title("任务管理")
                        .description("任务管理")
                        .termsOfService("https://www.xiaonuo.vip")
                        .contact("SNOWY_TEAM", "https://www.xiaonuo.vip", "xuyuxiang29@foxmail.com")
                        .version("3.0.0"))
                .globalResponseCodes(new CommonHttpCodes())
                .groupName("任务管理")
                .apis("vip.xiaonuo.task")
                .basicAuth(openApiExtensionResolver.getSetting().getBasic())
                .vendorExtensions(openApiExtensionResolver.buildExtensions());
    }
}
