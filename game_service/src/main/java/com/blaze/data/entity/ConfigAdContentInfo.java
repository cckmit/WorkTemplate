package com.blaze.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-16 20:58
 */
@Data
@TableName(schema = "persie", value = "config_ad_content_info")
public class ConfigAdContentInfo implements BaseEntity<ConfigAdContentInfo> {

    @TableId(value = "ddTargetAppId")
    private String targetAppId;

    @TableField(value = "ddTargetAppType")
    private int targetAppType;

    @TableField(value = "ddExtraParam")
    private String extraParam;

    @TableField(value = "ddTargetEnvVersion")
    private String targetEnvVersion;

    @TableField(value = "ddTargetAppPage")
    private String targetAppPage;

    @TableField(value = "ddPromoteAppId")
    private String promoteAppId;

    @TableField(value = "ddPromoteAppName")
    private String promoteAppName;

    @TableField(value = "ddPromoteAppType")
    private int promoteAppType;

    @TableField(value = "ddPromoteEnvVersion")
    private String promoteEnvVersion;

    @TableField(value = "ddPromoteAppPage")
    private String promoteAppPage;

    @Override
    public Serializable getCacheKey() {
        return this.targetAppId;
    }

}
