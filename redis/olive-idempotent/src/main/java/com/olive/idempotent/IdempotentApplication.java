package com.olive.idempotent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/9/13 9:40
 */
@SpringBootApplication
public class IdempotentApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdempotentApplication.class, args);
    }
}
