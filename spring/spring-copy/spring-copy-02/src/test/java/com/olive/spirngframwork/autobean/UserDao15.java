package com.olive.spirngframwork.autobean;

import java.util.HashMap;
import java.util.Map;

import com.olive.springframwork.stereotype.Component;

/**
 * 类UserDao15的实现描述：UserDao15
 *
 * @author dongtangqiang 2022/7/10 14:56
 */
@Component
public class UserDao15 {
    private static Map<String, String> hashMap = new HashMap<>();

    static {
        hashMap.put("10001", "Clare，shanghai，songjiang");
        hashMap.put("10002", "Tung，上海，虹口");
        hashMap.put("10003", "D，hanghzou，xihu");
    }

    public String queryUserName(String uId) {
        return hashMap.get(uId);
    }

}
