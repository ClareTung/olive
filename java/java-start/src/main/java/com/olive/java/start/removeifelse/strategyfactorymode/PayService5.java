package com.olive.java.start.removeifelse.strategyfactorymode;

import org.springframework.stereotype.Service;

@Service
public class PayService5 {
    public void toPay(String code) {
        PayStrategyFactory.get(code).pay();
    }
}
