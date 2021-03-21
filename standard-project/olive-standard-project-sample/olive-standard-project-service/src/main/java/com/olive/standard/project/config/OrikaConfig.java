package com.olive.standard.project.config;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: Orika配置
 * @program: olive
 * @author: dtq
 * @create: 2021/3/1 10:51
 */
@Configuration
public class OrikaConfig {

    @Bean
    public MapperFactory mapperFactory(){
        return new DefaultMapperFactory.Builder().build();
    }

    @Bean
    public MapperFacade mapperFacade(){
        return mapperFactory().getMapperFacade();
    }
}
