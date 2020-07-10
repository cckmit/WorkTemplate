package com.cc.manager.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 字段填充策略
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-11 13:37
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasGetter("createTime") && metaObject.hasGetter("createTime")) {
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        }
        if (metaObject.hasGetter("updateTime") && metaObject.hasGetter("updateTime")) {
            this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }
        if (metaObject.hasGetter("times") && metaObject.hasGetter("times")) {
            this.strictInsertFill(metaObject, "times", LocalDateTime.class, LocalDateTime.now());
        }
        if (metaObject.hasGetter("insertTime") && metaObject.hasGetter("times")) {
            this.strictInsertFill(metaObject, "insertTime", LocalDateTime.class, LocalDateTime.now());
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasGetter("updateTime") && metaObject.hasGetter("updateTime")) {
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }
        if (metaObject.hasGetter("times") && metaObject.hasGetter("times")) {
            this.strictUpdateFill(metaObject, "times", LocalDateTime.class, LocalDateTime.now());
        }
    }

}
