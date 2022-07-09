package com.olive.spirngframwork.test;

import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;

import com.olive.spirngframwork.bean.User03Service;
import com.olive.spirngframwork.bean.User04Service;
import com.olive.spirngframwork.bean.User05Service;
import com.olive.spirngframwork.bean.User06Service;
import com.olive.spirngframwork.bean.User07Service;
import com.olive.spirngframwork.bean.User08Service;
import com.olive.spirngframwork.bean.User09Service;
import com.olive.spirngframwork.bean.UserDao;
import com.olive.spirngframwork.bean.UserService;
import com.olive.spirngframwork.event.CustomEvent;
import com.olive.springframwork.beans.PropertyValue;
import com.olive.springframwork.beans.PropertyValues;
import com.olive.springframwork.beans.factory.config.BeanDefinition;
import com.olive.springframwork.beans.factory.config.BeanReference;
import com.olive.springframwork.beans.factory.support.DefaultListableBeanFactory;
import com.olive.springframwork.beans.factory.xml.XmlBeanDefinitionReader;
import com.olive.springframwork.context.support.ClassPathXmlApplicationContext;

/**
 * 类ApiTest的实现描述：单元测试类
 *
 * @author dongtangqiang 2022/5/27 9:02
 */
public class ApiTest {

    @Test
    public void testBeanFactory() {
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
    public void testBeanFactory03() {
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
    public void testBeanFactory04() {
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2.UserDao注册
        beanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));

        // 3. UserService 设置属性[uId、userDao]
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("uId", "10001"));
        propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));

        // 4. UserService 注入bean
        BeanDefinition beanDefinition = new BeanDefinition(User04Service.class, propertyValues);
        beanFactory.registerBeanDefinition("userService04", beanDefinition);

        // 5. UserService 获取bean
        User04Service user04Service = (User04Service) beanFactory.getBean("userService04");
        user04Service.queryUserInfo();
    }

    @Test
    public void testXmlRegisterBean() {
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

    @Test
    public void test06() {
        // 测试上下文
        // 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring06.xml");

        // 2. 获取Bean对象调用方法
        User06Service userService = applicationContext.getBean("user06Service", User06Service.class);
        String result = userService.queryUserInfo();
        System.out.println("测试结果：" + result);
    }

    @Test
    public void test07() {
        // 测试上下文
        // 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring07.xml");
        applicationContext.registerShutdownHook();

        // 2. 获取Bean对象调用方法
        User07Service userService = applicationContext.getBean("user07Service", User07Service.class);
        String result = userService.queryUserInfo();
        System.out.println("测试结果：" + result);
    }


    @Test
    public void test08() {
        // 测试感知接口对应的具体实现(BeanNameAware, BeanClassLoaderAware, ApplicationContextAware, BeanFactoryAware)
        // 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring08.xml");
        applicationContext.registerShutdownHook();

        // 2. 获取Bean对象调用方法
        User08Service userService = applicationContext.getBean("user08Service", User08Service.class);
        String result = userService.queryUserInfo();
        System.out.println("测试结果：" + result);
        System.out.println("ApplicationContextAware：" + userService.getApplicationContext());
        System.out.println("BeanFactoryAware：" + userService.getBeanFactory());
    }

    @Test
    public void testPrototype(){
        // 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring09.xml");
        applicationContext.registerShutdownHook();

        // 2. 获取Bean对象调用方法
        User09Service userService01 = applicationContext.getBean("user09Service", User09Service.class);
        User09Service userService02 = applicationContext.getBean("user09Service", User09Service.class);

        // 3. 配置 scope="prototype/singleton"
        System.out.println(userService01);
        System.out.println(userService02);

        // 4. 打印十六进制哈希
        System.out.println(userService01 + " 十六进制哈希：" + Integer.toHexString(userService01.hashCode()));
        System.out.println(ClassLayout.parseInstance(userService01).toPrintable());
        System.out.println(userService01 + " 十六进制哈希：" + Integer.toHexString(userService02.hashCode()));
        System.out.println(ClassLayout.parseInstance(userService02).toPrintable());
    }

    @Test
    public void test09() {
        // 测试感知接口对应的具体实现(BeanNameAware, BeanClassLoaderAware, ApplicationContextAware, BeanFactoryAware)
        // 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring09.xml");
        applicationContext.registerShutdownHook();

        // 2. 获取Bean对象调用方法
        User09Service userService01 = applicationContext.getBean("user09Service", User09Service.class);
        System.out.println("测试结果：" + userService01.queryUserInfo());
    }

    @Test
    public void testEvent() {
        // 测试感知接口对应的具体实现(BeanNameAware, BeanClassLoaderAware, ApplicationContextAware, BeanFactoryAware)
        // 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring10.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext, 1L, "success"));
        applicationContext.registerShutdownHook();
    }
}
