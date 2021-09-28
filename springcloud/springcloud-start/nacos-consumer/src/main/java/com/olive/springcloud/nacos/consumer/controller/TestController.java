package com.olive.springcloud.nacos.consumer.controller;

import com.olive.springcloud.nacos.consumer.feignclient.RemoteApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/9/28 9:33
 */
@RestController
public class TestController {

    @Resource
    private RemoteApi remoteApi;

    @GetMapping("/test")
    public String hello() {
        return remoteApi.hello();
    }
}
