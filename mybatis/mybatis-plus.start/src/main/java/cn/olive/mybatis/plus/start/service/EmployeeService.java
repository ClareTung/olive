package cn.olive.mybatis.plus.start.service;


import cn.olive.mybatis.plus.start.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface EmployeeService extends IService<Employee> {

    List<Employee> listAllByLastName(String lastName);
}
