package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 广告内容合集
 *
 * @author cf
 * @since 2020-06-19
 */
@Data
@TableName(schema = "persie", value = "config_ad_content_pool")
public class ConfigAdContentPool implements BaseCrudEntity<ConfigAdContentPool> {

    @TableId(value = "ddId", type = IdType.AUTO)
    private int id;

    /**
     * 广告内容合集名称
     */
    @TableField("ddName")
    private String name;

    /**
     * 广告内容合集
     */
    @TableField("ddContentIds")
    private String contentIds;

    /**
     * 数据更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Override
    public String getCacheKey() {
        return String.valueOf(this.id);
    }

    @Override
    public String getCacheValue() {
        return this.id + "-" + this.name;
    }
}
