package com.olive.multi.db;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/8/30 18:28
 */
@MapperScan(basePackages = "com.olive.multi.db.mapper")
@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
public class MultiDbApplication {
    public static void main(String[] args) {
        SpringApplication.run(MultiDbApplication.class, args);
    }
}
