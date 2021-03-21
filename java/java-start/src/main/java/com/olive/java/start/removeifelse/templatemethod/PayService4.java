package com.olive.java.start.removeifelse.templatemethod;

import com.olive.java.start.removeifelse.IPay2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PayService4 implements ApplicationContextAware, InitializingBean {
    private ApplicationContext applicationContext;
    private List<IPay2> payList = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (payList == null) {
            payList = new ArrayList<>();
            Map<String, IPay2> beansOfType = applicationContext.getBeansOfType(IPay2.class);

            beansOfType.forEach((key, value) -> payList.add(value));
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void toPay(String code) {
        for (IPay2 iPay : payList) {
            if (iPay.support(code)) {
                iPay.pay();
            }
        }
    }
}
