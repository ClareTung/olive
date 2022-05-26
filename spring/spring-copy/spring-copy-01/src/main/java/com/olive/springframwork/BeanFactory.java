package com.olive.springframwork;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类BeanFactory的实现描述：代表了 Bean 对象的工厂，可以存放 Bean 定义到 Map 中以及获取
 *
 * @author dongtangqiang 2022/5/26 21:10
 */
public class BeanFactory {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    public Object getBean(String name){
        return beanDefinitionMap.get(name).getBean();
    }

    public void registerBeanDefinition(String name, BeanDefinition beanDefinition){
        beanDefinitionMap.put(name, beanDefinition);
    }
}
