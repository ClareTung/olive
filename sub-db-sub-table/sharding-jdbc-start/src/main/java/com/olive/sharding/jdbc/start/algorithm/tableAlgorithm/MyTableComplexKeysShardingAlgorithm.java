package com.olive.sharding.jdbc.start.algorithm.tableAlgorithm;

import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.Collection;

/**
 * @description: 自定义复合分表策略
 * @program: olive
 * @author: dtq
 * @create: 2021/2/26 11:12
 */
public class MyTableComplexKeysShardingAlgorithm implements ComplexKeysShardingAlgorithm<Long> {
    @Override
    public Collection<String> doSharding(Collection<String> collection, ComplexKeysShardingValue<Long> complexKeysShardingValue) {
//        Collection<Integer> ids = getShardingValue(shardingValues, "id");
//        Collection<Integer> usernames = getShardingValue(shardingValues, "username");
        return null;
    }
}
