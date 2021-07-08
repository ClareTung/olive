# Mybatis-plus start


## 引入依赖

```text
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.4.2</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

## 创建一下数据表

```sql
DROP TABLE IF EXISTS `tbl_employee`;

CREATE TABLE `tbl_employee` (
  `id` bigint(20) NOT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` char(1) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=gbk;

insert  into `tbl_employee`(`id`,`last_name`,`email`,`gender`,`age`) values (1,'jack','jack@qq.com','1',35),(2,'tom','tom@qq.com','1',30),(3,'jerry','jerry@qq.com','1',40);


ALTER TABLE tbl_employee ADD COLUMN gmt_create DATETIME NOT NULL  DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tbl_employee ADD COLUMN gmt_modified DATETIME NOT NULL  DEFAULT CURRENT_TIMESTAMP;


alter table tbl_employee add column is_deleted tinyint not null DEFAULT 1;
```

## 报错表名不存在
* 实体类名为 Employee，MyBatisPlus 默认是以类名作为表名进行操作的，可如果类名和表名不相同（实际开发中也确实可能不同），就需要在实体类中使用 @TableName 注解来声明表的名称

```text
Table 'mybatisplus.employee' doesn't exist
```

## mapper配置文件

* MyBatisPlus 默认扫描的是类路径下的 mapper 目录
```text
"classpath*:/mapper/**/*.xml"
```
* Mapper 配置文件放在该目录下就没有任何问题，可如果不是这个目录，我们就需要进行配置，比如：
```text
mybatis-plus:
  mapper-locations: classpath:xml/*.xml
```

## id策略

* 实体类的主键名为 id，并且数据表的主键名也为 id 时，此时 MyBatisPlus 会自动判定该属性为主键 id，倘若名字不是 id 时，就需要标注 @TableId 注解，若是实体类中主键名与数据表的主键名不一致

```text
@TableId(value = "uid",type = IdType.AUTO) // 设置主键策略
private Long id;
```

* IdType：有多种id生成策略

* 还可以在配置文件中配置全局的主键策略：这样能够避免在每个实体类中重复设置主键策略。

```text
mybatis-plus:
  global-config:
    db-config:
      id-type: auto
```

## 属性自动填充

* 属性字段加上注解 `@TableField`
* 自定义MyMetaObjectHandler实现MetaObjectHandler

## 逻辑删除
* @TableLogin 注解用于设置逻辑删除属性
* MyBatisPlus 默认 0 为不删除，1 为删除。可以修改成 1不删除，2删除

```text
mybatis-plus:
  global-config:
    db-config:
      id-type: auto
      logic-delete-field: deleted # 逻辑删除属性名
      logic-delete-value: 2 # 删除值
      logic-not-delete-value: 1 # 不删除值
```

## sql日志打印

```text

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 输出SQL日志
```


## 分页插件
* 增加配置
```text
/**
 * 注册分页插件
 *
 * @return
 */
@Bean
public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    return interceptor;
}
```

## 乐观锁

* 创建数据表
```text
create table shop(
 id bigint(20) not null auto_increment,
  name varchar(30) not null,
  price int(11) default 0,
  version int(11) default 1,
  primary key(id)
);

insert into shop(id,name,price) values(1,'笔记本电脑',8000);
```
* @Version 

```text
@Configuration
public class MyBatisConfig {

    /**
     * 注册分页插件
     *
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
}

```

## 条件构造器

* 在分页插件中我们简单地使用了一下条件构造器（`Wrapper`），下面我们来详细了解一下。先来看看 `Wrapper` 的继承体系：

![image-20210707223239270](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210707223239270.png)

分别介绍一下它们的作用：

- `Wrapper`：条件构造器抽象类，最顶端的父类

- - `LambdaQueryWrapper`：用于对象封装，使用 Lambda 语法
  - `LambdaUpdateWrapper`：用于条件封装，使用 Lambda 语法
  - `QueryWrapper`：用于对象封装
  - `UpdateWrapper`：用于条件封装
  - `AbstractWrapper`：查询条件封装抽象类，生成 SQL 的 where 条件
  - `AbstractLambdaWrapper`：Lambda 语法使用 Wrapper

通常我们使用的都是 `QueryWrapper` 和 `UpdateWrapper`，若是想使用 Lambda 语法来编写，也可以使用 `LambdaQueryWrapper` 和 `LambdaUpdateWrapper`，通过这些条件构造器，我们能够很方便地来实现一些复杂的筛选操作，比如：

比如 `like` 方法会为我们建立模糊查询，可以看到它是对 `j` 的前后都加上了 `%` ，若是只想查询以 `j` 开头的名字，则可以使用 `likeRight` 方法，若是想查询以 `j` 结尾的名字，，则使用 `likeLeft` 方法。

`UpdateWrapper` 与 `QueryWrapper` 不同，它的作用是封装更新内容的。


## 分页插件

```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.3.0</version>
</dependency>
```

```java
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @param <Param> 泛型request
 * @param <Result> 泛型response
 */
public interface BaseService<Param, Result> {

    /**
     * 分页查询
     *
     * @param param 请求参数DTO
     * @return 分页集合
     */
    default PageInfo<Result> page(PageParam<Param> param) {
        return PageHelper.startPage(param).doSelectPageInfo(() -> list(param.getParam()));
    }

    /**
     * 集合查询
     *
     * @param param 查询参数
     * @return 查询响应
     */
    List<Result> list(Param param);
}
```





