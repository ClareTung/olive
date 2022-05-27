package com.olive.springframwork.beans.factory.support;

import com.olive.springframwork.beans.BeansException;
import com.olive.springframwork.beans.factory.BeanFactory;
import com.olive.springframwork.beans.factory.config.BeanDefinition;

/**
 * 类AbstractBeanFactory的实现描述：抽象bean工厂
 * <p>
 * 继承了 DefaultSingletonBeanRegistry，也就具备了使用单例注册类方法
 *
 * @author dongtangqiang 2022/5/26 22:21
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {
    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name, args);
    }

    protected <T> T doGetBean(final String name, final Object[] args) {
        Object bean = getSingleton(name);
        if (bean != null) {
            return (T) bean;
        }

        BeanDefinition beanDefinition = getBeanDefinition(name);
        return (T) createBean(name, beanDefinition, args);
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    // protected abstract Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException;

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;
}
