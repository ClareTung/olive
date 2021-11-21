package com.olive.design.pattern.chainofresponsibility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dongtangqiang
 */
public abstract class AbstractHandler {

    /**
     * 责任链中下一个对象
     */
    private AbstractHandler nextHandler;

    public void setNextHandler(AbstractHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public AbstractHandler getNextHandler() {
        return nextHandler;
    }

    public void filter(HttpServletRequest request, HttpServletResponse response) {
        doFilter(request, response);
        if (getNextHandler() != null) {
            getNextHandler().filter(request, response);
        }
    }

    abstract void doFilter(HttpServletRequest request, HttpServletResponse response);

}
