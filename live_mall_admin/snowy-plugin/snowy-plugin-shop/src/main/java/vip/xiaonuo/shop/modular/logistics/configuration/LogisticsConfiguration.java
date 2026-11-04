package vip.xiaonuo.shop.modular.logistics.configuration;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Condition;
import org.noear.solon.annotation.Configuration;
import vip.xiaonuo.shop.modular.logistics.kdniao.processor.KdniaoLogisticsProcessorImpl;
import api.logistics.processor.LogisticsProcessor;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/24 15:21
 */
@Slf4j
@Configuration
public class LogisticsConfiguration {

    /**
     * 根据平台选择对应厂商
     * @return
     */
    @Bean
    @Condition(onProperty = "${logistics.platform} = kdniao")
    public LogisticsProcessor logisticsProcessor(){
        return new KdniaoLogisticsProcessorImpl();
    }
}
