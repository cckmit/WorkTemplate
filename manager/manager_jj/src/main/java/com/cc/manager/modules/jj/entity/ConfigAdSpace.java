package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 广告位
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:47
 */
@Data
@TableName(schema = "persie", value = "config_ad_space")
public class ConfigAdSpace implements BaseCrudEntity<ConfigAdSpace> {

    @TableId(value = "ddId", type = IdType.AUTO)
    private int id;

    @TableField(value = "ddName", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String name;

    @TableField(value = "ddAdType", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private int adType;

    @TableField(value = "ddAllowedOperation", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private boolean allowedOperation;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 展示数据-广告类型名称
     */
    @TableField(exist = false)
    private String adTypeName;

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
