package com.olive.springframwork.test;

import org.junit.Test;

import com.olive.springframwork.BeanDefinition;
import com.olive.springframwork.BeanFactory;
import com.olive.springframwork.service.UserService;

/**
 * 类ApiTest的实现描述：测试
 *
 * @author dongtangqiang 2022/5/26 21:17
 */
public class ApiTest {

    @Test
    public void testBeanFactory(){
        // 1. 初始化bean工厂
        BeanFactory beanFactory = new BeanFactory();

        // 2.注册bean
        BeanDefinition beanDefinition = new BeanDefinition(new UserService());
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        // 3.获取bean
        UserService userService = (UserService)beanFactory.getBean("userService");
        userService.queryUserInfo();
    }
}
