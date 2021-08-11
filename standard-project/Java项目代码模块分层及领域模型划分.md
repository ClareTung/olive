# Java项目代码模块分层

## Alibaba项目模块划分

![alibaba项目规范图](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/alibaba%E9%A1%B9%E7%9B%AE%E8%A7%84%E8%8C%83%E5%9B%BE.png)

* 开放接口层:可直接封装 Service 方法暴露成 RPC 接口;通过 Web 封装成 http 接口;进行 网关安全控制、流量控制等。
* 终端显示层:各个端的模板渲染并执行显示的层。当前主要是 velocity 渲染，JS 渲染， JSP 渲染，移动端展示等。
* Web 层:主要是对访问控制进行转发，各类基本参数校验，或者不复用的业务简单处理等。
* Service 层:相对具体的业务逻辑服务层。
* Manager 层:通用业务处理层，它有如下特征:1. 对第三方平台封装的层，预处理返回结果及转化异常信息;2. 对Service层通用能力的下沉，如缓存方案、中间件通用处理;3. 与DAO层交互，对多个DAO的组合复用。
* DAO 层:数据访问层，与底层 MySQL、Oracle、Hbase 进行数据交互。

## 分层领域模型

* DO（Data Object）：与数据库表结构一一对应，通过DAO层向上传输数据源对象。
* DTO（Data Transfer Object）：数据传输对象，Service或Manager向外传输的对象。
* BO（Business Object）：业务对象。由Service层输出的封装业务逻辑的对象。
* AO（Application Object）：应用对象。在Web层与Service层之间抽象的复用对象模型，极为贴近展示层，复用度不高。
* VO（View Object）：显示层对象，通常是Web向模板渲染引擎层传输的对象。
* Query：数据查询对象，各层接收上层的查询请求。注意超过2个参数的查询封装，禁止使用Map类来传输。

## 实际项目分层结构及领域模型范围划分

![javapl](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/javapl.png)

* Manager层对数据库、缓存、第三方服务的封装
* Controller、Service提供对后台管理服务
* RemoteService、ApiService提供对外接口服务
* Vo适用Controller、Service层
* ForClient适用RemoteService、ApiService层
* Dto适用Controller、Service、RemoteService、ApiService、Manager、Dao层
* Entity适用Dao和Manager层