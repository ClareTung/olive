[TOC]

## OpenFeign核心原理

### 什么 是Feign

* Feign 是声明式 Web 服务客户端
* 不做任何请求处理，通过处理注解相关信息生成 Request，并对调用返回的数据进行解码，从而实现简化 HTTP API 的开发
* 如果要使用 Feign，需要创建一个接口并对其添加 Feign 相关注解，另外 Feign 还支持可插拔编码器和解码器，致力于打造一个轻量级 HTTP 客户端

### Feign和OpenFeign的区别

* Feign最早由Netflix公司进行维护的，后来Netflix 不再对其进行维护，最终 Feign 由社区进行维护，更名为 Openfeign
* SpringCloud封装OpenFeign的Starter

```
<!-- openfeign -->
<dependency>
 	<groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

* Spring Cloud 添加了对 Spring MVC 注解的支持，并支持使用 Spring Web 中默认使用的相同 HttpMessageConverters
* 另外，Spring Cloud 同时集成了 Ribbon 和 Eureka 以及 Spring Cloud LoadBalancer，以在使用 OpenFeign 时提供负载均衡的 HTTP 客户端

### OpenFeign的启动原理

#### 使用@EnableFeignClients启动组件

```java
@EnableFeignClients(basePackages = {"com.olive.springcloud.nacos.consumer.feignclient"})
@EnableDiscoveryClient
@SpringBootApplication
public class NacosConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosConsumerApplication.class, args);
    }
}
```

#### @EnableFeignClients注解

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(FeignClientsRegistrar.class)
public @interface EnableFeignClients {
```

* `@Import`的作用是创建Spring Bean，具体有四种用法：
  * 1.导入普通类，即将普通类变为Spring bean
  * 2.导入@Configuration，即将该注解生效，具体来说就是：将其注解的类成为Spring bean，该类中的@Bean注解的方法也变为Spring bean。注：在应用启动类上使用@ComponentScan也能让@Configuration生效
  * 3.导入ImportSelector的实现类。ImportSelector接口中定义了方法selectImports，它返回字符串数组，里面是类的全路径。使用@Import导入ImportSelector的实现类，就是将selectImports方法返回的类注册为Spring bean
  * 4.导入ImportBeanDefinitionRegistrar的实现类。ImportBeanDefinitionRegistrar接口中定义了方法registerBeanDefinitions，它的功能就是通过BeanDefinitionRegistry实例注册Spring bean

#### FeignClientsRegistrar类

![image-20210928102041903](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210928102041903.png)

* ResourceLoaderAware、EnvironmentAware 为 FeignClientsRegistrar 中两个属性 resourceLoader、environment 赋值
* ImportBeanDefinitionRegistrar 负责动态注入 IOC Bean，分别注入 OpenFeign 配置类、FeignClient Bean

```java
// 资源加载器，可以加载classpath下的所有文件
private ResourceLoader resourceLoader;
// 上下文，可以通过该环境获取当前应用配置属性等
private Environment environment;

@Override
public void registerBeanDefinitions(AnnotationMetadata metadata,
                                    BeanDefinitionRegistry registry) {
    // 注册@EnableFeignClients提供的自定义配置类中的相关Bean实例
    registerDefaultConfiguration(metadata, registry);
    // 扫描package，注册被@FeignClient修饰的接口类为IOC Bean
    registerFeignClients(metadata, registry);
}


```

* 添加全局配置，registerDefaultConfiguration方法的流程如下：

```java
private void registerDefaultConfiguration(AnnotationMetadata metadata,
			BeanDefinitionRegistry registry) {
    // 1.获取 @EnableFeignClients 注解上的属性以及对应 Value
	Map<String, Object> defaultAttrs = metadata
				.getAnnotationAttributes(EnableFeignClients.class.getName(), true);

		if (defaultAttrs != null && defaultAttrs.containsKey("defaultConfiguration")) {
			String name;
			if (metadata.hasEnclosingClass()) {
				name = "default." + metadata.getEnclosingClassName();
			}
			else {
				name = "default." + metadata.getClassName();
			}
            // 2.生成 FeignClientSpecification（存储 Feign 中的配置类） 对应的构造器 BeanDefinitionBuilder
            // 3.FeignClientSpecification Bean 名称为 default. + @EnableFeignClients 修饰类全限定名称 + FeignClientSpecification
            // 4.@EnableFeignClients defaultConfiguration 默认为 {}，如果没有相关配置，默认使用 FeignClientsConfiguration 并结合 name 填充到 FeignClientSpecification，最终注册为 IOC Bean
			registerClientConfiguration(registry, name,
					defaultAttrs.get("defaultConfiguration"));
		}
	}
```

* 注册 FeignClient 接口，将重点放在 registerFeignClients 上，该方法主要就是将修饰了 @FeignClient 的接口注册为 IOC Bean
  * 1.扫描 @EnableFeignClients 注解，如果有 clients，则加载指定接口，为空则根据 scanner 规则扫描出修饰了 @FeignClient 的接口
  * 2.获取 @FeignClient 上对应的属性，根据 configuration 属性去创建接口级的 FeignClientSpecification 配置类 IOC Bean
  * 3.将 @FeignClient 的属性设置到 FeignClientFactoryBean 对象上，并注册 IOC Bean
* @FengnClient 修饰的接口实际上使用了 Spring 的代理工厂生成代理类，所以这里会把修饰了 @FeignClient 接口的 BeanDefinition 设置为 FeignClientFactoryBean 类型，而 FeignClientFactoryBean 继承自 FactoryBean
* 也就是说，当我们定义 @FeignClient 修饰接口时，注册到 IOC 容器中 Bean 类型变成了 FeignClientFactoryBean
* 在 Spring 中，FactoryBean 是一个工厂 Bean，用来创建代理 Bean。工厂 Bean 是一种特殊的 Bean，对于需要获取 Bean 的消费者而言，它是不知道 Bean 是普通 Bean 或是工厂 Bean 的。工厂 Bean 返回的实例不是工厂 Bean 本身，而是会返回执行了工厂 Bean 中 FactoryBean#getObject 逻辑的实例

### OpenFeign的工作原理

* OpenFeign的工作原理，核心是围绕`@FeignClient`修饰的接口，如何发送和接收Http网络请求
* @FeignClient修饰的接口最终填充到IOC容器的类型是FeignClientFactoryBean

![image-20210928104616215](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210928104616215.png)

* FeignClientFactoryBean 都有哪些特征：
  * 1.它会在类初始化时执行一段逻辑，依据 Spring InitializingBean 接口
  * 2.如果它被别的类 @Autowired 进行注入，返回的不是它本身，而是 FactoryBean#getObject 返回的类，依据 Spring FactoryBean 接口
  * 3.它能够获取 Spring 上下文对象，依据 Spring ApplicationContextAware 接口

```java
@Override
public void afterPropertiesSet() {
    // 断言了两个字段不能为空
    Assert.hasText(contextId, "Context id must be set");
    Assert.hasText(name, "Name must be set");
}

// ApplicationContextAware，获取上下文对象赋值到局部变量里

// 重点方法
@Override
public Object getObject() throws Exception {
    return getTarget();
}

<T> T getTarget() {
    // 从IOC容器中获取FeignContext
    FeignContext context = applicationContext.getBean(FeignContext.class);
    // 通过context创建Feign构造器
    Feign.Builder builder = feign(context);
    
    /*
     1.注入@FeignClient 服务时，其实注入的是 FactoryBean#getObject 返回代理工厂对象
	2.通过 IOC 容器获取 FeignContext 上下文
	3.创建 Feign.Builder 对象时会创建 Feign 服务对应的子容器
	4.从子容器中获取日志工厂、编码器、解码器等 Bean
	5.为 Feign.Builder 设置配置，比如超时时间、日志级别等属性，每一个服务都可以个性化设置
    */

    if (!StringUtils.hasText(url)) { // 查看 @FeignClient url属性是否为空
        if (!name.startsWith("http")) { // 如果name开头不是http，会进行拼接
            url = "http://" + name; 
        }
        else {
            url = name;
        }
        url += cleanPath();
        return (T) loadBalance(builder, context, // type就是标记@FeignClient接口
                               // 将接口类型，名称（@FeignClient name），以及url包装成HardCodedTarget
                               new HardCodedTarget<>(type, name, url));
    }
    if (StringUtils.hasText(url) && !url.startsWith("http")) {
        url = "http://" + url;
    }
    String url = this.url + cleanPath();
    Client client = getOptional(context, Client.class);
    if (client != null) {
        if (client instanceof LoadBalancerFeignClient) {
            // not load balancing because we have a url,
            // but ribbon is on the classpath, so unwrap
            client = ((LoadBalancerFeignClient) client).getDelegate();
        }
        if (client instanceof FeignBlockingLoadBalancerClient) {
            // not load balancing because we have a url,
            // but Spring Cloud LoadBalancer is on the classpath, so unwrap
            client = ((FeignBlockingLoadBalancerClient) client).getDelegate();
        }
        builder.client(client);
    }
    Targeter targeter = get(context, Targeter.class);
    return (T) targeter.target(this, builder, context,
                               new HardCodedTarget<>(type, name, url));
}
```

```java
protected Feign.Builder feign(FeignContext context) {
    // 涉及父子容器概念
    FeignLoggerFactory loggerFactory = get(context, FeignLoggerFactory.class);
    Logger logger = loggerFactory.create(type); // 创建Logger

    // @formatter:off
    Feign.Builder builder = get(context, Feign.Builder.class)
        // required values
        .logger(logger)
        .encoder(get(context, Encoder.class))
        .decoder(get(context, Decoder.class))
        .contract(get(context, Contract.class));
    // @formatter:on

    // configureFeign 方法主要进行一些配置赋值，比如超时、重试、404 配置等
    configureFeign(context, builder);

    return builder;
}

// 初始化父子容器，feign方法里日志工厂、编码、解码等类均是通过get(...)方法得到
protected <T> T get(FeignContext context, Class<T> type) {
    T instance = context.getInstance(contextId, type);
    if (instance == null) {
        throw new IllegalStateException(
            "No bean found of type " + type + " for " + contextId);
    }
    return instance;
}

public <T> T getInstance(String name, Class<T> type) {
    // 获得context实例
    AnnotationConfigApplicationContext context = getContext(name);
    if (BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context,
                                                            type).length > 0) {
        // 通过context获取type类型Bean实例
        return context.getBean(type);
    }
    return null;
}

// 涉及Spring父子容器的概念，默认子容器Map为空，获取不到服务名对应context则新建
protected AnnotationConfigApplicationContext getContext(String name) {
    if (!this.contexts.containsKey(name)) {
        synchronized (this.contexts) {
            // private Map<String, AnnotationConfigApplicationContext> contexts = new ConcurrentHashMap<>();
            // 默认 contexts为空，所以会走createContext创建子容器
            if (!this.contexts.containsKey(name)) { // 根据name服务名{@FeignClient中的name}从contexts获取Spring子容器
                this.contexts.put(name, createContext(name));
            }
        }
    }
    return this.contexts.get(name);
}

protected AnnotationConfigApplicationContext createContext(String name) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    if (this.configurations.containsKey(name)) {
        for (Class<?> configuration : this.configurations.get(name)
             .getConfiguration()) {
            context.register(configuration);
        }
    }
    for (Map.Entry<String, C> entry : this.configurations.entrySet()) {
        if (entry.getKey().startsWith("default.")) {
            for (Class<?> configuration : entry.getValue().getConfiguration()) {
                context.register(configuration);
            }
        }
    }
    // 注册了一个 FeignClientsConfiguration 类型的 Bean 
    context.register(PropertyPlaceholderAutoConfiguration.class,
                     this.defaultConfigType);
    context.getEnvironment().getPropertySources().addFirst(new MapPropertySource(
        this.propertySourceName,
        Collections.<String, Object>singletonMap(this.propertyName, name)));
    if (this.parent != null) {
        // Uses Environment from parent as well as beans
        // 设置父容器，子容器不存在就去父容器查找
        context.setParent(this.parent);
        // jdk11 issue
        // https://github.com/spring-cloud/spring-cloud-netflix/issues/3101
        context.setClassLoader(this.parent.getClassLoader());
    }
    context.setDisplayName(generateDisplayName(name));
    context.refresh();
    return context;
}

// @FeignClient注解中使用的是name而不是url，会执行负载均衡策略
// Client： Feign 发送请求以及接收响应等都是由 Client 完成，该类默认 Client.Default，另外支持 HttpClient、OkHttp 等客户端
protected <T> T loadBalance(Feign.Builder builder, FeignContext context,
                            HardCodedTarget<T> target) {
    Client client = getOptional(context, Client.class); // LoadBalancerFeignClient
    if (client != null) {
        builder.client(client);
        Targeter targeter = get(context, Targeter.class); // HystrixTargeter
        return targeter.target(this, builder, context, target);
    }

    throw new IllegalStateException(
        "No Feign Client for loadBalancing defined. Did you forget to include spring-cloud-starter-netflix-ribbon?");
}

// 
class HystrixTargeter implements Targeter {

	@Override
	public <T> T target(FeignClientFactoryBean factory, Feign.Builder feign,
			FeignContext context, Target.HardCodedTarget<T> target) {
		if (!(feign instanceof feign.hystrix.HystrixFeign.Builder)) {
             // 执行到此处
			return feign.target(target);
		}
		feign.hystrix.HystrixFeign.Builder builder = (feign.hystrix.HystrixFeign.Builder) feign;
		String name = StringUtils.isEmpty(factory.getContextId()) ? factory.getName()
				: factory.getContextId();
		SetterFactory setterFactory = getOptional(name, context, SetterFactory.class);
		if (setterFactory != null) {
			builder.setterFactory(setterFactory);
		}
		Class<?> fallback = factory.getFallback();
		if (fallback != void.class) {
			return targetWithFallback(name, context, target, builder, fallback);
		}
		Class<?> fallbackFactory = factory.getFallbackFactory();
		if (fallbackFactory != void.class) {
			return targetWithFallbackFactory(name, context, target, builder,
					fallbackFactory);
		}

		return feign.target(target);
	}
}

//
public <T> T target(Target<T> target) {
    return build().newInstance(target);
}

public Feign build() {
        Client client = Capability.enrich(this.client, capabilities);
        Retryer retryer = Capability.enrich(this.retryer, capabilities);
        List<RequestInterceptor> requestInterceptors = this.requestInterceptors.stream()
            .map(ri -> Capability.enrich(ri, capabilities))
            .collect(Collectors.toList());
        Logger logger = Capability.enrich(this.logger, capabilities);
        Contract contract = Capability.enrich(this.contract, capabilities);
        Options options = Capability.enrich(this.options, capabilities);
        Encoder encoder = Capability.enrich(this.encoder, capabilities);
        Decoder decoder = Capability.enrich(this.decoder, capabilities);
        InvocationHandlerFactory invocationHandlerFactory =
            Capability.enrich(this.invocationHandlerFactory, capabilities);
        QueryMapEncoder queryMapEncoder = Capability.enrich(this.queryMapEncoder, capabilities);

        SynchronousMethodHandler.Factory synchronousMethodHandlerFactory =
            new SynchronousMethodHandler.Factory(client, retryer, requestInterceptors, logger,
                                                 logLevel, decode404, closeAfterDecode, propagationPolicy, forceDecoding);
        ParseHandlersByName handlersByName =
            new ParseHandlersByName(contract, options, encoder, decoder, queryMapEncoder,
                                    errorDecoder, synchronousMethodHandlerFactory);
        // 创建反射类 ReflectiveFeign，然后执行创建实例类
        return new ReflectiveFeign(handlersByName, invocationHandlerFactory, queryMapEncoder);
    }
}   

// newInstance 方法对 @FeignClient 修饰的接口中 SpringMvc 等配置进行解析转换，对接口类中的方法进行归类，生成动态代理类
/*
1.处理 @FeignCLient 注解（SpringMvc 注解等）封装为 MethodHandler 包装类
2.遍历接口中所有方法，过滤 Object 方法，并将默认方法以及 FeignClient 方法分类
3.创建动态代理对应的 InvocationHandler 并创建 Proxy 实例
4.接口内 default 方法 绑定动态代理类
*/
public <T> T newInstance(Target<T> target) {
    // 将修饰了@FeignClient的接口方法封装为方法处理器，包括SpringMvc注解逻辑处理
    Map<String, MethodHandler> nameToHandler = targetToHandlersByName.apply(target);
    // 接口方法对应的MethodHandler, MethodHandler 将方法参数、方法返回值、参数集合、请求类型、请求路径进行解析存储
    Map<Method, MethodHandler> methodToHandler = new LinkedHashMap<Method, MethodHandler>();
    // 添加JDK8以后出现的接口中默认方法
    List<DefaultMethodHandler> defaultMethodHandlers = new LinkedList<DefaultMethodHandler>();

    // 1.如果是Object方法跳过 2.default方法添加defaultMethodHandlers 3.否则添加methodToHandler
    for (Method method : target.type().getMethods()) {
        if (method.getDeclaringClass() == Object.class) {
            continue;
        } else if (Util.isDefault(method)) {
            DefaultMethodHandler handler = new DefaultMethodHandler(method);
            defaultMethodHandlers.add(handler);
            methodToHandler.put(method, handler);
        } else {
            methodToHandler.put(method, nameToHandler.get(Feign.configKey(target.type(), method)));
        }
    }
    // 根据target, methodToHandler创建InvocationHandler
    InvocationHandler handler = factory.create(target, methodToHandler);
    // 根据JDK proxy创建动态代理类
    T proxy = (T) Proxy.newProxyInstance(target.type().getClassLoader(),
                                         new Class<?>[] {target.type()}, handler);

    for (DefaultMethodHandler defaultMethodHandler : defaultMethodHandlers) {
        defaultMethodHandler.bindTo(proxy);
    }
    return proxy;
}
```

* 在调用 @FeignClient 接口时，会被 FeignInvocationHandler#invoke 拦截，并在动态代理方法中执行下述逻辑:
  * 1.接口注解信息封装为 HTTP Request
  * 2.通过 Ribbon 获取服务列表，并对服务列表进行负载均衡调用（服务名转换为 ip+port）
  * 3.请求调用后，将返回的数据封装为 HTTP Response，继而转换为接口中的返回类型

```java 
// SynchronousMethodHandler
@Override
public Object invoke(Object[] argv) throws Throwable {
    // 构建Request模板类
    RequestTemplate template = buildTemplateFromArgs.create(argv);
    // 存放连接、超时时间配置类
    Options options = findOptions(argv);
    // 失败重试策略类
    Retryer retryer = this.retryer.clone();
    while (true) {
        try {
            return executeAndDecode(template, options);
        } catch (RetryableException e) {
            try {
                retryer.continueOrPropagate(e);
            } catch (RetryableException th) {
                Throwable cause = th.getCause();
                if (propagationPolicy == UNWRAP && cause != null) {
                    throw cause;
                } else {
                    throw th;
                }
            }
            if (logLevel != Logger.Level.NONE) {
                logger.logRetry(metadata.configKey(), logLevel);
            }
            continue;
        }
    }
}

// AbstractLoadBalancerAwareClient
public T executeWithLoadBalancer(final S request, final IClientConfig requestConfig) throws ClientException {
    LoadBalancerCommand<T> command = buildLoadBalancerCommand(request, requestConfig);

    try {
        return command.submit(
            // 执行远端调用逻辑中使用到了 Rxjava （响应式编程），可以看到通过底层获取 server 后将服务名称转变为 ip+port 的方式 
            // 网络调用默认使用 HttpURLConnection，可以配置使用 HttpClient 或者 OkHttp 调用远端服务后，再将返回值解析正常返回
            new ServerOperation<T>() {
                @Override
                public Observable<T> call(Server server) {
                    URI finalUri = reconstructURIWithServer(server, request.getUri());
                    S requestForServer = (S) request.replaceUri(finalUri);
                    try {
                        return Observable.just(AbstractLoadBalancerAwareClient.this.execute(requestForServer, requestConfig));
                    } 
                    catch (Exception e) {
                        return Observable.error(e);
                    }
                }
            })
            .toBlocking()
            .single();
    }
```

![image-20210928141643074](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210928141643074.png)

![微信图片编辑_20210928141841](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87%E7%BC%96%E8%BE%91_20210928141841.jpg)

#### FeignContext是怎么注册到Spring容器里的

![image-20210928111948780](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210928111948780.png)

* SpringBoot使用自动装配功能，FeignContext就是在FeignAutoConfiguration中被创建的

### OpenFeign如何负载均衡

* OpenFeign内部集成了Ribbon

```java
// LoadBalancerCommand
private Observable<Server> selectServer() {
    return Observable.create(new OnSubscribe<Server>() {
        @Override
        public void call(Subscriber<? super Server> next) {
            try {
                // 获取可用服务
                Server server = loadBalancerContext.getServerFromLoadBalancer(loadBalancerURI, loadBalancerKey);   
                next.onNext(server);
                next.onCompleted();
            } catch (Exception e) {
                next.onError(e);
            }
        }
    });
}

// LoadBalancerContext，通过chooseServer选择一个健康实例返回，通过返回的Server替换URL中的服务名，最后使用网络调用服务进行远端调用
Server svc = lb.chooseServer(loadBalancerKey);
```

### 总结

* 1.通过 @EnableFeignCleints 注解启动 Feign Starter 组件
* 2.Feign Starter 在项目启动过程中注册全局配置，扫描包下所有的 @FeignClient 接口类，并进行注册 IOC 容器
* 3.@FeignClient 接口类被注入时，通过 FactoryBean#getObject 返回动态代理类
* 4.接口被调用时被动态代理类逻辑拦截，将 @FeignClient 请求信息通过编码器生成 Request
* 5.交由 Ribbon 进行负载均衡，挑选出一个健康的 Server 实例
* 6.继而通过 Client 携带 Request 调用远端服务返回请求响应
* 7.通过解码器生成 Response 返回客户端，将信息流解析成为接口返回数据



