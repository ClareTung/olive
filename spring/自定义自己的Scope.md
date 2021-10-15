### 自定义自己的Scope

我们都知道`spring`默认支持的`Scope`只有两种：

- singleton 单例，每次从spring容器中获取到的bean都是同一个对象。
- prototype 多例，每次从spring容器中获取到的bean都是不同的对象。

`spring web`又对`Scope`进行了扩展，增加了：

- RequestScope 同一次请求从spring容器中获取到的bean都是同一个对象。
- SessionScope 同一个会话从spring容器中获取到的bean都是同一个对象。

即便如此，有些场景还是无法满足我们的要求。

比如，我们想在同一个线程中从spring容器获取到的bean都是同一个对象，该怎么办？

这就需要自定义`Scope`了。

第一步实现`Scope`接口：

```
public class ThreadLocalScope implements Scope {

    private static final ThreadLocal THREAD_LOCAL_SCOPE = new ThreadLocal();

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        Object value = THREAD_LOCAL_SCOPE.get();
        if (value != null) {
            return value;
        }

        Object object = objectFactory.getObject();
        THREAD_LOCAL_SCOPE.set(object);
        return object;
    }

    @Override
    public Object remove(String name) {
        THREAD_LOCAL_SCOPE.remove();
        return null;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {

    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }
}
```

第二步将新定义的`Scope`注入到spring容器中：

```
@Component
public class ThreadLocalBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.registerScope("threadLocalScope", new ThreadLocalScope());
    }
}
```

第三步使用新定义的`Scope`：

```
@Scope("threadLocalScope")
@Service
public class CService {

    public void add() {
    }
}
```

