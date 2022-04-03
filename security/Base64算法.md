

## Java Base64算法

Base64算法并不是加密算法，它的出现是为了解决ASCII码在传输过程中可能出现乱码的问题。Base64是网络上最常见的用于传输8bit字节码的可读性编码算法之一。可读性编码算法不是为了保护数据的安全性，而是为了可读性。可读性编码不改变信息内容，只改变信息内容的表现形式。Base64使用了64种字符：大写A到Z、小写a到z、数字0到9、“+”和“/”，故得此名。



## 储备知识Byte和bit

Byte：字节，数据存储的基本单位；

bit：比特，也叫位，一个位只能存储0或者1。

关系：1Byte = 8bit。

一个英文字符占1个字节，8位：

```
@Test
public void demo1() {
    String a = "a";
    byte[] bytes = a.getBytes();
    for (byte b : bytes) {
        System.out.println(b);
        System.out.println(Integer.toBinaryString(b));
    }
}
```



程序输出：

```
97
1100001
```



一个中文字符在不同编码下所占的字节数不同：

```
@Test
public void demo2() throws UnsupportedEncodingException {
    String a = "鸟";
    byte[] utf8Bytes = a.getBytes("utf-8");
    for (byte b : utf8Bytes) {
        System.out.print(b);
        System.out.print("    ");
        System.out.println(Integer.toBinaryString(b));
    }
    System.out.println();
    byte[] gbkBytes = a.getBytes("gbk");
    for (byte b : gbkBytes) {
        System.out.print(b);
        System.out.print("    ");
        System.out.println(Integer.toBinaryString(b));
    }
}
```



程序输出：

```
-23    11111111111111111111111111101001
-72    11111111111111111111111110111000
-97    11111111111111111111111110011111

-60    11111111111111111111111111000100
-15    11111111111111111111111111110001
```



所以在UTF-8编码下，一个中文占3个字节；在GBK编码下，一个中文占2个字节。

## Base64编码原理

Base64编码表：

| **索引** | **对应字符** | **索引** | **对应字符** | **索引** | **对应字符** | **索引** | **对应字符** |
| -------- | ------------ | -------- | ------------ | -------- | ------------ | -------- | ------------ |
| 0        | **A**        | 17       | **R**        | 34       | **i**        | 51       | **z**        |
| 1        | **B**        | 18       | **S**        | 35       | **j**        | 52       | **0**        |
| 2        | **C**        | 19       | **T**        | 36       | **k**        | 53       | **1**        |
| 3        | **D**        | 20       | **U**        | 37       | **l**        | 54       | **2**        |
| 4        | **E**        | 21       | **V**        | 38       | **m**        | 55       | **3**        |
| 5        | **F**        | 22       | **W**        | 39       | **n**        | 56       | **4**        |
| 6        | **G**        | 23       | **X**        | 40       | **o**        | 57       | **5**        |
| 7        | **H**        | 24       | **Y**        | 41       | **p**        | 58       | **6**        |
| 8        | **I**        | 25       | **Z**        | 42       | **q**        | 59       | **7**        |
| 9        | **J**        | 26       | **a**        | 43       | **r**        | 60       | **8**        |
| 10       | **K**        | 27       | **b**        | 44       | **s**        | 61       | **9**        |
| 11       | **L**        | 28       | **c**        | 45       | **t**        | 62       | **+**        |
| 12       | **M**        | 29       | **d**        | 46       | **u**        | 63       | **/**        |
| 13       | **N**        | 30       | **e**        | 47       | **v**        |          |              |
| 14       | **O**        | 31       | **f**        | 48       | **w**        |          |              |
| 15       | **P**        | 32       | **g**        | 49       | **x**        |          |              |
| 16       | **Q**        | 33       | **h**        | 50       | **y**        |          |              |

Base64编码的过程：

1. 将字符串转换为字符数组；
2. 将每个字符转换为ASCII码；
3. 将ASCII码转换为8bit二进制码；
4. 然后每3个字节为一组（一个字节为8个bit，所以每组24个bit）；
5. 将每组的24个bit分为4份，每份6个bit；
6. 在每6个bit前补0，补齐8bit（前面补0不影响数值大小）；
7. 然后将每8bit转换为10进制数，根据上面的Base64编码表进行转换。

上面步骤中，为什么要将每组24个bit分为4份，每份6个bit呢？因为6bit的最大值为111111，转换为十进制为63，所以6bit的取值范围为0~63，这和base64编码表长度一致。

根据上面的过程，我们来举个例子：现要对hello这个字符串进行Base64编码，过程如下：

1. hello转换为字符数组：h e l l o；
2. 对应的ASCII码为：104 101 108 108 111；
3. 转换为8bit二进制数：01101000 01100101 01101100 01101100 01101111
4. 分组，每组24个bit（不足24个bit的用00000000补齐）： 011010000110010101101100 011011000110111100000000；
5. 每组24bit分为4份，每份6bit：011010 000110 010101 101100 011011 000110 111100 000000；
6. 在每6个bit前补0，补齐8bit：00011010 00000110 00010101 00101100 00011011 00000110 00111100 00000000；
7. 将每8bit转换为10进制数：26 6 21 44 27 6 60 0
8. 从上面Base64编码表中找到十进制数对应的字符（末尾的0并不是A，而是用=等号补位）：a G V s b G 8 =

所以hello经过Base64编码的结果为aGVsbG8=

我们可以用代码验证下（JDK8开始已经提供了Base64的实现）：

```
import org.junit.Test;
import java.util.Base64;

public class Base64Test {

    @Test
    public void demo1() {
        System.out.println(Base64.getEncoder().encodeToString("hello".getBytes()));
    }
}
```

程序输出也是aGVsbG8=

## URL Base64算法

Base64编码值通过URL传输会出现问题，因为Base64编码中的“+”和“/”符号是不允许出现在URL中的。同样，符号“=”用做参数分隔符，也不允许出现在URL中，根据RFC 4648中的建议，“~”和“.”符都有可能替代“=”符号。但“~”符号与文件系统相冲突，不能使用；如果使用“.”符号，某些文件系统认为该符号连续出现两次则为错误。

所以common codec包下的URL Base64算法舍弃了填充符，使用了不定长URL Base64编码。

引入common codec依赖包：

```
<dependency>
    <groupId>commons-codec</groupId>
    <artifactId>commons-codec</artifactId>
    <version>1.14</version>
</dependency>
```



举个例子：

```
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class Base64Test {

    @Test
    public void demo1() {
        String value = "hello";
        System.out.println(Base64.encodeBase64String(value.getBytes()));
        System.out.println(Base64.encodeBase64URLSafeString(value.getBytes()));
    }

}
```



输出如下：

```
aGVsbG8=
aGVsbG8
```