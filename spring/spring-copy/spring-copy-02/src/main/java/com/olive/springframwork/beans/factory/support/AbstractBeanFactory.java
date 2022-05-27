package com.olive.springframwork.beans.factory.support;

import com.olive.springframwork.beans.BeansException;
import com.olive.springframwork.beans.factory.BeanFactory;
import com.olive.springframwork.beans.factory.config.BeanDefinition;

/**
 * 类AbstractBeanFactory的实现描述：抽象bean工厂
 *
 * 继承了 DefaultSingletonBeanRegistry，也就具备了使用单例注册类方法
 *
 * @author dongtangqiang 2022/5/26 22:21
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {
    @Override
    public Object getBean(String name) throws BeansException {
        Object bean = getSingleton(name);
        if (bean != null) {
            return bean;
        }

        BeanDefinition beanDefinition = getBeanDefinition(name);
        return createBean(name, beanDefinition);
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException;
}
