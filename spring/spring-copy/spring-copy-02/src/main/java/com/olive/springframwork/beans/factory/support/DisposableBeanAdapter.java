package com.olive.springframwork.beans.factory.support;

import java.lang.reflect.Method;

import com.olive.springframwork.beans.BeansException;
import com.olive.springframwork.beans.factory.DisposableBean;
import com.olive.springframwork.beans.factory.config.BeanDefinition;

import cn.hutool.core.util.StrUtil;

/**
 * 类DisposableBeanAdapter的实现描述：定义销毁方法适配器(接口和配置)
 *
 * @author dongtangqiang 2022/6/4 18:15
 */
public class DisposableBeanAdapter implements DisposableBean {

    private final Object bean;
    private final String beanName;
    private String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
    }

    @Override
    public void destroy() throws Exception {
        // 1. 实现接口 DisposableBean
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }

        // 2. 配置信息 destroy-method {判断是为了避免二次执行销毁}
        if (StrUtil.isNotEmpty(destroyMethodName) && !(bean instanceof DisposableBean && "destroy".equals(this.destroyMethodName))) {
            Method destroyMethod = bean.getClass().getMethod(destroyMethodName);
            if (null == destroyMethod) {
                throw new BeansException("Couldn't find a destroy method named '" + destroyMethodName + "' on bean with name '" + beanName + "'");
            }
            destroyMethod.invoke(bean);
        }
    }
}
