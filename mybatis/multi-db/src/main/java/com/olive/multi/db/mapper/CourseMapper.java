package com.olive.multi.db.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.olive.multi.db.entity.Course;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/8/30 18:26
 */
@Mapper
public interface CourseMapper {

    @DS("slave_1")
    @Select("select * from tb_course where id=#{id}")
    Course findById(Long id);

    @DS("master")
    @Insert("insert into tb_course(id,class_hour) values(#{id},#{classHour})")
    int insert(Course course);
}
