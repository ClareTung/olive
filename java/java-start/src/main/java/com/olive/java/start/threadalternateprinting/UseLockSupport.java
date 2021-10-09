package com.olive.java.start.threadalternateprinting;

import java.util.concurrent.locks.LockSupport;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/10/9 13:50
 */
public class UseLockSupport {

    private static Thread t1;
    private static Thread t2;

    public static void main(String[] args) {
        String s1 = "123456789";
        char[] c1 = s1.toCharArray();

        String s2 = "abcdefghi";
        char[] c2 = s2.toCharArray();

        t1 = new Thread(()->{
            for (char c : c1) {
                System.out.print(c);
                LockSupport.unpark(t2);
                LockSupport.park();
            }
        });

        t2 = new Thread(()->{
            for (char c : c2) {
                LockSupport.park();
                System.out.print(c);
                LockSupport.unpark(t1);
            }
        });

        t1.start();
        t2.start();
    }
}
