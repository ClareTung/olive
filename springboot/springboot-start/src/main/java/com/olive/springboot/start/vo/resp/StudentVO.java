package com.olive.springboot.start.vo.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class StudentVO {

    /**
     * 学生id
     */
    private  Long id;

    /**
     * 学生姓名
     */
    @JsonProperty("student_name")
    private String studentName;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 作业列表
     */
    @JsonProperty("home_work_list")
    private List<HomeWorkVO> homeWorkList;
}
