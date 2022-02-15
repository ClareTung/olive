[toc]

## Drools规则引擎

## Drools简介

### 什么是规则引擎

* 规则引擎是一种嵌入在应用程序中的组件，实现了将业务决策从应用程序代码中分离出来，并使用预定义的语义模块编写业务决策。接受数据输入，解释业务规则，并根据业务规则做出业务决策。

### 为什么使用规则引擎

* 复杂企业级项目的开发以及其中随外部条件不断变化的业务规则（business logic），迫切需要**分离商业决策者的商业决策逻辑和应用开发者的技术决策，并把这些商业决策放在中心数据库或其他统一的地方，让它们能在运行时（即商务时间）可以动态地管理和修改从而提供软件系统的柔性和适应性**。规则引擎正是应用于上述动态环境中的一种解决方法。
* 企业管理者对企业级IT系统的开发有着如下的要求：为提高效率，管理流程必须自动化，即使现代商业规则异常复杂； 市场要求业务规则经常变化，IT系统必须依据业务规则的变化快速、低成本的更新；为了快速、低成本的更新，**业务人员应能直接管理IT系统中的规则，不需要程序开发人员参与。**

### Drools

* Drools 是用 Java 语言编写的具有一个易于访问企业策略、易于调整以及易于管理的开源业务规则引擎，其基于CHARLES FORGY'S的RETE算法符合业内标准，速度快且效率高。 业务分析师人员或审核人员可以利用它轻松查看业务规则，检验已编码的规则执行了所需的业务规则。

## .drl文件结构

- package 包充当每组规则的唯一名称空间。一个规则库可以包含多个程序包。通常，将包的所有规则与包声明存储在同一文件中，以便包是独立的。但是，也可以在规则中使用从其他的包中导入的对象。
- imports 与Java中的import语句类似，用来标识在规则中使用的任何对象的标准路径和类型名称
- factions 函数代码块如：

```
function String hello(String applicantName) {
    return "Hello " + applicantName + "!";
}
```

* queries 在Drools引擎的工作内存中搜索与DRL文件中的规则相关的事实

```
query "people under the age of 21"
    $person : Person( age < 21 )
end
```

- global 为规则提供数据或服务
- **rules 规则**
- 1.import引入java方法 以及 function

* 2.glable 全局变量
* 3.querys 查询
* 4.declare 自定义fact对象

## rules规则

### drl属性

| 属性             | 描述                                                         |
| ---------------- | ------------------------------------------------------------ |
| salience         | 定义规则优先级的整数，数值越大，优先级越高                   |
| enabled          | 规则启用开关                                                 |
| date-effective   | 包含日期和时间定义的字符串。仅当当前日期和时间在date-effective属性之后时，才能激活该规则。 |
| date-expires     | 如果当前日期和时间在date-expires属性之后，则无法激活该规则。 |
| no-loop          | 选择该选项后，如果规则的结果重新触发了先前满足的条件，则无法重新激活（循环）规则。如果未选择条件，则在这些情况下可以循环规则。 |
| agenda-group     | 标识要向其分配规则的议程组                                   |
| activation-group | 激活组，在激活组中，只能激活一个规则。触发的第一个规则将取消激活组中所有规则的所有未决激活。 |
| duration         | 定义了如果仍满足规则条件，则可以激活规则的持续时间（以毫秒为单位）。 |
| timer            | cron定时表达式                                               |
| calendar         | 时钟                                                         |
| auto-focus       | 仅适用于议程组中的规则。选择该选项后，下次激活该规则时，将自动将焦点分配给分配了该规则的议程组。 |
| lock-on-active   | no-loop属性的更强版                                          |
| ruleflow-group   | 标识规则流组的字符串                                         |
| dialect          | 用于标识规则中的代码表达式JAVA或MVEL将其用作语言             |

### 匹配模式

#### 没有约束的匹配模式

实事不需要满足任何条件，若类型相同，则触发该规则，如：

```
package com.olive.people2
import com.olive.drools.bean.People
dialect "java"

rule "gril"
    when
        People()
    then
        System.out.println("规则执行");
end
```

#### 有条件约束的匹配模式

实事类型相同，且满足条件，则触发该规则，如：

```
package com.olive.people2
import com.olive.drools.bean.People
dialect "java"

rule "gril"
    when
        People(sex == 0 && drlType == "people")
    then
        System.out.println("规则执行");
end
```

#### 匹配并绑定属性以及实事

实事类型相同，且满足条件，则触发该规则，并绑定数据，如：

```
package com.olive.people2
import com.olive.drools.bean.People
dialect "java"

rule "gril"
    when
        $p : People(sex == 0 && drlType == "people")
    then
        System.out.println($p.getName() + "是女孩");
end
```

### 条件

and，or 等结合规则条件的多个模式，没有定义关键字连词，默认是and：

```
rule "gril"
    when
        People(sex == 0) and
        Cat(sex == 0)
    then
        System.out.println("gril规则执行");
end
```

### 约束

标准Java运算符优先级适用于DRL中的约束运算符，而drl运算符除==和!=运算符外均遵循标准Java语义。在drl中 Person( firstName != “John” )类似于 !java.util.Objects.equals(person.getFirstName(), “John”)

| 约束                   | 描述                                                         |
| ---------------------- | ------------------------------------------------------------ |
| !.                     | 使用此运算符可以以空安全的方式取消引用属性。!.运算符左侧的值不能为null（解释为!= null） |
| []                     | 按List索引访问值或Map按键访问值                              |
| <，<=，>，>=           | 在具有自然顺序的属性上使用这些运算符                         |
| ==, !=                 | 在约束中使用这些运算符作为equals()和!equals()方法            |
| &&，\|\|               | 组合关系条件                                                 |
| matches，not matches   | 使用这些运算符可以指示字段与指定的Java正则表达式匹配或不匹配 |
| contains，not contains | 使用这些运算符可以验证Array或字段是否包含或不包含指定值      |
| memberOf，not memberOf | 使用这些运算符可以验证字段是否为定义为变量Array的成员        |
| soundslike             | 使用英语发音来验证单词是否具有与给定值几乎相同的声音（类似于该matches运算符） |
| in，notin              | 使用这些运算符可以指定一个以上的可能值来匹配约束（复合值限制） |

* 举例：

```
Person( country matches "(USA)?\\S*UK" )
Person( country not matches "(USA)?\\S*UK" )

FamilyTree(countries contains "UK" )
Person( fullName not contains "Jr" )
FamilyTree(countries contains $var)
Person( fullName not contains $var )

FamilyTree( person memberOf $europeanDescendants )
FamilyTree( person not memberOf $europeanDescendants )
```

### 集合

#### from 取集合中的元素

```
package com.olive.frm
import com.olive.drools.bean.People
import com.olive.drools.bean.Animal
dialect "java"

rule "from"
    when
        $an : Animal()
        $p : People(sex != 3 && drlType == "from") from $an.peoples
    then
        System.out.println($p);
 end
```

#### collect

从指定来源或从Drools引擎的工作内存中获取集合,可以使用Java集合（例如List，LinkedList和HashSet）

```
package com.olive.collt
import com.olive.drools.bean.People
import java.util.List
dialect "java"

rule "collect"
    when
        $alarms : List(size >= 3) from collect(People(sex != 3 && drlType == "collect"))
    then
        System.out.println("collect执行成功，匹配结果为：" + $alarms);
 end
```

#### accumulate 迭代器

用于遍历数据集对数据项执行自定义或预设动作并返回结果

##### accumulate 函数

- average
- min
- max
- count
- sum
- collectList 获取列表
- collectSet 获取集合

```
package com.olive.accul
import com.olive.drools.bean.Sensor
import com.olive.drools.bean.People
import java.util.List
dialect "java"

rule "accumulate"
    when
        $avg : Number() from accumulate(Sensor(temp >= 5 && $temp : temp),average($temp))
   then
        System.out.println("accumulate成功执行，平均温度为：" + $avg);
end
```

##### 自定义 accunmulate

- init 初始化变量
- action 每次遍历执行的动作
- reverse （可选）反转动作，用于优化
- result 返回的执行结果

```
rule "diyaccumulate"
    when
        People(drlType == "diyaccumulate")
        $avg: Number() from accumulate(People($age: age,drlType == "diyaccumulate"),
        init(int $total = 0, $count = 0;),
        action($total += $age; $count++;),
        result($total/$count))
   then
        System.out.println("Avg: " + $avg);
end
```

### RHS动作

* RHS 部分定义了当LHS满足时要进行的操作,规则操作的主要目的是在Drools引擎的工作内存中插入，删除或修改数据。RHS中可以编写代码，可以使用LHS 部分当中定义的绑定变量名以及drl头部定义的全局变量。在RHS当中如果需要条件判断,那么请重新考虑将其放在 LHS 当中,否则就违背了使用规则的初衷。

#### 主要操作

| 动作          | 描述                                                     |
| ------------- | -------------------------------------------------------- |
| set           | 给属性赋值                                               |
| modify        | 将改变通知drolls引擎                                     |
| update        | 将改变通知drolls引擎                                     |
| insert        | 将新实事插入到drools引擎的工作                           |
| insertLogical | insert增强版，需声明撤回事件，或待不在匹配条件后自动撤回 |
| delete        | 删除实事                                                 |

#### Update

Update用于将数据的更改更新到引擎，并通知引擎重新匹配该事实

```
package com.olive.udp
import com.olive.drools.bean.People
dialect "java"

rule "update1"
    when
        $p : People(drlType == "update" && sex == 0)
    then
        System.out.println("update1执行===" + $p);
        $p.setSex(1);
        update($p)
end

rule 'update2'
    when
       $p : People(drlType == "update" && sex == 1)
    then
        System.out.println("update2执行===" + $p);
end

rule 'modify'
    when
      $p : People(drlType == "update" && sex == 1)
    then
       System.out.println("update3执行===" + $p);
       modify($p){
         setSex(-1)
       }
end
```

####  drools header详解

##### import引入java方法 以及 function

导入规则文件需要使用到的外部规则文件或者变量，这里的使用方法跟java相同，但是不同于java的是，这里的import导入的不仅仅可以是一个类，也可以是这个类中的某一个可访问的静态方法

```
package com.olive.impot
import com.olive.drools.bean.People
import com.olive.drools.util.DroolsStringUtils
dialect "java"

function String hello(String name){
    return "Hello " + name + "!";
}

rule "impot"
    when
        $p : People(drlType == "impot")
    then
        System.out.println(DroolsStringUtils.isEmpty($p.getName()));
        System.out.println(hello("Clare"));
end
```

#### Global 全局变量

Drools规则文件中的全局变量（global variables）是规则文件代码与java代码之间相互交互的桥梁，我们可以利用全局变量让规则文件中的程序使用java代码中的基本变量、缓存信息或接口服务等等。

##### 基本语法：

global 类型 变量名

例如：
global java.util.List list

##### 使用全局变量来传递数据样例

全局变量可以是一个services或者一个对象，来方便Drools与java之间的数据传输

##### 在drl脚本中使用全局变量

全局变量以 global 类型 变量名 的方式在规则中定义，在规则Then语句中使用变量名直接对其进行操作

##### 在java代码中声明全局变量，并取值

在java代码中使用全局变量，首先要使用 session.setGlobal(变量名，变量)的方式声明变量，其中变量名要与规则中引入变量名相同

若要取值，要使用 session.getGlobal(变量名)的方法，其中变量名要与规则中引入变量名相同

```
package com.olive.glb
import com.olive.drools.bean.People
import java.lang.Integer
dialect "java"

global com.olive.drools.service.GlobalService service
global java.util.List list
global com.olive.drools.bean.NumCount numCount

rule "global"
    when
        People(drlType == "global")
        $p : People() from service.getPeoples()
    then
        list.add($p);
        numCount.plus();
 end
```

#### querys 查询

Query语法提供了一种查询working memory中符合约束条件的FACT对象的简单方法。它仅包含规则文件中的LHS部分，不用指定“when”和“then”部分。Query有一个可选参数集合，每一个参数都有可选的类型。如果没有指定类型，则默认为Object类型。

##### 定义drl查询语句

可以定义多个查询参数，在调用查询时传入

```
package com.olive.quey
import com.olive.drools.bean.People
dialect "java"

query "queryPeople" (String $name, Integer $sex)
    $p : People(name == $name, sex == $sex)
end
```

##### java中进行查询

使用session.getQueryResults(查询名, 参数, 参数。。。) 来获取QueryResults匹配对象列表

#### declare 自定义fact对象

##### declare在drl规则文件中定义：

```
package com.olive.declar
dialect "java"

declare Love
    feel : String
    continued : String
end

rule "love"
when
    $l : Love()
then
    System.out.println("自定义事件执行： " + $l);
end
```

##### 在api中使用

  通过 kieBase.getFactType(域名，实事名)的方式获取实事对象并实例
  通过 factType.set(实例，属性名，属性值)的方式来赋值变量
