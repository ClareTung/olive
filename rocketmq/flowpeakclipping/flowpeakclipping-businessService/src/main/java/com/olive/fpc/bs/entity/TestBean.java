package com.olive.fpc.bs.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/11/3 18:20
 */
@Data
@ToString
public class TestBean implements Serializable {
    private String message;
}
