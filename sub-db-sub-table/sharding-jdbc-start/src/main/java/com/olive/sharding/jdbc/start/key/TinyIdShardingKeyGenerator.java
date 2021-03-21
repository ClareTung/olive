package com.olive.sharding.jdbc.start.key;

import org.apache.shardingsphere.spi.keygen.ShardingKeyGenerator;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/26 12:01
 */
@Component
public class TinyIdShardingKeyGenerator implements ShardingKeyGenerator {

    /**
     * 自定义的生成方案类型
     */
    @Override
    public String getType() {
        return "tinyid";
    }

    /**
     * 核心方法-生成主键ID
     */
    @Override
    public Comparable<?> generateKey() {

//        Long id = TinyId.nextId("order");

//        return id;
        return null;
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}

