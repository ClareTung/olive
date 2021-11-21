package com.olive.design.pattern.observer;

import com.google.common.eventbus.Subscribe;

/**
 * 观察者角色
 *
 * @author dongtangqiang
 */
public class EventListener {

    @Subscribe //加了订阅，这里标记这个方法是事件处理方法
    public void handle(NotifyEvent notifyEvent) {
        System.out.println("发送IM消息" + notifyEvent.getImNo());
        System.out.println("发送短信消息" + notifyEvent.getMobileNo());
        System.out.println("发送Email消息" + notifyEvent.getEmailNo());
    }

    // 使用
    /*
    EventListener eventListener = new EventListener();
    EventBusCenter.register(eventListener);
    EventBusCenter.post(new NotifyEvent("13372817283", "123@qq.com", "666"));
    */
}
