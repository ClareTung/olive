package com.olive.java.start.serializable;

import java.io.Serializable;
import java.math.BigDecimal;

public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private BigDecimal price;

    private transient String desc;

    private static String content = "西天取经";
//    private static String content = "孙悟空大闹天空";

    public String getDesc() {
        return desc;
    }

    public Book setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public String getName() {
        return name;
    }

    public Book setName(String name) {
        this.name = name;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Book setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public static String getContent() {
        return content;
    }

    public static void setContent(String content) {
        Book.content = content;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", desc='" + desc + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
