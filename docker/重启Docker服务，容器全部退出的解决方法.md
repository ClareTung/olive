# 重启Docker服务，容器全部退出的解决方法

* 在启动时指定自动启动

```
docker run --restart=always
```

* 修改Docker默认配置文件

```
# 添加上下面这行
"live-restore": true
```

* Docker server配置文件`/etc/docker/daemon.json`参考

```
{
  "registry-mirrors": ["https://registry.docker-cn.com"],
  "graph": "/opt/mydocker", # 修改数据的存放目录到/opt/mydocker/，原/var/lib/docker/
  "insecure-registries": ["10.0.0.100:5000"],
  "live-restore": true
}
```

* 重启后生效

```
systemctl restart docker.service
```

