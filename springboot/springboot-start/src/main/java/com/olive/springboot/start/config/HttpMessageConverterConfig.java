package com.olive.springboot.start.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;

import java.nio.charset.StandardCharsets;

/**
 * @desc: Fastjson配置
 * @classname: HttpMessageConverterConfig
 * @author: dongtangqiang
 * @date: 2020-12-31
 */
//@Configuration
public class HttpMessageConverterConfig {
    //引入Fastjson解析json，不使用默认的jackson
    //必须在pom.xml引入fastjson的jar包，并且版必须大于1.2.10
    // @Bean
    /*public HttpMessageConverters fastJsonHttpMessageConverters() {
        //1、定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        //2、添加fastjson的配置信息
        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        SerializerFeature[] serializerFeatures = new SerializerFeature[]{
                //输出key是包含双引号
                //SerializerFeature.QuoteFieldNames,
                //是否输出为null的字段,若为null 则显示该字段
                //SerializerFeature.WriteMapNullValue,
                //数值字段如果为null，则输出为0
                SerializerFeature.WriteNullNumberAsZero,
                //List字段如果为null,输出为[],而非null
                SerializerFeature.WriteNullListAsEmpty,
                //字符类型字段如果为null,输出为"",而非null
                SerializerFeature.WriteNullStringAsEmpty,
                //Boolean字段如果为null,输出为false,而非null
                SerializerFeature.WriteNullBooleanAsFalse,
                //Date的日期转换器
                SerializerFeature.WriteDateUseDateFormat,
                //循环引用
                SerializerFeature.DisableCircularReferenceDetect,
        };

        fastJsonConfig.setSerializerFeatures(serializerFeatures);
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);

        //3、在convert中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);

        //4、将convert添加到converters中
        return new HttpMessageConverters(fastConverter);
    }*/
}
