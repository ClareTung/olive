package com.olive.springframwork.beans.factory.config;

import com.olive.springframwork.beans.BeansException;
import com.olive.springframwork.beans.factory.BeanFactory;

/**
 * 类AutowireCapableBeanFactory的实现描述：是一个自动化处理Bean工厂配置的接口
 *
 * @author dongtangqiang 2022/5/28 22:49
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * 执行 BeanPostProcessors 接口实现类的 postProcessBeforeInitialization 方法
     *
     * @param existingBean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;

    /**
     * 执行 BeanPostProcessors 接口实现类的 postProcessorsAfterInitialization 方法
     *
     * @param existingBean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;
}
