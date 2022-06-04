package com.olive.springframwork.context.support;

import com.olive.springframwork.beans.factory.support.DefaultListableBeanFactory;
import com.olive.springframwork.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * 类AbstractXmlApplicationContext的实现描述：上下文中对配置信息的加载
 *
 * @author dongtangqiang 2022/6/4 10:07
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory, this);
        String[] configLocations = getConfigLocations();
        if (null != configLocations){
            beanDefinitionReader.loadBeanDefinitions(configLocations);
        }
    }

    protected abstract String[] getConfigLocations();
}
