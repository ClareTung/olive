package com.olive.spirngframwork.bean;

import com.olive.springframwork.beans.BeansException;
import com.olive.springframwork.beans.factory.BeanClassLoaderAware;
import com.olive.springframwork.beans.factory.BeanFactory;
import com.olive.springframwork.beans.factory.BeanFactoryAware;
import com.olive.springframwork.beans.factory.BeanNameAware;
import com.olive.springframwork.context.ApplicationContext;
import com.olive.springframwork.context.ApplicationContextAware;

/**
 * 类User08Service的实现描述：User08Service
 *
 * @author dongtangqiang 2022/7/9 8:37
 */
public class User08Service implements BeanNameAware, BeanClassLoaderAware, ApplicationContextAware, BeanFactoryAware {
    private ApplicationContext applicationContext;
    private BeanFactory beanFactory;

    private String uId;
    private String company;
    private String location;
    private User07Dao userDao;


    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("ClassLoader：" + classLoader);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("Bean Name is：" + name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public String queryUserInfo() {
        return userDao.queryUserName(uId);
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User07Dao getUserDao() {
        return userDao;
    }

    public void setUserDao(User07Dao userDao) {
        this.userDao = userDao;
    }
}
