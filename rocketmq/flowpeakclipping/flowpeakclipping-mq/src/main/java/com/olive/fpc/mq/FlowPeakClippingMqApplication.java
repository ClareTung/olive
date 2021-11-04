package com.olive.fpc.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/11/3 15:52
 */
@SpringBootApplication
@EnableDiscoveryClient
public class FlowPeakClippingMqApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlowPeakClippingMqApplication.class, args);
    }
}
