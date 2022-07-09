package com.olive.springframwork.aop;

import java.lang.reflect.Method;

/**
 * 类MethodMatcher的实现描述：方法匹配，找到表达式范围内匹配下的目标类和方法
 *
 * @author dongtangqiang 2022/7/9 16:40
 */
public interface MethodMatcher {
    /**
     * Perform static checking whether the given method matches. If this
     * @return whether or not this method matches statically
     */
    boolean matches(Method method, Class<?> targetClass);

}
