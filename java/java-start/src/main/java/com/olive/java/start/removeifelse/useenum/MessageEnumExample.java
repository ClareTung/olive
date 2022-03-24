package com.olive.java.start.removeifelse.useenum;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author dongtangqiang
 */
public class MessageEnumExample {

    public String getMessage(int code){
        MessageEnum messageEnum = MessageEnum.getMessageEnum(code);
        return messageEnum.getMessage();
    }

    public static void main(String[] args) {
        MessageEnum messageEnum = MessageEnum.getMessageEnum(5);
        // Optional<MessageEnum> optionalMessageEnum = Optional.of(messageEnum);
        // optionalMessageEnum.isPresent() ? optionalMessageEnum.get().getMessage() : 5;

        if (messageEnum == null) {
            System.out.println("没问题");
            return;
        }
        System.out.println("有问题");
    }
}
