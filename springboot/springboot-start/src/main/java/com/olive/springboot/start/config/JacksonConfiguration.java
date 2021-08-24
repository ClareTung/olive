package com.olive.springboot.start.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @desc: Jackson配置输入输出字段为蛇形命名法
 * @classname: JacksonConfiguration
 * @author: dongtangqiang
 * @date: 2020-12-31
 */
//@Configuration
public class JacksonConfiguration {
/*
    @Bean
    public HttpMessageConverters jacksonConverter() {
        return new HttpMessageConverters(new CustomJacksonConverter());
    }


    public static class CustomJacksonConverter extends MappingJackson2HttpMessageConverter {

        public CustomJacksonConverter() {
            this.getObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            getObjectMapper().setSerializerFactory(getObjectMapper().getSerializerFactory().withSerializerModifier(new MyBeanSerializerModifier()));
        }

        *//**
         * 处理数组类型的null值
         *//*
        public class NullArrayJsonSerializer extends JsonSerializer<Object> {

            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                if (value == null) {
                    jgen.writeStartArray();
                    jgen.writeEndArray();
                }
            }
        }


        *//**
         * 处理字符串等类型的null值
         *//*
        public class NullStringJsonSerializer extends JsonSerializer<Object> {

            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(StringUtils.EMPTY);
            }
        }

        *//**
         * 处理字符串等类型的null值
         *//*
        public class NullNumberJsonSerializer extends JsonSerializer<Object> {

            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeNumber(0);
            }
        }

        *//**
         * 处理字符串等类型的null值
         *//*
        public class NullBooleanJsonSerializer extends JsonSerializer<Object> {

            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeBoolean(false);
            }
        }


        public class MyBeanSerializerModifier extends BeanSerializerModifier {


            @Override
            public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
                //循环所有的beanPropertyWriter
                for (Object beanProperty : beanProperties) {
                    BeanPropertyWriter writer = (BeanPropertyWriter) beanProperty;
                    //判断字段的类型，如果是array，list，set则注册nullSerializer
                    if (isArrayType(writer)) {
                        //给writer注册一个自己的nullSerializer
                        writer.assignNullSerializer(new NullArrayJsonSerializer());
                    } else if (isNumberType(writer)) {
                        writer.assignNullSerializer(new NullNumberJsonSerializer());
                    } else if (isBooleanType(writer)) {
                        writer.assignNullSerializer(new NullBooleanJsonSerializer());
                    } else if (isStringType(writer)) {
                        writer.assignNullSerializer(new NullStringJsonSerializer());
                    }
                }
                return beanProperties;
            }

            *//**
             * 是否是数组
             *//*
            private boolean isArrayType(BeanPropertyWriter writer) {
                Class<?> clazz = writer.getType().getRawClass();
                return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
            }

            *//**
             * 是否是string
             *//*
            private boolean isStringType(BeanPropertyWriter writer) {
                Class<?> clazz = writer.getType().getRawClass();
                return CharSequence.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz);
            }


            *//**
             * 是否是int
             *//*
            private boolean isNumberType(BeanPropertyWriter writer) {
                Class<?> clazz = writer.getType().getRawClass();
                return Number.class.isAssignableFrom(clazz);
            }

            *//**
             * 是否是boolean
             *//*
            private boolean isBooleanType(BeanPropertyWriter writer) {
                Class<?> clazz = writer.getType().getRawClass();
                return clazz.equals(Boolean.class);
            }

        }
    }*/
}
