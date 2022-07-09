package com.olive.springframwork.aop;

/**
 * 类PointcutAdvisor的实现描述：Advisor 承担了 Pointcut 和 Advice 的组合，Pointcut 用于获取 JoinPoint，而 Advice 决定于 JoinPoint 执行什么操作。
 *
 * @author dongtangqiang 2022/7/9 17:54
 */
public interface PointcutAdvisor extends Advisor {

    /**
     * Get the Pointcut that drives this advisor.
     */
    Pointcut getPointcut();

}