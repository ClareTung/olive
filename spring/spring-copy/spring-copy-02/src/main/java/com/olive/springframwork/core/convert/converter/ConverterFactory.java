package com.olive.springframwork.core.convert.converter;

/**
 * 类ConverterFactory的实现描述：类型转换工厂
 *
 * @author dongtangqiang 2022/7/10 22:12
 */
public interface ConverterFactory<S, R>{

    /**
     * Get the converter to convert from S to target type T, where T is also an instance of R.
     * @param <T> the target type
     * @param targetType the target type to convert to
     * @return a converter from S to T
     */
    <T extends R> Converter<S, T> getConverter(Class<T> targetType);

}
