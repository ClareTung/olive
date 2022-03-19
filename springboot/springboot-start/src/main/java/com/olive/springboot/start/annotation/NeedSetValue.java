package com.olive.springboot.start.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记需要设置属性值的属性，将目标对象的值设置到该属性上
 *
 * @author dongtangqiang
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedSetValue {

    /**
     * 调用的bean
     */
    Class<?> beanClass();

    /**
     * 调用的方法
     */
    String method();

    /**
     * 传入的值作为参数
     */
    String param();

    /**
     * 获取到的目标对象的哪个属性值
     */
    String targetField();
}
