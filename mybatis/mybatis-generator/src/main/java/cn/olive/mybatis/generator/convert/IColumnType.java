package cn.olive.mybatis.generator.convert;

/**
 * 获取实体类字段属性类信息接口
 */
public interface IColumnType {
    /**
     * <p>获取字段类型</p>
     *
     * @return 字段类型
     */
    String getType();

    /**
     * <p> 获取字段类型完整名</p>
     *
     * @return 字段类型完整名
     */
    String getPkg();
}
