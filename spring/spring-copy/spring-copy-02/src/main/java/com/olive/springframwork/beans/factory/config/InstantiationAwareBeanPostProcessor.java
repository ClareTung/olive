package com.olive.springframwork.beans.factory.config;

import com.olive.springframwork.beans.BeansException;

/**
 * 类InstantiationAwareBeanPostProcessor的实现描述：InstantiationAwareBeanPostProcessor
 *
 * @author dongtangqiang 2022/7/9 18:02
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * Apply this BeanPostProcessor <i>before the target bean gets instantiated</i>.
     * The returned bean object may be a proxy to use instead of the target bean,
     * effectively suppressing default instantiation of the target bean.
     *
     * 在 Bean 对象执行初始化方法之前，执行此方法
     *
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

}