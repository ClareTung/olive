package com.olive.spirngframwork.bean;

import java.lang.reflect.Method;

import com.olive.springframwork.aop.MethodBeforeAdvice;

/**
 * 类UserServiceBeforeAdvice的实现描述：UserServiceBeforeAdvice
 *
 * @author dongtangqiang 2022/7/9 18:10
 */
public class UserServiceBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("拦截方法：" + method.getName());
    }
}
