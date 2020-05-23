package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 广告策略
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:47
 */
@Data
@TableName(schema = "persie", value = "config_ad_strategy")
public class ConfigAdStrategy implements BaseCrudEntity<ConfigAdStrategy> {

    @TableId(value = "ddId", type = IdType.AUTO)
    private int id;

    @TableField(value = "ddName", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String name;

    @TableField(value = "ddValueExample")
    private String valueExample;

    @TableField(value = "ddDescription")
    private String description;

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
        return this.id + "-" + this.name;
    }
}
