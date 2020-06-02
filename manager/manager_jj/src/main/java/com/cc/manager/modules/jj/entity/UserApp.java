package com.cc.manager.modules.jj.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseStatsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(schema = "persie", value = "user_app")
public class UserApp extends BaseStatsEntity<UserApp> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户openId
     */
    @TableId("ddOId")
    private String ddOId;

    /**
     * 用户 id 信息
     */
    @TableField("ddUid")
    private String ddUid;

    /**
     * 注册appId
     */
    @TableField("ddAppId")
    private String ddAppId;

    /**
     * 客户端版本
     */
    @TableField("ddClientVersion")
    private String ddClientVersion;

    /**
     * 注册时间
     */
    @TableField("ddRegisterTime")
    private LocalDateTime ddRegisterTime;

    @TableField(exist = false)
    private boolean haveDetail = false;

}
