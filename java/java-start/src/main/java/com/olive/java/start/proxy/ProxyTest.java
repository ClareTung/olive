package com.olive.java.start.proxy;

import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @description: 代理测试
 * @program: olive
 * @author: dtq
 * @create: 2021/9/24 13:57
 */
public class ProxyTest {

    @Test
    public void testReflect() throws Exception {
        Class<UserServiceImpl> clazz = UserServiceImpl.class;
        Method queryUserInfo = clazz.getMethod("queryUserInfo");
        Object invoke = queryUserInfo.invoke(clazz.newInstance());
        System.out.println(invoke);
    }

    @Test
    public void testJDKProxy() throws Exception {
        IUserService userService = JDKProxy.getProxy(IUserService.class);
        String result = userService.queryUserInfo();
        System.out.println("JDKProxy：" + result);
    }

    @Test
    public void testCglibProxy() {
        CglibProxy cglibProxy = new CglibProxy();
        IUserService userService = (IUserService) cglibProxy.newInstall(new UserServiceImpl());
        String result = userService.queryUserInfo();
        System.out.println("Cglib:" + result);
    }

    @Test
    public void testASMProxy() throws Exception {
        IUserService userService = ASMProxy.getProxy(UserServiceImpl.class);
        String result = userService.queryUserInfo();
        System.out.println("ASM:" + result);
    }

    @Test
    public void testByteBuddyProxy() throws Exception {
        IUserService userService = ByteBuddyProxy.getProxy(UserServiceImpl.class);
        String result = userService.queryUserInfo();
        System.out.println("ByteBuddy:" + result);
    }

    @Test
    public void testJavassistProxy() throws Exception {
        IUserService userService = JavassistProxy.getProxy(UserServiceImpl.class);
        String result = userService.queryUserInfo();
        System.out.println("Javassist:" + result);
    }
}
