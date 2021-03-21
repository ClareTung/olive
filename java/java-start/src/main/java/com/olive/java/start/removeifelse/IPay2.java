package com.olive.java.start.removeifelse;

public interface IPay2 {

    //判断参数传的code是否自己可以处理，如果可以处理则走支付逻辑
    boolean support(String code);

    void pay();
}
