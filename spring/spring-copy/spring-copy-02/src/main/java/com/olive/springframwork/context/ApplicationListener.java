package com.olive.springframwork.context;

import java.util.EventListener;

/**
 * 类ApplicationListener的实现描述：ApplicationListener
 *
 * @author dongtangqiang 2022/7/9 15:47
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    void onApplicationEvent(E event);

}
