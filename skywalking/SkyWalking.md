# SkyWalking

* 分布式追踪系统
* 在微服务架构系统中，一个请求可能会涉及多个服务，而服务本身可能也会依赖其他服务，整个请求路径就构成了一个网状的调用链，而在整个调用链中一旦某个节点发生异常，整个调用链的稳定性就会受到影响。

![image-20210106145029883](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210106145029883.png)

* SkyWalking 的核心是**数据分析和度量结果的存储平台**，通过 HTTP 或 gRPC 方式向 SkyWalking Collecter 提交分析和度量数据，**SkyWalking Collecter 对数据进行分析和聚合，存储到 Elasticsearch、H2、MySQL、TiDB 等其一即可，最后我们可以通过 SkyWalking UI 的可视化界面对最终的结果进行查看。**Skywalking 支持从多个来源和多种格式收集数据：多种语言的 Skywalking Agent 、Zipkin v1/v2 、Istio 勘测、Envoy 度量等数据格式。

## 下载地址

* https://archive.apache.org/dist/skywalking/
* http://skywalking.apache.org/downloads/

## 启动（windows）

* 修改数据存储配置为es

![image-20210106145528139](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210106145528139.png)

![image-20210106145617859](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210106145617859.png)

* 启动：D:\apache-skywalking-es7\bin\startup.bat

## Java项目接入

* 参考 [部署 skywalking javaagent](https://links.jianshu.com/go?to=https%3A%2F%2Fgithub.com%2Fapache%2Fincubator-skywalking%2Fblob%2F5.x%2Fdocs%2Fcn%2FDeploy-skywalking-agent-CN.md)，skywalking-agent.jar 位于下载包的 agent 目录下

* https://github.com/apache/skywalking/blob/master/docs/en/setup/README.md

### 使用步骤

* 找到SkyWalking包中的 `agent` 目录

* 将 `agent` 目录拷贝到任意位置

* 配置 `config/agent.config` ：

  * 将 `agent.service_name` 修改成你的微服务名称；
  * 如果Skywalking和微服务部署在不同的服务器，还需修改 `collector.backend_service` 的值，该配置用来指定微服务和Skywalking通信的地址，默认是 `127.0.0.1:11800` ，按需修改即可。当然 `agent.config` 文件里面有很多的配置，本文下面的表格有详细讲解。

* 命令行：

  ```
  java -javaagent:/opt/agent/skywalking-agent.jar -jar somr-spring-boot.jar
  ```

* IDEA

  ![image-20210106151631786](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210106151631786.png)

### 追踪

* 这个导航栏是我们定位问题时最常用的，可以搜索查询的具体细节。定位性能瓶颈出在了哪个阶段。

![image-20210106152044374](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210106152044374.png)

## Java Agent插件

* Java Agent是插件化、可插拔的。Skywalking的插件分为三种：
  * 引导插件：在agent的 `bootstrap-plugins` 目录下
  * 内置插件：在agent的 `plugins` 目录下
  * 可选插件：在agent的 `optional-plugins` 目录下

* Java Agent只会启用 `plugins` 目录下的所有插件，`bootstrap-plugins` 目录以及`optional-plugins` 目录下的插件不会启用。如需启用引导插件或可选插件，只需将JAR包移到 `plugins` 目录下，如需禁用某款插件，只需从 `plugins` 目录中移除即可。

### 引导插件

目前只有两款引导插件：

* `apm-jdk-http-plugin` 用来是监测HttpURLConnection；
* `apm-jdk-threading-plugin` 用来监测Callable以及Runnable；

有关引导插件的功能描述，可详见： `https://github.com/apache/skywalking/blob/v6.6.0/docs/en/setup/service-agent/java-agent/README.md#bootstrap-class-plugins` 。

### 内置插件

* 内置插件主要用来为业界主流的技术与框架提供支持。所支持的技术&框架，详见 `https://github.com/apache/skywalking/blob/v6.6.0/docs/en/setup/service-agent/java-agent/Supported-list.md` 。

### 可选插件

* 关于可选插件的功能描述，可详见 `https://github.com/apache/skywalking/blob/v6.6.0/docs/en/setup/service-agent/java-agent/README.md` 。

### 插件扩展

* Skywalking生态还有一些插件扩展，例如Oracle、Resin插件等。这部分插件主要是由于许可证不兼容/限制，Skywalking无法将这部分插件直接打包到Skywalking安装包内，于是托管在这个地址： `https://github.com/SkyAPM/java-plugin-extensions` ，使用方式：
* 前往 `https://github.com/SkyAPM/java-plugin-extensions/releases` ，下载插件JAR包•将JAR包挪到 `plugins` 目录即可启用。

## Java Agent配置

* 本表格基于**Skywalking 6.6.0**，官方文档详见：`https://github.com/apache/skywalking/blob/v6.6.0/docs/en/setup/service-agent/java-agent/README.md` ，其他版本配置项**不完全相同**，请自行将链接中的 `v6.6.0` 修改成你所使用的版本。

| 属性名                                                       | 描述                                                         | 默认值                                               |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ---------------------------------------------------- |
| `agent.namespace`                                            | 命名空间，用于隔离跨进程传播的header。如果进行了配置，header将为`HeaderName:Namespace`. | 未设置                                               |
| `agent.service_name`                                         | 在SkyWalking UI中展示的服务名。5.x版本对应Application，6.x版本对应Service。建议：为每个服务设置个唯一的名字，服务的多个服务实例为同样的服务名 | `Your_ApplicationName`                               |
| `agent.sample_n_per_3_secs`                                  | 负数或0表示不采样，默认不采样。SAMPLE_N_PER_3_SECS表示每3秒采样N条。 | 未设置                                               |
| `agent.authentication`                                       | 鉴权是否开启取决于后端的配置，可查看application.yml的详细描述。对于大多数的场景，需要后端对鉴权进行扩展。目前仅实现了基本的鉴权功能。 | 未设置                                               |
| `agent.span_limit_per_segment`                               | 单个segment中的span的最大个数。通过这个配置项，Skywalking可评估应用程序内存使用量。 | 300                                                  |
| `agent.ignore_suffix`                                        | 如果这个集合中包含了第一个span的操作名，这个segment将会被忽略掉。 | 未设置                                               |
| `agent.is_open_debugging_class`                              | 如果为true，skywalking会将所有经Instrument转换过的类文件保存到`/debugging`文件夹下。Skywalking团队会要求你提供这些类文件以解决兼容性问题。 | 未设置                                               |
| `agent.active_v2_header`                                     | 是否默认使用v2版本的header。                                 | `true`                                               |
| `agent.instance_uuid`                                        | 实例id。skywalking会将实例id相同的看做一个实例。如果为空，skywalking agent会生成一个32位的uuid。 | `""`                                                 |
| `agent.instance_properties[key]=value`                       | 添加服务实例的定制属性。                                     | 未设置                                               |
| `agent.cause_exception_depth`                                | agent记录的异常时，代理进入的深度                            | 5                                                    |
| `agent.active_v1_header`                                     | 是否默认使用v1版本的header。                                 | `false`                                              |
| `agent.cool_down_threshold`                                  | 收到reset命令后，代理应等待多久（以分钟为单位）才能重新注册到OAP服务器。 | `10`                                                 |
| `agent.force_reconnection_period`                            | 根据grpc_channel_check_interval强制重新连接grpc。            | `1`                                                  |
| `agent.operation_name_threshold`                             | operationName最大长度，不建议将此值设置为> 500。             | `500`                                                |
| `collector.grpc_channel_check_interval`                      | 检查grpc的channel状态的时间间隔。                            | `30`                                                 |
| `collector.app_and_service_register_check_interval`          | 检查应用和服务的注册状态的时间间隔。                         | `3`                                                  |
| `collector.backend_service`                                  | 接收skywalking trace数据的后端地址                           | `127.0.0.1:11800`                                    |
| `collector.grpc_upstream_timeout`                            | grpc客户端向上游发送数据时的超时时间，单位秒。               | `30` 秒                                              |
| `logging.level`                                              | 日志级别。默认为debug。                                      | `DEBUG`                                              |
| `logging.file_name`                                          | 日志文件名                                                   | `skywalking-api.log`                                 |
| `logging.output`                                             | 日志输出，默认FILE。CONSOLE表示输出到stdout。                | `FILE`                                               |
| `logging.dir`                                                | 日志目录。默认为空串，表示使用”system.out”输出日志。         | `""`                                                 |
| `logging.pattern`                                            | 日志记录格式。所有转换说明符：* `%level` 表示日志级别。* `%timestamp` 表示现在的时间，格式`yyyy-MM-dd HH:mm:ss:SSS`。* `%thread` 表示当前线程的名称。* `%msg` 表示消息。* `%class` 表示TargetClass的SimpleName。* `%throwable` 表示异常。* `%agent_name` 表示`agent.service_name` | `%level %timestamp %thread %class : %msg %throwable` |
| `logging.max_file_size`                                      | 日志文件的最大大小。当日志文件大小超过这个数，归档当前的日志文件，将日志写入到新文件。 | `300 * 1024 * 1024`                                  |
| `logging.max_history_files`                                  | 最大历史记录日志文件。发生翻转时，如果日志文件超过此值，则最早的文件将被删除。默认情况下，负数或零表示关闭。 | `-1`                                                 |
| `jvm.buffer_size`                                            | 收集JVM信息的buffer的大小。                                  | `60 * 10`                                            |
| `buffer.channel_size`                                        | buffer的channel大小。                                        | `5`                                                  |
| `buffer.buffer_size`                                         | buffer的大小                                                 | `300`                                                |
| `dictionary.service_code_buffer_size`                        | The buffer size of application codes and peer                | `10 * 10000`                                         |
| `dictionary.endpoint_name_buffer_size`                       | The buffer size of endpoint names and peer                   | `1000 * 10000`                                       |
| `plugin.peer_max_length`                                     | Peer最大描述限制                                             | `200`                                                |
| `plugin.mongodb.trace_param`                                 | 如果为true，记录所有访问MongoDB的参数信息。默认为false，表示仅记录操作名，不记录参数信息。 | `false`                                              |
| `plugin.mongodb.filter_length_limit`                         | 如果设为正数，`WriteRequest.params` 将被截断为该长度，否则将被完全保存，这可能会导致性能问题。 | `256`                                                |
| `plugin.elasticsearch.trace_dsl`                             | 如果为true，记录所有访问ElasticSearch的DSL信息。默认为false。 | `false`                                              |
| `plugin.springmvc.use_qualified_name_as_endpoint_name`       | 如果为true，endpoint的name为方法的全限定名，而不是请求的URL。默认为false。 | `false`                                              |
| `plugin.toolit.use_qualified_name_as_operation_name`         | 如果为true，operation的name为方法的全限定名，而不是给定的operation name。默认为false。 | `false`                                              |
| `plugin.mysql.trace_sql_parameters`                          | 如果设置为true，则将收集sql的参数（通常为`java.sql.PreparedStatement`）。 | `false`                                              |
| `plugin.mysql.sql_parameters_max_length`                     | 如果设置为正数，`db.sql.parameters` 将被截断为该长度，否则将被完全保存，这可能会导致性能问题。 | `512`                                                |
| `plugin.postgresql.trace_sql_parameters`                     | 如果设置为true，则将收集sql的参数（通常为`java.sql.PreparedStatement`）。 | `false`                                              |
| `plugin.postgresql.sql_parameters_max_length`                | 如果设置为正数，`db.sql.parameters` 将被截断为该长度，否则将被完全保存，这可能会导致性能问题。 | `512`                                                |
| `plugin.solrj.trace_statement`                               | 如果为true，则在Solr查询请求中跟踪所有查询参数（包括deleteByIds和deleteByQuery） | `false`                                              |
| `plugin.solrj.trace_ops_params`                              | 如果为true，则跟踪Solr请求中的所有操作参数                   | `false`                                              |
| `plugin.light4j.trace_handler_chain`                         | 如果为true，请跟踪Light4J的请求的所有中间件/业务handler。    | `false`                                              |
| `plugin.opgroup.*`                                           | 支持操作名称自定义不同插件中的组的规则。详见 支持组规则的插件[1] | 未设置                                               |
| `plugin.springtransaction.simplify_transaction_definition_name` | 设为true，则简化事务定义名称。                               | false                                                |
| `plugin.jdkthreading.threading_class_prefixes`               | 将对名称与任意一个 `THREADING_CLASS_PREFIXES` (多个使用 `,` 分隔)匹配的线程化类（ `java.lang.Runnable` 和 `java.util.concurrent.Callable` ）及其子类进行 Instrument，请确保仅将窄前缀指定为您希望Instrument的前缀（安全考虑， `java.` 和 `javax.` 将被忽略） | 未设置                                               |

### Java Agent配置方式

agent配置有多种姿势，上面修改 `agent.config` 文件中的值，只是其中一种。下面专门探讨agent支持的配置方式。

#### 系统属性(-D)

使用 `-Dskywalking.` + `agent.config配置文件中的key` 即可。例如：

`agent.config` 文件中有一个属性名为 `agent.service_name` ，那么如果使用系统属性的方式，则可以写成

```
java -javaagent:/opt/agent/skywalking-agent.jar -Dskywalking.agent.service_name=你想设置的值 -jar somr-spring-boot.jar
```

#### 代理选项

在JVM参数中的代理路径之后添加属性即可。格式：

```
-javaagent:/path/to/skywalking-agent.jar=[option1]=[value1],[option2]=[value2]
```

例如：

```
java -javaagent:/opt/agent/skywalking-agent.jar=agent.service_name=你想设置的值 -jar somr-spring-boot.jar
```

#### 系统环境变量

`agent.config` 文件中默认的大写值，都可以作为环境变量引用。例如，`agent.config` 中有如下内容

```
agent.service_name=${SW_AGENT_NAME:Your_ApplicationName}
```

这说明Skywalking会读取名为 `SW_AGENT_NAME` 的环境变量。

#### 优先级

```
代理选项` > `系统属性（-D）` > `系统环境变量` > `配置文件
```







