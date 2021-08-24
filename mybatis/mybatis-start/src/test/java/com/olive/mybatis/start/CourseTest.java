package com.olive.mybatis.start;

import com.olive.mybatis.start.entity.Course;
import com.olive.mybatis.start.mapper.CourseMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/8/24 17:20
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CourseTest {

    @Resource
    private CourseMapper courseMapper;

    @Test
    public void intTypeQuery() {
        // case1: 测试classHour在数据库中是int类型
        List<Course> courseList = courseMapper.findAll();

        courseList.forEach(course -> {
            System.out.println("课程id = " + course.getId() + "的课时为： " + course.getClassHour());
        });
        // 测试结果：
        // 课程id = 1的课时为： 10
        // 课程id = 2的课时为： 20
    }

    @Test
    public void intTypeInsert() {
        // case2: 正常插入数据
        Course course = new Course();
        course.setId(3L);
        course.setClassHour(99);

        // 插入数据
        courseMapper.insert(course);

        // 验证是3条数据
        List<Course> courseList = courseMapper.findAll();
        Assert.assertEquals(3, courseList.size());
    }

    @Test
    public void intTypeUpdate() {
        // case3: 正常更新数据
        Course course = new Course();
        course.setId(3L);
        course.setClassHour(100);

        // 插入数据
        courseMapper.update(course);

        // 验证
        Course findCourse = courseMapper.findById(3L);
        Assert.assertNotNull(findCourse);
        Assert.assertEquals(100, (int) findCourse.getClassHour());
    }

    // 修改数据库字段为decimal类型后：ALTER TABLE  `tb_course` MODIFY `class_hour` decimal(8,2) DEFAULT NULL;

    @Test
    public void decimalTypeQuery() {
        // case4: 测试classHour在数据库中是decimal类型
        List<Course> courseList = courseMapper.findAll();

        courseList.forEach(course -> {
            System.out.println("课程id = " + course.getId() + "的课时为： " + course.getClassHour());
        });
        // 测试结果：
        // 课程id = 1的课时为： 10
        // 课程id = 2的课时为： 20
        // 课程id = 3的课时为： 100
    }

    @Test
    public void decimalTypeUpdate() {
        // case5: 数据库字段类型是decimal时，更新数据
        Course course = new Course();
        course.setId(3L);
        course.setClassHour(66);

        // 插入数据
        courseMapper.update(course);

        // 验证
        Course findCourse = courseMapper.findById(3L);
        Assert.assertNotNull(findCourse);
        Assert.assertEquals(66, (int) findCourse.getClassHour());
    }


    @Test
    public void decimalTypeInsert() {
        // case6: 数据库字段类型是decimal时，插入数据，然后读取
        Course course = new Course();
        course.setId(4L);
        course.setClassHour(88);

        // 插入数据
        courseMapper.insert(course);

        // 验证
        Course findCourse = courseMapper.findById(4L);
        Assert.assertNotNull(findCourse);
        Assert.assertEquals(88, (int) findCourse.getClassHour());
    }

    @Test
    public void actualDecimalTypeFind() {
        // case6: 数据库字段类型是decimal时，并且数据是小数，查询数据，读取到的结果是保留整数部分，小数部分舍弃

        Course findCourse = courseMapper.findById(4L);
        Assert.assertNotNull(findCourse);
        Assert.assertEquals(88, findCourse.getClassHour(), 0.0);
    }

    @Test
    public void actualDecimalTypeUpdate() {
        // case6: 数据库字段类型是decimal时，并且数据是小数，更新数据为整数，正常更新和读取
        Course course = new Course();
        course.setId(4L);
        course.setClassHour(99);

        // 更新数据
        courseMapper.update(course);

        // 验证
        Course findCourse = courseMapper.findById(4L);
        Assert.assertNotNull(findCourse);
        Assert.assertEquals(99, findCourse.getClassHour(), 0.0);
    }
}
