package com.cc.manager.modules.jj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

/**
 * @author cf
 * @since 2020-05-08
 */
@Data
@TableName(schema = "persie_deamon", value = "games")
public class Games implements BaseCrudEntity<Games> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 游戏代号信息（四位数字，唯一）
     */
    @TableField("ddCode")
    private Integer ddCode;

    /**
     * 游戏名称信息
     */
    @TableField("ddName")
    private String ddName;

    /**
     * 游戏单人组队所需金币数
     */
    @TableField("ddSingleCoin")
    private Integer ddSingleCoin;

    /**
     * 游戏多人组队所需金币数
     */
    @TableField("ddMultiCoin")
    private Integer ddMultiCoin;

    /**
     * 游戏容纳人数上限
     */
    @TableField("ddMaxPlayer")
    private Integer ddMaxPlayer;

    /**
     * 游戏是否为PK
     */
    @TableField("ddIsPk")
    private Integer ddIsPk;

    /**
     * 游戏是否有效
     */
    @TableField("ddAvailable")
    private Integer ddAvailable;

    /**
     * 游戏称号相关内容
     */
    @TableField("ddTitle")
    private String ddTitle;

    /**
     * 游戏角色个数
     */
    @TableField("ddRoleCount")
    private Integer ddRoleCount;

    /**
     * 游戏手柄方案
     */
    @TableField("ddRocker")
    private Integer ddRocker;

    /**
     * 游戏引擎方案
     */
    @TableField("ddEngine")
    private Integer ddEngine;

    /**
     * 游戏屏幕分辨率
     */
    @TableField("ddResolution")
    private Integer ddResolution;

    /**
     * 是否自动选择
     */
    @TableField("ddAutoSelect")
    private Integer ddAutoSelect;

    /**
     * 复活自动选择
     */
    @TableField("ddCanSelect")
    private Boolean ddCanSelect;

    /**
     * 圈子链接地址
     */
    @TableField("ddFriendUrl")
    private String ddFriendUrl;

    /**
     * 旋转屏幕
     */
    @TableField("ddRotate")
    private Boolean ddRotate;

    /**
     * 允许单机
     */
    @TableField("ddAllowSingle")
    private Boolean ddAllowSingle;

    /**
     * 分享json資源
     */
    @TableField("ddShareRes")
    private String ddShareRes;

    /**
     * 废弃
     */
    @TableField("ddResVersion")
    private Integer ddResVersion;

    /**
     * 废弃
     */
    @TableField("ddName128u")
    private String ddName128u;

    /**
     * 废弃
     */
    @TableField("ddTitle2048u")
    private String ddTitle2048u;

    /**
     * 废弃
     */
    @TableField("ddRound1024a")
    private String ddRound1024a;

    /**
     * 废弃
     */
    @TableField("ddResPath2048u")
    private String ddResPath2048u;

    /**
     * 废弃
     */
    @TableField("ddResCount")
    private Integer ddResCount;

    /**
     * 废弃
     */
    @TableField("ddAppid")
    private String ddAppid;

    @Override
    public String getCacheKey() {
        return this.ddCode.toString();
    }

    @Override
    public String getCacheValue() {
        return this.ddCode + "-" + this.ddName;
    }

}
