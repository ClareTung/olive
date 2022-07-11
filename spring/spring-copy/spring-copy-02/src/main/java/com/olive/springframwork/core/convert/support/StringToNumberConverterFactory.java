package com.olive.springframwork.core.convert.support;

import com.olive.springframwork.core.convert.converter.Converter;
import com.olive.springframwork.core.convert.converter.ConverterFactory;
import com.olive.springframwork.util.NumberUtils;
import com.sun.istack.internal.Nullable;

/**
 * 类StringToNumberConverterFactory的实现描述：StringToNumberConverterFactory
 *
 * @author dongtangqiang 2022/7/10 22:18
 */
public class StringToNumberConverterFactory implements ConverterFactory<String, Number> {

    @Override
    public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToNumber<>(targetType);
    }

    private static final class StringToNumber<T extends Number> implements Converter<String, T> {

        private final Class<T> targetType;

        public StringToNumber(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        @Nullable
        public T convert(String source) {
            if (source.isEmpty()) {
                return null;
            }
            return NumberUtils.parseNumber(source, this.targetType);
        }
    }

}
