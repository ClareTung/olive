package com.olive.idempotent.interceptor;

import com.olive.idempotent.annotation.AutoIdempotent;
import com.olive.idempotent.util.TokenUtilService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/9/13 10:22
 */
@Component
public class AutoIdempotentInterceptor implements HandlerInterceptor {

    private static final String TOKEN_NAME = "token";

    @Resource
    private TokenUtilService tokenUtilService;

    /**
     * 预处理
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //被ApiIdempotment标记的扫描
        AutoIdempotent methodAnnotation = method.getAnnotation(AutoIdempotent.class);
        if (methodAnnotation != null) {
            try {
                String token = request.getHeader(TOKEN_NAME);
                if (StringUtils.isEmpty(token)) {// header中不存在token
                    token = request.getParameter(TOKEN_NAME);
                    if (StringUtils.isEmpty(token)) {// parameter中也不存在token
                        throw new RuntimeException("token参数不存在");
                    }
                }
                // 获取用户信息（这里使用模拟数据）
                String userInfo = "mydlq";
                return tokenUtilService.validToken(token, userInfo);// 幂等性校验, 校验通过则放行, 校验失败则抛出异常, 并通过统一异常处理返回友好提示
            } catch (Exception ex) {
                writeReturnJson(response, ex.getMessage());
                throw ex;
            }
        }
        //必须返回true,否则会被拦截一切请求
        return true;
    }


    /**
     * 返回的json值
     *
     * @param response
     * @param json
     * @throws Exception
     */
    private void writeReturnJson(HttpServletResponse response, String json) throws Exception {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException e) {
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
