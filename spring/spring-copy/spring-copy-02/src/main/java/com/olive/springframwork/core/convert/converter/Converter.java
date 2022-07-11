package com.olive.springframwork.core.convert.converter;

/**
 * 类Converter的实现描述：定义类型转换接口
 *
 * @author dongtangqiang 2022/7/10 22:10
 */
public interface Converter<S, T>  {

    /** Convert the source object of type {@code S} to target type {@code T}. */
    T convert(S source);

}

