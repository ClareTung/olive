## Spring-IOC特性

* IOC（Inversion of Controller），控制反转的核心思想在于，资源的使用不由使用者各自管理，而是交给不使用资源的第三方进行管理。好处是资源是集中管理的，可配置，易维护，同时也降低了双方的依赖度，做到了低耦合。

![springioc](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/springioc.jpg)

### alias

* 用于给bean起别名

### autowire

* 目的：autowire 用于把类中的属性注入交给 Spring 管理
* 在 xml 配置中，有两种方式分别是：手动配置依赖、自动配置依赖，手动的大家基本很常用，自动的配置一般可能更多的对于注解的使用。其实这里的 autowire 和注解有一样的作用，autowire 几个可选项，byName、byType、constructor 等

### factory-method

* 标识静态工厂的工厂方法(工厂方法是静态的)

### factory-bean

* 实例化工厂类，factory-bean、factory-method 需要配合使用，调用的是对应的非静态方法返回实例化结果

### depends-on

* 处理依赖初始化顺序问题

### lookup-method & ApplicationContextAware

* 获取单例下的原型模式，每次获取都要有新的对象产生。
* 使用：其实核心在于 ApplicationContextAware 的使用和 `scope="prototype"` 配置，Spring 内部实现为使用 Cglib 方法，重新生成子类，重写配置的方法和返回对象，达到动态改变的效果

### FactoryBean

```java
public class MyFactoryBean implements FactoryBean<UserDao> {

    @Override
    public UserDao getObject() throws Exception {
        return new UserDao();
    }

    @Override
    public Class<?> getObjectType() {
        return UserDao.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
    
}
```

* 用于生成 Bean 的 Bean，叫 FactoryBean

### BeanPostProcessor

```java
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("初始化前：" + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("初始化后：" + beanName);
        return bean;
    }
    
}
```

* 拿到 Bean 对象初始化前后的动作，做相应的处理
* 使用：BeanPostProcessor 是 Spring 框架的扩展接口类，通过对这个接口的实现，就可以在 Bean 实例化的过程中做相关的动作，比如拦截以后发布到注册中心等。AOP 的操作也是通过 BeanPostProcessor 和 IOC 容器建立起联系

### BeanFactoryAware

```java
public class MyBeanFactoryAware implements BeanFactoryAware {
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        
    }
}
```

* 用于获取运行时 Bean 的配置信息
* 使用：BeanFactoryAware 的实现类可以拿到 beanFactory，也就获取到了bean的上下文信息，此时你想获取一些对象的属性就非常容易了



