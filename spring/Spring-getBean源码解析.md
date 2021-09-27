## Spring中getBean源码解析

### getBean源码全局预览

* **源码位置**：AbstractBeanFactory -> getBean() -> doGetBean()

```java
@Override
public <T> T getBean(String name, Class<T> requiredType) throws BeansException { 
    // getBean 就像你的领导其实没做啥，都在 doGetBean 里
 return doGetBean(name, requiredType, null, false);
}
```

```java
protected <T> T doGetBean(final String name, final Class<T> requiredType, final Object[] args, boolean typeCheckOnly)
  throws BeansException {     
    
 // 处理别名BeanName、处理带&符的工厂BeanName
 final String beanName = transformedBeanName(name);
 Object bean;  

 // 先尝试从缓存中获取Bean实例，这个位置就是三级缓存解决循环依赖的方法
 Object sharedInstance = getSingleton(beanName);   

 if (sharedInstance != null && args == null) {
  if (logger.isDebugEnabled()) {
   if (isSingletonCurrentlyInCreation(beanName)) {
    logger.debug("Returning eagerly cached instance of singleton bean '" + beanName +
      "' that is not fully initialized yet - a consequence of a circular reference");
   }
   else {
    logger.debug("Returning cached instance of singleton bean '" + beanName + "'");
   }
  }        
        
// 1. 如果 sharedInstance 是普通的 Bean 实例，则下面的方法会直接返回
// 2. 如果 sharedInstance 是工厂Bean类型，则需要获取 getObject 方法，可以参考关于 FactoryBean 的实现类 
  bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
 }
 else {
  
 // 循环依赖有三种，setter注入、多实例和构造函数，Spring 只能解决 setter 注入，所以这里是 Prototype 则会抛出异常
  if (isPrototypeCurrentlyInCreation(beanName)) {
   throw new BeanCurrentlyInCreationException(beanName);
  }    

  // 1. 父 bean 工厂存在
  // 2. 当前 bean 不存在于当前bean工厂，则到父工厂查找 bean 实例
  BeanFactory parentBeanFactory = getParentBeanFactory();
  if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
   // 获取 name 对应的 beanName，如果 name 是以 & 开头，则返回 & + beanName
   String nameToLookup = originalBeanName(name);         
            
   // 根据 args 参数是否为空，调用不同的父容器方法获取 bean 实例
   if (args != null) {
    return (T) parentBeanFactory.getBean(nameToLookup, args);
   }
   else {
    return parentBeanFactory.getBean(nameToLookup, requiredType);
   }
  }       

  // 1. typeCheckOnly，用于判断调用 getBean 方法时，是否仅是做类型检查
  // 2. 如果不是只做类型检查，就会调用 markBeanAsCreated 进行记录
  if (!typeCheckOnly) {
   markBeanAsCreated(beanName);
  }
  try {    
    
   // 从容器 getMergedLocalBeanDefinition 获取 beanName 对应的 GenericBeanDefinition，转换为 RootBeanDefinition
   final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName); 
   // 检查当前创建的 bean 定义是否为抽象 bean 定义
   checkMergedBeanDefinition(mbd, beanName, args);
   
   // 处理使用了 depends-on 注解的依赖创建 bean 实例
   String[] dependsOn = mbd.getDependsOn();
   if (dependsOn != null) {
    for (String dep : dependsOn) {   
     // 监测是否存在 depends-on 循环依赖，若存在则会抛出异常
     if (isDependent(beanName, dep)) {
      throw new BeanCreationException(mbd.getResourceDescription(), beanName,
        "Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
     }       
                    
     // 注册依赖记录
     registerDependentBean(dep, beanName);
     try {    
      // 加载 depends-on 依赖（dep 是 depends-on 缩写）
      getBean(dep);
     }
     catch (NoSuchBeanDefinitionException ex) {
      throw new BeanCreationException(mbd.getResourceDescription(), beanName,
        "'" + beanName + "' depends on missing bean '" + dep + "'", ex);
     }
    }
   }  

   // 创建单例 bean 实例
   if (mbd.isSingleton()) {    

    // 把 beanName 和 new ObjectFactory 匿名内部类传入回调
    sharedInstance = getSingleton(beanName, new ObjectFactory<Object>() {
     @Override
     public Object getObject() throws BeansException {
      try {    
                            // 创建 bean
       return createBean(beanName, mbd, args);
      }
      catch (BeansException ex) {
       // 创建失败则销毁
       destroySingleton(beanName);
       throw ex;
      }
     }
    });
    bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
   }      
   // 创建其他类型的 bean 实例
   else if (mbd.isPrototype()) {
    // It's a prototype -> create a new instance.
    Object prototypeInstance = null;
    try {
     beforePrototypeCreation(beanName);
     prototypeInstance = createBean(beanName, mbd, args);
    }
    finally {
     afterPrototypeCreation(beanName);
    }
    bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
   }
   else {
    String scopeName = mbd.getScope();
    final Scope scope = this.scopes.get(scopeName);
    if (scope == null) {
     throw new IllegalStateException("No Scope registered for scope name '" + scopeName + "'");
    }
    try {
     Object scopedInstance = scope.get(beanName, new ObjectFactory<Object>() {
      @Override
      public Object getObject() throws BeansException {
       beforePrototypeCreation(beanName);
       try {
        return createBean(beanName, mbd, args);
       }
       finally {
        afterPrototypeCreation(beanName);
       }
      }
     });
     bean = getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
    }
    catch (IllegalStateException ex) {
     throw new BeanCreationException(beanName,
       "Scope '" + scopeName + "' is not active for the current thread; consider " +
       "defining a scoped proxy for this bean if you intend to refer to it from a singleton",
       ex);
    }
   }
  }
  catch (BeansException ex) {
   cleanupAfterBeanCreationFailure(beanName);
   throw ex;
  }
 }
 // 如果需要类型转换，这里会进行操作
 if (requiredType != null && bean != null && !requiredType.isInstance(bean)) {
  try {
   return getTypeConverter().convertIfNecessary(bean, requiredType);
  }
  catch (TypeMismatchException ex) {
   if (logger.isDebugEnabled()) {
    logger.debug("Failed to convert bean '" + name + "' to required type '" +
      ClassUtils.getQualifiedName(requiredType) + "'", ex);
   }
   throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
  }
 }    

 // 返回 Bean
 return (T) bean;
}
```

综上基本就是 getBean 过程涉及到的核心处理方法，基本包括；

- transformedBeanName，处理别名BeanName、处理带&符的工厂BeanName。
- getSingleton，先尝试从缓存中获取Bean实例，这个位置就是三级缓存解决循环依赖的方法。
- getObjectForBeanInstance，如果 sharedInstance 是普通的 Bean 实例，则下面的方法会直接返回。另外 sharedInstance 是工厂Bean类型，则需要获取 getObject 方法，可以参考关于 FactoryBean 的实现类。
- isPrototypeCurrentlyInCreation，循环依赖有三种，setter注入、多实例和构造函数，Spring 只能解决 setter 注入，所以这里是 Prototype 则会抛出异常。
- getParentBeanFactory，父 bean 工厂存在，当前 bean 不存在于当前bean工厂，则到父工厂查找 bean 实例。
- originalBeanName，获取 name 对应的 beanName，如果 name 是以 & 开头，则返回 & + beanName
- args != null，根据 args 参数是否为空，调用不同的父容器方法获取 bean 实例
- !typeCheckOnly，typeCheckOnly，用于判断调用 getBean 方法时，是否仅是做类型检查，如果不是只做类型检查，就会调用 markBeanAsCreated 进行记录
- mbd.getDependsOn，处理使用了 depends-on 注解的依赖创建 bean 实例
- isDependent，监测是否存在 depends-on 循环依赖，若存在则会抛出异常
- registerDependentBean，注册依赖记录
- getBean(dep)，加载 depends-on 依赖（dep 是 depends-on 缩写）
- mbd.isSingleton()，创建单例 bean 实例
- mbd.isPrototype()，创建其他类型的 bean 实例
- return (T) bean，返回 Bean 实例

### beanName转换操作

* **处理 & 符：transformedBeanName() -> BeanFactoryUtils.transformedBeanName(name)**

```java
public static String transformedBeanName(String name) {
 Assert.notNull(name, "'name' must not be null");
 String beanName = name;
 while (beanName.startsWith(BeanFactory.FACTORY_BEAN_PREFIX)) {
  beanName = beanName.substring(BeanFactory.FACTORY_BEAN_PREFIX.length());
 }
 return beanName;
}
```

- 使用 FactoryBean 创建出的对象，会在 DefaultListableBeanFactory 初始化的时候，使用 getBean(FACTORY_BEAN_PREFIX + beanName) 给 beanName 加上 & `(String FACTORY_BEAN_PREFIX = "&")`
- 这里是使用 while 循环逐步的把 & 去掉，只要截取首个字符是 & 符，就继续循环截取。`&&&userService -> &&userService -> &userService -> userService`

* **别名转换：transformedBeanName() -> canonicalName**

```java
public String canonicalName(String name) {
 String canonicalName = name;
 // Handle aliasing...
 String resolvedName;
 do {
  resolvedName = this.aliasMap.get(canonicalName);
  if (resolvedName != null) {
   canonicalName = resolvedName;
  }
 }
 while (resolvedName != null);
 return canonicalName;
}
```

* 首先 Spring 对 Bean 的存放并不会使用别名作为Map中的key，所以遇到所有别名获取 Bean 都需要查到对应原来名字，才可以。
* do...while 循环会依次像链条一样不断的寻找别名对应的名称，直到当前这个名称没有别名了，就返回对应 BeanName

### depends-on 依赖 Bean

* **AbstractBeanFactory -> isDependent(beanName, dep) -> DefaultSingletonBeanRegistry**

```java
protected boolean isDependent(String beanName, String dependentBeanName) {
 synchronized (this.dependentBeanMap) {
  return isDependent(beanName, dependentBeanName, null);
 }
```

* isDependent 处理的是使用了 depends-on 配置的 Bean 定义。

```java
private boolean isDependent(String beanName, String dependentBeanName, Set<String> alread
 if (alreadySeen != null && alreadySeen.contains(beanName)) {
  return false;
 }
 String canonicalName = canonicalName(beanName);
 Set<String> dependentBeans = this.dependentBeanMap.get(canonicalName);
 if (dependentBeans == null) {
  return false;
 }
 if (dependentBeans.contains(dependentBeanName)) {
  return true;
 }
 for (String transitiveDependency : dependentBeans) {
  if (alreadySeen == null) {
   alreadySeen = new HashSet<String>();
  }
  alreadySeen.add(beanName);
  if (isDependent(transitiveDependency, dependentBeanName, alreadySeen)) {
   return true;
  }
 }
 return false;
}
```

- alreadySeen != null，监测已经依赖的 Bean
- canonicalName，处理别名配置，找到最原来是的 BeanName
- SetdependentBeans，获取依赖的 Bean 集合
- for 循环递归检测依赖的 Bean，并添加到 alreadySeen 中

* **AbstractBeanFactory -> registerDependentBean(dep, beanName) -> DefaultSingletonBeanRegistry**

```java
public void registerDependentBean(String beanName, String dependentBeanName) {
 String canonicalName = canonicalName(beanName);  

 synchronized (this.dependentBeanMap) {
  Set<String> dependentBeans = this.dependentBeanMap.get(canonicalName);
  if (dependentBeans == null) {
   dependentBeans = new LinkedHashSet<String>(8);
   this.dependentBeanMap.put(canonicalName, dependentBeans);
  }
  dependentBeans.add(dependentBeanName);
 }   

 synchronized (this.dependenciesForBeanMap) {
  Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(dependentBeanName
  if (dependenciesForBean == null) {
   dependenciesForBean = new LinkedHashSet<String>(8);
   this.dependenciesForBeanMap.put(dependentBeanName, dependenciesForBean);
  }
  dependenciesForBean.add(canonicalName);
 }
}
```

- canonicalName(beanName)，获取原始的 beanName
- synchronized (this.dependentBeanMap)，添加 <canonicalName, dependentBeanName> 到 dependentBeanMap 中
- synchronized (this.dependenciesForBeanMap)，添加 <dependentBeanName, canonicalName> 到 dependenciesForBeanMap 中

* **getBean(dep)，就可以获取到 depends-on 依赖的 Bean 了**

### 处理单实例 Bean

* **AbstractBeanFactory -> mbd.isSingleton()**

```java
if (mbd.isSingleton()) {
 sharedInstance = getSingleton(beanName, new ObjectFactory<Object>() {
  @Override
  public Object getObject() throws BeansException {
   try {
    return createBean(beanName, mbd, args);
   }
   catch (BeansException ex) {
    destroySingleton(beanName);
    throw ex;
   }
  }
 });
 bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
}
```

* 这一部分是使用 beanName 和 singletonFactory 匿名内部类传入等待回调的方式创建单实例 Bean 实例

```java
public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
 Assert.notNull(beanName, "'beanName' must not be null");
 synchronized (this.singletonObjects) {
  Object singletonObject = this.singletonObjects.get(beanName);
  if (singletonObject == null) {
   if (this.singletonsCurrentlyInDestruction) {
    throw new BeanCreationNotAllowedException(beanName,
      "Singleton bean creation not allowed while singletons of this factory are in destruction " +
      "(Do not request a bean from a BeanFactory in a destroy method implementation!)");
   }
   if (logger.isDebugEnabled()) {
    logger.debug("Creating shared instance of singleton bean '" + beanName + "'");
   }
   beforeSingletonCreation(beanName);
   boolean newSingleton = false;
   boolean recordSuppressedExceptions = (this.suppressedExceptions == null);
   if (recordSuppressedExceptions) {
    this.suppressedExceptions = new LinkedHashSet<Exception>();
   }
   try {
    singletonObject = singletonFactory.getObject();
    newSingleton = true;
   }
   catch (IllegalStateException ex) {
    singletonObject = this.singletonObjects.get(beanName);
    if (singletonObject == null) {
     throw ex;
    }
   }
   catch (BeanCreationException ex) {
    if (recordSuppressedExceptions) {
     for (Exception suppressedException : this.suppressedExceptions) {
      ex.addRelatedCause(suppressedException);
     }
    }
    throw ex;
   }
   finally {
    if (recordSuppressedExceptions) {
     this.suppressedExceptions = null;
    }
    afterSingletonCreation(beanName);
   }
   if (newSingleton) {
    addSingleton(beanName, singletonObject);
   }
  }
  return (singletonObject != NULL_OBJECT ? singletonObject : null);
 }
}
```

- this.singletonObjects.get(beanName)，先尝试从缓存池中获取对象，没有就继续往下执行
- beforeSingletonCreation(beanName)，标记当前 bean 被创建，如果有构造函数注入的循环依赖会报错
- singletonObject = singletonFactory.getObject()，创建 bean 过程就是调用 createBean() 方法
- afterSingletonCreation(beanName)，最后把标记从集合中移除
- addSingleton(beanName, singletonObject)，新创建的会加入缓存集合

### 从缓存中获取 bean 实例

* **doCreateBean -> if (earlySingletonExposure) -> getSingleton(beanName, false)**

```java
protected Object getSingleton(String beanName, boolean allowEarlyReference) {
// 从 singletonObjects 获取实例，singletonObjects 中缓存的实例都是完全实例化好的 bean，可以直接使用
 Object singletonObject = this.singletonObjects.get(beanName);
 // 如果 singletonObject 为空，则没有创建或创建中
 if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
  // 加锁
  synchronized (this.singletonObjects) {
   // 单例缓存池中，没有当前beanName
   singletonObject = this.earlySingletonObjects.get(beanName);
   if (singletonObject == null && allowEarlyReference) {
    ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
    if (singletonFactory != null) {
     // 加入到三级缓存，暴漏早期对象用于解决三级缓存
     singletonObject = singletonFactory.getObject();  
     this.earlySingletonObjects.put(beanName, singletonObject);
     this.singletonFactories.remove(beanName);
    }
   }
  }
 }
 return (singletonObject != NULL_OBJECT ? singletonObject : null);
}
```

- singletonObjects，用于存放初始化好的 bean 实例。
- earlySingletonObjects，用于存放初始化中的 bean，来解决循环依赖。
- singletonFactories，用于存放 bean 工厂，bean 工厂所生成的 bean 还没有完成初始化 bean。

### FactoryBean 中获取 bean 实例

* **AbstractBeanFactory -> getObjectForBeanInstance(sharedInstance, name, beanName, null)**

```java
protected Object getObjectForBeanInstance(Object beanInstance, String name, String beanName, RootBeanDefinition mbd) {
    
    // 如果 beanName 以 & 开头，但 beanInstance 却不是 FactoryBean，则会抛出异常
 if (BeanFactoryUtils.isFactoryDereference(name) && !(beanInstance instanceof FactoryBean)) {
  throw new BeanIsNotAFactoryException(transformedBeanName(name), beanInstance.getClass());
 }

    // 这里判断就是这个 bean 是不是 FactoryBean，不是就直接返回了
 if (!(beanInstance instanceof FactoryBean) || BeanFactoryUtils.isFactoryDereference(name)) {
  return beanInstance;
 }   

 Object object = null;
 if (mbd == null) {    
// 如果 mbd 为空，则从缓存加载 bean（FactoryBean 生成的单例 bean 实例会缓存到 factoryBeanObjectCache 集合中，方便使用）
  object = getCachedObjectForFactoryBean(beanName);
 }   

 if (object == null) {
  // 到这，beanInstance 是 FactoryBean 类型，所以就强转了
  FactoryBean<?> factory = (FactoryBean<?>) beanInstance;
  // mbd 为空且判断 containsBeanDefinition 是否包含 beanName
  if (mbd == null && containsBeanDefinition(beanName)) {    
   // 合并 BeanDefinition
   mbd = getMergedLocalBeanDefinition(beanName);
  }    
  boolean synthetic = (mbd != null && mbd.isSynthetic());   
  // 调用 getObjectFromFactoryBean 获取实例 
  object = getObjectFromFactoryBean(factory, beanName, !synthetic);
 }
 return object;
}
```

- (!(beanInstance instanceof FactoryBean)，这里判断就是这个 bean 是不是 FactoryBean，不是就直接返回了
- 如果 mbd 为空，则从缓存加载 bean（FactoryBean 生成的单例 bean 实例会缓存到 factoryBeanObjectCache 集合中，方便使用）
- 调用 getObjectFromFactoryBean 获取实例，这里会包括一部分对单例以及非单例的处理，以及最终返回 factory.getObject(); 对应的 Bean 实例

![spring-getbean流程](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/spring-getbean%E6%B5%81%E7%A8%8B.png)