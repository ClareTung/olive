package com.olive.springboot.start.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 解决swagger异常
 *
 * @author dongtangqiang
 */
@Configuration
public class GsonSwaggerConfig {

    //设置swagger支持gson
    @Bean
    public IGsonHttpMessageConverter IGsonHttpMessageConverter() {
        return new IGsonHttpMessageConverter();
    }
}
