package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-16 20:58
 */
@Data
@TableName(schema = "persie", value = "config_ad_content_info")
public class ConfigAdContentInfo implements BaseCrudEntity<ConfigAdContentInfo> {

    @TableId(value = "ddTargetAppId", type = IdType.INPUT)
    private String targetAppId;

    @TableField(value = "ddTargetAppType")
    private int targetAppType;

    @TableField(value = "ddTargetAppName")
    private String targetAppName;

    @TableField(value = "ddExtraParam")
    private String extraParam;

    @TableField(value = "ddTargetEnvVersion")
    private String targetEnvVersion;

    @TableField(value = "ddTargetAppPage")
    private String targetAppPage;

    @TableField(value = "ddPromoteAppId", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String promoteAppId;

    @TableField(value = "ddPromoteAppName", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String promoteAppName;

    @TableField(value = "ddPromoteAppType")
    private int promoteAppType;

    @TableField(value = "ddPromoteEnvVersion")
    private String promoteEnvVersion;

    @TableField(value = "ddPromoteAppPage")
    private String promoteAppPage;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Override
    @JSONField(serialize = false, deserialize = false)
    public String getCacheKey() {
        return this.targetAppId;
    }

    @Override
    @JSONField(serialize = false, deserialize = false)
    public String getCacheValue() {
        return this.targetAppId + "-" + this.targetAppName;
    }

}
