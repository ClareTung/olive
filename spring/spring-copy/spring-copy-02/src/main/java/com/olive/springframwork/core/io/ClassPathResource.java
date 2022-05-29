package com.olive.springframwork.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.olive.springframwork.util.ClassUtils;

import cn.hutool.core.lang.Assert;

/**
 * 类ClassPathResource的实现描述：从classpath中获取文件
 *
 * @author dongtangqiang 2022/5/28 22:05
 */
public class ClassPathResource implements Resource {

    private final String path;

    private ClassLoader classLoader;

    public ClassPathResource(String path) {
        this(path, (ClassLoader) null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        Assert.notNull(path, "Path must not be null");
        this.path = path;
        this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        // 通过 ClassLoader 读取ClassPath 下的文件信息
        InputStream is = classLoader.getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException(
                    this.path + " cannot be opened because it does not exist");
        }
        return is;
    }
}
