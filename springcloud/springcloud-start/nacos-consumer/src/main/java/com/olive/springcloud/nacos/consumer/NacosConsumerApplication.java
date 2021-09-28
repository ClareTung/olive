package com.olive.springcloud.nacos.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/9/27 19:02
 */
@EnableFeignClients(basePackages = {"com.olive.springcloud.nacos.consumer.feignclient"})
@EnableDiscoveryClient
@SpringBootApplication
public class NacosConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosConsumerApplication.class, args);
    }
}
