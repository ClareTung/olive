package com.olive.springframwork.aop.framework.autoproxy;

import java.util.Collection;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import com.olive.springframwork.aop.AdvisedSupport;
import com.olive.springframwork.aop.Advisor;
import com.olive.springframwork.aop.ClassFilter;
import com.olive.springframwork.aop.Pointcut;
import com.olive.springframwork.aop.TargetSource;
import com.olive.springframwork.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.olive.springframwork.aop.framework.ProxyFactory;
import com.olive.springframwork.beans.BeansException;
import com.olive.springframwork.beans.factory.BeanFactory;
import com.olive.springframwork.beans.factory.BeanFactoryAware;
import com.olive.springframwork.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.olive.springframwork.beans.factory.support.DefaultListableBeanFactory;

/**
 * 类DefaultAdvisorAutoProxyCreator的实现描述：融入Bean生命周期的自动代理创建者
 *
 * @author dongtangqiang 2022/7/9 17:59
 */
public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {

        if (isInfrastructureClass(beanClass)) {
            return null;
        }

        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();

        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            if (!classFilter.matches(beanClass)) {
                continue;
            }

            AdvisedSupport advisedSupport = new AdvisedSupport();

            TargetSource targetSource = null;
            try {
                targetSource = new TargetSource(beanClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
            advisedSupport.setTargetSource(targetSource);
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupport.setProxyTargetClass(false);

            return new ProxyFactory(advisedSupport).getProxy();

        }

        return null;
    }

    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}