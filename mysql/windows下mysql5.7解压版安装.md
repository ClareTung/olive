# Windows下MySQL5.7解压版安装

* 1.下载地址：https://dev.mysql.com/downloads/mysql/5.7.html#downloads
* 2.可以把解压的内容随便放到一个目录，我的是如下目录（放到C盘的话，可能在修改ini文件时涉及权限问题，之后我就改放D盘了）
* 3.在D:\MySQL\MySQL Server 5.7.22目录下新建my.ini文件，复制如下内容
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
* MySQL安装过程：
* 管理员身份运行cmd
* 进入如下目录：D:\MySQL\MySQL Server 5.7.22\bin
* 运行命令：mysqld  --initialize (此时会生成data目录)
  * 如果运行命令提示：由于找不到MSVCR120.dll,无法继续执行代码.重新安装程序可能...
  * 这种情况需要安装 vcredist 下载vcredist ：https://www.microsoft.com/zh-CN/download/details.aspx?id=40784
* 运行mysqld -install （安装）
* 运行net start mysql (启动mysql服务)


* 设置root账户密码：
* 在my.ini文件（MySQL的配置文件）的[mysqld]下加一行skip-grant-tables
* 然后在任务管理器中重启MySQL服务

重启MqSQL服务后，运行mysql -uroot -p,可以成功登入mysql

然后更新root账户的密码为'root'

命令：update mysql.user set authentication_string=password("root") where user="root";

然后输入flush privileges;（刷新账户信息）

执行quit或ctrl+Z退出

然后将my.ini文件中刚才加的skip-grant-tables这一行删掉，保存后再重启MySQL服务


