package cn.olive.mybatis.generator.convert;

/**
 * 该类的功能描述
 */
public interface ITypeConvert {
    /**
     * <p>说明:执行类型转换</p>
     *
     * @param dateType  时间类型
     * @param fieldType 字段类型
     * @return ignore
     */
    IColumnType processTypeConvert(DateType dateType, String fieldType);
}
