package com.olive.springboot.start.controller;

import com.google.common.collect.Lists;
import com.olive.springboot.start.vo.resp.ClassVO;
import com.olive.springboot.start.vo.resp.HomeWorkVO;
import com.olive.springboot.start.vo.resp.LessonVO;
import com.olive.springboot.start.vo.resp.StudentVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("lesson")
public class LessonController {

    @GetMapping(value = "/info/get")
    public LessonVO getUserInfo() {
        LessonVO result = new LessonVO();
        result.setId(1001L);

        List<ClassVO> classList = Lists.newArrayList();
        ClassVO class1 = new ClassVO();
        class1.setId(2001L);
        class1.setClassName("1班");
        class1.setStudentCount(20);
        classList.add(class1);
        ClassVO class2 = new ClassVO();
        class2.setId(2002L);
        class2.setClassName("2班");
        class2.setStudentCount(20);
        classList.add(class2);

        result.setClassList(classList);

        List<StudentVO> studentList = Lists.newArrayList();
        StudentVO stu1 = new StudentVO();
        stu1.setId(3001L);
        stu1.setStudentName("王二");
        stu1.setAge(18);
        studentList.add(stu1);
        StudentVO stu2 = new StudentVO();
        stu2.setId(3002L);
        stu2.setStudentName("张明");
        stu2.setAge(19);

        List<HomeWorkVO> homeWorkList = Lists.newArrayList();
        HomeWorkVO homeWork1 = new HomeWorkVO();
        homeWork1.setId(300201L);
        homeWork1.setHomeWorkName("课外读书");
        homeWork1.setHomeWorkContent("小王子");
        homeWorkList.add(homeWork1);
        stu2.setHomeWorkList(homeWorkList);
        studentList.add(stu2);

        result.setStudentList(studentList);

        return result;
    }

}
