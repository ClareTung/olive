package com.olive.redis.start.service;

import com.olive.redis.start.entity.RedisStartEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/20 17:47
 */
@Service
@CacheConfig(cacheNames = "my-redis-cache2", cacheManager = "redisCacheManager")
public class RedisStartServiceImpl {

    @Cacheable(key = "#id", sync = true)
    public RedisStartEntity getRedisStartEntity(Integer id) {
        System.out.println("操作数据库，返回RedisStartEntity");
        return new RedisStartEntity(1, "clare", "handsome");
    }

    /**
     * 使用@CachePut注解的方法，一定要有返回值，该注解声明的方法缓存的是方法的返回结果。
     * it always causes the
     * method to be invoked and its result to be stored in the associated cache
     **/
    @CachePut(key = "#redisStartEntity.getId()")
    public RedisStartEntity setRedisStartEntity(RedisStartEntity redisStartEntity) {
        System.out.println("存入数据库");
        return redisStartEntity;
    }

    @CacheEvict(key = "#id")
    public String deleteRedisStartEntity(Integer id) {
        System.out.println("删除数据库中RedisStartEntity");
        return "success";
    }

    @CachePut(key = "#redisStartEntity.getId()")
    public RedisStartEntity updateRedisStartEntity(RedisStartEntity redisStartEntity) {
        System.out.println("修改redisStartEntity,并存入数据库");
        return redisStartEntity;
    }

}
