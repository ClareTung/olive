## Jedis/Redisson/Lettuce的区别

### ![JedisRedissonLettuce的区别](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/JedisRedissonLettuce%E7%9A%84%E5%8C%BA%E5%88%AB.png)

### RedisTemplate

```
<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
```

```
opsForHash --> hash操作
opsForList --> list操作 
opsForSet --> set操作
opsForValue --> string操作
opsForZSet --> Zset操作
```

### RedissonClient

```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
	<groupId>org.redisson</groupId>
	<artifactId>redisson</artifactId>
	<version>3.8.2</version>
	<optional>true</optional>
</dependency>
<dependency>
	<groupId>org.redisson</groupId>
	<artifactId>redisson-spring-boot-starter</artifactId>
	<version>LATEST</version>
</dependency>
```

#### 添加配置文件Yaml或者json格式

* redisson-config.yml

```yml
# Redisson 配置
singleServerConfig:
  address: "redis://192.168.1.140:6379"
  password: null
  clientName: null
  database: 15 #选择使用哪个数据库0~15
  idleConnectionTimeout: 10000
  pingTimeout: 1000
  connectTimeout: 10000
  timeout: 3000
  retryAttempts: 3
  retryInterval: 1500
  reconnectionTimeout: 3000
  failedAttempts: 3
  subscriptionsPerConnection: 5
  subscriptionConnectionMinimumIdleSize: 1
  subscriptionConnectionPoolSize: 50
  connectionMinimumIdleSize: 32
  connectionPoolSize: 64
  dnsMonitoringInterval: 5000
  #dnsMonitoring: false

threads: 0
nettyThreads: 0
codec:
  class: "org.redisson.codec.JsonJacksonCodec"
transportMode: "NIO"
```

* redisson-config.json

```json
{
  "singleServerConfig": {
    "idleConnectionTimeout": 10000,
    "pingTimeout": 1000,
    "connectTimeout": 10000,
    "timeout": 3000,
    "retryAttempts": 3,
    "retryInterval": 1500,
    "reconnectionTimeout": 3000,
    "failedAttempts": 3,
    "password": null,
    "subscriptionsPerConnection": 5,
    "clientName": null,
    "address": "redis://192.168.1.140:6379",
    "subscriptionConnectionMinimumIdleSize": 1,
    "subscriptionConnectionPoolSize": 50,
    "connectionMinimumIdleSize": 10,
    "connectionPoolSize": 64,
    "database": 0,
    "dnsMonitoring": false,
    "dnsMonitoringInterval": 5000
  },
  "threads": 0,
  "nettyThreads": 0,
  "codec": null,
  "useLinuxNativeEpoll": false
}
```

#### 读取配置

* 新建读取配置类

```
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redisson() throws IOException {

        // 两种读取方式，Config.fromYAML 和 Config.fromJSON
//        Config config = Config.fromJSON(RedissonConfig.class.getClassLoader().getResource("redisson-config.json"));
        Config config = Config.fromYAML(RedissonConfig.class.getClassLoader().getResource("redisson-config.yml"));
        return Redisson.create(config);
    }
}
```

* 在 application.yml中配置

```yml
spring:
  redis:
    redisson:
      config: classpath:redisson-config.yaml
```

#### Api使用

```java
// 设置字符串
RBucket<String> keyObj = redissonClient.getBucket(key);
keyObj.set(key + "1-v1");
// 获取字符串
RBucket<String> keyObj = redissonClient.getBucket(key);
String s = keyObj.get();

// 存放 Hash
RMap<String, Ur> ss = redissonClient.getMap("UR");
ss.put(ur.getId().toString(), ur);
// hash 查询
RMap<String, Ur> ss = redissonClient.getMap("UR");
Ur ur = ss.get(id);

//        RedissonLock.
RBucket<String> ls_count = redissonClient.getBucket("LS_COUNT");
ls_count.set("300",360000000l, TimeUnit.SECONDS);
```

### 基于注解实现Redis缓存

* 配置类

```java
@EnableCaching
@Configuration
@ConfigurationProperties(prefix = "spring.cache.redis")
public class RedisCacheConfig {

    private Duration timeToLive = Duration.ZERO;

    public void setTimeToLive(Duration timeToLive) {
        this.timeToLive = timeToLive;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        // 解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // 配置序列化（解决乱码的问题）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(timeToLive)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();

        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
        return cacheManager;
    }

}
```

#### 使用示例

```java
@Transactional
@Service
public class ReImpl implements RedisService {

    @Resource
    private CustomerRepo customerRepo;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public static final String REDIS_CUSTOMERS_ONE = "Customers";

    public static final String REDIS_CUSTOMERS_ALL = "allList";

    // =====================================================================使用Spring cahce 注解方式实现缓存
    // ==================================单个操作

    @Override
    @Cacheable(value = "cache:customer", unless = "null == #result",key = "#id")
    public CustomersEntity cacheOne(Integer id) {
        final Optional<CustomersEntity> byId = customerRepo.findById(id);
        return byId.isPresent() ? byId.get() : null;
    }

    @Override
    @Cacheable(value = "cache:customer", unless = "null == #result", key = "#id")
    public CustomersEntity cacheOne2(Integer id) {
        final Optional<CustomersEntity> byId = customerRepo.findById(id);
        return byId.isPresent() ? byId.get() : null;
    }

     // todo 自定义redis缓存的key,
    @Override
    @Cacheable(value = "cache:customer", unless = "null == #result", key = "#root.methodName + '.' + #id")
    public CustomersEntity cacheOne3(Integer id) {
        final Optional<CustomersEntity> byId = customerRepo.findById(id);
        return byId.isPresent() ? byId.get() : null;
    }

    // todo 这里缓存到redis，还有响应页面是String（加了很多转义符\,），不是Json格式
    @Override
    @Cacheable(value = "cache:customer", unless = "null == #result", key = "#root.methodName + '.' + #id")
    public String cacheOne4(Integer id) {
        final Optional<CustomersEntity> byId = customerRepo.findById(id);
        return byId.map(JSONUtil::toJsonStr).orElse(null);
    }

     // todo 缓存json，不乱码已处理好,调整序列化和反序列化
    @Override
    @Cacheable(value = "cache:customer", unless = "null == #result", key = "#root.methodName + '.' + #id")
    public CustomersEntity cacheOne5(Integer id) {
        Optional<CustomersEntity> byId = customerRepo.findById(id);
        return byId.filter(obj -> !StrUtil.isBlankIfStr(obj)).orElse(null);
    }



    // ==================================删除缓存
    @Override
    @CacheEvict(value = "cache:customer", key = "'cacheOne5' + '.' + #id")
    public Object del(Integer id) {
        // 删除缓存后的逻辑
        return null;
    }

    @Override
    @CacheEvict(value = "cache:customer",allEntries = true)
    public void del() {

    }

    @CacheEvict(value = "cache:all",allEntries = true)
    public void delall() {

    }
    // ==================List操作

    @Override
    @Cacheable(value = "cache:all")
    public List<CustomersEntity> cacheList() {
        List<CustomersEntity> all = customerRepo.findAll();
        return all;
    }

    // todo 先查询缓存，再校验是否一致，然后更新操作，比较实用，要清楚缓存的数据格式（明确业务和缓存模型数据）
    @Override
    @CachePut(value = "cache:all",unless = "null == #result",key = "#root.methodName")
    public List<CustomersEntity> cacheList2() {
        List<CustomersEntity> all = customerRepo.findAll();
        return all;
    }

}
```



