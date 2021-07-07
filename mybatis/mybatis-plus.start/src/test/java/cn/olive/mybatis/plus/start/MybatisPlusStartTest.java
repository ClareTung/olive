package cn.olive.mybatis.plus.start;


import cn.olive.mybatis.plus.start.entity.Employee;
import cn.olive.mybatis.plus.start.entity.Shop;
import cn.olive.mybatis.plus.start.mapper.EmployeeMapper;
import cn.olive.mybatis.plus.start.mapper.ShopMapper;
import cn.olive.mybatis.plus.start.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan("cn.olive.mybatis.plus.start.mapper")
public class MybatisPlusStartTest {

    @Resource
    private ShopMapper shopMapper;

    @Resource
    private EmployeeMapper employeeMapper;

    @Resource
    private EmployeeService employeeService;

    @Test
    public void testUpdateWrapper(){
        UpdateWrapper<Employee> wrapper = new UpdateWrapper<Employee>()
                .set("age", 50)
                .set("email", "emp@163.com")
                .like("last_name", "j")
                .gt("age", 20);
        employeeMapper.update(null, wrapper);
    }

    @Test
    public void testLambdaQueryWrapper(){
        // 查询名字中包含'j'，年龄大于20岁，邮箱不为空的员工信息
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<Employee>()
                .like(Employee::getLastName,"j")
                .gt(Employee::getAge,20)
                .isNotNull(Employee::getEmail);
        List<Employee> list = employeeMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    @Test
    public void testWrapper(){

        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("last_name", "j");
        queryWrapper.gt("age", 20);
        queryWrapper.isNotNull("email");
        List<Employee> employeeList = employeeMapper.selectList(queryWrapper);
        employeeList.forEach(System.out::println);

    }

    @Test
    public void testVersion(){
        // A、B管理员读取数据
        Shop A = shopMapper.selectById(1L);
        Shop B = shopMapper.selectById(1L);
        // B管理员先修改
        B.setPrice(9000);
        int result = shopMapper.updateById(B);
        if (result == 1) {
            System.out.println("B管理员修改成功!");
        } else {
            System.out.println("B管理员修改失败!");
        }
        // A管理员后修改
        A.setPrice(8500);
        int result2 = shopMapper.updateById(A);
        if (result2 == 1) {
            System.out.println("A管理员修改成功!");
        } else {
            System.out.println("A管理员修改失败!");
        }
        // 最后查询
        System.out.println(shopMapper.selectById(1L));
    }

    @Test
    public void testPage() {
        Page<Employee> page = new Page<>(1, 2);
        employeeService.page(page, null);
        List<Employee> employeeList = page.getRecords();
        employeeList.forEach(System.out::println);
        System.out.println("获取总条数:" + page.getTotal());
        System.out.println("获取当前页码:" + page.getCurrent());
        System.out.println("获取总页码:" + page.getPages());
        System.out.println("获取每页显示的数据条数:" + page.getSize());
        System.out.println("是否有上一页:" + page.hasPrevious());
        System.out.println("是否有下一页:" + page.hasNext());
    }

    @Test
    public void testLogicDelete() {
        employeeService.removeById(3);
    }

    @Test
    public void testAutoFill() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setAge(15);
        employeeService.updateById(employee);
    }

    @Test
    public void testSave() {
        Employee employee = new Employee();
        employee.setLastName("lisa");
        employee.setEmail("lisa@qq.com");
        employee.setAge(20);
        employeeService.save(employee);
    }

    @Test
    public void testListAllByLastName() {
        List<Employee> employeeList = employeeService.listAllByLastName("tom");
        employeeList.forEach(System.out::println);
    }

    @Test
    public void testService() {
        List<Employee> list = employeeService.list();
        list.forEach(System.out::println);
    }


    @Test
    public void testMapper() {
        List<Employee> employees = employeeMapper.selectList(null);
        employees.forEach(System.out::println);
    }


}
