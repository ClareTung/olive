package com.olive.multi.db;

import com.olive.multi.db.entity.Course;
import com.olive.multi.db.mapper.CourseMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/8/30 18:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CourseTest {
    @Resource
    private CourseMapper courseMapper;

    @Test
    public void testInsert() {
        // case: 主库插入数据
        Course course = new Course();
        course.setId(5L);
        course.setClassHour(55);

        // 插入数据
        courseMapper.insert(course);
    }

    @Test
    public void testFindById() {
        // case: 从库读取数据
        Course course = courseMapper.findById(4L);

        System.out.println("从库读取数据："+course.toString());
    }
}
