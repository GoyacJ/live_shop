package vip.xiaonuo.live.core.config;

import com.github.xiaoymin.knife4j.solon.extension.OpenApiExtensionResolver;
import lombok.Data;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.solon.docs.DocDocket;
import org.noear.solon.docs.models.ApiInfo;
import vip.xiaonuo.common.pojo.CommonHttpCodes;

/**
 * 直播配置类。
 * 该类用于存储和管理直播相关的配置信息，包括推流域名、拉流域名、直播应用名、直播流名、推流和拉流的鉴权URL以及鉴权URL的有效时间。
 */
@Configuration
@Data
public class LiveConfig {

    /**
     * 推流域名。
     * 从配置文件中注入，用于生成推流地址。
     */
    @Inject("${live.push.domain}")
    private String LivePushDomain;

    /**
     * 拉流域名。
     * 从配置文件中注入，用于生成拉流地址。
     */
    @Inject("${live.pull.domain}")
    private String LivePullDomain;

    /**
     * 直播协议。
     * 从配置文件中注入，用于生成拉流地址。
     */
    @Inject("${live.protocol}")
    private String protocol;

    /**
     * 直播应用名。
     * 从配置文件中注入，用于标识直播应用。
     */
    @Inject("${live.app.name}")
    private String LiveAppName;

    /**
     * 直播流名。
     * 从配置文件中注入，用于标识直播流。
     */
    @Inject("${live.stream.name}")
    private String streamName;

    /**
     * 推流鉴权URL。
     * 从配置文件中注入，用于生成推流地址时的鉴权。
     */
    @Inject("${live.push.ident.key}")
    private String livePushIdentKey;

    /**
     * 拉流鉴权URL。
     * 从配置文件中注入，用于生成拉流地址时的鉴权。
     */
    @Inject("${live.pull.ident.key}")
    private String livePullIdentKey;

    /**
     * 鉴权URL的有效时间。
     * 从配置文件中注入，用于设置生成的鉴权URL的有效时间。
     */
    @Inject("${live.ident.valid.time}")
    private Integer liveIdentUrlValidTime;

    @Inject
    private OpenApiExtensionResolver openApiExtensionResolver;

    /**
     * API文档分组配置
     *
     * @author xuyuxiang
     * @date 2022/7/7 16:18
     **/
    @Bean("liveDocApi")
    public DocDocket liveDocApi() {
        return new DocDocket()
                .info(new ApiInfo()
                        .title("直播")
                        .description("直播")
                        .termsOfService("https://www.xiaonuo.vip")
                        .contact("SNOWY_TEAM", "https://www.xiaonuo.vip", "xuyuxiang29@foxmail.com")
                        .version("3.0.0"))
                .globalResponseCodes(new CommonHttpCodes())
                .groupName("直播")
                .apis("vip.xiaonuo.live")
                .basicAuth(openApiExtensionResolver.getSetting().getBasic())
                .vendorExtensions(openApiExtensionResolver.buildExtensions());
    }
}