package com.olive.springframwork.aop.framework;

/**
 * 类AopProxy的实现描述：定义一个标准接口，用于获取代理类。因为具体实现代理的方式可以有 JDK 方式，也可以是 Cglib 方式，所以定义接口会更加方便管理实现类
 *
 * @author dongtangqiang 2022/7/9 16:51
 */
public interface AopProxy {

    Object getProxy();
}
