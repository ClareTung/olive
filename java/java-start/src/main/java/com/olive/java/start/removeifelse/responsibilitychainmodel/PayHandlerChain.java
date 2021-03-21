package com.olive.java.start.removeifelse.responsibilitychainmodel;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PayHandlerChain implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;
    private PayHandler header;


    public void handlePay(String code) {
        header.pay(code);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, PayHandler> beansOfTypeMap = applicationContext.getBeansOfType(PayHandler.class);
        if (beansOfTypeMap.size() == 0) {
            return;
        }
        List<PayHandler> handlers = new ArrayList<>(beansOfTypeMap.values());
        for (int i = 0; i < handlers.size(); i++) {
            PayHandler payHandler = handlers.get(i);
            if (i != handlers.size() - 1) {
                payHandler.setNext(handlers.get(i + 1));
            }
        }
        header = handlers.get(0);
    }
}
