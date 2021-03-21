# Annotation

## 定义注解

### 注解体

* @interface

### 注解变量

* 变量后加一对小括号，可以有默认值。返回值只能是Java基本类型、String类型或者枚举类，不可以是对象类型。

### 元注解

* 给注解加注解的注解。

#### Target注解

* 描述注解的使用范围，即注解可以用在什么地方
* 取值：ElementType

#### Retention注解

* 描述注解保留的时间，即注解的生命周期
* 取值：RetentionPolicy
  * SOURCE, *// 源文件保留*  
  * CLASS, *// 编译期保留，默认值*  
  * RUNTIME // 运行期保留，可通过反射去获取注解信息

#### Documented注解

* 描述在使用 javadoc 工具为类生成帮助文档时是否要保留其注解信息

#### Inherited注解

* 被Inherited注解修饰的注解具有继承性，如果父类使用了被@Inherited修饰的注解，则其子类将自动继承该注解。



