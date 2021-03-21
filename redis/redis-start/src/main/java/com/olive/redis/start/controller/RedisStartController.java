package com.olive.redis.start.controller;

import com.olive.redis.start.entity.RedisStartEntity;
import com.olive.redis.start.service.RedisStartServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/20 17:54
 */
@RestController
@RequestMapping("/redis/start")
public class RedisStartController {

    @Resource
    private RedisStartServiceImpl redisStartService;


    @GetMapping("/get")
    public RedisStartEntity get(@RequestParam(value = "id") Integer id) {
        return redisStartService.getRedisStartEntity(id);
    }

    @PostMapping("/set")
    public RedisStartEntity set(@RequestBody RedisStartEntity redisStartEntity) {
        return redisStartService.setRedisStartEntity(redisStartEntity);
    }

    @GetMapping("/delete")
    public String delete(@RequestParam(value = "id") Integer id) {
        return redisStartService.deleteRedisStartEntity(id);
    }

    @PostMapping("/update")
    public RedisStartEntity update(@RequestBody RedisStartEntity redisStartEntity) {
        return redisStartService.updateRedisStartEntity(redisStartEntity);
    }
}
