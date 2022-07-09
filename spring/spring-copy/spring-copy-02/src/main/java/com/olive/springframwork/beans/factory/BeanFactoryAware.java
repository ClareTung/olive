package com.olive.springframwork.beans.factory;

import com.olive.springframwork.beans.BeansException;

/**
 * 类BeanFactoryAware的实现描述：实现此接口，既能感知到所属的 BeanFactory
 *
 * @author dongtangqiang 2022/7/9 8:17
 */
public interface BeanFactoryAware extends Aware{

    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}