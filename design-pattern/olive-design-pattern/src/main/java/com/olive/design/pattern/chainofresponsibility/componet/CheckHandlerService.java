package com.olive.design.pattern.chainofresponsibility.componet;

import com.olive.design.pattern.chainofresponsibility.AbstractHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author dongtangqiang
 */
@Component
public class CheckHandlerService {

    // 自动注入各个责任链的对象
    @Autowired
    private List<AbstractHandler> handlerList;

    private AbstractHandler abstractHandler;


    @PostConstruct
    public void initializeChainFilter() {
        for (int i = 0; i < handlerList.size(); i++) {
            if (i == 0) {
                abstractHandler = handlerList.get(0);
            } else {
                AbstractHandler currentHandler = handlerList.get(i - 1);
                AbstractHandler nextHandler = handlerList.get(i);
                currentHandler.setNextHandler(nextHandler);
            }
        }
    }

    public HttpServletResponse exec(HttpServletRequest request, HttpServletResponse response) {
        abstractHandler.filter(request, response);
        return response;
    }


    public AbstractHandler getAbstractHandler() {
        return abstractHandler;
    }

    public void setAbstractHandler(AbstractHandler abstractHandler) {
        this.abstractHandler = abstractHandler;
    }


}
