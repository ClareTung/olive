package com.olive.springframwork.beans;

/**
 * 类BeansException的实现描述：BeansException
 *
 * @author dongtangqiang 2022/5/26 22:06
 */
public class BeansException extends RuntimeException {

    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg, Throwable cause) {
        super(msg, cause);
    }
}