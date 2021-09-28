package com.olive.springcloud.nacos.provide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/9/27 18:54
 */
@EnableDiscoveryClient
@SpringBootApplication
public class NacosProvideApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosProvideApplication.class, args);
    }
}
