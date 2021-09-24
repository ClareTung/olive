package com.olive.java.start.proxy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/9/24 16:22
 */
public class InvocationHandler {
    @RuntimeType
    public static Object intercept(@Origin Method method, @AllArguments Object[] args, @SuperCall Callable<?> callable) throws Exception {
        System.out.println(method.getName() + " 你被代理了，By Byte-Buddy！");
        return callable.call();
    }
}
