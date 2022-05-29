package com.olive.springframwork.beans.factory.support;

import com.olive.springframwork.core.io.DefaultResourceLoader;
import com.olive.springframwork.core.io.ResourceLoader;

/**
 * 类AbstractBeanDefinitionReader的实现描述：Bean定义抽象类实现
 *
 * @author dongtangqiang 2022/5/28 22:31
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    private final BeanDefinitionRegistry registry;

    private ResourceLoader resourceLoader;

    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this(registry, new DefaultResourceLoader());
    }

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        this.registry = registry;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}
