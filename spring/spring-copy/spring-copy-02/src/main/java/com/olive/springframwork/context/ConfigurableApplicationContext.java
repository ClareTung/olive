package com.olive.springframwork.context;

import com.olive.springframwork.beans.BeansException;

/**
 * 类ConfigurableApplicationContext的实现描述：ConfigurableApplicationContext
 *
 * @author dongtangqiang 2022/6/4 9:32
 */
public interface ConfigurableApplicationContext extends ApplicationContext{

    /**
     * 刷新容器
     *
     * @throws BeansException
     */
    void refresh() throws BeansException;

    /**
     * 注册虚拟机钩子
     */
    void registerShutdownHook();

    /**
     * 手动执行关闭方法
     */
    void close();
}