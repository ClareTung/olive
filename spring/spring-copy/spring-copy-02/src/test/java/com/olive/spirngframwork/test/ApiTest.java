package com.olive.spirngframwork.test;

import org.junit.Test;

import com.olive.spirngframwork.bean.User03Service;
import com.olive.spirngframwork.bean.User04Service;
import com.olive.spirngframwork.bean.User05Service;
import com.olive.spirngframwork.bean.UserDao;
import com.olive.spirngframwork.bean.UserService;
import com.olive.springframwork.beans.PropertyValue;
import com.olive.springframwork.beans.PropertyValues;
import com.olive.springframwork.beans.factory.config.BeanDefinition;
import com.olive.springframwork.beans.factory.config.BeanReference;
import com.olive.springframwork.beans.factory.support.DefaultListableBeanFactory;
import com.olive.springframwork.beans.factory.xml.XmlBeanDefinitionReader;

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

    @Test
    public void testBeanFactory04(){
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2.UserDao注册
        beanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));

        // 3. UserService 设置属性[uId、userDao]
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("uId", "10001"));
        propertyValues.addPropertyValue(new PropertyValue("userDao",new BeanReference("userDao")));

        // 4. UserService 注入bean
        BeanDefinition beanDefinition = new BeanDefinition(User04Service.class, propertyValues);
        beanFactory.registerBeanDefinition("userService04", beanDefinition);

        // 5. UserService 获取bean
        User04Service user04Service = (User04Service) beanFactory.getBean("userService04");
        user04Service.queryUserInfo();
    }

    @Test
    public void testXmlRegisterBean(){
        // case：测试xml加载bean
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2. 读取配置文件&注册Bean
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");

        // 3. 获取Bean对象调用方法
        User05Service userService = beanFactory.getBean("user05Service", User05Service.class);
        String result = userService.queryUserInfo();
        System.out.println("测试结果：" + result);
    }
}
