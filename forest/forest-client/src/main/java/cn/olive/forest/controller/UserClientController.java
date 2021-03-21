package cn.olive.forest.controller;

import cn.olive.forest.client.IUserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserClientController {

    @Autowired
    private IUserClient iUserClient;

    @RequestMapping("/user/password")
    public String getUserPasswordByName() {
        return "userName is Clare， password is : " + iUserClient.getUserPasswordByName("Clare");
    }

}
