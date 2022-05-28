package com.olive.springframwork.beans.factory.config;

/**
 * 类BeanReference的实现描述：Bean的引用
 *
 * @author dongtangqiang 2022/5/28 18:04
 */
public class BeanReference {
    private final String beanName;

    public BeanReference(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
