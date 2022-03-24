package com.olive.java.start.removeifelse.useenum;

/**
 * @author dongtangqiang
 */
public class RuleCheckExample {
    public static void main(String[] args) {

        String result = "RULE_1";
        String checkResult = RuleCheckExceptionEnum.valueOf(result).check();
        System.out.println(checkResult);
        // 非错误规则
        String result2 = "NO_IS_RULE";
        String checkResult2 = RuleCheckExceptionEnum.valueOf(result2).check(); // 会报错
        System.out.println(checkResult2);
    }
}
