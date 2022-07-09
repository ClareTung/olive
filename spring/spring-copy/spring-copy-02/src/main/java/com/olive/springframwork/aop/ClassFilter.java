package com.olive.springframwork.aop;

/**
 * 类ClassFilter的实现描述：定义类匹配类，用于切点找到给定的接口和目标类
 *
 * @author dongtangqiang 2022/7/9 16:40
 */
public interface ClassFilter {
    /**
     * Should the pointcut apply to the given interface or target class?
     * @param clazz the candidate target class
     * @return whether the advice should apply to the given target class
     */
    boolean matches(Class<?> clazz);

}