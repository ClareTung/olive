package com.olive.springframwork.context.support;

import com.olive.springframwork.beans.BeansException;
import com.olive.springframwork.beans.factory.ConfigurableListableBeanFactory;
import com.olive.springframwork.beans.factory.support.DefaultListableBeanFactory;

/**
 * 类AbstractRefreshableApplicationContext的实现描述：获取Bean工厂和加载资源
 *
 * @author dongtangqiang 2022/6/4 9:49
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext{

    private DefaultListableBeanFactory beanFactory;

    @Override
    protected void refreshBeanFactory() throws BeansException {
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    private DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);

    @Override
    protected ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
