package com.olive.springframwork.context.event;

/**
 * 类ContextRefreshedEvent的实现描述：ContextRefreshedEvent
 *
 * @author dongtangqiang 2022/7/9 15:45
 */
public class ContextRefreshedEvent extends ApplicationContextEvent{
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ContextRefreshedEvent(Object source) {
        super(source);
    }
}
