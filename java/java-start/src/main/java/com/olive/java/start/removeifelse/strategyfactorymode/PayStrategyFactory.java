package com.olive.java.start.removeifelse.strategyfactorymode;

import com.olive.java.start.removeifelse.IPay;

import java.util.HashMap;
import java.util.Map;

public class PayStrategyFactory {
    private static Map<String, IPay> PAY_REGISTERS = new HashMap<>();


    public static void register(String code, IPay iPay) {
        if (null != code && !"".equals(code)) {
            PAY_REGISTERS.put(code, iPay);
        }
    }


    public static IPay get(String code) {
        return PAY_REGISTERS.get(code);
    }
}
