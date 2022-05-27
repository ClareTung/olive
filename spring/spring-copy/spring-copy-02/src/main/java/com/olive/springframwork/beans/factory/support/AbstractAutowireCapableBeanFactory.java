package com.olive.springframwork.beans.factory.support;

import com.olive.springframwork.beans.BeansException;
import com.olive.springframwork.beans.factory.config.BeanDefinition;

/**
 * 类AbstractAutowireCapableBeanFactory的实现描述：实例化Bean类
 *
 * @author dongtangqiang 2022/5/27 8:52
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        Object bean = null;
        try {
            bean = beanDefinition.getBeanClass().newInstance();
        } catch  (InstantiationException | IllegalAccessException e) {
            throw new BeansException("Instantiation of bean failed", e);
        }

        addSingleton(beanName, bean);
        return bean;
    }
}
