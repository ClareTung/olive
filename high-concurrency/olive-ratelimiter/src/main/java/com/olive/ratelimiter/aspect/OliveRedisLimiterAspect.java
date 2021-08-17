package com.olive.ratelimiter.aspect;

import com.google.common.collect.Lists;
import com.olive.ratelimiter.annotation.OliveRateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

/**
 * @description: OliveRedisLimiter注解的切面类
 * @program: olive
 * @author: dtq
 * @create: 2021/8/17 17:16
 */
@Aspect
@Component
public class OliveRedisLimiterAspect {
    private final Logger logger = LoggerFactory.getLogger(OliveRedisLimiterAspect.class);

    @Resource
    private HttpServletResponse response;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private DefaultRedisScript<Long> redisScript;

    @PostConstruct
    public void init() {
        redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(("limit.lua"))));
    }

    @Pointcut("@annotation(com.olive.ratelimiter.annotation.OliveRateLimiter)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object process(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        // 使用反射获取OliveRateLimiter注解
        OliveRateLimiter rateLimiter = signature.getMethod().getDeclaredAnnotation(OliveRateLimiter.class);
        if (Objects.isNull(rateLimiter)) {
            //正常执行方法
            return proceedingJoinPoint.proceed();
        }

        //获取注解上的参数，获取配置的速率
        int limit = rateLimiter.limit();

        //List设置Lua的KEYS[1]
        String key = "ip:" + System.currentTimeMillis() / 1000;
        List<String> keyList = Lists.newArrayList(key);

        //调用Lua脚本并执行
        Long result = stringRedisTemplate.execute(redisScript, keyList, limit);
        logger.info("Lua脚本的执行结果：" + result);

        //Lua脚本返回0，表示超出流量大小，返回1表示没有超出流量大小。
        if (Objects.isNull(result) || 0 == result) {
            fullBack();
            return null;
        }

        //获取到令牌，继续向下执行
        return proceedingJoinPoint.proceed();
    }

    private void fullBack() {
        response.setHeader("Content-Type", "text/html;charset=UTF8");
        try (PrintWriter writer = response.getWriter()) {
            writer.println("服务器请求过于频繁");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}