package com.olive.spirngframwork.event;

import com.olive.springframwork.context.ApplicationListener;
import com.olive.springframwork.context.event.ContextClosedEvent;

/**
 * 类ContextClosedEventListener的实现描述：ContextClosedEventListener
 *
 * @author dongtangqiang 2022/7/9 16:13
 */
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {

    }
}
