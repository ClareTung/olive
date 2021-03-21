package com.olive.java.start.removeifelse.useenum;

import java.util.Arrays;

public enum MessageEnum {
    SUCCESS(1, "成功"),
    FAIL(-1, "失败"),
    TIME_OUT(-2, "网络超时"),
    PARAM_ERROR(-3, "参数错误");

    private int code;
    private String message;

    MessageEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public static MessageEnum getMessageEnum(int code) {
        return Arrays.stream(MessageEnum.values()).filter(x -> x.code == code).findFirst().orElse(null);
    }
}
