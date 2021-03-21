package cn.olive.forest.client;

import com.dtflys.forest.annotation.Get;

public interface IUserClient {

    @Get(url = "http://localhost:8080/forest-server/userService/getUserPasswordByName?userName=${0}")
    String getUserPasswordByName(String userName);

}
