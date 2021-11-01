## Docker安装RocketMQ

### 检索镜像

```
docker search rocketmq
```

### 检索具体版本

```
curl https://registry.hub.docker.com/v1/repositories/foxiswho/rocketmq/tags | tr -d '[\[\]" ]' | tr '}' '\n' | awk -F: -v image='foxiswho/rocketmq' '{if(NR!=NF && $3 != ""){printf("%s:%s\n",image,$3)}}'
```

### 启动NameServer

```
docker run -d -p 9876:9876 --name rmqserver foxiswho/rocketmq:server-4.5.1
```

### 启动broker

```
docker run -d -p 10911:10911 -p 10909:10909 --name rmqbroker --link rmqserver:namesrv -e "NAMESRV_ADDR=namesrv:9876" -e "JAVA_OPTS=-Duser.home=/opt" -e "JAVA_OPT_EXT=-server -Xms128m -Xmx128m" foxiswho/rocketmq:broker-4.5.1
```

Broker容器中默认的配置文件的路径为：

```
/etc/rocketmq/broker.conf
```

注意事项：

broker.conf的配置内容如下：

```none
brokerClusterName = DefaultCluster
brokerName = broker-a
brokerId = 0
deleteWhen = 04
fileReservedTime = 48
brokerRole = ASYNC_MASTER
flushDiskType = ASYNC_FLUSH
```

在最后一行后面新增

```
brokerIP1 = 192.168.3.27
```

ip改成你的linux宿主机的ip

也可以通过-v参数指定本机的配置文件：

```
docker run -d -p 10911:10911 -p 10909:10909 --name rmqbroker --link rmqserver:namesrv -e "NAMESRV_ADDR=namesrv:9876" -e "JAVA_OPTS=-Duser.home=/opt" -e "JAVA_OPT_EXT=-server -Xms128m -Xmx128m" -v /conf/broker.conf:/etc/rocketmq/broker.conf foxiswho/rocketmq:broker-4.5.1
```

### 安装 rocketmq console

```
docker run -d --name rmqconsole -p 8180:8080 --link rmqserver:namesrv -e "JAVA_OPTS=-Drocketmq.namesrv.addr=namesrv:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false" -t styletang/rocketmq-console-ng
```

* http://172.24.161.151:8180/