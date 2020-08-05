package com.blaze.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 广告内容
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:47
 */
@Data
@TableName(schema = "persie", value = "config_ad_content")
public class ConfigAdContent implements BaseEntity<ConfigAdContent> {

    @TableId(value = "ddId")
    private int id;

    @TableField(value = "ddAdType")
    private int adType;

    @TableField(value = "ddTargetAppId")
    private String targetAppId;

    @TableField(value = "ddTargetAppName")
    private String targetAppName;

    @TableField(value = "ddWeight")
    private int weight = 1;

    @TableField(value = "ddImageUrl")
    private String imageUrl;

    @TableField(value = "ddVideoUrl")
    private String videoUrl;

    @TableField(value = "ddTargetAppDesc")
    private String targetAppDesc;

    @Override
    public Serializable getCacheKey() {
        return this.id;
    }

}
