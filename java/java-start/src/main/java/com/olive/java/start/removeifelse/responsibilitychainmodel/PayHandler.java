package com.olive.java.start.removeifelse.responsibilitychainmodel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PayHandler {
    protected PayHandler next;

    public abstract void pay(String pay);

}
