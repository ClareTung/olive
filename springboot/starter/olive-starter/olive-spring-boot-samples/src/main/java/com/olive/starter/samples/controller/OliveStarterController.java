package com.olive.starter.samples.controller;

import com.olive.starter.autoconfigure.annotation.AspectLog;
import com.olive.starter.autoconfigure.service.OliveService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/4 17:27
 */
@RestController
public class OliveStarterController {

    @Resource
    private OliveService oliveService;

    @AspectLog
    @GetMapping("/olive/starter/userid/get")
    public String getCurrentUserId() throws Exception {
        Thread.sleep(1500);
        return oliveService.getUserId();
    }

}
