# DockerFile

## DockerFile是什么
* 用来构建docker镜像的文件
* 一般分为四部分：基础镜像信息、维护者信息、镜像操作指令信息和容器启动时执行指令
* `#` 为DockerFile中的注释

* 如tomcat的dockerfile如下：

```
 FROM adoptopenjdk:8-jdk-hotspot
 ENV CATALINA_HOME /usr/local/tomcat
 ENV PATH $CATALINA_HOME/bin:$PATH
 RUN mkdir -p "$CATALINA_HOME"
 WORKDIR $CATALINA_HOME
 ENV TOMCAT_NATIVE_LIBDIR $CATALINA_HOME/native-jni-lib
 ENV LD_LIBRARY_PATH ${LD_LIBRARY_PATH:+$LD_LIBRARY_PATH:}$TOMCAT_NATIVE_LIBDIR
 ENV GPG_KEYS 05AB33110949707C93A279E3D3EFE6B686867BA6 
 EXPOSE 8080
 CMD ["catalina.sh", "run"]
```

## DockerFile解析
1. 基础结构由大写的保留字指令+参数构成。
2. 指令从上到下顺序执行。
3. `#`表示注解。
4. 每条指令都会创建新的镜像层，并对镜像进行提交。

## DockerFile保留字指令解析
### FROM
* 基础镜像，当前新建的镜像是基于哪个镜像的。

### MAINTAINER
* 镜像维护者的姓名邮箱主页

### RUN
* 容器构建的时候需要的命令　

### EXPOSE
* 当前容器对外暴露的端口

### WORKDIR
* 指定在创建容器后，终端默认登录的进来工作目录

### ENV
* 镜像构建过程中需要设置的环境变量

### ADD
* 将宿主机目录下的文件拷贝进镜像且ADD命令会自动处理URL和解压tar压缩包

### COPY
* 将宿主机目录下的文件拷贝进镜像，或者拷贝出来

### VOLUME
* 容器数据卷，**用于数据保存和持久化工作**。可以将宿主机目录和容器绑定，这样两个文件夹下的文件都能共通，如tomcat镜像的项目部署文件夹，映射到宿主机后，部署项目不需要进入镜像，直接部署到宿主机对应目录就可以了。

### CMD
* 指定一个容器启动时要运行的命令，dockerfile中可以有多个cmd命令，但是只有最后一个会生效，之前的都会被覆盖不会被执行。

### ENTRYPOINT
* 指定一个容器启动时要运行的命令，和cmd一样，但是可以指定多个命令，在容器启动时，会顺序执行。

### ONBUILD
* 当构建一个被继承的dockerfile时运行命令，父镜像在被子继承后父镜像的onbuild被触发




