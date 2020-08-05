package com.blaze.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-09 21:09
 */
@Data
@TableName(schema = "persie", value = "wx_config")
public class WxConfig implements BaseEntity<WxConfig> {

    @TableId(value = "ddAppId", type = IdType.INPUT)
    private String appId;

    @TableField("ddAppSecret")
    private String appSecret;

    @TableField("ddAppPlatform")
    private String appPlatform;

    @TableField("program_type")
    private int programType;
    /**
     * banner广告
     */
    @TableField(value = "banner")
    private String banner;
    /**
     * 插屏广告
     */
    @TableField(value = "screen")
    private String init;


    /**
     * 当前数据需要缓存
     *
     * @return 缓存-key
     */
    @Override
    public Serializable getCacheKey() {
        return this.appId;
    }

}
