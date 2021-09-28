package com.olive.springcloud.nacos.consumer.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/9/28 9:27
 */
@FeignClient(
        /*
        为了方便后期服务名发生改变时不需要修改代码，
        可以在使用该远程接口的配置文件中配置远程服务名称：remote.feign.api.name=xxxx
        没有配置则使用默认名称
         */
        name = "${remote.feign.api.name:nacos-provide}",
        path = "/np",
        contextId = "RemoteApi"
)
public interface RemoteApi {

    @GetMapping("/hello")
    String hello();

}
