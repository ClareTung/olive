package com.olive.redisson.strategy;

import com.olive.redisson.entity.RedissonProperties;
import org.redisson.config.Config;

/**
 * @description: Redisson配置构建接口
 * @program: dtq
 * @author: dtq
 * @create: 2021/8/12 16:14
 */
public interface RedissonConfigService {
    /**
     * 根据不同的Redis配置策略创建对应的Config
     *
     * @param redissonProperties
     * @return Config
     */
    Config createRedissonConfig(RedissonProperties redissonProperties);
}
