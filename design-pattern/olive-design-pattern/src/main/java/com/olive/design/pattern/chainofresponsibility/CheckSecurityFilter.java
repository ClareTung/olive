package com.olive.design.pattern.chainofresponsibility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dongtangqiang
 */
@Slf4j
@Service
@Order(2)
public class CheckSecurityFilter extends AbstractHandler{
    @Override
    void doFilter(HttpServletRequest request, HttpServletResponse response) {
        log.info("安全性检查");
    }
}
