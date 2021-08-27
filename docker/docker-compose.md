# docker-compose

* docker-compose 是单机多容器部署工具，通过yml文件定义多容器如何部署。
* docker-compose能力有限，只能在一台宿主机上对容器编排部署。集群部署由另外的工具k8s来完成

* 安装

```
root@DESKTOP-48RTCQO:/# curl -L https://github.com/docker/compose/releases/download/1.22.0/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100   633  100   633    0     0    900      0 --:--:-- --:--:-- --:--:--   900
100 11.2M  100 11.2M    0     0  2355k      0  0:00:04  0:00:04 --:--:-- 3254k
root@DESKTOP-48RTCQO:/# chmod +x /usr/local/bin/docker-compose
root@DESKTOP-48RTCQO:/# docker-compose -version
docker-compose version 1.22.0, build f46880fe
```

* 卸载

```
rm /usr/local/bin/docker-compose
```
* 解释执行docker-compose文件
    * up表示解析该脚本
    * -d表示后台运行 
```
docker-compose up -d
```

* docker-compose.yml
```yaml
version: '3.3'
services:
  db:
    build: ./bsbdj-db/
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root
  app:
    build: ./bsbdj-app/
    depends_on:
      - db
    ports:
      - "80:80"
    restart: always

```
* 其中
    * 1、version:'3.3'是docker-compose最常见的版本，不同版本的配置项使用略有差别。
    * 2、services是一个父级节点，表示该编排存在的各种服务。
    * 3、services下级，是各个容器的服务信息，我们这里存在db和app两个服务，db和app也会作为容器的名称，并且我们可以通过服务名替换容器间相互通信的ip（网络的主机名），也可达到互相访问的效果
    * 4、build是服务的子节点，表示该服务容器通过哪里的Dockerfile来构建
    * 5、restart表示服务重启的策略，如果容器意外退出，那么docker会重新启动
    * 6、environment指定服务容器的环境变量
    * 7、depends_on表示该容器的以来，我们的app容器依赖于db服务容器，depends_on下跟服务名
    * 8、ports用来设置该服务的端口和宿主机端口的一个映射，格式为宿主机端口：容器暴露的端口
