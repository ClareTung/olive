package com.olive.starter.autoconfigure.service;

import lombok.Data;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/4 17:00
 */
@Data
public class OliveService {

    private String userId;

    public OliveService(String userId) {
        this.userId = userId;
    }

    public String getCurrentUserId() {
        return userId;
    }
}
