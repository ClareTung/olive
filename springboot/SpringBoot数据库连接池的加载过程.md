## SpringBoot数据库连接池的加载过程

1. springboot中JDBC的自动配置类是`DataSourceHealthContributorAutoConfiguration`
2. `DataSourceAutoConfiguration`包含有数据源的自动配置
3. `PooledDataSourceConfiguration`的 `DataSourceConfiguration`数据源配置中分别包含了Hikari，Tomcat，Dbcp2，Generic。
4. springBoot默认导入的是`Hikari`。其它连接池如果需要，必须在pom文件中导入。其中`createDataSource`方法是创建数据源。
5. 当自己需要自定义配置数据源时有两种方法。
   - 是直接使用某个数据源的启动器
   - 导入相依数据源连接池的依赖，使用spring.datasource.type=数据源连接池。进行预先配置