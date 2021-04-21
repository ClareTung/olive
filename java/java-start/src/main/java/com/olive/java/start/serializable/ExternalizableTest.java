package com.olive.java.start.serializable;

import java.io.*;

public class ExternalizableTest {

    private static void serialize(BookExternalizable book) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("D:\\\\test.txt")));
        oos.writeObject(book);
        oos.close();
    }

    private static BookExternalizable deserialize() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("D:\\\\test.txt")));
        return (BookExternalizable) ois.readObject();
    }


    public static void main(String[] args) throws Exception {
//        BookExternalizable book = new BookExternalizable();
//        book.setName("西游记");
//        book.setDesc("五百年前");
//        System.out.println("序列化前的结果: " + book);
//
//        serialize(book);

        BookExternalizable dBook = deserialize();
        System.out.println("反序列化后的结果: " + dBook);
    }
}
