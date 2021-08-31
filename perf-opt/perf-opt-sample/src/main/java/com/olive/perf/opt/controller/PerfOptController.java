package com.olive.perf.opt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/8/31 17:52
 */
@RestController
public class PerfOptController {

    @GetMapping("/perf/opt/test")
    public String test() {

        int temp = 1;
        while (temp < 10000) {
            temp = temp * 10 / 10 + 10 - 10 + 1;
        }

        return "success";
    }

}
