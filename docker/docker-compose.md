# docker-compose

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

