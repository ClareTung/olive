package com.olive.springframwork.context;

import java.util.EventObject;

/**
 * 类ApplicationEvent的实现描述：定义出具备事件功能的抽象类 ApplicationEvent，后续所有事件的类都需要继承这个类
 *
 * @author dongtangqiang 2022/7/9 15:42
 */
public abstract class ApplicationEvent extends EventObject {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ApplicationEvent(Object source) {
        super(source);
    }
}
