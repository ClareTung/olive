[TOC]

## ElasticSearch 简介

### 简介

* ElasticSearch 是一个基于 Lucene 的搜索服务器。它提供了一个分布式多员工能力的全文搜索引擎，基于 RESTful web 接口。Elasticsearch 是用 Java 语言开发的，并作为 Apache 许可条款下的开放源码发布，是一种流行的企业级搜索引擎。

* ElasticSearch 用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。

###  特性

* 分布式的文档存储引擎
* 分布式的搜索引擎和分析引擎
* 分布式，支持PB级数据

### 使用场景
* 搜索领域： 如百度、谷歌，全文检索等。
* 门户网站： 访问统计、文章点赞、留言评论等。
* 广告推广： 记录员工行为数据、消费趋势、员工群体进行定制推广等。
* 信息采集： 记录应用的埋点数据、访问日志数据等，方便大数据进行分析。



## ElasticSearch 基础概念

* Elasticsearch 集群可以包含多个索引 `indices`，每一个索引可以包含多个类型 `types`，每一个类型包含多个文档 `documents`，然后每个文档包含多个字段 `Fields`。

### 索引

- 索引基本概念（indices）：

索引是含义相同属性的文档集合，是 ElasticSearch 的一个逻辑存储，可以理解为关系型数据库中的数据库，ElasticSearch 可以把索引数据存放到一台服务器上，也可以 sharding 后存到多台服务器上，每个索引有一个或多个分片，每个分片可以有多个副本。

- 索引类型（index_type）：

索引可以定义一个或多个类型，文档必须属于一个类型。在 `ElasticSearch` 中，一个索引对象可以存储多个不同用途的对象，通过索引类型可以区分单个索引中的不同对象，可以理解为关系型数据库中的表。每个索引类型可以有不同的结构，但是不同的索引类型不能为相同的属性设置不同的类型。

### 文档

- 文档（document）：

文档是可以被索引的基本数据单位。存储在 ElasticSearch 中的主要实体叫文档 `document`，可以理解为关系型数据库中表的一行记录。每个文档由多个字段构成，ElasticSearch 是一个非结构化的数据库，每个文档可以有不同的字段，并且有一个唯一的标识符。

### 映射

- 映射（mapping）:

`ElasticSearch` 的 `Mapping` 非常类似于静态语言中的数据类型：声明一个变量为 int 类型的变量，以后这个变量都只能存储 `int` 类型的数据。同样的，一个 `number` 类型的 `mapping` 字段只能存储 `number` 类型的数据。

同语言的数据类型相比，`Mapping` 还有一些其他的含义，`Mapping` 不仅告诉 ElasticSearch 一个 `Field` 中是什么类型的值， 它还告诉 ElasticSearch 如何索引数据以及数据是否能被搜索到。

ElaticSearch 默认是动态创建索引和索引类型的 Mapping 的。这就相当于无需定义 `Solr` 中的 `Schema`，无需指定各个字段的索引规则就可以索引文件，很方便。但有时方便就代表着不灵活。比如，ElasticSearch 默认一个字段是要做分词的，但我们有时要搜索匹配整个字段却不行。如有统计工作要记录每个城市出现的次数。对于 `name` 字段，若记录 `new york` 文本，ElasticSearch 可能会把它拆分成 `new` 和 `york` 这两个词，分别计算这个两个单词的次数，而不是我们期望的 `new york`。

## 下载地址（7.10.1）

* [https://www.elastic.co/cn/downloads/elasticsearch](https://www.elastic.co/cn/downloads/elasticsearch)
* [https://www.elastic.co/cn/downloads/kibana](https://www.elastic.co/cn/downloads/kibana)
* [https://nodejs.org/en/download/](https://nodejs.org/en/download/)
* https://github.com/mobz/elasticsearch-head



## 本地地址

* elasticsearch: http://localhost:9200
* ElasticSearch-headhttp://localhost:9100
* kibana: http://localhost:5601



##  安装ElasticSearch-head插件

* 安装grunt
  * grunt是一个很方便的构建工具，可以进行打包压缩、测试、执行等等的工作，5.x里之后的head插件就是通过grunt启动的。因此需要安装grunt
  *  npm install -g grunt-cli

*  下载head插件
  * 1.网址:https://github.com/mobz/elasticsearch-head下载安装包

  * 2.解压
  * 3.进入head文件夹下，执行命令：npm install (此处是为安装进行安装pathomjs)。 如果安装速度慢,设置成淘宝的镜像重新安装 npm config set registry https://registry.npm.taobao.org
  * 4.安装完成之后npm run start或**grunt server,启动head插件**
  * 5.修改es使用的参数.编辑D:\elasticsearch\elasticsearch-7.3.2-windows-x86_64\elasticsearch-7.3.2\config\elasticsearch.yml文件，增加新的参数，这样head插件可以访问es
    * http.cors.enabled: true 
    * http.cors.allow-origin: "*"
  * 6.修改完配置将es重启,浏览器访问 http://localhost:9100





## Root mapping definition has unsupported parameters

* 7.4 默认不在支持指定索引类型，默认索引类型是_doc（隐含：include_type_name=false）

## Kibana操作
* Dev Tools下操作即可


## 创建索引

```
PUT /es-start-user
{
  "mappings": {
  "dynamic": true,
  "properties": {
	"name": {
	  "type": "text",
	  "fields": {
		"keyword": {
		  "type": "keyword"
		}
	  }
	},
	"address": {
	  "type": "text",
	  "fields": {
		"keyword": {
		  "type": "keyword"
		}
	  }
	},
	"remark": {
	  "type": "text",
	  "fields": {
		"keyword": {
		  "type": "keyword"
		}
	  }
	},
	"age": {
	  "type": "integer"
	},
	"salary": {
	  "type": "float"
	},
	"birthDate": {
	  "type": "date",
	  "format": "yyyy-MM-dd||epoch_millis"
	},
	"createTime": {
	  "type": "date"
	}
  }
  }
}
```

## 查询索引

```
GET es-start-user
```

## 删除索引

```
DELETE /es-start-user
```

## 添加一条数据

```
POST /es-start-user/_doc/1
{
    "address": "上海市闵行区",
    "age": 26,
    "birthDate": "1995-01-05",
    "createTime": 1420070400001,
    "name": "李四",
    "remark": "上海市大佬",
    "salary": 10000.0
}
```

## 批量插入数据

```
POST es-start-user/_bulk
{"index":{"_id":"1"} }
{"address": "上海市闵行区","age": 26,"birthDate": "1995-01-05","createTime": 1609825689000,"name": "李四","remark": "上海市大佬", "salary": 1000.0}
{"index":{"_id":"2"} }
{"address": "上海市黄埔区","age": 28,"birthDate": "1993-10-24","createTime": 1609825689000,"name": "张三","remark": "颜值界的扛把子","salary": 5000.0}
{"index":{"_id":"3"} }
{"address": "北京市大兴区","age": 30,"birthDate": "1990-03-15","createTime": 1609825689000,"name": "王二","remark": "家里有矿","salary": 3000.0}
```

## 删除数据

```
POST es-start-user/_delete_by_query
{
  "query": {
    "match_all": {}
  }
}
```

## 查询数据

```
GET es-start-user/_search
```

## 精确查询

```
GET es-start-user/_search
{
  "query": {
    "term": {
      "address.keyword": {
        "value": "上海市闵行区"
      }
    }
  }
}
```

```
GET es-start-user/_search
{
  "query": {
    "terms": {
      "address.keyword": [
        "上海市闵行区",
        "北京市大兴区"
      ]
    }
  }
}
```

## 匹配查询

```
GET es-start-user/_search
{
  "query": {
    "match_all": {}
  },
  "from": 0,
  "size": 10,
  "sort": [
    {
      "salary": {
        "order": "desc"
      }
    }
  ]
}
```

```
GET es-start-user/_search
{
    "query": {
    "match": {
      "address": "上海市"
    }
  }
}
```

```
POST es-start-user/_search
{ "query": 
  { "match_all": 
    {} 
  } 
}
```

### 词语匹配查询

```
GET es-start-user/_search
{
  "query": {
    "match_phrase": {
      "address": "北京市"
    }
  }
}
```

### 内容多字段查询

```
GET es-start-user/_search
{
  "query": {
    "multi_match": {
      "query": "有矿",
      "fields": ["address","remark"]
    }
  }
}
```

## 模糊查询

```
GET es-start-user/_search
{
  "query": {
    "fuzzy": {
      "name": "三"
    }
  }
}
```

## 范围查询

### 查询岁数 ≥ 30 岁的员工数据

```
GET es-start-user/_search
{
  "query": {
    "range": {
      "age": {
        "gte": 30
      }
    }
  }
}
```

### 查询生日距离现在 30 年间的员工数据

```
GET es-start-user/_search
{
  "query": {
    "range": {
      "birthDate": {
        "gte": "now-30y"
      }
    }
  }
}
```

## 通配符查询(wildcard)

```
GET es-start-user/_search
{
  "query": {
    "wildcard": {
      "name.keyword": {
        "value": "*三"
      }
    }
  }
}
```

## 布尔查询(bool)

### 查询出生在 1990-2000年期间，且地址在 上海市闵行区 的员工信息

```
GET es-start-user/_search
{
  "query": {
    "bool": {
      "filter": {
        "range": {
          "birthDate": {
            "format": "yyyy", 
            "gte": 1990,
            "lte": 2000
          }
        }
      },
      "must": [
        {
          "terms": {
            "address.keyword": [
              "上海市闵行区"
            ]
          }
        }
      ]
    }
  }
}
```

## 聚合查询

### Metric 聚合分析

#### 统计员工总数、工资最高值、工资最低值、工资平均工资、工资总和

```
GET es-start-user/_search
{
  "size": 0,
  "aggs": {
    "salary_stats": {
      "stats": {
        "field": "salary"
      }
    }
  }
}
```

####  统计员工工资最低值

```
GET es-start-user/_search
{
  "size": 0,
  "aggs": {
    "salary_min": {
      "min": {
        "field": "salary"
      }
    }
  }
}
```

#### 统计员工工资最高值

```
GET es-start-user/_search
{
  "size": 0,
  "aggs": {
    "salary_max": {
      "max": {
        "field": "salary"
      }
    }
  }
}
```

#### 统计员工工资总值

```
GET es-start-user/_search
{
  "size": 0,
  "aggs": {
    "salary_sum": {
      "sum": {
        "field": "salary"
      }
    }
  }
}
```

#### 统计员工总数

```
GET es-start-user/_search
{
  "size": 0,
  "aggs": {
    "employee_count": {
      "value_count": {
        "field": "salary"
      }
    }
  }
}
```

#### 统计员工工资百分位

```
GET es-start-user/_search
{
  "size": 0,
  "aggs": {
    "salary_percentiles": {
      "percentiles": {
        "field": "salary"
      }
    }
  }
}
```

### Bucket 聚合分析

#### 按岁数进行聚合分桶，统计各个岁数员工的人数

```
GET es-start-user/_search
{
  "size": 0,
  "aggs": {
    "age_bucket": {
      "terms": {
        "field": "age",
        "size": "10"
      }
    }
  }
}
```

#### 按工资范围进行聚合分桶，统计工资在 3000-5000、5000-9000 和 9000 以上的员工信息

```
GET es-start-user/_search
{
  "aggs": {
    "salary_range_bucket": {
      "range": {
        "field": "salary",
        "ranges": [
          {
            "key": "低级员工", 
            "to": 3000
          },{
            "key": "中级员工",
            "from": 5000,
            "to": 9000
          },{
            "key": "高级员工",
            "from": 9000
          }
        ]
      }
    }
  }
}
```

#### 按照时间范围进行分桶，统计 1985-1990 年和 1990-1995 年出生的员工信息

```
GET es-start-user/_search
{
  "size": 10,
  "aggs": {
    "date_range_bucket": {
      "date_range": {
        "field": "birthDate",
        "format": "yyyy", 
        "ranges": [
          {
            "key": "出生日期1985-1990的员工", 
            "from": "1985",
            "to": "1990"
          },{
            "key": "出生日期1990-2000的员工", 
            "from": "1990",
            "to": "2000"
          }
        ]
      }
    }
  }
}
```

#### 按工资多少进行聚合分桶，设置统计的最小值为 0，最大值为 12000，区段间隔为 3000

```
GET es-start-user/_search
{
  "size": 0,
  "aggs": {
    "salary_histogram": {
      "histogram": {
        "field": "salary",
        "extended_bounds": {
          "min": 0,
          "max": 12000
        }, 
        "interval": 3000
      }
    }
  }
}
```

### 按出生日期进行分桶

```
GET es-start-user/_search
{
  "size": 0,
  "aggs": {
    "birthday_histogram": {
      "date_histogram": {
        "format": "yyyy", 
        "field": "birthDate",
        "interval": "year"
      }
    }
  }
}
```

### Metric 与 Bucket 聚合分析

#### 按照员工岁数分桶、然后统计每个岁数员工工资最高值

```
GET es-start-user/_search
{
  "size": 0,
  "aggs": {
    "salary_bucket": {
      "terms": {
        "field": "age",
        "size": "10"
      },
      "aggs": {
        "salary_max_user": {
          "top_hits": {
            "size": 1,
            "sort": [
              {
                "salary": {
                  "order": "desc"
                }
              }
            ]
          }
        }
      }
    }
  }
}
```

