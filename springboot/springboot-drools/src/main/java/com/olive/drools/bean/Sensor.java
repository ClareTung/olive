package com.olive.drools.bean;

import lombok.Data;

/**
 * 类Sensor的实现描述：Sensor
 *
 * @author dongtangqiang 2022/2/15 15:39
 */
@Data
public class Sensor {

    private String description;

    private Double temp;

    public Sensor() {
    }

    public Sensor(String description, Double temp) {
        this.description = description;
        this.temp = temp;
    }
}
