package com.olive.springframwork.core.io;

import java.net.MalformedURLException;
import java.net.URL;

import cn.hutool.core.lang.Assert;

/**
 * 类DefaultResourceLoader的实现描述：默认资源加载器
 *
 * @author dongtangqiang 2022/5/28 22:16
 */
public class DefaultResourceLoader implements ResourceLoader {

    @Override
    public Resource getResource(String location) {
        Assert.notNull(location, "Location must not be null");
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        } else {
            try {
                URL url = new URL(location);
                return new UrlResource(url);
            } catch (MalformedURLException e) {
                return new FileSystemResource(location);
            }
        }
    }
}
