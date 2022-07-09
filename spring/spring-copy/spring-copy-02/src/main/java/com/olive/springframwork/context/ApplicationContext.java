package com.olive.springframwork.context;

import com.olive.springframwork.beans.factory.HierarchicalBeanFactory;
import com.olive.springframwork.beans.factory.ListableBeanFactory;
import com.olive.springframwork.core.io.ResourceLoader;

/**
 * 类ApplicationContext的实现描述：应用上下文接口
 *
 * @author dongtangqiang 2022/6/4 9:29
 */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {
}
