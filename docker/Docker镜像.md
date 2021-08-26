# Docker镜像

## Docker镜像生命周期

![image-20210825200006813](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210825200006813.png)

## 搜索官方仓库镜像

```
docker search centos
```

```
ubuntu@DESKTOP-48RTCQO:/$ docker search mysql
NAME                              DESCRIPTION                                     STARS               OFFICIAL
  AUTOMATED
mysql                             MySQL is a widely used, open-source relation…   11320               [OK]

mariadb                           MariaDB Server is a high performing open sou…   4302                [OK]

mysql/mysql-server                Optimized MySQL Server Docker images. Create…   840
  [OK]
centos/mysql-57-centos7           MySQL 5.7 SQL database server                   91
```

* name：镜像名称
* descriptin：镜像说明
* starts：点赞数量
* official：是否是官方的
* automated：是否自动构建的

## 获取镜像

* 根据镜像名称拉取镜像

```
docker pull centos
```

* 拉取第三方镜像

```
docker pull index.tenxcluod.com/tenxcloud/httpd
```



## 查看当前主机镜像列表

```
ubuntu@DESKTOP-48RTCQO:/$ docker image list
REPOSITORY            TAG                 IMAGE ID            CREATED             SIZE
portainer/portainer   latest              580c0e4e98b0        5 months ago        79.1MB
```

## 删除镜像

```
docker image rm centos:latest
```

## 导出镜像

```
docker image save centos > docker-centos.tar.gz
```

## 导入镜像

```
docker image load -i docker-centos.tar.gz
```



## 查看镜像详细信息

```
ubuntu@DESKTOP-48RTCQO:/$ docker image inspect portainer/portainer
[
    {
        "Id": "sha256:580c0e4e98b06d258754cf28c55f21a6fa0dc386e6fe0bf67e453c3642de9b8b",
        "RepoTags": [
            "portainer/portainer:latest"
        ],
        "RepoDigests": [
            "portainer/portainer@sha256:fb45b43738646048a0a0cc74fcee2865b69efde857e710126084ee5de9be0f3f"
        ],
        "Parent": "",
        "Comment": "buildkit.dockerfile.v0",
        "Created": "2021-03-18T19:53:42.48462213Z",
        "Container": "",
        "ContainerConfig": {
            "Hostname": "",
            "Domainname": "",
            "User": "",
            "AttachStdin": false,
            "AttachStdout": false,
            "AttachStderr": false,
            "Tty": false,
            "OpenStdin": false,
            "StdinOnce": false,
            "Env": null,
            "Cmd": null,
            "Image": "",
            "Volumes": null,
            "WorkingDir": "",
            "Entrypoint": null,
            "OnBuild": null,
            "Labels": null
        },
        "DockerVersion": "",
        "Author": "",
        "Config": {
            "Hostname": "",
            "Domainname": "",
            "User": "",
            "AttachStdin": false,
            "AttachStdout": false,
            "AttachStderr": false,
            "ExposedPorts": {
                "9000/tcp": {}
            },
            "Tty": false,
            "OpenStdin": false,
            "StdinOnce": false,
            "Env": [
                "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"
            ],
            "Cmd": null,
            "Image": "",
            "Volumes": {
                "/data": {}
            },
            "WorkingDir": "/",
            "Entrypoint": [
                "/portainer"
            ],
            "OnBuild": null,
            "Labels": null
        },
        "Architecture": "amd64",
        "Os": "linux",
        "Size": 79085285,
        "VirtualSize": 79085285,
        "GraphDriver": {
            "Data": {
                "LowerDir": "/var/lib/docker/overlay2/2ea0280e94e64ebea4ff79ebf1881d0f54d9cd8d52280fefb63e7528f1083cbf/diff:/var/lib/docker/overlay2/da023fde2ab304b2082f86345b3a591ca45daaa3cad517b73b303921da0830fe/diff",
                "MergedDir": "/var/lib/docker/overlay2/1d94cdabfda3d34bf482c65efd64d904a6e1513fe9b146847ef8c488e44a548c/merged",
                "UpperDir": "/var/lib/docker/overlay2/1d94cdabfda3d34bf482c65efd64d904a6e1513fe9b146847ef8c488e44a548c/diff",
                "WorkDir": "/var/lib/docker/overlay2/1d94cdabfda3d34bf482c65efd64d904a6e1513fe9b146847ef8c488e44a548c/work"
            },
            "Name": "overlay2"
        },
        "RootFS": {
            "Type": "layers",
            "Layers": [
                "sha256:8dfce63a73970a18bcc2ca447d9c252aedd3157e9ee02a88e66c53571279aee9",
                "sha256:11bdf2a940a7eb35fe69359d45eaeb6f8553a682a19e26db49d4c924588bb6c4",
                "sha256:658693958bcb13c9d33a49d82f1e1297073066bec8d8b07dd49357ad5c08ce58"
            ]
        },
        "Metadata": {
            "LastTagTime": "0001-01-01T00:00:00Z"
        }
    }
]
```



