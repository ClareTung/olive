package com.olive.java.start.removeifelse.responsibilitychainmodel;

import org.springframework.stereotype.Service;

@Service
public class WeixinPayHandler extends PayHandler {

    @Override
    public void pay(String code) {
        if ("weixin".equals(code)) {
            System.out.println("===发起微信支付===");
        } else {
            getNext().pay(code);
        }
    }
}
