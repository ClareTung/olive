package com.olive.mybatis.start.mapper;

import com.olive.mybatis.start.entity.Course;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @description: Mapper
 * @program: olive
 * @author: dtq
 * @create: 2021/8/24 17:07
 */
@Mapper
public interface CourseMapper {

    @Select("select * from tb_course")
    List<Course> findAll();

    @Select("select * from tb_course where id=#{id}")
    Course findById(Long id);

    @Insert("insert into tb_course(id,class_hour) values(#{id},#{classHour})")
    int insert(Course course);

    @Update("update tb_course set class_hour=#{classHour} where id=#{id}")
    int update(Course course);

    //@Delete("delete from tb_course where id=#{id}")
    void delete(Long id);
}
