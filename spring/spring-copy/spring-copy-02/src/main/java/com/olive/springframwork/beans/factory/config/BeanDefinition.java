package com.olive.springframwork.beans.factory.config;

/**
 * 类BeanDefinition的实现描述：用于定义 Bean 实例化信息
 *
 * @author dongtangqiang 2022/5/26 21:53
 */
public class BeanDefinition {

    private Class beanClass;

    public BeanDefinition(Class beanClass) {
        this.beanClass = beanClass;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }
}
