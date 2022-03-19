package com.olive.springboot.start.aop;

import com.olive.springboot.start.util.BeanUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * 需要设置属性值切面
 *
 * @author dongtangqiang
 */
@Component
@Aspect
public class NeedSetFiledValueAspect {

    @Resource
    private BeanUtil beanUtil;

    @Around("@annotation(com.olive.springboot.start.annotation.NeedSetFieldValue)")
    public Object doSetFiledValue(ProceedingJoinPoint pjp) throws Throwable {

        // 执行被切面的方法，获取结果集
        Object ret = pjp.proceed();

        // 将被NeedSetValue标记的属性设置值
        beanUtil.setFiledValueForColl((Collection) ret);

        return ret;
    }
}
