package cn.olive.mybatis.generator.convert;

/**
 * 数据库时间类型 到 实体类时间类型 对应策略
 */
public enum DateType {
    /**
     * <p>只使用 java.util.date 代替</p>
     */
    ONLY_DATE,
    /**
     * <p>使用 java.sql 包下的</p>
     */
    SQL_PACK,
    /**
     * <p>使用 java.time 包下的</p>
     * <p>java8 新的时间类型</p>
     */
    TIME_PACK
}
