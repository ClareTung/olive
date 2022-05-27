package com.olive.springframwork.beans.factory.support;

import java.lang.reflect.Constructor;

import com.olive.springframwork.beans.BeansException;
import com.olive.springframwork.beans.factory.config.BeanDefinition;

/**
 * 类InstantiationStrategy的实现描述：定义实例化策略接口
 *
 * @author dongtangqiang 2022/5/27 21:57
 */
public interface InstantiationStrategy {

    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) throws BeansException;
}