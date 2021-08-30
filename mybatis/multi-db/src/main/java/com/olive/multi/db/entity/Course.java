package com.olive.multi.db.entity;

import lombok.Data;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/8/30 18:20
 */
@Data
public class Course {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 课时
     */
    private Integer classHour;
}