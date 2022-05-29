package com.olive.springframwork.beans.factory;

import com.olive.springframwork.beans.BeansException;

/**
 * 类BeanFactory的实现描述：bean工厂
 *
 * @author dongtangqiang 2022/5/26 22:05
 */
public interface BeanFactory {

    Object getBean(String name) throws BeansException;

    Object getBean(String name, Object... args)throws BeansException;

    <T> T getBean(String name, Class<T> requiredType) throws BeansException;
}