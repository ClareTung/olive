package com.olive.java.start.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @description: Cglib代理
 * @program: olive
 * @author: dtq
 * @create: 2021/9/24 14:52
 */
public class CglibProxy implements MethodInterceptor {

    public Object newInstall(Object object) {
        return Enhancer.create(object.getClass(), this);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("被cglib代理了");
        return methodProxy.invokeSuper(o, objects);
    }
}
