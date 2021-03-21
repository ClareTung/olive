package cn.olive.mybatis.generator.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyInfo implements Serializable {
    private static final long serialVersionUID = -7154249369267049187L;

    private String column;

    private String jdbcType;

    private String comment;

    private String property;

    private String javaType;

}
