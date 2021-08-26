package com.olive.docker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/8/26 17:55
 */
@RestController
public class DockerController {

    @GetMapping("/{name}")
    public String sayHello(@PathVariable(value = "name") String name) {
        return "hi , " + name;
    }
}
