package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-09
 */
@Data
@TableName(schema = "persie", value = "wx_group_manager")
public class WxGroupManager implements BaseCrudEntity<WxGroupManager> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * config_confirm的ddId
     */
    @TableField("cdId")
    private String cdId;

    /**
     * 微信群名
     */
    @TableField("wxGroupName")
    private String wxGroupName;

    /**
     * 群管理员
     */
    @TableField("wxGroupManager")
    private String wxGroupManager;

    /**
     * 微信号
     */
    @TableField("wxNumber")
    private String wxNumber;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private String createTime;

    /**
     * 二维码更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "updateQrCodeTime", fill = FieldFill.INSERT_UPDATE)
    private String updateQrCodeTime;

    /**
     * *微信群备注
     */
    @TableField(exist = false)
    private String ddDescribe;
    /**
     * 二维码状态 0：关闭；1开启
     */
    @TableField(exist = false)
    private Boolean ddStatus;
    /**
     * 微信群二维码地址
     */
    @TableField(exist = false)
    private String ddYes;
    /**
     * 客服二维码
     */
    @TableField(exist = false)
    private String ddNo;
    /**
     * 更新表的时间
     */
    @TableField(exist = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
