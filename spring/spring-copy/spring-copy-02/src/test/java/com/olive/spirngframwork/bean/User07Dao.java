package com.olive.spirngframwork.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 类User07Dao的实现描述：User07Dao
 *
 * @author dongtangqiang 2022/6/4 20:40
 */
public class User07Dao {
    private static Map<String, String> hashMap = new HashMap<>();

    public void initDataMethod(){
        System.out.println("执行：init-method");
        hashMap.put("10001", "1");
        hashMap.put("10002", "2");
        hashMap.put("10003", "3");
    }

    public void destroyDataMethod(){
        System.out.println("执行：destroy-method");
        hashMap.clear();
    }

    public String queryUserName(String uId) {
        return hashMap.get(uId);
    }
}
