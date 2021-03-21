package com.olive.springboot.start.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * @desc: Jackson配置输入输出字段为蛇形命名法
 * @classname: JacksonConfiguration
 * @author: dongtangqiang
 * @date: 2020-12-31
 */
//@Configuration
public class JacksonConfiguration {

/*    @Bean
    public HttpMessageConverters jacksonConverter() {
        return new HttpMessageConverters(new CustomJacksonConverter());
    }


    public static class CustomJacksonConverter extends MappingJackson2HttpMessageConverter {

        public CustomJacksonConverter() {
            this.getObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        }

    }*/
}
