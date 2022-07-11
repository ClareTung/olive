package com.olive.springframwork.context.support;

import java.util.Set;

import com.olive.springframwork.beans.factory.FactoryBean;
import com.olive.springframwork.beans.factory.InitializingBean;
import com.olive.springframwork.core.convert.ConversionService;
import com.olive.springframwork.core.convert.converter.Converter;
import com.olive.springframwork.core.convert.converter.ConverterFactory;
import com.olive.springframwork.core.convert.converter.ConverterRegistry;
import com.olive.springframwork.core.convert.converter.GenericConverter;
import com.olive.springframwork.core.convert.support.DefaultConversionService;
import com.olive.springframwork.core.convert.support.GenericConversionService;
import com.sun.istack.internal.Nullable;

/**
 * 类ConversionServiceFactoryBean的实现描述：提供创建 ConversionService 工厂
 *
 * @author dongtangqiang 2022/7/11 16:51
 */
public class ConversionServiceFactoryBean implements FactoryBean<ConversionService>, InitializingBean {

    @Nullable
    private Set<?> converters;

    @Nullable
    private GenericConversionService conversionService;

    @Override
    public ConversionService getObject() throws Exception {
        return conversionService;
    }

    @Override
    public Class<?> getObjectType() {
        return conversionService.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.conversionService = new DefaultConversionService();
        registerConverters(converters, conversionService);
    }

    private void registerConverters(Set<?> converters, ConverterRegistry registry) {
        if (converters != null) {
            for (Object converter : converters) {
                if (converter instanceof GenericConverter) {
                    registry.addConverter((GenericConverter) converter);
                } else if (converter instanceof Converter<?, ?>) {
                    registry.addConverter((Converter<?, ?>) converter);
                } else if (converter instanceof ConverterFactory<?, ?>) {
                    registry.addConverterFactory((ConverterFactory<?, ?>) converter);
                } else {
                    throw new IllegalArgumentException("Each converter object must implement one of the " +
                            "Converter, ConverterFactory, or GenericConverter interfaces");
                }
            }
        }
    }

    public void setConverters(Set<?> converters) {
        this.converters = converters;
    }

}
