package com.olive.springframwork.beans.factory;

/**
 * 类DisposableBean的实现描述：销毁方法接口
 *
 * @author dongtangqiang 2022/6/4 17:58
 */
public interface DisposableBean {

    void destroy() throws Exception;
}