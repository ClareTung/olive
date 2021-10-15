## Enable开关

不知道你有没有用过`Enable`开头的注解，比如：`EnableAsync`、`EnableCaching`、`EnableAspectJAutoProxy`等，这类注解就像开关一样，只要在`@Configuration`定义的配置类上加上这类注解，就能开启相关的功能。

是不是很酷？

让我们一起实现一个自己的开关：

第一步，定义一个LogFilter：

```
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("记录请求日志");
        chain.doFilter(request, response);
        System.out.println("记录响应日志");
    }

    @Override
    public void destroy() {
        
    }
}
```

第二步，注册LogFilter：

```
@ConditionalOnWebApplication
public class LogFilterWebConfig {

    @Bean
    public LogFilter timeFilter() {
        return new LogFilter();
    }
}
```

注意，这里用了`@ConditionalOnWebApplication`注解，没有直接使用`@Configuration`注解。

第三步，定义开关`@EnableLog`注解：

```
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LogFilterWebConfig.class)
public @interface EnableLog {

}
```

第四步，只需在`springboot`启动类加上`@EnableLog`注解即可开启LogFilter记录请求和响应日志的功能。