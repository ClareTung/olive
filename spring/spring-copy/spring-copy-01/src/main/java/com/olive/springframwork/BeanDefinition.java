package com.olive.springframwork;

/**
 * 类BeanDefinition的实现描述：用于定义 Bean 实例化信息，现在的实现是以一个 Object 存放对象
 *
 * @author dongtangqiang 2022/5/26 21:07
 */
public class BeanDefinition {

    private Object bean;

    public BeanDefinition(Object bean) {
        this.bean = bean;
    }

    public Object getBean(){
        return bean;
    }
}
