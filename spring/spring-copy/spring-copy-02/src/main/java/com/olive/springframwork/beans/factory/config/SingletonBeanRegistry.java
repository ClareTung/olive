package com.olive.springframwork.beans.factory.config;

/**
 * 类SingletonBeanRegistry的实现描述：单例注册接口
 *
 * @author dongtangqiang 2022/5/26 21:57
 */
public interface SingletonBeanRegistry {

    Object getSingleton(String beanName);

    void registerSingleton(String beanName, Object singletonObject);
}
