package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseStatsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(schema = "persie", value = "user_info")
public class UserInfo extends BaseStatsEntity<UserInfo> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户 id 信息
     */
    @TableField("ddUid")
    private String ddUid;

    /**
     * 用户openId信息
     */
    @TableField("ddOId")
    private String ddOId;

    /**
     * 注册appId
     */
    @TableField("ddAppId")
    private String ddAppId;

    /**
     * 用户客户端版本
     */
    @TableField("ddClientVersion")
    private String ddClientVersion;

    /**
     * 用户昵称信息
     */
    @TableField("ddName")
    private String ddName;

    /**
     * 用户头像地址
     */
    @TableField("ddAvatarUrl")
    private String ddAvatarUrl;

    /**
     * 用户当前头像框
     */
    @TableField("ddAvatarFrame")
    private Integer ddAvatarFrame;

    /**
     * 用户当前获得的头像框
     */
    @TableField("ddAvatarFrameGain")
    private String ddAvatarFrameGain;

    /**
     * 用户性别
     */
    @TableField("ddSex")
    private Integer ddSex;

    /**
     * 用户省份
     */
    @TableField("ddProvince")
    private String ddProvince;

    /**
     * 用户城市
     */
    @TableField("ddCity")
    private String ddCity;

    /**
     * 用户国家
     */
    @TableField("ddCountry")
    private String ddCountry;

    /**
     * 用户语言
     */
    @TableField("ddLanguage")
    private String ddLanguage;

    /**
     * 用户奖金记录
     */
    @TableField("ddAwardList")
    private String ddAwardList;

    /**
     * 用户（永久）是否已经收藏
     */
    @TableField("ddCollected")
    private Integer ddCollected;

    /**
     * 用户（永久）是否已经关注公众号
     */
    @TableField("ddInterested")
    private Integer ddInterested;

    /**
     * 用户上次领客服奖励的时间
     */
    @TableField("ddServicedTime")
    private String ddServicedTime;

    /**
     * 用户上次领取分享奖励的时间
     */
    @TableField("ddSharedTime")
    private String ddSharedTime;

    /**
     * 用户当日观看视频广告次数
     */
    @TableField("ddDayWatchVideo")
    private Integer ddDayWatchVideo;

    /**
     * 用户每日登录奖励及领取情况
     */
    @TableField("ddDayLoginGift")
    private Integer ddDayLoginGift;

    /**
     * 用户首次登录时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "ddRegisterTime")
    private LocalDateTime ddRegisterTime;

    /**
     * 用户最新登录时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "ddLoginTime")
    private LocalDateTime ddLoginTime;
    /**
     * 展示数据-产品名称
     */
    @TableField(exist = false)
    private String productName;
    /**
     * 展示数据-剩余金币数
     */
    @TableField(exist = false)
    private Integer ddCoinCount;
    /**
     * 展示数据-剩余金额数
     */
    @TableField(exist = false)
    private Double ddMoney;
    /**
     * 展示数据-提现金额
     */
    @TableField(exist = false)
    private Integer cashOut = 0;

    @TableField(exist = false)
    private boolean haveDetail = false;

}
