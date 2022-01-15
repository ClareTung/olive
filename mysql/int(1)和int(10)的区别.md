# int(1)和int(10)的区别

MySQL中int占4个字节，对于无符号的int，最大值为2的31次方减1，4294967295。

使用int(1)依然能存下4294967295，不影响int本身支持的大小，int(1)与int(10)没什么区别。

int后面的数字不能表示字段的长度，int(num)一般加上zerofill。

```
`id` int(4) unsigned zerofill NOT NULL AUTO_INCREMENT
```

例如，上面可以实现不足4位补0（0001，0010，0100，1000），查询的时候做零填充。实际存储还是1， 10， 100，1000。