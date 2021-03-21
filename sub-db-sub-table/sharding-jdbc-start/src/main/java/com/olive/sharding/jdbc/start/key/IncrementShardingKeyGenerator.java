package com.olive.sharding.jdbc.start.key;

import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.spi.keygen.ShardingKeyGenerator;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/26 12:02
 */
public final class IncrementShardingKeyGenerator implements ShardingKeyGenerator {

    @Getter
    private final String type = "INCREMENT";

    private final AtomicInteger count = new AtomicInteger();

    @Getter
    @Setter
    private Properties properties = new Properties();

    @Override
    public Comparable<?> generateKey() {
        return count.incrementAndGet();
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
