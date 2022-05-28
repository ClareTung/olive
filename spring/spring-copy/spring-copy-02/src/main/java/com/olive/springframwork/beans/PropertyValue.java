package com.olive.springframwork.beans;

/**
 * 类PropertyValue的实现描述：定义属性
 *
 * @author dongtangqiang 2022/5/28 17:59
 */
public class PropertyValue {

    private final String name;

    private final Object value;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
