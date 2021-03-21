package com.olive.springboot.start.vo.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClassVO {
    /**
     * 班级id
     */
    private Long id;

    /**
     * 班级名称
     */
    @JsonProperty("class_name")
    private String className;

    /**
     * 学生数
     */
    @JsonProperty("student_count")
    private Integer studentCount;
}
