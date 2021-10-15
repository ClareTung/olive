## RestTemplate拦截器

我们使用`RestTemplate`调用远程接口时，有时需要在`header`中传递信息，比如：traceId，source等，便于在查询日志时能够串联一次完整的请求链路，快速定位问题。

这种业务场景就能通过`ClientHttpRequestInterceptor`接口实现，具体做法如下：

第一步，实现`ClientHttpRequestInterceptor`接口：

```
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().set("traceId", MdcUtil.get());
        return execution.execute(request, body);
    }
}
```

第二步，定义配置类：

```
@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(restTemplateInterceptor()));
        return restTemplate;
    }

    @Bean
    public RestTemplateInterceptor restTemplateInterceptor() {
        return new RestTemplateInterceptor();
    }
}
```

其中MdcUtil其实是利用`MDC`工具在`ThreadLocal`中存储和获取traceId

```
public class MdcUtil {

    private static final String TRACE_ID = "TRACE_ID";

    public static String get() {
        return MDC.get(TRACE_ID);
    }

    public static void add(String value) {
        MDC.put(TRACE_ID, value);
    }
}
```

当然，这个例子中没有演示MdcUtil类的add方法具体调的地方，我们可以在filter中执行接口方法之前，生成traceId，调用MdcUtil类的add方法添加到`MDC`中，然后在同一个请求的其他地方就能通过MdcUtil类的get方法获取到该traceId。