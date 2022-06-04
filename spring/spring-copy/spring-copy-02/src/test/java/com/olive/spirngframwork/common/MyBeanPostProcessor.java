package com.olive.spirngframwork.common;

import com.olive.spirngframwork.bean.User06Service;
import com.olive.springframwork.beans.BeansException;
import com.olive.springframwork.beans.factory.config.BeanPostProcessor;

/**
 * 类MyBeanPostProcessor的实现描述：MyBeanPostProcessor
 *
 * @author dongtangqiang 2022/6/4 11:17
 */
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("user06Service".equals(beanName)) {
            User06Service userService = (User06Service) bean;
            userService.setLocation("上海");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
