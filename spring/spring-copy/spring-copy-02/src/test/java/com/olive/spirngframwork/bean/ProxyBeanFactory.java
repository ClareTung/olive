package com.olive.spirngframwork.bean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.olive.springframwork.beans.factory.FactoryBean;

/**
 * 类ProxyBeanFactory的实现描述：定义 FactoryBean 对象
 *
 * @author dongtangqiang 2022/7/9 9:34
 */
public class ProxyBeanFactory implements FactoryBean<IUserDao> {

    @Override
    public IUserDao getObject() throws Exception {
        InvocationHandler handler = (proxy, method, args) -> {

            // 添加排除方法
            if ("toString".equals(method.getName())) return this.toString();

            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("10001", "clare");
            hashMap.put("10002", "tung");
            hashMap.put("10003", "d");

            return "你被代理了 " + method.getName() + "：" + hashMap.get(args[0].toString());
        };
        return (IUserDao) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{IUserDao.class}, handler);
    }

    @Override
    public Class<?> getObjectType() {
        return IUserDao.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
