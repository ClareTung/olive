package com.olive.java.start.removeifelse.templatemethod;

import com.olive.java.start.removeifelse.IPay2;
import org.springframework.stereotype.Service;

@Service
public class AliaPay2 implements IPay2 {
    @Override
    public boolean support(String code) {
        return "alia".equals(code);
    }

    @Override
    public void pay() {
        System.out.println("===发起支付宝支付===");
    }
}
