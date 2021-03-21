package com.olive.sharding.jdbc.start.algorithm.tableAlgorithm;

import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/26 11:30
 */
public class MyTableHintShardingAlgorithm implements HintShardingAlgorithm<String> {

    @Override
    public Collection<String> doSharding(Collection<String> tableNames, HintShardingValue<String> hintShardingValue) {

        Collection<String> result = new ArrayList<>();
        for (String tableName : tableNames) {
            Collection<String> values = hintShardingValue.getValues();
            for (String shardingValue : values) {
                if (tableName.endsWith(String.valueOf(Long.parseLong(shardingValue) % tableNames.size()))) {
                    result.add(tableName);
                }
            }
        }
        return result;
    }
}
