# CentOS安装Docker

```
wget -O /etc/yum.repos.d/docker-ce.repo https://mirrors.ustc.edu.cn/docker-ce/linux/centos/docker-ce.repo
sed -i 's#download.docker.com#mirrors.ustc.edu.cn/docker-ce#g' /etc/yum.repos.d/docker-ce.repo
yum install docker-ce -y
```

* 修改在docker配置

```
# 修改启动文件，监听远程端口
vim /usr/lib/systemd/system/docker.service
ExecStart=/usr/bin/dockerd -H unix:///var/run/docker.sock -H tcp://10.0.0.100:2375
systemctl daemon-reload
systemctl enable docker.service 
systemctl restart docker.service
# ps -ef检查进行，是否启动
```

* 查看docker相关信息

```
docker version
```

* docker镜像加速

```
vi /etc/docker/daemon.json
{
  "registry-mirrors": ["https://registry.docker-cn.com"]
}  
```

* 创建启动容器
  * run 创建并运行一个容器
  * -d  放入后台
  * -p 端口映射
  * nginx 镜像名称

```
docker run -d -p 80:80 nginx
```