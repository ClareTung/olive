# Docker安装Redis

## 安装目录

```
clare@olive:/opt$ sudo mkdir docker-redis
```

## 下载redis配置文件

```
clare@olive:/opt/docker-redis$ sudo wget http://download.redis.io/redis-stable/redis.conf
```

### 编辑配置文件

```
clare@olive:/opt/docker-redis$ sudo vi redis.conf
```

```
# bind 127.0.0.1
protected-mode no
requirepass redis
appendonly yes     #redis持久化　　默认是no
daemonize no       #用守护线程的方式启动
tcp-keepalive 300  #防止出现远程主机强迫关闭了一个现有的连接的错误 默认是300
```

### 创建保存数据文件

```
clare@olive:/opt/docker-redis$ sudo mkdir data
clare@olive:/opt/docker-redis$ ls
data  redis.conf
```

## 拉取镜像

```
clare@olive:/opt/docker-redis$ docker pull redis:latest
```

## 创建启动容器

```
docker run -p 6379:6379 --name redis -v /opt/docker-redis/redis.conf:/etc/redis/redis.conf -v /opt/docker-redis/data:/data -d redis redis-server /etc/redis/redis.conf --appendonly yes
```

> `-p 6379:6379`：把容器内的6379端口映射到宿主机6379端口
> `-v /opt/docker-redis/redis.conf:/etc/redis/redis.conf`：把宿主机配置好的redis.conf放到容器内的这个位置中
> `-v /opt/docker-redis/data:/data`：把redis持久化的数据在宿主机内显示，做数据备份
> `redis-server /etc/redis/redis.conf`：这个是关键配置，让redis不是无配置启动，而是按照这个redis.conf的配置启动
> `--appendonly yes`：redis启动后数据持久化

