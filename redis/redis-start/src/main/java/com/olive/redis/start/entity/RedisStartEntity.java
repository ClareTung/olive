package com.olive.redis.start.entity;

import lombok.Data;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/20 17:49
 */
@Data
public class RedisStartEntity {

    private Integer id;

    private String name;

    private String desc;

    public RedisStartEntity(Integer id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }
}
