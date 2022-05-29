package com.olive.springframwork.core.io;

/**
 * 类ResourceLoader的实现描述：包装资源加载器
 *
 * @author dongtangqiang 2022/5/28 22:15
 */
public interface ResourceLoader {

    /**
     * Pseudo URL prefix for loading from the class path: "classpath:"
     */
    String CLASSPATH_URL_PREFIX = "classpath:";

    Resource getResource(String location);
}
