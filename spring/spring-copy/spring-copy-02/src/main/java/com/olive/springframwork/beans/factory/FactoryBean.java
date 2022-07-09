package com.olive.springframwork.beans.factory;

/**
 * 类FactoryBean的实现描述：FactoryBean 中需要提供3个方法，获取对象、对象类型，以及是否是单例对象，如果是单例对象依然会被放到内存中
 *
 * @author dongtangqiang 2022/7/9 9:24
 */
public interface FactoryBean<T> {

    T getObject() throws Exception;

    Class<?> getObjectType();

    boolean isSingleton();

}
