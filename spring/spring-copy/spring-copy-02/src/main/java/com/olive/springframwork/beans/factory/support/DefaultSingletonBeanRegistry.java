package com.olive.springframwork.beans.factory.support;

import java.util.HashMap;
import java.util.Map;

import com.olive.springframwork.beans.factory.config.SingletonBeanRegistry;

/**
 * 类DefaultSingletonBeanRegistry的实现描述：单例注册接口实现
 *
 * @author dongtangqiang 2022/5/26 22:00
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private Map<String, Object> singletonObjects = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    protected void addSingleton(String beanName, Object singletonObject){
        singletonObjects.put(beanName, singletonObject);
    }
}
