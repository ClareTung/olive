
package com.olive.liteflow.start.enums;

/**
 * 商品来源枚举
 */
public enum SkuSourceEnum {
	RAW(0, "自购"),
	GIFT(3, "买赠"),
	ADDITION(4,"换购"),
	BENEFIT(5,"权益商品");

    private Integer code;

    private String name;

    SkuSourceEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
