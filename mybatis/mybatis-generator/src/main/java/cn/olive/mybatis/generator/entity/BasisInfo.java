package cn.olive.mybatis.generator.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasisInfo implements Serializable {
    private static final long serialVersionUID = 593280805192161207L;

    private String project;

    private String author;

    private String version;

    private String dbUrl;

    private String dbName;

    private String dbPassword;

    private String database;

    private String table;

    private String entityName;

    private String objectName;

    private String entityComment;

    private String createTime;

    private String agile;

    private String entityUrl;

    private String daoUrl;

    private String mapperUrl;

    private String serviceUrl;

    private String serviceImplUrl;

    private String abstractControllerUrl;

    private String controllerUrl;

    private String swaggerConfigUrl;

    private String idType;

    private String idJdbcType;

    private List<PropertyInfo> cis;

    private String isSwagger = "true";

    private Set<String> pkgs = new HashSet<>();

    public BasisInfo(String project, String author, String version, String dbUrl, String dbName, String dbPassword,
                     String database, String createTime, String agile, String entityUrl, String daoUrl, String mapperUrl,
                     String serviceUrl, String serviceImplUrl, String controllerUrl, String isSwagger) {
        super();
        this.project = project;
        this.author = author;
        this.version = version;
        this.dbUrl = dbUrl;
        this.dbName = dbName;
        this.dbPassword = dbPassword;
        this.database = database;
        this.createTime = createTime;
        this.agile = agile;
        this.entityUrl = entityUrl;
        this.daoUrl = daoUrl;
        this.mapperUrl = mapperUrl;
        this.serviceUrl = serviceUrl;
        this.serviceImplUrl = serviceImplUrl;
        this.controllerUrl = controllerUrl;
        this.abstractControllerUrl = controllerUrl.substring(0, controllerUrl.lastIndexOf(".")) + ".aid";
        this.swaggerConfigUrl = controllerUrl.substring(0, controllerUrl.lastIndexOf(".")) + ".config";
        this.isSwagger = isSwagger;
    }
}
