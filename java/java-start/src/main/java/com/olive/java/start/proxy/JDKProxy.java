package com.olive.java.start.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description: jdk代理
 * @program: olive
 * @author: dtq
 * @create: 2021/9/24 14:07
 */
public class JDKProxy {

    public static <T> T getProxy(Class clazz) throws Exception{
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return (T) Proxy.newProxyInstance(classLoader, new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method.getName() + "被JDKProxy代理了");
                return "Nice to meet you";
            }
        });
    }
}
