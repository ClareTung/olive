package com.olive.spirngframwork.test;

import org.junit.Test;

import com.olive.spirngframwork.bean.User03Service;
import com.olive.spirngframwork.bean.UserService;
import com.olive.springframwork.beans.factory.config.BeanDefinition;
import com.olive.springframwork.beans.factory.support.DefaultListableBeanFactory;

/**
 * 类ApiTest的实现描述：单元测试类
 *
 * @author dongtangqiang 2022/5/27 9:02
 */
public class ApiTest {

    @Test
    public void testBeanFactory(){
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 2.注册 bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition("userService", beanDefinition);
        // 3.第一次获取 bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();
        // 4.第二次获取 bean from Singleton
        UserService userService_singleton = (UserService) beanFactory.getBean("userService");
        userService_singleton.queryUserInfo();
    }

    @Test
    public void testBeanFactory03(){
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2. 注入bean
        BeanDefinition beanDefinition = new BeanDefinition(User03Service.class);
        beanFactory.registerBeanDefinition("userService03", beanDefinition);

        // 3.获取bean
        User03Service userService03 = (User03Service) beanFactory.getBean("userService03", "ClareTung");
        userService03.queryUserInfo();
    }
}
