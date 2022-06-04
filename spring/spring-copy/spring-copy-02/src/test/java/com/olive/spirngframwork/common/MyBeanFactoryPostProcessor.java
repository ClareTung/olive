package com.olive.spirngframwork.common;

import com.olive.springframwork.beans.BeansException;
import com.olive.springframwork.beans.PropertyValue;
import com.olive.springframwork.beans.PropertyValues;
import com.olive.springframwork.beans.factory.ConfigurableListableBeanFactory;
import com.olive.springframwork.beans.factory.config.BeanDefinition;
import com.olive.springframwork.beans.factory.config.BeanFactoryPostProcessor;

/**
 * 类MyBeanFactoryPostProcessor的实现描述：自定义BeanFactoryPostProcessor
 *
 * @author dongtangqiang 2022/6/4 11:13
 */
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("user06Service");
        PropertyValues propertyValues = beanDefinition.getPropertyValues();

        propertyValues.addPropertyValue(new PropertyValue("company", "XXX公司"));
    }
}
