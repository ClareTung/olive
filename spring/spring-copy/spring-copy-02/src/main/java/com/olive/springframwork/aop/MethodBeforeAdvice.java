package com.olive.springframwork.aop;

import java.lang.reflect.Method;

/**
 * 类MethodBeforeAdvice的实现描述：MethodBeforeAdvice
 *
 * @author dongtangqiang 2022/7/9 17:52
 */
public interface MethodBeforeAdvice extends BeforeAdvice{

    /**
     * Callback before a given method is invoked.
     *
     * @param method method being invoked
     * @param args   arguments to the method
     * @param target target of the method invocation. May be <code>null</code>.
     * @throws Throwable if this object wishes to abort the call.
     *                   Any exception thrown will be returned to the caller if it's
     *                   allowed by the method signature. Otherwise the exception
     *                   will be wrapped as a runtime exception.
     */
    void before(Method method, Object[] args, Object target) throws Throwable;
}
