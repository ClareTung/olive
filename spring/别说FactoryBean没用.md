### 别说FactoryBean没用

说起`FactoryBean`就不得不提`BeanFactory`，因为面试官老喜欢问它们的区别。

- BeanFactory：spring容器的顶级接口，管理bean的工厂。
- FactoryBean：并非普通的工厂bean，它隐藏了实例化一些复杂Bean的细节，给上层应用带来了便利。

如果你看过spring源码，会发现它有70多个地方在用FactoryBean接口。

![image-20211015175222316](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20211015175222316.png)

上面这张图足以说明该接口的重要性，请勿忽略它好吗？

特别提一句：`mybatis`的`SqlSessionFactory`对象就是通过`SqlSessionFactoryBean`类创建的。

我们一起定义自己的`FactoryBean`：

```
@Component
public class MyFactoryBean implements FactoryBean {

    @Override
    public Object getObject() throws Exception {
        String data1 = buildData1();
        String data2 = buildData2();
        return buildData3(data1, data2);
    }

    private String buildData1() {
        return "data1";
    }

    private String buildData2() {
        return "data2";
    }

    private String buildData3(String data1, String data2) {
        return data1 + data2;
    }


    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
```

获取`FactoryBean`实例对象：

```
@Service
public class MyFactoryBeanService implements BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void test() {
        Object myFactoryBean = beanFactory.getBean("myFactoryBean");
        System.out.println(myFactoryBean);
        Object myFactoryBean1 = beanFactory.getBean("&myFactoryBean");
        System.out.println(myFactoryBean1);
    }
}
```

- `getBean("myFactoryBean");`获取的是MyFactoryBeanService类中getObject方法返回的对象，
- `getBean("&myFactoryBean");`获取的才是MyFactoryBean对象。