package com.olive.java.start.proxy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @description: ByteBuddyProxy
 * @program: olive
 * @author: dtq
 * @create: 2021/9/24 15:42
 */
public class ByteBuddyProxy {

    public static <T> T getProxy(Class clazz) throws Exception {

        DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
                .subclass(clazz)
                .method(ElementMatchers.<MethodDescription>named("queryUserInfo"))
                .intercept(MethodDelegation.to(InvocationHandler.class))
                .make();

        return (T) dynamicType.load(Thread.currentThread().getContextClassLoader()).getLoaded().newInstance();
    }

}
