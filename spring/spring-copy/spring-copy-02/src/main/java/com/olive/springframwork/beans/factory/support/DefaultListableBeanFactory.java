package com.olive.springframwork.beans.factory.support;

import java.util.HashMap;
import java.util.Map;

import com.olive.springframwork.beans.BeansException;
import com.olive.springframwork.beans.factory.config.BeanDefinition;

/**
 * 类DefaultListableBeanFactory的实现描述：核心类实现
 *
 * @author dongtangqiang 2022/5/27 8:55
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry{
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    @Override
    protected BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new BeansException("No bean named '" + beanName + "' is defined");
        }
        return beanDefinition;
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }
}
