package com.olive.starter.autoconfigure.configuration;

import com.olive.starter.autoconfigure.properties.OliveProperties;
import com.olive.starter.autoconfigure.service.OliveService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @description: 包含我们需要提供starter特性的所有 @Bean 定义
 * @program: olive
 * @author: dtq
 * @create: 2021/2/4 16:46
 */
@Configuration
@ConditionalOnClass(OliveService.class)
@EnableConfigurationProperties(OliveProperties.class)
public class OliveAutoConfiguration {

    @Resource
    private OliveProperties oliveProperties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "olive", value = "enable", havingValue = "true")
    public OliveService oliveService() {
        return new OliveService(oliveProperties.getUserId());
    }

}
