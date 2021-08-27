# Javadoc

> public和protected

* 所有public和protected方法都应该有相应的Javadoc
* 子类覆盖父类的方法，一般不需要Javadoc，除非与原来的差别很大，使用Javadoc说明差异部分

> 使用标准的Javadoc风格注释

```java
/**
*
*/
```

> 用简单的HTML tags，不需要XHTML

* Javadoc用HTML tags来识别段落、列表等等

> 用单个`<p>`来分割段落

* Javadoc会分为好几段，使用`<p>`标签即可

```java
/**
* First paragraph
* <p>
* Next paragraph
*/
```

> 用的那个`<li>`来标记列表项

* `<li>`不需要闭合

```java
/**
* First paragraph
* <p><ul>
* <li> 1
* <li> 2
* </ul><p>
* Next paragraph
*/
```

> 首句

* Javadoc的摘要，简洁有力，不能太长

> 用“this”指代类的对象

* 当你想描述这个类的一个实例对象的时候，用“this”来指代它

> 别写太长的句子

* 一句话尽量在一行

> 正确使用@link和@code

* 涉及类或方法，最好用@link和@code
* @link会变成一个超链接
* @code用来标记一小段等宽字体，可以用来标记某个类或方法，但不会生成超链接

> 不要在首句用@link

* 首句会做为概要，用超链接容易造成混乱

> 使用@param、@return和@throws

* @param：描述参数
* @return：描述返回值
* @throws：描述异常

> 泛型参数加上@param

* 为泛型加一个@param说明

> @param之前空一行

* @param和@return之前空一行，增加代码可读性

> 用短语来描述@param和@return

* @param 和 @return 后面跟的的描述是个短语

> 用if-句来描述@throws

* @throws 通常跟着一个 “if” 句子来描述抛异常的情形，比如 “@throws IllegalArgumentException if the file could not be found”。

> @param的参数之后空两格

* 对齐，好看

> 写明各参数和返回值的null行为

* 一个方法是否接受 null、会不会返回 null 对于其他开发者是十分重要的信息。除非是原始类型，@param 和 @return 都应该注明它是否接受或返回 null。以下标准若适用请务必遵循：
  * “not null” 表明不接受 null，若输入 null 可能导致异常，例如 NullPointerException
  * “may be null” 表明可以传入 null 参数
  * “null treated as xxx” 表明 null 值等价于某个值
  * “null returns xxx” 表明如果输入 null 则一定会返回某个值

* 定义清楚这些之后，**不**要再为 NullPointerException 写 @throws
* 约束条件都可以写

> 给 Specification 加上 implementation notes

* 如果某个接口允许第三方来实现，而你为这个接口写了个正式的规格说明（specification），这时候考虑加个 “implementation notes” 章节。这通常出现在类的 Javadoc 上，用于描述一些不太好写在特定方法上的东西，或者一些其他人不感兴趣的东西。

> @author一般加载类上即可



