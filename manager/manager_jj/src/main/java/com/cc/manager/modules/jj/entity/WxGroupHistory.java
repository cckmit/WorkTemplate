package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-09
 */
@Data
@TableName(schema = "persie", value = "wx_group_history")
public class WxGroupHistory implements BaseCrudEntity<WxGroupHistory> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "createTime", fill = FieldFill.INSERT_UPDATE)
    private String createTime;

    /**
     * 二维码更新时间
     */
    @TableField("updateQrCodeTime")
    private String updateQrCodeTime;

    /**
     * 描述
     */
    @TableField("`describe`")
    private String describe;

    /**
     * 二维码状态
     */
    @TableField("ddStatus")
    private Integer ddStatus;

    /**
     * 群二维码
     */
    @TableField("ddYes")
    private String ddYes;

    /**
     * 客服二维码
     */
    @TableField("ddNo")
    private String ddNo;

    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
