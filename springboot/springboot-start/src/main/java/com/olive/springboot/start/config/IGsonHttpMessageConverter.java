package com.olive.springboot.start.config;

import com.google.gson.GsonBuilder;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import springfox.documentation.spring.web.json.Json;

/**
 * @author dongtangqiang
 */
public class IGsonHttpMessageConverter extends GsonHttpMessageConverter {
    public IGsonHttpMessageConverter() {
        //自定义Gson适配器
        super.setGson(new GsonBuilder()
                .registerTypeAdapter(Json.class, new SpringfoxJsonToGsonAdapter())
                .serializeNulls()//空值也参与序列化
                .create());
    }
}
