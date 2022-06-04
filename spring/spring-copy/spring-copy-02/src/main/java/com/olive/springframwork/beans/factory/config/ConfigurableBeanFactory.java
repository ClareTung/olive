package com.olive.springframwork.beans.factory.config;

import com.olive.springframwork.beans.factory.HierarchicalBeanFactory;

/**
 * 类ConfigurableBeanFactory的实现描述：ConfigurableBeanFactory
 *
 * @author dongtangqiang 2022/5/28 22:50
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

}
