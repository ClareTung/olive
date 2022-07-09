package com.olive.springframwork.context;

import com.olive.springframwork.beans.BeansException;
import com.olive.springframwork.beans.factory.Aware;

/**
 * 类ApplicationContextAware的实现描述：实现此接口，既能感知到所属的 ApplicationContext
 *
 * @author dongtangqiang 2022/7/9 8:23
 */
public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}