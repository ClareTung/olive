package com.olive.springboot.start.vo.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class LessonVO {

    /**
     * 课程id
     */
    private Long id;

    /**
     * 班级列表
     */
    @JsonProperty("class_list")
    private List<ClassVO> classList;

    /**
     * 学生列表
     */
    @JsonProperty("student_list")
    private List<StudentVO> studentList = Lists.newArrayList();

}
