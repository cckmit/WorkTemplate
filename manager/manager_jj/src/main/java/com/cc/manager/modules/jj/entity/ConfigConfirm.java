package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-09
 */
@Data
@TableName(schema = "persie", value = "config_confirm")
public class ConfigConfirm implements BaseCrudEntity<ConfigConfirm> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键标志
     */
    @TableId("ddId")
    private String ddId;

    /**
     * 描述信息
     */
    @TableField("`describe`")
    private String describe;

    /**
     * 状态
     */
    @TableField("ddStatus")
    private Boolean ddStatus;

    /**
     * 确认值
     */
    @TableField("ddYes")
    private String ddYes;

    /**
     * 否认值
     */
    @TableField("ddNo")
    private String ddNo;

    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @TableField(value = "updateTime")
    private LocalDateTime updateTime;

    @Override
    public String getCacheKey() {
        return this.ddId;
    }

    @Override
    public String getCacheValue() {
        return this.ddId + "-" + this.describe;
    }
}
