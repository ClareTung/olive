package com.olive.fpc.bs.controller;

import com.olive.fpc.bs.entity.TestBean;
import com.olive.fpc.bs.service.TestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/11/3 18:28
 */
@RestController
public class TestController {

    @Resource
    private TestService testService;

    @PostMapping("/getMessage")
    public String getMessage(@RequestBody TestBean testBean) {
        return testService.operation(testBean);
    }

}
