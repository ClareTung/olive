package com.olive.mybatis.start;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description: 启动类
 * @program: olive
 * @author: dtq
 * @create: 2021/8/24 17:10
 */
@MapperScan(basePackages = "com.olive.mybatis.start.mapper")
@SpringBootApplication
public class MybatisStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisStartApplication.class, args);
    }
}
