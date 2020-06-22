package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 广告位置
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:47
 */
@Data
@TableName(schema = "persie", value = "config_ad_position")
public class ConfigAdPosition implements BaseCrudEntity<ConfigAdPosition> {

    @TableId(value = "ddId", type = IdType.AUTO)
    private int id;

    @TableField(value = "ddName", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String name;

    @TableField(value = "ddMinVersion")
    private String minVersion;

    @TableField(value = "ddAdTypes", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String adTypes;

    @TableField(value = "ddAllowedOperation", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private boolean allowedOperation;

    @TableField(value = "ddShowWxAd")
    private boolean showWxAd;

    @TableField(value = "ddShowWxReVideoAd")
    private boolean showWxReVideoAd;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 展示数据-由ddAdTypes关联的广告类型名称
     */
    @TableField(exist = false)
    private String adTypeNames;

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
