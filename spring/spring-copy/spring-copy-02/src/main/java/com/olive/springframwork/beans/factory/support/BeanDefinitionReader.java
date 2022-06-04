package com.olive.springframwork.beans.factory.support;

import com.olive.springframwork.beans.BeansException;
import com.olive.springframwork.core.io.Resource;
import com.olive.springframwork.core.io.ResourceLoader;

/**
 * 类BeanDefinitionReader的实现描述：Bean定义读取接口
 * </br>
 * 接口管定义，抽象类处理非接口功能外的注册Bean组件填充，最终实现类即可只关心具体的业务实现
 * @author dongtangqiang 2022/5/28 22:24
 */
public interface BeanDefinitionReader {
    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resource) throws BeansException;

    void loadBeanDefinitions(Resource... resources) throws BeansException;

    void loadBeanDefinitions(String location) throws BeansException;

    void loadBeanDefinitions(String... locations) throws BeansException;
}