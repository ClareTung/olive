package com.olive.spirngframwork.event;

import com.olive.springframwork.context.ApplicationListener;
import com.olive.springframwork.context.event.ContextRefreshedEvent;

/**
 * 类ContextRefreshedEventListener的实现描述：ContextRefreshedEventListener
 *
 * @author dongtangqiang 2022/7/9 16:13
 */
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }
}
