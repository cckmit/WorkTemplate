package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 广告组合
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-13 16:59
 */
@Data
@TableName(schema = "persie", value = "config_ad_combination")
public class ConfigAdCombination implements BaseCrudEntity<ConfigAdCombination> {
    /**
     * 自增ID
     */
    @TableId(value = "ddId", type = IdType.AUTO)
    private int id;
    /**
     * 名称
     */
    @TableField(value = "ddName", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String name;
    /**
     * 配置Json
     */
    @JSONField(serialize = false)
    @TableField(value = "ddJson", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String combinationJson;
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
        return this.id + "-" + this.name;
    }

}
