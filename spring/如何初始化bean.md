### 如何初始化bean

spring中支持3种初始化bean的方法：

- xml中指定init-method方法
- 使用@PostConstruct注解
- 实现InitializingBean接口

第一种方法太古老了，现在用的人不多，具体用法就不介绍了。

#### **1.使用@PostConstruct注解**

```
@Service
public class AService {

    @PostConstruct
    public void init() {
        System.out.println("===初始化===");
    }
}
```

在需要初始化的方法上增加`@PostConstruct`注解，这样就有初始化的能力。

#### **2.实现InitializingBean接口**

```
@Service
public class BService implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("===初始化===");
    }
}
```

实现`InitializingBean`接口，重写`afterPropertiesSet`方法，该方法中可以完成初始化功能。

这里顺便抛出一个有趣的问题：`init-method`、`PostConstruct` 和 `InitializingBean` 的执行顺序是什么样的？

决定他们调用顺序的关键代码在`AbstractAutowireCapableBeanFactory`类的`initializeBean`方法中。

![image-20211015174512902](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20211015174512902.png)

这段代码中会先调用`BeanPostProcessor`的`postProcessBeforeInitialization`方法，而`PostConstruct`是通过`InitDestroyAnnotationBeanPostProcessor`实现的，它就是一个`BeanPostProcessor`，所以`PostConstruct`先执行。

而`invokeInitMethods`方法中的代码：

![image-20211015174644497](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20211015174644497.png)

决定了先调用`InitializingBean`，再调用`init-method`。

所以得出结论，他们的调用顺序是：

![image-20211015174717022](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20211015174717022.png)



