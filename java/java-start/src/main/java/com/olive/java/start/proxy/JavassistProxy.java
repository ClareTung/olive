package com.olive.java.start.proxy;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * @description: JavassistProxy
 * @program: olive
 * @author: dtq
 * @create: 2021/9/24 16:28
 */
public class JavassistProxy extends ClassLoader {

    public static <T> T getProxy(Class clazz) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        // 获取类
        CtClass ctClass = pool.get(clazz.getName());
        // 获取方法
        CtMethod ctMethod = ctClass.getDeclaredMethod("queryUserInfo");
        // 方法前加强
        ctMethod.insertBefore("{System.out.println(\"" + ctMethod.getName() + " 你被代理了，By Javassist\");}");

        byte[] bytes = ctClass.toBytecode();

        return (T) new JavassistProxy().defineClass(clazz.getName(), bytes, 0, bytes.length).newInstance();
    }
}
