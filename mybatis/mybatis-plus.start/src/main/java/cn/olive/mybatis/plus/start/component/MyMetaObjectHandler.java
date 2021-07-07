package cn.olive.mybatis.plus.start.component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 实现插入时的自动填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        boolean hasGmtCreate = metaObject.hasSetter("gmtCreate");
        boolean hasGmtModified = metaObject.hasSetter("gmtModified");
        if (hasGmtCreate) {
            Object gmtCreate = this.getFieldValByName("gmtCreate", metaObject);
            if (gmtCreate == null) {
                this.strictInsertFill(metaObject, "gmtCreate", LocalDateTime.class, LocalDateTime.now());
            }
        }
        if (hasGmtModified) {
            Object gmtModified = this.getFieldValByName("gmtModified", metaObject);
            if (gmtModified == null) {
                this.strictInsertFill(metaObject, "gmtModified", LocalDateTime.class, LocalDateTime.now());
            }
        }
    }

    /**
     * 实现更新时的自动填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        boolean hasGmtModified = metaObject.hasSetter("gmtModified");
        if (hasGmtModified) {
            Object gmtModified = this.getFieldValByName("gmtModified", metaObject);
            if (gmtModified == null) {
                this.strictInsertFill(metaObject, "gmtModified", LocalDateTime.class, LocalDateTime.now());
            }
        }
    }

}
