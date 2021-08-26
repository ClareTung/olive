# Xshell链接Windows子系统Linux

```
root@DESKTOP-48RTCQO:/# apt-get update
root@DESKTOP-48RTCQO:/# sudo apt autoremove --purge openssh-server -y && sudo apt install openssh-server -y
root@DESKTOP-48RTCQO:/# vim /etc/ssh/sshd_config
```

```text
Port 2222
PasswordAuthentication yes
AllowUsers root
PermitRootLogin yes
```

```
root@DESKTOP-48RTCQO:/# service ssh --full-restart
root@DESKTOP-48RTCQO:/# ifconfig
eth0: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet 172.20.171.67  netmask 255.255.240.0  broadcast 172.20.175.255
```

* 连接成功

![image-20210826131046117](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210826131046117.png)