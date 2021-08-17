package com.olive.ratelimiter.controller;

import com.olive.ratelimiter.annotation.OliveRateLimiter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: Redis+lua脚本实现分布式限流
 * @program: olive
 * @author: dtq
 * @create: 2021/8/17 17:44
 */
@RestController
public class RateLimiterController {

    @OliveRateLimiter(limit = 10)
    @GetMapping("/limiter")
    public String sendMessage2() {
        return "请求正常，结果正确！！！";
    }
}
