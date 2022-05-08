package com.olive.springboot.start.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import springfox.documentation.spring.web.json.Json;

import java.lang.reflect.Type;

/**
 * @author dongtangqiang
 */
public class SpringfoxJsonToGsonAdapter implements JsonSerializer<Json> {
    @Override
    public JsonElement serialize(Json json, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonParser().parse(json.value());
    }
}
