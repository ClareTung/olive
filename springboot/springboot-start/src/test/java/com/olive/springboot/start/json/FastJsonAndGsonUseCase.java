package com.olive.springboot.start.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.olive.springboot.start.config.GsonDateTypeAdapter;
import com.olive.springboot.start.vo.resp.UserRespVO;

import java.util.Date;
import java.util.List;

/**
 * FastJson和Gson使用案例
 *
 * @author dongtangqiang
 */
public class FastJsonAndGsonUseCase {

    public static void main(String[] args) {
        String jsonArrayStr = "[{\"id\": 1, \"userName\": \"clare\", \"hobby\": [\"play\", \"eat\"]}," +
                "{\"id\": 2, \"userName\": \"tung\", \"hobby\": [\"drink\", \"happy\"]}]";

        // fastjson 反序列化 json数组
        JSONArray fJsonArray = JSON.parseArray(jsonArrayStr);
        System.out.println(fJsonArray);
        System.out.println(fJsonArray.getJSONObject(0).getString("userName"));
        System.out.println(fJsonArray.getJSONObject(1).getString("userName"));

        /*
        [{"id":1,"userName":"clare","hobby":["play","eat"]},{"id":2,"userName":"tung","hobby":["drink","happy"]}]
        clare
        tung
         */

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        // gson 反序列化 json数组
        JsonArray gJsonArray = gson.fromJson(jsonArrayStr, JsonArray.class);
        System.out.println(gJsonArray);
        System.out.println(gJsonArray.get(0).getAsJsonObject().get("userName").getAsString());
        System.out.println(gJsonArray.get(1).getAsJsonObject().get("userName").getAsString());

        /*
        [{"id":1,"userName":"clare","hobby":["play","eat"]},{"id":2,"userName":"tung","hobby":["drink","happy"]}]
        clare
        tung
         */

        // 空对象： {} ， 空数组：[] fastjson和gson处理都一样，没有问题


        /*
        泛型处理
         */
        List<UserRespVO> userList = Lists.newArrayList();
        UserRespVO user = UserRespVO.builder().id(1000L)
                .userName("clare")
                .userNick("tang tang")
                .hobby(Lists.newArrayList("ride", "swim"))
                .birthday(new Date())
                .build();
        userList.add(user);
        userList.add(UserRespVO.builder().build());


        // fastjson
        List<UserRespVO> fUserList = JSONArray.parseArray(JSON.toJSONString(userList), UserRespVO.class);
        UserRespVO fUser = JSON.parseObject(JSON.toJSONString(user), new TypeReference<UserRespVO>() {
        });
        System.out.println(fUserList);
        System.out.println(fUser);

        // gson
        List<UserRespVO> gUserList = gson.fromJson(gson.toJson(userList), new TypeToken<List<UserRespVO>>() {
        }.getType());
        UserRespVO gUser = gson.fromJson(gson.toJson(user), new TypeToken<UserRespVO>() {
        }.getType());
        System.out.println(gUserList);
        System.out.println(gUser);

        /*
        [UserRespVO(id=1000, userName=clare, userNick=tang tang, hobby=[ride, swim], birthday=Sun May 08 16:18:21 CST 2022),
        UserRespVO(id=null, userName=null, userNick=null, hobby=null, birthday=null)]
        UserRespVO(id=1000, userName=clare, userNick=tang tang, hobby=[ride, swim], birthday=Sun May 08 16:18:21 CST 2022)
         */

        // List/Map写入

        // fastjson
        JSONObject fJsonObject = new JSONObject();
        fJsonObject.put("user", user);
        fJsonObject.put("userList", userList);
        System.out.println(fJsonObject);

        // gson
        JsonObject gJsonObject = new JsonObject();
        gJsonObject.add("user", gson.toJsonTree(user));
        gJsonObject.add("userList", gson.toJsonTree(userList));
        System.out.println(gJsonObject);

        /* 有差异
        {"userList":[{"birthday":1651998238626,"hobby":["ride","swim"],"id":1000,"userName":"clare","userNick":"tang tang"},{}],
        "user":{"$ref":"$.userList[0]"}}
        {"user":{"id":1000,"userName":"clare","userNick":"tang tang","hobby":["ride","swim"],"birthday":"May 8, 2022 4:23:58 PM"},
        "userList":[{"id":1000,"userName":"clare","userNick":"tang tang","hobby":["ride","swim"],"birthday":"May 8, 2022 4:23:58 PM"},{}]}
         */

        // 驼峰和下划线转换
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson1 = gsonBuilder.create();
        System.out.println(gson1.toJson(user));

        /*
        {"id":1000,"user_name":"clare","user_nick":"tang tang","hobby":["ride","swim"],"birthday":"May 8, 2022 4:29:36 PM"}
         */

        // Date序列化方式不同
        Gson gson2 = new GsonBuilder().registerTypeAdapter(Date.class, new GsonDateTypeAdapter()).create();
        System.out.println(gson2.toJson(user));

        /*
        {"id":1000,"userName":"clare","userNick":"tang tang","hobby":["ride","swim"],"birthday":1651999413037}
         */

        // gson 使用 toJson() 序列化 Java 对象时，返回的 JSON 字符串中没有空格，可以按json格式输出
        Gson gson3 = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson3.toJson(user));

        /*
       {
          "id": 1000,
          "userName": "clare",
          "userNick": "tang tang",
          "hobby": [
            "ride",
            "swim"
          ],
          "birthday": "May 8, 2022 6:06:27 PM"
        }
         */

        // gson 序列化时默认会忽略null字段，可以不忽略
        Gson gson4 = new GsonBuilder().serializeNulls().create();
        System.out.println(gson4.toJson(user));

    }

}
