package cn.olive.mybatis.plus.start.mapper;

import cn.olive.mybatis.plus.start.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeMapper extends BaseMapper<Employee> {

    List<Employee> selectAllByLastName(@Param("lastName") String lastName);
}
