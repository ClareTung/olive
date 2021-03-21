package com.olive.java.start.removeifelse.responsibilitychainmodel;

import org.springframework.stereotype.Service;

@Service
public class AliaPayHandler extends PayHandler {

    @Override
    public void pay(String code) {
        if ("alia".equals(code)) {
            System.out.println("===发起支付宝支付===");
        } else {
            getNext().pay(code);
        }
    }

}

