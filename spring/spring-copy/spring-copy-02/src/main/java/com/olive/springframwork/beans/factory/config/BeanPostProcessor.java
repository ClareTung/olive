package com.olive.springframwork.beans.factory.config;

import com.olive.springframwork.beans.BeansException;

/**
 * 类BeanPostProcessor的实现描述：提供了修改新实例化 Bean 对象的扩展点
 *
 * @author dongtangqiang 2022/6/4 9:27
 */
public interface BeanPostProcessor {

    /**
     * 在 Bean 对象执行初始化方法之前，执行此方法
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    /**
     * 在 Bean 对象执行初始化方法之后，执行此方法
     *
     * @param bean
     * @param beanName
     * @throws BeansException
     */
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}