package com.olive.springframwork.context.event;

/**
 * 类ContextClosedEvent的实现描述：ContextClosedEvent
 *
 * @author dongtangqiang 2022/7/9 15:45
 */
public class ContextClosedEvent extends ApplicationContextEvent{
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ContextClosedEvent(Object source) {
        super(source);
    }
}
