package com.olive.standard.project.controller;

import com.olive.standard.project.dto.TestDTO;
import com.olive.standard.project.service.TestService;
import com.olive.standard.project.vo.resp.TestResp;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/26 16:38
 */
@RestController
@RequestMapping(value = "/test/v1", produces = "application/json;charset=UTF-8")
public class TestController {

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private TestService testService;

    @GetMapping(value = "get")
    public TestResp getTestData() {
        TestDTO testDTO = testService.getTestDTO();
        return mapperFacade.map(testDTO, TestResp.class);
    }

}
