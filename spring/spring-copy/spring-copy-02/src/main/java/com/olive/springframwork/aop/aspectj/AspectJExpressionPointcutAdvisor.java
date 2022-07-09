package com.olive.springframwork.aop.aspectj;

import org.aopalliance.aop.Advice;

import com.olive.springframwork.aop.Pointcut;
import com.olive.springframwork.aop.PointcutAdvisor;

/**
 * 类AspectJExpressionPointcutAdvisor的实现描述：把切面 pointcut、拦截方法 advice 和具体的拦截表达式包装在一起。这样就可以在 xml 的配置中定义一个 pointcutAdvisor 切面拦截器了
 *
 * @author dongtangqiang 2022/7/9 17:55
 */
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    // 切面
    private AspectJExpressionPointcut pointcut;
    // 具体的拦截方法
    private Advice advice;
    // 表达式
    private String expression;

    public void setExpression(String expression){
        this.expression = expression;
    }

    @Override
    public Pointcut getPointcut() {
        if (null == pointcut) {
            pointcut = new AspectJExpressionPointcut(expression);
        }
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    public void setAdvice(Advice advice){
        this.advice = advice;
    }

}
