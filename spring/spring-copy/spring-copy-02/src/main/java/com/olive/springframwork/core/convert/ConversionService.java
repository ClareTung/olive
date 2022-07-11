package com.olive.springframwork.core.convert;

import com.sun.istack.internal.Nullable;

/**
 * 类ConversionService的实现描述：TODO 类实现描述
 *
 * @author dongtangqiang 2022/7/10 22:18
 */
public interface ConversionService {

    /** Return {@code true} if objects of {@code sourceType} can be converted to the {@code targetType}. */
    boolean canConvert(@Nullable Class<?> sourceType, Class<?> targetType);

    /** Convert the given {@code source} to the specified {@code targetType}. */
    <T> T convert(Object source, Class<T> targetType);

}
