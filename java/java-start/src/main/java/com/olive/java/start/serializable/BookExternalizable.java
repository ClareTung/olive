package com.olive.java.start.serializable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class BookExternalizable implements Externalizable {

    private String name;

    private transient String desc;

    private static String content = "三打白骨精";

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(content);
        out.writeUTF(desc);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        content = in.readUTF();
        desc = in.readUTF();
    }

    public String getName() {
        return name;
    }

    public BookExternalizable setName(String name) {
        this.name = name;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public BookExternalizable setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public static String getContent() {
        return content;
    }

    public static void setContent(String content) {
        BookExternalizable.content = content;
    }

    @Override
    public String toString() {
        return "BookExternalizable{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
