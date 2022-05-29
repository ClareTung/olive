package com.olive.springframwork.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 类Resource的实现描述：资源加载接口
 *
 * @author dongtangqiang 2022/5/28 22:00
 */
public interface Resource {

    InputStream getInputStream() throws IOException;
}
