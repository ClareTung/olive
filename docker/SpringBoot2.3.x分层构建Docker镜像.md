# SpringBoot2.3.x分层构建Docker镜像

## 镜像分层

* docker镜像构建是按照命令一行构建一层镜像的，会存在镜像缓存，如果有直接引用。

## SpringBoot2.3.x新增对分层的支持

* 使用打包插件`spring-boot-maven-plugin`并开启`layers`功能，然后执行Maven编译源码构建Jar包，使用该Jar包就能构建基于分层模式的Docker镜像。

```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.3.6.RELEASE</version>
    <relativePath/>
</parent>
```

```
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <!--开启分层编译支持-->
                <layers>
                    <enabled>true</enabled>
                </layers>
            </configuration>
        </plugin>
    </plugins>
</build>
```

```
mvn install
```

* 观察 Jar 结构，可以看到里面多了 `classpath.idx` 与 `layers.idx` 两个文件：
  * **classpath.idx：** 文件列出了依赖的 jar 包列表，到时候会按照这个顺序载入。
  * **layers.idx：** 文件清单，记录了所有要被复制到 Dokcer 镜像中的文件信息。

* 根据官方介绍，在构建 [Docker 镜像](http://mp.weixin.qq.com/s?__biz=MzI4Njc5NjM1NQ==&mid=2247500873&idx=3&sn=b64376259e07adaa7c0943842c4618d2&chksm=ebd5fb65dca272734254fb28e276cb7836377eb0a00f45400c77aebcb9651f1db7dc09c4e883&scene=21#wechat_redirect)前需要从 Jar 中提起出对应的分层文件到 Jar 外面，可用使用下面命令列出可以从分层 Jar 中提取出的文件夹信息：

```
$ java -Djarmode=layertools -jar target/springboot-layer-0.0.1.jar list
```

* 可用该看到以下输出，下面的内容就是接下来使用分层构建后，生成的 Jar 提取出对应资源后的结构：

```
dependencies
spring-boot-loader
snapshot-dependencies
application
```

* 上面即是使用分层工具提取 Jar 的内容后生成的文件夹，其中各个文件夹作用是：
  * **dependencies：** 存储项目正常依赖 Jar 的文件夹。
  * **snapshot-dependencies：** 存储项目快照依赖 Jar 的文件夹。
  * **resources：** 用于存储静态资源的文件夹。
  * **application：** 用于存储应用程序类相关文件的文件夹。

## 总结

* 镜像构建：分层Jar构建镜像可能比普通方式构建更加繁琐
* 镜像推送：分层Jar构建的镜像，可以大大加速镜像推送速度
* 镜像拉取：分层镜像拉取速度自然也很快

