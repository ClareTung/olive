package com.olive.springframwork.beans.factory;

/**
 * 类BeanNameAware的实现描述：实现此接口，既能感知到所属的 BeanName
 *
 * @author dongtangqiang 2022/7/9 8:22
 */
public interface BeanNameAware extends Aware{

    void setBeanName(String name);
}