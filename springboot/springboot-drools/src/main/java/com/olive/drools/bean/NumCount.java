package com.olive.drools.bean;

import lombok.Data;

/**
 * 类NumCount的实现描述：NumCount
 *
 * @author dongtangqiang 2022/2/15 16:42
 */
@Data
public class NumCount {

    private int count;

    public void plus() {
        count = count + 1;
    }
}
