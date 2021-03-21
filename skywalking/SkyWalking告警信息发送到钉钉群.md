# SkyWalking告警信息发送到钉钉群

## 添加钉钉群机器人

* PC版点击左上角头像

![image-20210106180859076](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210106180859076.png)

* 自定义webhook

![image-20210106181115433](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210106181115433.png)

* 填写机器人信息

![image-20210106181221330](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210106181221330.png)

* 机器人添加完成

![image-20210106181302106](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210106181302106.png)

## SkyWalking配置告警信息

* 配置文件地址：config/alarm-settings.yml

### 配置告警规则（alarm-settings.yml）

```
rules:
  # Rule unique name, must be ended with `_rule`.
  endpoint_percent_rule:
    metrics-name: endpoint_percent
    threshold: 75
    op: <
    period: 10
    count: 3
    silence-period: 10
    message: Successful rate of endpoint {name} is lower than 75%
  service_resp_time_rule:
    metrics-name: service_resp_time
    # [Optional] Default, match all services in this metrics
    include-names:
      - common-bff
      - student-application
    threshold: 100
    op: ">"
    period: 10
    count: 1
  service_instance_resp_time_rule:
    metrics-name: service_instance_resp_time
    op: ">"
    threshold: 100
    include-names-regex: instance\_\d+
    period: 3
    count: 1
    silence-period: 5
    message: Response time of service instance {name} is more than 100ms in 1 minutes of last 3 minutes
```

### 配置发送钉钉信息

```
dingtalkHooks:
  textTemplate: |-
    {
      "msgtype": "text",
      "text": {
        "content": "Apache SkyWalking Alarm: \n %s."
      }
    }
  webhooks:
    - url: https://oapi.dingtalk.com/robot/send?access_token=yours
      secret: yours

```

## 添加完成效果

![image-20210106181715353](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210106181715353.png)

## 参考链接

* SkyWalking告警介绍文档：https://github.com/apache/skywalking/blob/v8.2.0/docs/en/setup/backend/backend-alarm.md
* 钉钉机器人使用：https://ding-doc.dingtalk.com/doc#/serverapi2/qf2nxq
* 告警规则详解：https://www.jianshu.com/p/5cc42569af6f