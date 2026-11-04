package vip.xiaonuo.core.config;

import org.noear.redisx.RedisClient;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;

/**
 * @author noear 2022/12/18 created
 */
@Configuration
public class RedisClientConfigure {

    @Bean
    public RedisClient redisClient(@Inject("${snowy.redis}") RedisClient redisClient){
        return redisClient;
    }
}
