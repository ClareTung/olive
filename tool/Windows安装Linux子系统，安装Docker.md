# Windows安装Linux子系统，安装Docker

## 安装Windows Terminal

* 在应用里搜索，Windows Terminal，安装即可

## 开启WSL2

* 启用和关闭windows功能 -> 勾选 适用Windows的Linux子系统

## 安装Linux

* 在应用商店里，搜索“Linux”，选择安装Ubuntu

* 首先把ubuntu的软件源给换掉。编辑`/etc/apt/sources.list`文件，把它的内容换成下面的源。

```
deb https://mirrors.ustc.edu.cn/ubuntu/ bionic main restricted universe multiverse
deb https://mirrors.ustc.edu.cn/ubuntu/ bionic-updates main restricted universe multiverse
deb https://mirrors.ustc.edu.cn/ubuntu/ bionic-backports main restricted universe multiverse
deb https://mirrors.ustc.edu.cn/ubuntu/ bionic-security main restricted universe multiverse
deb https://mirrors.ustc.edu.cn/ubuntu/ bionic-proposed main restricted universe multiverse
deb-src https://mirrors.ustc.edu.cn/ubuntu/ bionic main restricted universe multiverse
deb-src https://mirrors.ustc.edu.cn/ubuntu/ bionic-updates main restricted universe multiverse
deb-src https://mirrors.ustc.edu.cn/ubuntu/ bionic-backports main restricted universe multiverse
deb-src https://mirrors.ustc.edu.cn/ubuntu/ bionic-security main restricted universe multiverse
deb-src https://mirrors.ustc.edu.cn/ubuntu/ bionic-proposed main restricted universe multiverse
```

* 安装最好用的`oh-my-zsh`。先用`sudo apt install zsh`安装shell终端，然后运行下面的命令。

```
sh -c "$(curl -fsSL https://raw.github.com/ohmyzsh/ohmyzsh/master/tools/install.sh)"
```

## 安装Docker

* 下载安装最新Windows版本

```
https://www.docker.com/products/docker-desktop
```

* 安装完成，更改镜像仓库

```
"registry-mirrors": [
    "https://registry.docker-cn.com",
    "https://dockerhub.azk8s.cn",
    "https://reg-mirror.qiniu.com",
    "http://hub-mirror.c.163.com",
    "https://docker.mirrors.ustc.edu.cn"
]
```

* `apply & restart` 重启生效一下

## **安装 portainer**

* Docker的管理工具
* 它不仅能管理单机上的docker，还能够管理局域网中的集群，只要你使用`-H 0.0.0.0:2375`把它监听在网络上。

```
docker volume create portainer_data
docker run -d -p 9000:9000 -p 8000:8000 --name portainer --restart always -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer
```

* 访问http://localhost:9000，设置用户
  * admin/admin1234
* 在模板页面，有很多常见的软件应用，直接点击部署，就可以将软件安装在系统上。如果你是作为开发机使用，那么推荐使用docker的host模式，相当于使用主机的网络，和安装一个平常的软件没什么区别。



## WSL1升级为WSL2

* https://docs.microsoft.com/zh-cn/windows/wsl/install-win10#step-4---download-the-linux-kernel-update-package
* https://docs.docker.com/desktop/windows/wsl/
* 下载 Linux 内核更新包

![image-20210825134200635](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210825134200635.png)

* wsl --set-default-version 2
* wsl.exe -l -v
* wsl.exe --set-version Ubuntu-18.04 2

## Got permission denied while trying to connect to the Docker daemon socket at unix

```
clare@olive:/$ docker volume create portainer_data
Got permission denied while trying to connect to the Docker daemon socket at unix:///var/run/docker.sock: Post "http://%2Fvar%2Frun%2Fdocker.sock/v1.24/volumes/create": dial unix /var/run/docker.sock: connect: permission denied
clare@olive:/$ sudo groupadd docker
[sudo] password for clare:
groupadd: group 'docker' already exists
clare@olive:/$ sudo gpasswd -a $USER docker
Adding user clare to group docker
clare@olive:/$ newgrp docker
clare@olive:/$ docker volume create portainer_data
portainer_data
```

```
sudo groupadd docker #添加docker用户组
sudo gpasswd -a $USER docker #将登陆用户加入到docker用户组中
newgrp docker #更新用户组
```
##  Unable to proxy the request via the Docker socket (err=dial unix /var/run/docker.sock: connect: connection refused) (code=500)

```
clare@olive:/$ sudo gpasswd -a $USER docker
Adding user clare to group docker
clare@olive:/$ newgrp docker
```


## Linux使用

### 删除一行 

* `dd`

### su: Authentication failure

* `sudo passwd root`重置root密码

### Unable to locate package zsh  

* `sudo apt-get update` 先下载包

### readonly option is set

* `su root`获取root用户权限



