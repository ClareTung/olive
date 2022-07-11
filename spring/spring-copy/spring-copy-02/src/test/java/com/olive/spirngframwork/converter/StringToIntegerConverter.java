package com.olive.spirngframwork.converter;

import com.olive.springframwork.core.convert.converter.Converter;

/**
 * 类StringToIntegerConverter的实现描述：StringToIntegerConverter
 *
 * @author dongtangqiang 2022/7/11 16:47
 */
public class StringToIntegerConverter implements Converter<String, Integer> {

    @Override
    public Integer convert(String source) {
        return Integer.valueOf(source);
    }

}
