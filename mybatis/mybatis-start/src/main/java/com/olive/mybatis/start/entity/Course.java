package com.olive.mybatis.start.entity;

import lombok.Data;

/**
 * @description: 课程
 * @program: olive
 * @author: dtq
 * @create: 2021/8/24 17:05
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
