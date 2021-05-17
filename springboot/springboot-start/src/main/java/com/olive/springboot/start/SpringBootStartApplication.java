package com.olive.springboot.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @desc: 启动类
 * @classname: SpringBootStartApplication
 * @author: dongtangqiang
 * @date: 2020-12-31
 */
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class SpringBootStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStartApplication.class, args);
    }
}
