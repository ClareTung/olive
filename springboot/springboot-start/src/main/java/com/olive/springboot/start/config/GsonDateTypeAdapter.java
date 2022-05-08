package com.olive.springboot.start.config;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;

/**
 * Gson日期序列化转换为unix时间戳，为了和fastjson保持一致
 *
 * @author dongtangqiang
 */
public class GsonDateTypeAdapter extends TypeAdapter<Date> {
    @Override
    public void write(JsonWriter jsonWriter, Date date) throws IOException {
        if (date == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(date.getTime());
        }
    }

    @Override
    public Date read(JsonReader jsonReader) throws IOException {
        if (jsonReader != null) {
            return new Date(jsonReader.nextLong());
        } else {
            return null;
        }
    }
}
