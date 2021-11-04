package com.olive.fpc.bs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/11/3 18:02
 */
@SpringBootApplication
@EnableDiscoveryClient
public class FlowPeakClippingBusinessServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowPeakClippingBusinessServiceApplication.class, args);
    }
}
