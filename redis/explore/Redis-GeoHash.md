## Redis-GeoHash

### GeoHash算法

* 业界比较通用的地理位置距离排序算法是 GeoHash 算法，Redis 也使用 GeoHash 算 法。
* GeoHash 算法将二维的经纬度数据映射到一维的整数，这样所有的元素都将在挂载到一 条线上，距离靠近的二维坐标映射到一维后的点之间距离也会很接近。
* 当我们想要计算「附近的人时」，首先将目标位置映射到这条线上，然后在这个一维的线上获取附近的点就行 了。

### Redis的Geo指令基本使用

#### 增加

```
hp笔记本:0>geoadd company 116.48105 39.996794 juejin
"1"

hp笔记本:0>geoadd company 116.514203 39.905409 ireader
"1"

hp笔记本:0>geoadd company 116.489033 40.007669 meituan
"1"

hp笔记本:0>geoadd company 116.562108 39.787602 jd 116.334255 40.027400 xiaomi
"2"
```

#### 距离

```
hp笔记本:0>geodist company juejin ireader km
"10.5501"
```

#### 获取元素位置

```
hp笔记本:0>geopos company juejin
 1)    1)   "116.48104995489120483"
  2)   "39.99679348858259686"

hp笔记本:0>geopos company jd ireader
 1)    1)   "116.56210631132125854"
  2)   "39.78760295130235392"

 2)    1)   "116.5142020583152771"
  2)   "39.90540918662494363"

```

#### 获取元素的hash值

* geohash 可以获取元素的经纬度编码字符串，上面已经提到，它是 base32 编码
* 使用这个编码值去 http://geohash.org/${hash}中进行直接定位

```
hp笔记本:0>geohash company ireader
 1)  "wx4g52e1ce0"
```

#### 附近的公司

* georadiusbymember 指令是最为关键的指令，它可以用来查询指定元素附近的其它元素

```
hp笔记本:0>georadiusbymember company ireader 20 km count 3 asc
 1)  "ireader"
 2)  "juejin"
 3)  "meituan"
hp笔记本:0>georadiusbymember company ireader 20 km count 3 desc
 1)  "jd"
 2)  "meituan"
 3)  "juejin"
hp笔记本:0>georadiusbymember company ireader 20 km withcoord withdist withhash count 3 asc
 1)    1)   "ireader"
  2)   "0.0000"
  3)   "4069886008361398"
  4)      1)    "116.5142020583152771"
   2)    "39.90540918662494363"


 2)    1)   "juejin"
  2)   "10.5501"
  3)   "4069887154388167"
  4)      1)    "116.48104995489120483"
   2)    "39.99679348858259686"


 3)    1)   "meituan"
  2)   "11.5748"
  3)   "4069887179083478"
  4)      1)    "116.48903220891952515"
   2)    "40.00766997707732031"
```

* Redis 还提供了根据坐标值来查询附近的元素

```
hp笔记本:0>georadius company 116.514202 39.905409 20 km withdist count 3 asc
 1)    1)   "ireader"
  2)   "0.0000"

 2)    1)   "juejin"
  2)   "10.5501"

 3)    1)   "meituan"
  2)   "11.5748"
```



