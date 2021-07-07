package cn.olive.mybatis.plus.start.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("tbl_employee") // 声明表名称
public class Employee {

    @TableId(type = IdType.AUTO) // 设置主键策略
    private Long id;

    private String lastName;
    private String email;
    private Integer age;

    @TableField(fill = FieldFill.INSERT) // 插入的时候自动填充
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新的时候自动填充
    private LocalDateTime gmtModified;

    /**
     * 逻辑删除属性
     * <br>
     * pojo中布尔类型变量，不要加is
     */
    @TableLogic
    @TableField("is_deleted")
    private Boolean deleted;

}
