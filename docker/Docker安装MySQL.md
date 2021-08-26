# Docker安装MySQL

## 获取镜像

```
docker pull mysql:8.0.16
```

## 配置文件

* 将全部的配置文件和关联的文件夹统一放到`/opt/docker-mysql`中

```
mkdir -p /opt/docker-mysql/conf.d
```

* 增加并修改配置文件`config-file.cnf`

```
cd /opt/docker-mysql/conf.d
vi config-file.cnf
```

* 内容如下,设置表名不区分大小写; linux下默认是区分的，windows下默认不区分

```
[mysqld]
# 表名不区分大小写
lower_case_table_names=1 
#server-id=1
datadir=/var/lib/mysql
#socket=/var/lib/mysql/mysqlx.sock
#symbolic-links=0
# sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES 
[mysqld_safe]
log-error=/var/log/mysqld.log
pid-file=/var/run/mysqld/mysqld.pid
```

* 增加数据文件夹

```
mkdir -p /opt/docker-mysql/var/lib/mysql
```



## 创建容器并启动

```
docker run --name mysql \
    --restart=always \
    -p 3307:3306 \
    -v /opt/docker-mysql/conf.d:/etc/mysql/conf.d \
    -v /opt/docker-mysql/var/lib/mysql:/var/lib/mysql \
    -e MYSQL_ROOT_PASSWORD=root \
    -e TZ=Asia/Shanghai \
    -d mysql:8.0.16
```

## 常用命令

* 进入容器

```
docker exec -it mysql bash
```

* 查看日志

```
docker logs -f mysql
```

* 备份数据

```
$ docker exec mysql sh -c 'exec mysqldump --all-databases -uroot -p"root"' > /some/path/on/your/host/all-databases.sql
```

* 恢复数据

```
$ docker exec -i mysql sh -c 'exec mysql -uroot -p"root"' < /some/path/on/your/host/all-databases.sql
```

## 其他问题

* only_full_group_by问题

如果安装的版本是 `5.7`版本, 查询数据时出现如下错误

> this is incompatible with sql_mode=only_full_group_by

可以使用使用下列方式解决

### 查询sql_mode

```
select @@sql_mode
```

结果如下

> ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION

### 重置

删除其中的 `ONLY_FULL_GROUP_BY`配置，重新设置到 `config-file.cnf`中

```
[mysqld]
# 表名不区分大小写
lower_case_table_names=1 
#server-id=1
datadir=/var/lib/mysql
#socket=/var/lib/mysql/mysqlx.sock
#symbolic-links=0
sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION
[mysqld_safe]
log-error=/var/log/mysqld.log
pid-file=/var/run/mysqld/mysqld.pid
```

### 重启容器

```
docker restart mysql
```

