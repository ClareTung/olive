package com.olive.drools.bean;

import java.util.List;

import lombok.Data;

/**
 * 类Animal的实现描述：Animal
 *
 * @author dongtangqiang 2022/2/15 15:11
 */
@Data
public class Animal {

    private Integer level;

    private List<People> peoples;
}
