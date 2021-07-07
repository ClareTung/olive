package cn.olive.mybatis.plus.start.entity;


import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

@Data
public class Shop {

    private Long id;
    private String name;
    private Integer price;

    @Version // 声明版本号属性
    private Integer version;
}
