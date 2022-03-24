package com.olive.java.start.removeifelse.useenum;

/**
 * @author dongtangqiang
 */
public enum RuleCheckExceptionEnum implements RuleCheck{
    RULE_1{
        @Override
        public String check() {
            return "error 1";
        }
    },
    RULE_2{
        @Override
        public String check() {
            return "error 2";
        }
    };
}
