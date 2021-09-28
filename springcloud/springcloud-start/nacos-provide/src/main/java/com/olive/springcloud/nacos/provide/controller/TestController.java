package com.olive.springcloud.nacos.provide.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/9/27 18:56
 */
@RestController
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "你好， Nacos!";
    }
}
