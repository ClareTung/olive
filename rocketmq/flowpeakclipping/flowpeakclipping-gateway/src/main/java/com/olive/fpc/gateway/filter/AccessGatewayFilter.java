package com.olive.fpc.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.olive.fpc.gateway.response.BaseResponse;
import com.olive.fpc.gateway.response.TokenForbiddenResponse;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/11/3 14:28
 */
@Configuration
public class AccessGatewayFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestUri = request.getPath().pathWithinApplication().value();
        /**
         * 如果需要削峰的请求是需要权限认证，则在网关的全局过滤器拿到jwt，再在全局过滤器中调用auth服务
         * 根据token拿到权限信息返回，然后在判断用户是否拥有请求该路由的权限，没有则返回无权访问，有就执行filer.chain
         * request.getheaders().get("authentication")->authService.getPermission(token)->checkUserPermission(permission,requestUri,user)
         * 这里因为权限服务比较复杂，直接对请求的路由放行
         */
        if (requestUri.contains("/getMessage")) {
            return chain.filter(exchange);//直接放行
        } else {//需要权限认证则走这里,进行权限认证
            return getVoidMono(exchange, new TokenForbiddenResponse("User Forbidden!Does not has Permission!"));
        }
    }

    /**
     * 网关抛异常
     *
     * @param body
     */
    private Mono<Void> getVoidMono(ServerWebExchange serverWebExchange, BaseResponse body) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
        byte[] bytes = JSONObject.toJSONString(body).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(bytes);
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }

}
