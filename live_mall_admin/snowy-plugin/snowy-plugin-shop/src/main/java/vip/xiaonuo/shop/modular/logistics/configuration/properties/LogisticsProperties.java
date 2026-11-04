package vip.xiaonuo.shop.modular.logistics.configuration.properties;

import lombok.Data;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import api.logistics.common.enums.LogisticsPlatform;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>快递配置</p>
 *
 * @author goya
 * @since 2025/4/24 15:20
 */
@Data
@Inject("${logistics}")
@Configuration
public class LogisticsProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 6493762527113733056L;

    /**
     * 是否开启
     */
    private Boolean enabled = false;

    /**
     * 快递平台
     */
    private LogisticsPlatform platform = LogisticsPlatform.kdniao;

    /**
     * 快递鸟
     */
    private Kdniao kdniao;

    /**
     * 快递鸟配置
     */
    @Data
    public static class Kdniao implements Serializable {

        @Serial
        private static final long serialVersionUID = 5415837373335948143L;

        /**
         * 请求地址
         */
        private String url;

        /**
         * 用户Id
         */
        private String userId;

        /**
         * API KEY
         */
        private String apiKey;

    }
}
