package com.olive.springboot.start.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author dongtangqiang
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 5012789757086671874L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户姓名
     */
    private String name;
}
