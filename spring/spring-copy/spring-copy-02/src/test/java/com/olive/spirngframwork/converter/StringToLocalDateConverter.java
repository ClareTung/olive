package com.olive.spirngframwork.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.olive.springframwork.core.convert.converter.Converter;

/**
 * 类StringToLocalDateConverter的实现描述：StringToLocalDateConverter
 *
 * @author dongtangqiang 2022/7/11 16:46
 */
public class StringToLocalDateConverter  implements Converter<String, LocalDate> {

    private final DateTimeFormatter DATE_TIME_FORMATTER;

    public StringToLocalDateConverter(String pattern) {
        DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public LocalDate convert(String source) {
        return LocalDate.parse(source, DATE_TIME_FORMATTER);
    }

}
