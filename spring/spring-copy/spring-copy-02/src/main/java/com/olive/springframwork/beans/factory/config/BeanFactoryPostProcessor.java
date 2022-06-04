package com.olive.springframwork.beans.factory.config;

import com.olive.springframwork.beans.BeansException;
import com.olive.springframwork.beans.factory.ConfigurableListableBeanFactory;

/**
 * 类BeanFactoryPostProcessor的实现描述：允许在 Bean 对象注册后但未实例化之前，对 Bean 的定义信息 BeanDefinition 执行修改操作
 *
 * @author dongtangqiang 2022/6/4 9:24
 */
public interface BeanFactoryPostProcessor {

    /**
     * 在所有的 BeanDefinition 加载完成后，实例化 Bean 对象之前，提供修改 BeanDefinition 属性的机制
     *
     * @param beanFactory
     * @throws BeansException
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}