package com.cc.manager.modules.jj.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-09
 */
@Data
@TableName(schema = "persie", value = "app_config")
public class AppConfig implements BaseCrudEntity<AppConfig> {
    private static final long serialVersionUID = 1L;

    /**
     * AppId
     */
    @TableId("ddAppId")
    private String id;

    /**
     * 审核版本
     */
    @TableField("ddCheckVersion")
    private String ddCheckVersion;

    /**
     * 0-小游戏,1-小程序，2-公众号
     */
    @TableField("ddProgram")
    private Integer ddProgram;

    /**
     * 应用名称
     */
    @TableField("ddName")
    private String ddName;

    /**
     * 小程序连接
     */
    @TableField("ddGameUrl")
    private String ddGameUrl;

    /**
     * 游戏合集
     */
    @TableField("ddCode")
    private Integer ddCode;

    /**
     * 审核合集
     */
    @TableField("ddCheckCode")
    private Integer ddCheckCode;

    /**
     * 新增时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField("ddTime")
    private LocalDateTime ddTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 展示字段--审核合集名称
     */
    @TableField(exist = false)
    private String checkCodeName;

}
