package com.olive.sharding.jdbc.start.key;

import org.apache.shardingsphere.spi.keygen.ShardingKeyGenerator;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 自定义主键生成器
 * @program: olive
 * @author: dtq
 * @create: 2021/2/26 11:55
 */
@Component
public class MyShardingKeyGenerator implements ShardingKeyGenerator {


    private final AtomicInteger count = new AtomicInteger();

    /**
     * 自定义的生成方案类型
     */
    @Override
    public String getType() {
        return "XXX";
    }

    /**
     * 核心方法-生成主键ID
     */
    @Override
    public Comparable<?> generateKey() {
        return count.incrementAndGet();
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}

