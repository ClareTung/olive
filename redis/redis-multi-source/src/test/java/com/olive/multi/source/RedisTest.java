package com.olive.multi.source;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/9/13 20:11
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    @Qualifier("secondaryRedisTemplate")
    RedisTemplate<String, Object> secondaryRedisTemplate;

    @Autowired
    private Environment environment;

    @Test
    public void testEnvironment() {
        System.out.println("开始测试");
        String property = environment.getProperty("spring.secondaryRedis.cluster.nodes");
        System.out.println("property = " + property);
        System.out.println("over");
    }

    // 同时设置两个数据源的数据
    @Test
    public void testTemplateSet() {
        System.out.println("同时设置数据");
        redisTemplate.opsForValue().set("redis-key2", "redisTemplate");
        secondaryRedisTemplate.opsForValue().set("redis-key2", "secondaryRedisTemplate");
        System.out.println("over");
    }

    // 同时获取两个数据源的数据
    @Test
    public void testTemplateGet() {
        System.out.println("同时获取数据");
        Object key1 = redisTemplate.opsForValue().get("redis-key2");
        Object key2 = secondaryRedisTemplate.opsForValue().get("redis-key2");
        System.out.println("key1 = " + key1);
        System.out.println("key2 = " + key2);
        System.out.println("over");
    }
}
