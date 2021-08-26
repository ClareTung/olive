# Docker 数据卷的管理

## 挂载时创建卷

* 挂载卷

```
ubuntu@DESKTOP-48RTCQO:/$ docker run -d -p 80:80 -v /data:/usr/share/nginx/html nginx:latest
5dd97d3919c5dedcb62c8e6170176607d14a9b62b55328be84f15d6692909a8a
```

* 容器内站点目录: /usr/share/nginx/html
* 在宿主机写入数据，查看

```
root@DESKTOP-48RTCQO:/# echo "www.baidu.com" > /data/index.html
root@DESKTOP-48RTCQO:/# curl 172.20.171.67
www.baidu.com
```

* 设置共享卷，使用同一个卷启动一个新的容器

```
root@DESKTOP-48RTCQO:/# docker run -d -p 8080:80 -v /data:/usr/share/nginx/html nginx:latest
root@DESKTOP-48RTCQO:/# curl 172.20.171.67:8080
www.baidu.com
```

* 查看卷列表

```
root@DESKTOP-48RTCQO:/# docker volume ls
DRIVER              VOLUME NAME
local               portainer_data
```

## 创建卷后挂载

* 创建一个卷

```
[root@docker01 ~]# docker volume create 
f3b95f7bd17da220e63d4e70850b8d7fb3e20f8ad02043423a39fdd072b83521
[root@docker01 ~]# docker volume ls 
DRIVER              VOLUME NAME
local               f3b95f7bd17da220e63d4e70850b8d7fb3e20f8ad02043423a39fdd072b83521
```

* 指定卷名

```
root@DESKTOP-48RTCQO:/# docker volume create ngx_data
DRIVER              VOLUME NAME
local               ngx_data
local               portainer_data
```

* 查看卷路径

```
root@DESKTOP-48RTCQO:/# docker volume inspect ngx_data
[
    {
        "CreatedAt": "2021-08-25T13:50:21Z",
        "Driver": "local",
        "Labels": {},
        "Mountpoint": "/var/lib/docker/volumes/ngx_data/_data",
        "Name": "ngx_data",
        "Options": {},
        "Scope": "local"
    }
]
```

* 使用卷创建

```
root@DESKTOP-48RTCQO:/# docker run -d -p 8081:80 -v ngx_data:/usr/share/nginx/html nginx:latest
```

* 设置卷

```
[root@docker01 ~]# docker run  -d  -P  --volumes-from 079786c1e297 nginx:latest 
b54b9c9930b417ab3257c6e4a8280b54fae57043c0b76b9dc60b4788e92369fb
```

* 查看使用的端口

```
root@DESKTOP-48RTCQO:~# netstat -lntup
Active Internet connections (only servers)
Proto Recv-Q Send-Q Local Address           Foreign Address         State       PID/Program name
tcp6       0      0 :::8000                 :::*                    LISTEN      -
tcp6       0      0 :::9000                 :::*                    LISTEN      -
tcp6       0      0 :::80                   :::*                    LISTEN      -
tcp6       0      0 :::8080                 :::*                    LISTEN      -
tcp6       0      0 :::8081                 :::*                    LISTEN      -
```

## 手动将容器保存为镜像

* 本次是基于docker官方centos 6.8 镜像创建
* 官方镜像列表：https://hub.docker.com/explore/
* 启动一个centos6.8的镜像

```
[root@docker01 ~]# docker pull  centos:6.8
[root@docker01 ~]# docker run -it -p 1022:22 centos:6.8  /bin/bash
# 在容器种安装sshd服务，并修改系统密码
[root@582051b2b92b ~]# yum install  openssh-server -y 
[root@582051b2b92b ~]# echo "root:123456" |chpasswd
[root@582051b2b92b ~]#  /etc/init.d/sshd start
```

* 启动完成后镜像ssh连接测试
* 将容器提交为镜像

```
[root@docker01 ~]# docker commit brave_mcclintock  centos6-ssh
```

* 使用新的镜像启动容器

```
[root@docker01 ~]# docker run -d  -p 1122:22  centos6-ssh:latest  /usr/sbin/sshd -D 
5b8161fda2a9f2c39c196c67e2eb9274977e7723fe51c4f08a0190217ae93094
```

* 在容器安装httpd服务

```
[root@5b8161fda2a9 /]#  yum install httpd -y
```

* 编写启动脚本脚本

```
[root@5b8161fda2a9 /]# cat  init.sh 
#!/bin/bash 
/etc/init.d/httpd start 
/usr/sbin/sshd -D
[root@5b8161fda2a9 /]# chmod +x init.sh 
# 注意执行权限
```

* 再次提交为新的镜像

```
[root@docker01 ~]# docker commit  5b8161fda2a9 centos6-httpd 
sha256:705d67a786cac040800b8485cf046fd57b1828b805c515377fc3e9cea3a481c1
```

* 启动镜像，做好端口映射。并在浏览器中测试访问

```
[root@docker01 ~]# docker run -d -p 1222:22 -p 80:80  centos6-httpd /init.sh 
46fa6a06644e31701dc019fb3a8c3b6ef008d4c2c10d46662a97664f838d8c2c
```