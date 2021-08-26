# Docker容器

## 创建运行容器

```
docker run 镜像名
```

## 查看正在运行的容器

```
ubuntu@DESKTOP-48RTCQO:/$ docker container ls
CONTAINER ID        IMAGE                 COMMAND             CREATED             STATUS              PORTS
                               NAMES
43f0f17c8bfd        portainer/portainer   "/portainer"        6 hours ago         Up 6 hours          0.0.0.0:8000->8000/tcp, 0.0.0.0:9000->9000/tcp   portainer
ubuntu@DESKTOP-48RTCQO:/$ docker ps
CONTAINER ID        IMAGE                 COMMAND             CREATED             STATUS              PORTS
                               NAMES
43f0f17c8bfd        portainer/portainer   "/portainer"        6 hours ago         Up 6 hours          0.0.0.0:8000->8000/tcp, 0.0.0.0:9000->9000/tcp   portainer
```

## 查看容器详细信息

```
 docker container inspect 43f0f1
```

## 查看所有容器（包括未运行）

```
docker ps -a
```

## 停止容器

```
docker stop 43f0f1

docker container kill 43f0f1
```

## 进入容器方法

* 启动时进入

```
docker run -it 镜像名 /bin/bash
```

* 退出离开容器

```
ctrl+p & ctrl+q
```

* 启动后进入容器

```
docker attach 43f0f1
```

* **推荐**：exec进入容器

```
docker exec -it 43f0f1 /bin/bash
```

## 删除所有容器

* -f 强制删除

```
docker rm -f `docker ps -a -q`
```

## 端口映射

* -p 

```
docker run -d -p 8888:80 nginx:latest
```

![image-20210825202509690](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210825202509690.png)

* 随机映射

```
docker run -P （大P）# 需要镜像支持
```

