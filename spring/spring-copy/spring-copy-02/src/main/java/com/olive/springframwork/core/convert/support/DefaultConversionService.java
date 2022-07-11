package com.olive.springframwork.core.convert.support;

import com.olive.springframwork.core.convert.converter.ConverterRegistry;

/**
 * 类DefaultConversionService的实现描述：实现类型转换服务
 *
 * @author dongtangqiang 2022/7/10 22:16
 */
public class DefaultConversionService extends GenericConversionService{

    public DefaultConversionService() {
        addDefaultConverters(this);
    }

    public static void addDefaultConverters(ConverterRegistry converterRegistry) {
        // 添加各类类型转换工厂
        converterRegistry.addConverterFactory(new StringToNumberConverterFactory());
    }

}