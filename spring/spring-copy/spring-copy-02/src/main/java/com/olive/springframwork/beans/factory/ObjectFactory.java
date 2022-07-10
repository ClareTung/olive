package com.olive.springframwork.beans.factory;

import com.olive.springframwork.beans.BeansException;

/**
 * 类ObjectFactory的实现描述：ObjectFactory
 *
 * @author dongtangqiang 2022/7/10 21:24
 */
public interface ObjectFactory<T> {
    T getObject() throws BeansException;

}
