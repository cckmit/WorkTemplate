package com.blaze.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-24 11:04
 */
@Data
@TableName(schema = "persie", value = "app_config")
public class WxAppConfig implements BaseEntity<WxAppConfig> {

    @TableId(value = "ddAppId")
    private String appId;

    @TableField(value = "ddCheckVersion")
    private String checkVersion;

    @Override
    public Serializable getCacheKey() {
        return this.appId;
    }

}
