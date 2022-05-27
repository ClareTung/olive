package com.olive.springframwork.beans.factory.support;

import com.olive.springframwork.beans.factory.config.BeanDefinition;

/**
 * 类BeanDefinitionRegistry的实现描述：注册beanDefinition
 *
 * @author dongtangqiang 2022/5/27 8:57
 */
public interface BeanDefinitionRegistry {

    /**
     * 向注册表中注册 BeanDefinition
     *
     * @param beanName
     * @param beanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}