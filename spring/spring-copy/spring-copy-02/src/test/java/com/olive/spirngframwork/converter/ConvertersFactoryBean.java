package com.olive.spirngframwork.converter;

import java.util.HashSet;
import java.util.Set;

import com.olive.springframwork.beans.factory.FactoryBean;

/**
 * 类ConvertersFactoryBean的实现描述：ConvertersFactoryBean
 *
 * @author dongtangqiang 2022/7/11 16:46
 */
public class ConvertersFactoryBean implements FactoryBean<Set<?>> {

    @Override
    public Set<?> getObject() throws Exception {
        HashSet<Object> converters = new HashSet<>();
        StringToLocalDateConverter stringToLocalDateConverter = new StringToLocalDateConverter("yyyy-MM-dd");
        converters.add(stringToLocalDateConverter);
        return converters;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
