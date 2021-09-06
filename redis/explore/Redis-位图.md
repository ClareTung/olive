Redis-位图

* 位图不是特殊的数据结构，它的内容就是普通的字符串，也就是byte数组。

### 基本使用

* Redis的位数组是自动扩展，如果设置了某个偏移位置超过了现有的内容范围，就会自动将位数组进行零扩充。

* 获取字符串的ASCII码，设置位数组相应位置的值。

* “h”的Ascii码为：01101000，可以看到h字符的1/2/4位是1。

* 位数组的顺序和字符的位顺序是相反的

  ![image-20210828153313923](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210828153313923.png)

#### 零存整取

```
hp笔记本:0>setbit s 1 1
"0"

hp笔记本:0>setbit s 2 1
"0"

hp笔记本:0>setbit s 4 1
"0"

hp笔记本:0>get s
"h"

```

#### 零存零取

```
hp笔记本:0>setbit w 1 1
"0"

hp笔记本:0>setbit w 2 1
"0"

hp笔记本:0>getbit w 1
"1"

hp笔记本:0>getbit w 2
"1"
```

#### 整存零取

* 整存就是一次性填充所有位数组

```
hp笔记本:0>set s2 h
"OK"

hp笔记本:0>getbit s2 4
"1"
```

### 统计和查找

* `bitcount`：统计指定位置范围内1的个数
* `bitpos`：查找指定范围内出现的第一个0或1
* 范围[start，end]必须是8的倍数，不能任意指定，可以getrange获取子串，在内存中统计

```
hp笔记本:0>set w hello
"OK"

hp笔记本:0>bitcount w
"21"

hp笔记本:0>bitcount w 0 0           # 第一个字符中 1 的位数，h有3个1
"3"

hp笔记本:0>bitcount w 0 1           # 前两个字符中 1 的位数，e（01100101）是4个1，加上h有3个1，共计7个1
"7"

hp笔记本:0>bitpos w 0               # 第一个 0 位
"0"

hp笔记本:0>bitpos w 1               # 第一个 1 位
"1"

hp笔记本:0>bitpos w 1 1 1           # 从第二个字符算起，第一个 1 位（bitpos w 1 1 2 结果是：9）
"9"
```

### 魔术指令 bitfield

* bitfield有三个子指令，get/set/incrby，他们都可以对指定位片段进行读写，最多只能处理64个连续位，超过64位就得使用多个子指令，bitfield可以一次执行多个子指令

```
hp笔记本:0>bitfield w get u3 1 # 从第2位开始取3个位，结果是无符号数
 1)  "6"
hp笔记本:0>bitfield w get i3 1 # 从第2位开始取3个位，结果是有符号数
 1)  "-2"
```

* 所谓有符号数是指获取的位数组中第一个位是符号位，剩下的才是值。如果第一位是 1，那就是负数。
* 无符号数表示非负数，没有符号位，获取的位数组全部都是值。有

```
hp笔记本:0>bitfield w get u3 1 get i3 1
 1)  "6"
 2)  "-2"
```

* a的ascii码是97

```
hp笔记本:0>bitfield w set u8 8 97  #从第 8 个位开始，将接下来的 8 个位用无符号数 97 替换
 1)  "101"
hp笔记本:0>get w
"hallo"
```

* incrby，它用来对指定范围的位进行自增操作。
  * 自增，就有 可能出现溢出。如果增加了正数，会出现上溢，如果增加的是负数，就会出现下溢出。Redis  默认的处理是折返。如果出现了溢出，就将溢出的符号位丢掉。如果是 8 位无符号数 255， 加 1 后就会溢出，会全部变零。如果是 8 位有符号数 127，加 1 后就会溢出变成 -128。

```
hp笔记本:0>bitfield w incrby u4 2 1
 1)  "11"
hp笔记本:0>bitfield w incrby u4 2 1
 1)  "12"
hp笔记本:0>bitfield w incrby u4 2 1
 1)  "13"
hp笔记本:0>bitfield w incrby u4 2 1
 1)  "14"
hp笔记本:0>bitfield w incrby u4 2 1
 1)  "15"
hp笔记本:0>bitfield w incrby u4 2 1
 1)  "0"
```

* bitfield 指令提供了溢出策略子指令 overflow，用户可以选择溢出行为，默认是折返 (wrap)，还可以选择失败 (fail) 报错不执行，以及饱和截断 (sat)，超过了范围就停留在最大 最小值。overflow 指令只影响接下来的第一条指令，这条指令执行完后溢出策略会变成默认 值折返 (wrap)。

#### 饱和截断 SAT

```
hp笔记本:0>bitfield w overflow sat incrby u4 2 1
 1)  "11"
hp笔记本:0>bitfield w overflow sat incrby u4 2 1
 1)  "12"
hp笔记本:0>bitfield w overflow sat incrby u4 2 1
 1)  "13"
hp笔记本:0>bitfield w overflow sat incrby u4 2 1
 1)  "14"
hp笔记本:0>bitfield w overflow sat incrby u4 2 1
 1)  "15"
hp笔记本:0>bitfield w overflow sat incrby u4 2 1
 1)  "15"
hp笔记本:0>bitfield w overflow sat incrby u4 2 1
 1)  "15"
```

#### 失败不执行 FALL

```
hp笔记本:0>bitfield w overflow fail incrby u4 2 1
 1)  "11"
hp笔记本:0>bitfield w overflow fail incrby u4 2 1
 1)  "12"
hp笔记本:0>bitfield w overflow fail incrby u4 2 1
 1)  "13"
hp笔记本:0>bitfield w overflow fail incrby u4 2 1
 1)  "14"
hp笔记本:0>bitfield w overflow fail incrby u4 2 1
 1)  "15"
hp笔记本:0>bitfield w overflow fail incrby u4 2 1
 1)  null
hp笔记本:0>
```



