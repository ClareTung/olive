## spring mvc拦截器

spring mvc拦截器根spring拦截器相比，它里面能够获取`HttpServletRequest`和`HttpServletResponse` 等web对象实例。

spring mvc拦截器的顶层接口是：`HandlerInterceptor`，包含三个方法：

- preHandle 目标方法执行前执行
- postHandle 目标方法执行后执行
- afterCompletion 请求完成时执行

为了方便我们一般情况会用HandlerInterceptor接口的实现类`HandlerInterceptorAdapter`类。

假如有权限认证、日志、统计的场景，可以使用该拦截器。

第一步，继承`HandlerInterceptorAdapter`类定义拦截器：

```
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestUrl = request.getRequestURI();
        if (checkAuth(requestUrl)) {
            return true;
        }

        return false;
    }

    private boolean checkAuth(String requestUrl) {
        System.out.println("===权限校验===");
        return true;
    }
}
```

第二步，将该拦截器注册到spring容器：

```
@Configuration
public class WebAuthConfig extends WebMvcConfigurerAdapter {
 
    @Bean
    public AuthInterceptor getAuthInterceptor() {
        return new AuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor());
    }
}
```

第三步，在请求接口时spring mvc通过该拦截器，能够自动拦截该接口，并且校验权限。

该拦截器其实相对来说，比较简单，可以在`DispatcherServlet`类的`doDispatch`方法中看到调用过程：

![image-20211015180530298](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20211015180530298.png)

顺便说一句，这里只讲了spring mvc的拦截器，并没有讲spring的拦截器，是因为我有点小私心，后面就会知道。





