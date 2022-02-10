package com.olive.drools.bean;

import lombok.Data;

/**
 * 类People的实现描述：People
 *
 * @author dongtangqiang 2022/2/10 15:04
 */
@Data
public class People {

    private int sex;

    private String name;

    private Integer age;

    private String drlType;

    public People(int sex, String name, Integer age, String drlType) {
        this.sex = sex;
        this.name = name;
        this.age = age;
        this.drlType = drlType;
    }

    public People(int sex, String name, String drlType) {
        this.sex = sex;
        this.name = name;
        this.drlType = drlType;
    }

    public People() {
    }
}
