# docker企业级镜像仓库harbor

* 先安装好docker和docker-compose

## 获取离线包

* 下载地址：https://github.com/goharbor/harbor/releases

```
root@DESKTOP-48RTCQO:/opt# ls
harbor-offline-installer-v2.3.2.tgz
```

## 解压压缩包

```
root@DESKTOP-48RTCQO:/opt# tar -zxvf harbor-offline-installer-v2.3.2.tgz
harbor/harbor.v2.3.2.tar.gz
harbor/prepare
harbor/LICENSE
harbor/install.sh
harbor/common.sh
harbor/harbor.yml.tmpl
root@DESKTOP-48RTCQO:/opt# ls
harbor  harbor-offline-installer-v2.3.2.tgz
```

## 修改配置

```
root@DESKTOP-48RTCQO:/opt/harbor# cp harbor.yml.tmpl harbor.yml
root@DESKTOP-48RTCQO:/opt/harbor# vim harbor.yml
```

![image-20210826132739894](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210826132739894.png)

## 执行安装

```
root@DESKTOP-48RTCQO:/opt/harbor# ./prepare
prepare base dir is set to /opt/harbor
Unable to find image 'goharbor/prepare:v2.3.2' locally
v2.3.2: Pulling from goharbor/prepare
```

```
root@DESKTOP-48RTCQO:/opt/harbor# ./install.sh
```

## 登录

* http://172.20.170.128:9001/harbor/projects
* admin/Harbor1234

![image-20210826153906584](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210826153906584.png)