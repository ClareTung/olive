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
@Order(4)
public class CheckRuleHandler extends AbstractHandler{
    @Override
    void doFilter(HttpServletRequest request, HttpServletResponse response) {
        log.info("自定义规则检查");
    }
}
