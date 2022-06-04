package com.olive.springframwork.beans.factory;

import com.olive.springframwork.beans.BeansException;
import com.olive.springframwork.beans.factory.config.AutowireCapableBeanFactory;
import com.olive.springframwork.beans.factory.config.BeanDefinition;
import com.olive.springframwork.beans.factory.config.BeanPostProcessor;
import com.olive.springframwork.beans.factory.config.ConfigurableBeanFactory;

/**
 * 提供分析和修改Bean以及预先实例化的操作接口
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    void preInstantiateSingletons() throws BeansException;

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}