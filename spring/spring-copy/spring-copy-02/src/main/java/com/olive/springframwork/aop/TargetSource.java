package com.olive.springframwork.aop;

import com.olive.springframwork.util.ClassUtils;

/**
 * 类TargetSource的实现描述：一个目标对象，在目标对象类中提供 Object 入参属性，以及获取目标类 TargetClass 信息
 *
 * @author dongtangqiang 2022/7/9 16:48
 */
public class TargetSource {

    private final Object target;

    public TargetSource(Object target) {
        this.target = target;
    }

    /**
     * Return the type of targets returned by this {@link TargetSource}.
     * <p>Can return <code>null</code>, although certain usages of a
     * <code>TargetSource</code> might just work with a predetermined
     * target class.
     * @return the type of targets returned by this {@link TargetSource}
     */
    public Class<?>[] getTargetClass() {
        Class<?> clazz = this.target.getClass();
        //这个 target 可能是 JDK 代理 创建也可能是 CGlib 创建，为了保证都能正确的获取到结果，这里需要增加判读
        //ClassUtils.isCglibProxyClass(clazz)
        clazz = ClassUtils.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;
        return clazz.getInterfaces();
    }

    /**
     * Return a target instance. Invoked immediately before the
     * AOP framework calls the "target" of an AOP method invocation.
     * @return the target object, which contains the joinpoint
     * @throws Exception if the target object can't be resolved
     */
    public Object getTarget(){
        return this.target;
    }
}
