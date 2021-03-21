package cn.olive.mybatis.generator.util;

import cn.olive.mybatis.generator.convert.DateType;
import cn.olive.mybatis.generator.convert.MySqlTypeConvert;

/**
 * 获奖java中需要的驼峰命名
 */
public class MySqlToJavaUtil {
    /**
     * <p>说明:获取java类名</p>
     *
     * @param table 表名
     * @return String
     */
    public static String getClassName(String table) {
        table = changeToJavaFiled(table);
        StringBuilder sbuilder = new StringBuilder();
        char[] cs = table.toCharArray();
        cs[0] -= 32;
        sbuilder.append(String.valueOf(cs));
        return sbuilder.toString();
    }

    /**
     * <p>说明:获取字段名，把"_"后面字母变大写</p>
     *
     * @param field 字段名
     * @return String
     */
    public static String changeToJavaFiled(String field) {
        String[] fields = field.split("_");
        StringBuilder sbuilder = new StringBuilder(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            char[] cs = fields[i].toCharArray();
            cs[0] -= 32;
            sbuilder.append(String.valueOf(cs));
        }
        return sbuilder.toString();
    }


    /**
     * <p>说明:把sql的数据类型转为java需要的类型</p>
     *
     * @param sqlType sql类型
     * @return String  java类型
     */
    public static String jdbcTypeToJavaType(String sqlType) {
        MySqlTypeConvert typeConvert = new MySqlTypeConvert();
        return typeConvert.processTypeConvert(DateType.ONLY_DATE, sqlType).getType();
    }

    /**
     * <p>说明:把sql的数据类型转为java需要的类型</p>
     *
     * @param sqlType sql类型
     * @return String  java类型
     */
    public static String jdbcTypeToJavaTypePck(String sqlType) {
        MySqlTypeConvert typeConvert = new MySqlTypeConvert();
        return typeConvert.processTypeConvert(DateType.ONLY_DATE, sqlType).getPkg();
    }
}
