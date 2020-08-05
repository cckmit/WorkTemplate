package com.blaze.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 广告组合
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-13 16:59
 */
@Data
@TableName(schema = "persie", value = "config_ad_content_pool")
public class ConfigAdContentPool implements BaseEntity<ConfigAdContentPool> {
    /**
     * 自增ID
     */
    @TableId(value = "ddId")
    private int id;
    /**
     * 新广告合集-广告内容列表
     */
    @TableField(value = "ddContentIds")
    private String contentIds;

    @Override
    public Serializable getCacheKey() {
        return this.id;
    }

}
