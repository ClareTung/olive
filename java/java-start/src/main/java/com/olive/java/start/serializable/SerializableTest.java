package com.olive.java.start.serializable;

import java.io.*;
import java.math.BigDecimal;

public class SerializableTest {
    private static void serialize(Book book) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("D:\\\\test.txt")));
        oos.writeObject(book);
        oos.close();
    }

    private static Book deserialize() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("D:\\\\test.txt")));
        return (Book) ois.readObject();
    }


    public static void main(String[] args) throws Exception {
//        Book book = new Book();
//        book.setName("西游记");
//        book.setPrice(BigDecimal.valueOf(45.8));
//        book.setDesc("五百年前");
//        System.out.println("序列化前的结果: " + book);
//
//        serialize(book);

        Book dBook = deserialize();
        System.out.println("反序列化后的结果: " + dBook);
    }
}
