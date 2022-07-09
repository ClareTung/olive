package com.olive.springframwork.aop;

import org.aopalliance.aop.Advice;

/**
 * 类Advisor的实现描述：Advisor
 *
 * @author dongtangqiang 2022/7/9 17:53
 */
public interface Advisor {

    /**
     * Return the advice part of this aspect. An advice may be an
     * interceptor, a before advice, a throws advice, etc.
     *
     * @return the advice that should apply if the pointcut matches
     * @see org.aopalliance.intercept.MethodInterceptor
     * @see BeforeAdvice
     */
    Advice getAdvice();
}
