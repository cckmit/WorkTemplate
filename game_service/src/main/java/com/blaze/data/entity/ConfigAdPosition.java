package com.blaze.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 广告位置
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:47
 */
@Data
@TableName(schema = "persie", value = "config_ad_position")
public class ConfigAdPosition implements BaseEntity<ConfigAdPosition> {

    @TableId(value = "ddId")
    private int id;

    @TableField(value = "ddMinVersion")
    private String minVersion;

    @TableField(value = "ddAdTypes")
    private String adTypes;

    @TableField(value = "ddAllowedOperation")
    private boolean allowedOperation;

    @TableField(value = "ddShowWxAd")
    private boolean showWxAd;

    @TableField(value = "ddShowWxReVideoAd")
    private boolean showWxReVideoAd;

    @Override
    public Serializable getCacheKey() {
        return this.id;
    }

}
