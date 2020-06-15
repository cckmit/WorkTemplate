package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 广告内容
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:47
 */
@Data
@TableName(schema = "persie", value = "config_ad_content")
public class ConfigAdContent implements BaseCrudEntity<ConfigAdContent> {

    @TableId(value = "ddId", type = IdType.AUTO)
    private int id;

    @TableField(value = "ddAdType", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private int adType;

    @TableField(value = "ddWeight")
    private int weight = 1;

    @TableField(value = "ddImageUrl")
    private String imageUrl;

    @TableField(value = "ddVideoUrl")
    private String videoUrl;

    @TableField(value = "ddTargetAppId", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String targetAppId;

    @TableField(value = "ddTargetAppName", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String targetAppName;

    @TableField(value = "ddTargetAppDesc")
    private String targetAppDesc;

    @TableField(value = "ddTargetAppType")
    private int targetAppType;

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

    /**
     * 展示数据-由ddAdType关联得到的广告内容名称
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
        return this.targetAppName;
    }

}
