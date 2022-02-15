package com.olive.drools.util;

/**
 * 类DroolsStringUtils的实现描述：DroolsStringUtils
 *
 * @author dongtangqiang 2022/2/15 16:20
 */
public class DroolsStringUtils {
    public static boolean isEmpty(String param) {
        return param == null || "".equals(param);
    }
}
