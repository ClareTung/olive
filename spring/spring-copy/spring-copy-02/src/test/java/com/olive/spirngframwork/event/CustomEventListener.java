package com.olive.spirngframwork.event;

import java.util.Date;

import com.olive.springframwork.context.ApplicationListener;

/**
 * 类CustomEventListener的实现描述：监听 CustomEvent 事件的监听器
 *
 * @author dongtangqiang 2022/7/9 16:09
 */
public class CustomEventListener implements ApplicationListener<CustomEvent> {
    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println("收到：" + event.getSource() + "消息;时间：" + new Date());
        System.out.println("消息：" + event.getId() + ":" + event.getMessage());
    }
}
