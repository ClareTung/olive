package com.olive.springframwork.beans.factory;

/**
 * 类InitializingBean的实现描述：初始化接口
 *
 * @author dongtangqiang 2022/6/4 17:56
 */
public interface InitializingBean {

    /**
     * Bean 处理了属性填充后调用
     *
     * @throws Exception
     */
    void afterPropertiesSet() throws Exception;
}