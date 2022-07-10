package com.olive.springframwork.context.annotation;

import java.util.LinkedHashSet;
import java.util.Set;

import com.olive.springframwork.beans.factory.config.BeanDefinition;
import com.olive.springframwork.stereotype.Component;

import cn.hutool.core.util.ClassUtil;

/**
 * 类ClassPathScanningCandidateComponentProvider的实现描述：描到所有 @Component 注解的 Bean 对象
 *
 * @author dongtangqiang 2022/7/10 8:56
 */
public class ClassPathScanningCandidateComponentProvider {

    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);
        for (Class<?> clazz : classes) {
            candidates.add(new BeanDefinition(clazz));
        }
        return candidates;
    }
}
