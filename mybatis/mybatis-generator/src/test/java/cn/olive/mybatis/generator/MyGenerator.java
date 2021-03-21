package cn.olive.mybatis.generator;

import cn.olive.mybatis.generator.entity.BasisInfo;
import cn.olive.mybatis.generator.util.EntityInfoUtil;
import cn.olive.mybatis.generator.util.Generator;
import cn.olive.mybatis.generator.util.MySqlToJavaUtil;

import java.sql.SQLException;
import java.util.Date;

/**
 * 自动生成工具
 */
public class MyGenerator {
    // 基础信息：项目名、作者、版本
    public static final String PROJECT = "olive-test";
    public static final String AUTHOR = "ClareTung";
    public static final String VERSION = "V1.0";
    // 数据库连接信息：连接URL、用户名、秘密、数据库名
    public static final String URL = "jdbc:mysql://127.0.0.1:3306/sharding?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&failOverReadOnly=false&useSSL=true&serverTimezone=UTC";
    public static final String NAME = "root";
    public static final String PASS = "root";
    public static final String DATABASE = "mybatis";
    // 类信息：类名、对象名（一般是【类名】的首字母小些）、类说明、时间
    public static final String CLASSNAME = "AdminUser";
    public static final String TABLE = "t_order_2";
    public static final String CLASS_COMMENT = "用户";
    public static final String TIME = "2020年11月7日";
    public static final String AGILE = new Date().getTime() + "";
    // 路径信息，分开路径方便聚合工程项目，微服务项目
    public static final String ENTITY_URL = "cn.olive.test.entity";
    public static final String DAO_URL = "cn.olive.test.dao";
    public static final String XML_URL = "cn.olive.test.dao.impl";
    public static final String SERVICE_URL = "cn.olive.test.service";
    public static final String SERVICE_IMPL_URL = "cn.olive.test.service.impl";
    public static final String CONTROLLER_URL = "cn.olive.test.web";
    //是否是Swagger配置
    public static final String IS_SWAGGER = "true";

    public static void main(String[] args) {
        BasisInfo bi = new BasisInfo(PROJECT, AUTHOR, VERSION, URL, NAME, PASS, DATABASE, TIME, AGILE, ENTITY_URL,
                DAO_URL, XML_URL, SERVICE_URL, SERVICE_IMPL_URL, CONTROLLER_URL, IS_SWAGGER);
        bi.setTable(TABLE);
        bi.setEntityName(MySqlToJavaUtil.getClassName(TABLE));
        bi.setObjectName(MySqlToJavaUtil.changeToJavaFiled(TABLE));
        bi.setEntityComment(CLASS_COMMENT);
        try {
            bi = EntityInfoUtil.getInfo(bi);
            String fileUrl = "C:\\Users\\Administrator\\Desktop\\temp\\";// 生成文件存放位置
            //开始生成文件
            String aa1 = Generator.createEntity(fileUrl, bi).toString();
            String aa2 = Generator.createDao(fileUrl, bi).toString();
            String aa3 = Generator.createDaoImpl(fileUrl, bi).toString();
            String aa4 = Generator.createService(fileUrl, bi).toString();
            String aa5 = Generator.createServiceImpl(fileUrl, bi).toString();
            String aa6 = Generator.createController(fileUrl, bi).toString();
            // 是否创建swagger配置文件
            String aa7 = Generator.createSwaggerConfig(fileUrl, bi).toString();

            System.out.println(aa1);
            System.out.println(aa2);
            System.out.println(aa3);
            System.out.println(aa4);
            System.out.println(aa5);
            System.out.println(aa6);
            System.out.println(aa7);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
