package com.olive.sharding.jdbc.start;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/25 16:59
 */
@MapperScan(basePackages = "com.olive.sharding.jdbc.start.mapper")
@SpringBootApplication
public class ShardingJdbcStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShardingJdbcStartApplication.class, args);
    }
}
