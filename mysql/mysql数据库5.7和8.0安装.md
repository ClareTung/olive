## MySQL5.7

```text
[mysqld]
# 设置3306端口
port=3307
# 设置mysql的安装目录
basedir=D:\mysql5.7
# 设置mysql数据库的数据的存放目录
datadir=D:\mysql5.7\data
# 允许最大连接数
max_connections=500
# 允许连接失败的次数。
max_connect_errors=10
# 服务端使用的字符集默认为utf8mb4
character-set-server=utf8mb4
# 创建新表时将使用的默认存储引擎
default-storage-engine=INNODB
# 默认使用“mysql_native_password”插件认证
#mysql_native_password
default_authentication_plugin=mysql_native_password
[mysql]
# 设置mysql客户端默认字符集
default-character-set=utf8mb4
[client]
# 设置mysql客户端连接服务端时默认使用的端口
port=3307
default-character-set=utf8mb4
```

## MySQL8.0

```text
[mysqld]
# 设置3306端口
port=3306
# 设置mysql的安装目录
basedir=D:\MySQL
# 设置mysql数据库的数据的存放目录
datadir=D:\MySQL\Data
# 允许最大连接数
max_connections=200
# 允许连接失败的次数。
max_connect_errors=10
# 服务端使用的字符集默认为utf8mb4
character-set-server=utf8mb4
# 创建新表时将使用的默认存储引擎
default-storage-engine=INNODB
# 默认使用“mysql_native_password”插件认证
#mysql_native_password
default_authentication_plugin=mysql_native_password
[mysql]
# 设置mysql客户端默认字符集
default-character-set=utf8mb4
[client]
# 设置mysql客户端连接服务端时默认使用的端口
port=3306
default-character-set=utf8mb4
```