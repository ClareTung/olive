package com.olive.springframwork.context.event;

import com.olive.springframwork.beans.factory.BeanFactory;
import com.olive.springframwork.context.ApplicationEvent;
import com.olive.springframwork.context.ApplicationListener;

/**
 * 类SimpleApplicationEventMulticaster的实现描述：SimpleApplicationEventMulticaster
 *
 * @author dongtangqiang 2022/7/9 15:59
 */
public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {

    public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
        setBeanFactory(beanFactory);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void multicastEvent(final ApplicationEvent event) {
        for (final ApplicationListener listener : getApplicationListeners(event)) {
            listener.onApplicationEvent(event);
        }
    }

}