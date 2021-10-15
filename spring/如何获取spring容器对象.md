### 如何获取spring容器对象

1. 实现BeanFactoryAware接口
2. 实现ApplicationContextAware接口
3. 实现ApplicationListener接口

#### 1.实现BeanFactoryAware接口

* 实现`BeanFactoryAware`接口，然后重写`setBeanFactory`方法，就能从该方法中获取到spring容器对象。

```
@Service
public class PersonService implements BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void add() {
        Person person = (Person) beanFactory.getBean("person");
    }
}
```

#### 2.实现ApplicationContextAware接口

* 实现`ApplicationContextAware`接口，然后重写`setApplicationContext`方法，也能从该方法中获取到spring容器对象。

```
@Service
public class PersonService2 implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void add() {
        Person person = (Person) applicationContext.getBean("person");
    }

}
```

#### 3.实现ApplicationListener接口

* 实现`ApplicationListener`接口，需要注意的是该接口接收的泛型是`ContextRefreshedEvent`类，然后重写`onApplicationEvent`方法，也能从该方法中获取到spring容器对象。

```
@Service
public class PersonService3 implements ApplicationListener<ContextRefreshedEvent> {
    private ApplicationContext applicationContext;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        applicationContext = event.getApplicationContext();
    }

    public void add() {
        Person person = (Person) applicationContext.getBean("person");
    }

}
```

* 此外，不得不提一下`Aware`接口，它其实是一个空接口，里面不包含任何方法。
* 它表示已感知的意思，通过这类接口可以获取指定对象，比如：
  - 通过BeanFactoryAware获取BeanFactory
  - 通过ApplicationContextAware获取ApplicationContext
  - 通过BeanNameAware获取BeanName等
* Aware接口是很常用的功能，目前包含如下功能：

![image-20211015173749784](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20211015173749784.png)

