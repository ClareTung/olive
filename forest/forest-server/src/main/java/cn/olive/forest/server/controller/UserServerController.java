package cn.olive.forest.server.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserServerController {

    @RequestMapping("/userService/getUserPasswordByName")
    public String getUserPasswordByName(String userName) {
        if (userName == null) {
            throw new RuntimeException("用户名不能为空");
        }

        return "123456";
    }
}
