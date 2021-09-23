[TOC]

## Spring-循环依赖

### 什么是循环依赖

* 循环依赖有自身依赖自身，互相循环依赖
* 循环依赖的本质是，A的创建依赖B，B的创建依赖A，但AB互相没法解耦，导致最终创建失败
* Spring中循环依赖解决方法：构造函数注入，prototype （原型）注入，setter循环依赖注入
* Spring解决循环依赖是有前置条件的
  1. 出现循环依赖的Bean必须要是单例
  2. 依赖注入的方式不能全是构造器注入的方式

### 源码分析

#### org.springframework.beans.factory.support.AbstractBeanFactory

```java
@Override
public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
 return doGetBean(name, requiredType, null, false);
}
```

* 从 getBean 进入后，获取 bean 的操作会进入到 doGetBean
* 之所以这样包装一层，是因为 doGetBean 有很多不同入参的重载方法，方便外部操作

```java
protected <T> T doGetBean(
  final String name, final Class<T> requiredType, final Object[] args, boolean typeCheckOnly)
  throws BeansException {
 
  // 从缓存中获取 bean 实例
 Object sharedInstance = getSingleton(beanName);
 
   // mbd.isSingleton() 用于判断 bean 是否是单例模式
   if (mbd.isSingleton()) {
     // 获取 bean 实例
    sharedInstance = getSingleton(beanName, new ObjectFactory<Object>() {
     @Override
     public Object getObject() throws BeansException {
      try {
        // 创建 bean 实例，createBean 返回的 bean 实例化好的
       return createBean(beanName, mbd, args);
      }
      catch (BeansException ex) {
       destroySingleton(beanName);
       throw ex;
      }
     }
    });
    // 后续的处理操作
    bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
   }
   
 // ...

  // 返回 bean 实例
 return (T) bean;
}
```

* getSingleton 先判断是否有实例对象，对于第一次进入是肯定没有对象的，要继续往下走
* 在判断 mbd.isSingleton() 单例以后，开始使用基于 ObjectFactory 包装的方式创建 createBean，进入后核心逻辑是开始执行 doCreateBean 操作

#### org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory

* AbstractAutowireCapableBeanFactory.createBean()中调用了doCreateBean()

```java
protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, final Object[] args)
  throws BeanCreationException {
 
   // 创建 bean 实例，并将 bean 实例包装到 BeanWrapper 对象中返回
  instanceWrapper = createBeanInstance(beanName, mbd, args);
 
  // 添加 bean 工厂对象到 singletonFactories 缓存中
  addSingletonFactory(beanName, new ObjectFactory<Object>() {
   @Override
   public Object getObject() throws BeansException {
     // 获取原始对象的早期引用，在 getEarlyBeanReference 方法中，会执行 AOP 相关逻辑。若 bean 未被 AOP 拦截，getEarlyBeanReference 原样返回 bean。
    return getEarlyBeanReference(beanName, mbd, bean);
   }
  });
  
 try {
   // 填充属性，解析依赖关系
  populateBean(beanName, mbd, instanceWrapper);
  if (exposedObject != null) {
   exposedObject = initializeBean(beanName, exposedObject, mbd);
  }
 }
 
 // 返回 bean 实例
 return exposedObject;
}
```

- 在 doCreateBean 方法中包括的内容较多，但核心主要是创建实例、加入缓存以及最终进行属性填充，属性填充就是把一个 bean 的各个属性字段涉及到的类填充进去。
- `createBeanInstance`，创建 bean 实例，并将 bean 实例包装到 BeanWrapper 对象中返回
- `addSingletonFactory`，添加 bean 工厂对象到 singletonFactories 缓存中
- `getEarlyBeanReference`，获取原始对象的早期引用，在 getEarlyBeanReference 方法中，会执行 AOP 相关逻辑。若 bean 未被 AOP 拦截，getEarlyBeanReference 原样返回 bean。
- `populateBean`，填充属性，解析依赖关系。也就是从这开始去找寻 A 实例中属性 B，紧接着去创建 B 实例，最后在返回回来
- org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator.getEarlyBeanReference

```java
public Object getEarlyBeanReference(Object bean, String beanName) {
    Object cacheKey = getCacheKey(bean.getClass(), beanName);
    this.earlyProxyReferences.put(cacheKey, bean);
    // 如果需要代理，返回一个代理对象，不需要代理，直接返回当前传入的这个bean对象
    return wrapIfNecessary(bean, beanName, cacheKey);
}
```

* org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton

```java
protected Object getSingleton(String beanName, boolean allowEarlyReference) {
  // 从 singletonObjects 获取实例，singletonObjects 是成品 bean
 Object singletonObject = this.singletonObjects.get(beanName);
 // 判断 beanName ，isSingletonCurrentlyInCreation 对应的 bean 是否正在创建中
 if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
  synchronized (this.singletonObjects) {
    // 从 earlySingletonObjects 中获取提前曝光未成品的 bean
   singletonObject = this.earlySingletonObjects.get(beanName);
   if (singletonObject == null && allowEarlyReference) {
     // 获取相应的 bean 工厂
    ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
    if (singletonFactory != null) {
      // 提前曝光 bean 实例，主要用于解决AOP循环依赖
     singletonObject = singletonFactory.getObject();
     
     // 将 singletonObject 放入缓存中，并将 singletonFactory 从缓存中移除
     this.earlySingletonObjects.put(beanName, singletonObject);
     this.singletonFactories.remove(beanName);
    }
   }
  }
 }
 return (singletonObject != NULL_OBJECT ? singletonObject : null);
}
```

- `singletonObjects.get(beanName)`，从 singletonObjects 获取实例，singletonObjects 是成品 bean
- `isSingletonCurrentlyInCreation`，判断 beanName ，isSingletonCurrentlyInCreation 对应的 bean 是否正在创建中
- `allowEarlyReference`，从 earlySingletonObjects 中获取提前曝光未成品的 bean
- `singletonFactory.getObject()`，提前曝光 bean 实例，主要用于解决AOP循环依赖

### 总结

#### Spring是如何解决的循环依赖

* Spring通过三级缓存解决了循环依赖，其中一级缓存为单例池（`singletonObjects`）,二级缓存为早期曝光对象`earlySingletonObjects`，三级缓存为早期曝光对象工厂（`singletonFactories`）。当A、B两个类发生循环引用时，在A完成实例化后，就使用实例化后的对象去创建一个对象工厂，并添加到三级缓存中，如果A被AOP代理，那么通过这个工厂获取到的就是A代理后的对象，如果A没有被AOP代理，那么这个工厂获取到的就是A实例化的对象。当A进行属性注入时，会去创建B，同时B又依赖了A，所以创建B的同时又会去调用getBean(a)来获取需要的依赖，此时的getBean(a)会从缓存中获取，第一步，先获取到三级缓存中的工厂；第二步，调用对象工工厂的getObject方法来获取到对应的对象，得到这个对象后将其注入到B中。紧接着B会走完它的生命周期流程，包括初始化、后置处理器等。当B创建完后，会将B再注入到A中，此时A再完成它的整个生命周期。至此，循环依赖结束

#### 为什么要使用三级缓存呢？二级缓存能解决循环依赖吗？

* 如果要使用二级缓存解决循环依赖，意味着所有Bean在实例化后就要完成AOP代理，这样违背了Spring设计的原则，Spring在设计之初就是通过`AnnotationAwareAspectJAutoProxyCreator`这个后置处理器来在Bean生命周期的最后一步来完成AOP代理，而不是在实例化后就立马进行AOP代理

![spring循环依赖1](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/spring%E5%BE%AA%E7%8E%AF%E4%BE%9D%E8%B5%961.png)



