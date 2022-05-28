package com.olive.spirngframwork.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 类UserDao的实现描述：UserDao
 *
 * @author dongtangqiang 2022/5/28 18:08
 */
public class UserDao {
    private static Map<String, String> hashMap = new HashMap<>();

    static {
        hashMap.put("10001", "Clare");
        hashMap.put("10002", "Tung");
        hashMap.put("10003", "Dong");
    }

    public String queryUserName(String uId) {
        return hashMap.get(uId);
    }

}
