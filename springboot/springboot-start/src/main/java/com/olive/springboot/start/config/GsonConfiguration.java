package com.olive.springboot.start.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author dongtangqiang
 */
@Configuration
public class GsonConfiguration implements WebMvcConfigurer {

    // 实现日期格式化方法1：
    // 覆盖 GsonHttpMessageConvertersConfiguration 中默认的 GsonHttpMessageConverter
    @Bean
    GsonHttpMessageConverter gsonHttpMessageConverter() {
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setGson(new GsonBuilder().setDateFormat("yyyy/MM/dd").create());
        return converter;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 删除MappingJackson2HttpMessageConverter
        converters.removeIf(httpMessageConverter -> httpMessageConverter instanceof MappingJackson2HttpMessageConverter);
        // 添加GsonHttpMessageConverter
        converters.add(new GsonHttpMessageConverter());
    }

    // 实现日期格式化方法2（更小的粒度）：
    // 覆盖 GsonHttpMessageConvertersConfiguration 中默认的 Gson（由 GsonAutoConfiguration 中注入）
    /*@Bean
    Gson gson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }*/
}
