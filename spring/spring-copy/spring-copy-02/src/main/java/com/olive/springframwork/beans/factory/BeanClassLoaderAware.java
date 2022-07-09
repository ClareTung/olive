package com.olive.springframwork.beans.factory;

/**
 * 类BeanClassLoaderAware的实现描述：实现此接口，既能感知到所属的 ClassLoader
 *
 * @author dongtangqiang 2022/7/9 8:19
 */
public interface BeanClassLoaderAware  extends Aware{

    void setBeanClassLoader(ClassLoader classLoader);
}