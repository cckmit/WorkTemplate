package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 广告类型
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:47
 */
@Data
@TableName(schema = "persie", value = "config_ad_type")
public class ConfigAdType implements BaseCrudEntity<ConfigAdType> {
    /**
     * 广告类型ID
     */
    @TableId(value = "ddId", type = IdType.AUTO)
    private Integer id;
    /**
     * 广告类型名称
     */
    @TableField(value = "ddName", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String name;
    /**
     * 数据更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Override
    @JSONField(serialize = false, deserialize = false)
    public String getCacheKey() {
        return String.valueOf(this.id);
    }

    @Override
    @JSONField(serialize = false, deserialize = false)
    public String getCacheValue() {
        return this.name;
    }
}
