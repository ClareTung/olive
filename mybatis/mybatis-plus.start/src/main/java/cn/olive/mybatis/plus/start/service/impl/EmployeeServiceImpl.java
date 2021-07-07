package cn.olive.mybatis.plus.start.service.impl;

import cn.olive.mybatis.plus.start.entity.Employee;
import cn.olive.mybatis.plus.start.mapper.EmployeeMapper;
import cn.olive.mybatis.plus.start.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Override
    public List<Employee> listAllByLastName(String lastName) {
        return baseMapper.selectAllByLastName(lastName);
    }
}
