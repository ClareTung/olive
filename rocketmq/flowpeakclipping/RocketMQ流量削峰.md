## RocketMQ流量削峰

![mq流量削峰](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/mq%E6%B5%81%E9%87%8F%E5%89%8A%E5%B3%B0.png)

### 总体思路

1. http请求到gateway网关
2. gateway通过RouteLocator配置路由信息转发到mq服务
3. mq服务接收到数据后，将数据发送到对应的topic
4. mq消费者监听消息，拉取消息，进行业务处理，处理完后将msgId作为key，结果作为value存入redis
5. mq服务发送消息的线程发送完消息后挂起，每隔一秒根据msgId去redis查询消费结果

### 服务项目

* 包含技术点
  * SpringBoot
  * SpringCloud
    * Gateway：网关
  * AlibabaCluod
    * Nacos：配置中心，注册中心
  * Nacos
  * RocketMQ
  * Redis

* flowpeakclipping
  * flowpeakclipping-gateway：网关服务
  * flowpeakclipping-mq：mq服务
  * flowpeakclipping-businessService：消费mq消息，进行业务处理

#### flowpeakclipping-gateway：网关服务

* gateway过滤器

```java
@Configuration
public class AccessGatewayFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestUri = request.getPath().pathWithinApplication().value();
        /**
         * 如果需要削峰的请求是需要权限认证，则在网关的全局过滤器拿到jwt，再在全局过滤器中调用auth服务
         * 根据token拿到权限信息返回，然后在判断用户是否拥有请求该路由的权限，没有则返回无权访问，有就执行filer.chain
         * request.getheaders().get("authentication")->authService.getPermission(token)->checkUserPermission(permission,requestUri,user)
         * 这里因为权限服务比较复杂，直接对请求的路由放行
         */
        if (requestUri.contains("/getMessage")) {
            return chain.filter(exchange);//直接放行
        } else {//需要权限认证则走这里,进行权限认证
            return getVoidMono(exchange, new TokenForbiddenResponse("User Forbidden!Does not has Permission!"));
        }
    }

    /**
     * 网关抛异常
     *
     * @param body
     */
    private Mono<Void> getVoidMono(ServerWebExchange serverWebExchange, BaseResponse body) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
        byte[] bytes = JSONObject.toJSONString(body).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(bytes);
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }

}
```

* 将要削峰的请求转发到mq服务

```java
@Bean
public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
        // 这里转发的路由可以弄成动态路由的形式来管理，在数据库中存储
        .route(r -> r.path("/api/businessService/getMessage").filters(f -> f.stripPrefix(2)).uri("lb://fpc-mq"))
        .build();
}
```

#### flowpeakclipping-mq：mq服务

* 将请求的数据发送到mq，并查找消费结果

```java
@PostMapping("/getMessage")
public BaseResponse getMessage(@RequestBody Object jsonData) throws InterruptedException {
    try {
        String jsonString = JSON.toJSONString(jsonData);
        Message msg = new Message(getMessageTopic/*Topic*/,
                                  "*" /*Tag*/,
                                  jsonString.getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */);
        SendResult sendResult = defaultMQProducer.send(msg);
        String msgId = sendResult.getMsgId();
        SendStatus sendStatus = sendResult.getSendStatus();

        // 如果sendStatus是发送成功的状态，则在30秒内尝试在redis根据msgId获取消费者业务处理的结果,15秒后没拿到则返回超时
        int i = 30;
        String consumerResult = null;
        if (StringUtils.equals(sendStatus.toString(), "SEND_OK")) {
            while (i > 0) {
                consumerResult = stringRedisTemplate.opsForValue().get(msgId);
                if (consumerResult != null) {
                    BaseResponse baseResponse = new BaseResponse();
                    baseResponse.setMessage(consumerResult + msgId);

                    return baseResponse;
                }
                Thread.sleep(1000);
                i--;
            }
            return new BaseResponse(500001, "请求返回超时");
        }

        System.out.printf("%s%n", sendStatus);
    } catch (Exception e) {
        e.printStackTrace();
        Thread.sleep(1000);
    }

    return new BaseResponse(500, "生产者发送消息失败");
}
```

#### flowpeakclipping-businessService：消费mq消息，进行业务处理

* 从topic中拉取消息，并进行业务处理，得到结果放入缓存

```java
public void dealBody(List<MessageExt> msgs) {
    // 从消息队列拿到数据进行业务处理
    log.info("拉取消息");

    msgs.stream().forEach(msg -> {
        try {
            String msgStr = new String(msg.getBody(), "utf-8");
            JSONObject jsonObject = JSON.parseObject(msgStr);

            TestBean testBean = new TestBean();
            testBean.setMessage(jsonObject.getString("message"));
            // 这里可以进行业务操作
            String operationResult = testService.operation(testBean);
            // 将业务操作的结果放入redis缓存
            stringRedisTemplate.opsForValue().set(msg.getMsgId(), operationResult);
            log.info(operationResult + msg.getMsgId() + new Date());
        } catch (UnsupportedEncodingException e) {
            log.error("body转字符串解析失败");
        }
    });

}
```

### 测试
* postman请求地址：localhost:7654/api/businessService/getMessage
* 返回结果
```json
{
    "status": 200,
    "message": "RocketMQ流量削峰0AE722B6226018B4AAC212FC96F40001"
}
```
* redis中会存在处理后的结果数据
