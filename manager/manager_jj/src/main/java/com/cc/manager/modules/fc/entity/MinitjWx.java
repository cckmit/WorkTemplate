package com.cc.manager.modules.fc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseStatsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(schema = "minigameback", value = "minitj_wx")
public class MinitjWx extends BaseStatsEntity<MinitjWx> {
    /**
     * 游戏appid
     */
    @TableId("wx_appid")
    private String wxAppId;
    /**
     * 日期
     */
    private LocalDate wxDate;

    /**
     * 注册用户数
     */
    private Integer wxNew;

    /**
     * 活跃用户数
     */
    private Integer wxActive;

    /**
     * 访问用户数
     */
    private Integer wxVisit;

    /**
     * 人均登录次数
     */
    private BigDecimal wxAvgLogin;

    /**
     * 人均在线时长
     */
    private BigDecimal wxAvgOnline;

    /**
     * 注册次留
     */
    private BigDecimal wxRemain2;

    /**
     * 视频曝光量
     */
    private Integer wxVideoShow;

    /**
     * 视频点击率
     */
    private BigDecimal wxVideoClickrate;

    /**
     * 视频收入
     */
    private BigDecimal wxVideoIncome = new BigDecimal(0);

    /**
     * bannel曝光量
     */
    private Integer wxBannerShow;

    /**
     * bannel点击率
     */
    private BigDecimal wxBannerClickrate;

    /**
     * bannel收入
     */
    private BigDecimal wxBannerIncome = new BigDecimal(0);

    /**
     * 注册用户来自广告占比
     */
    private Integer wxRegAd;

    /**
     * 注册用户来自跳转占比
     */
    private Integer wxRegJump;

    /**
     * 注册用户来自搜索占比
     */
    private Integer wxRegSearch;

    /**
     * 注册用户来自app占比
     */
    private Integer wxRegApp;

    /**
     * 注册用户来自二维码占比
     */
    private Integer wxRegCode;

    /**
     * 注册用户来自会话占比
     */
    private Integer wxRegSession;

    /**
     * 活跃女性占比
     */
    private BigDecimal wxActiveWomen;

    /**
     * 分享人数
     */
    private Integer wxShareUser;

    /**
     * 分享次数
     */
    private Integer wxShareCount;

    /**
     * 分享率
     */
    private BigDecimal wxShareRate;

    /**
     * 注册用户来源`minitj_wx`
     */
    private String wxRegJson;

    /**
     * 活跃用户来源--新增活跃来源
     */
    private String wxActiveSource;

    /**
     * 更新时间
     */
    private LocalDateTime wxUpdatetime;
    /**
     * 产品数量
     */
    @TableField(exist = false)
    private Integer productCount;
    /**
     * 产品类型
     */
    @TableField(exist = false)
    private Integer programType;
    /**
     * 产品名称
     */
    @TableField(exist = false)
    private String productName;

    @TableField(exist = false)
    private String ddAppPlatform;

    /**
     * 充值收入
     */
    @TableField(exist = false)
    private BigDecimal recharge = new BigDecimal(0);
    /**
     * 总收入
     */
    @TableField(exist = false)
    private BigDecimal revenueCount;
    /**
     * 广告收入
     */
    @TableField(exist = false)
    private BigDecimal adRevenue = new BigDecimal(0);

    /**
     * 展示字段  新增单价  买量支出/广告新增人数
     */
    @TableField(exist = false)
    private BigDecimal wxAdNewPrice = new BigDecimal(0);
    /**
     *
     */
    @TableField(exist = false)
    private BigDecimal videoECPM;
    /**
     *
     */
    @TableField(exist = false)
    private BigDecimal bannerECPM;
    /**
     * 新增用户来源展示字段-- 任务栏-我的小程序
     */
    @TableField(exist = false)
    private Integer wxRegTaskBarMySp = 0;
    /**
     * 新增用户来源展示字段-- 发现-我的小程序
     */
    @TableField(exist = false)
    private Integer wxRegFindMySp = 0;
    /**
     * 新增用户来源展示字段-- 任务栏-最近使用
     */
    @TableField(exist = false)
    private Integer wxRegTaskBarRecent = 0;
    /**
     * 新增用户来源展示字段-- 其他
     */
    @TableField(exist = false)
    private Integer wxRegOther = 0;
    /**
     * 新增用户来源展示字段-- 其他小程序
     */
    @TableField(exist = false)
    private Integer wxRegOtherSp = 0;
    /**
     * 新增用户来源展示字段-- 其他小程序返回
     */
    @TableField(exist = false)
    private Integer wxRegOtherReturn = 0;
    /**
     * 展示数据-买量支出
     */
    @TableField(exist = false)
    private BigDecimal buyCost = new BigDecimal(0);
    /**
     * 展示数据-买量单价
     */
    @TableField(exist = false)
    private BigDecimal buyClickPrice = new BigDecimal(0);
    /**
     * 展示数据--插屏收入
     */
    @TableField(exist = false)
    private BigDecimal screenIncome = new BigDecimal(0);
    /**
     * 活跃来源展示字段--广告
     */
    @TableField(exist = false)
    private Integer wxActiveAd = 0;
    /**
     * 活跃来源展示字段-- 任务栏-我的小程序
     */
    @TableField(exist = false)
    private Integer wxActiveTaskBarMySp = 0;
    /**
     * 活跃来源展示字段-- 发现-我的小程序
     */
    @TableField(exist = false)
    private Integer wxActiveFindMySp = 0;
    /**
     * 活跃来源展示字段-- 任务栏-最近使用
     */
    @TableField(exist = false)
    private Integer wxActiveTaskBarRecent = 0;
    /**
     * 活跃来源展示字段-- 搜索
     */
    @TableField(exist = false)
    private Integer wxActiveSearch = 0;
    /**
     * 活跃来源展示字段-- 其他
     */
    @TableField(exist = false)
    private Integer wxActiveOther = 0;
    /**
     * 活跃来源展示字段-- 其他小程序返回
     */
    @TableField(exist = false)
    private Integer wxActiveOtherReturn = 0;
    /**
     * 活跃来源展示字段-- 其他小程序
     */
    @TableField(exist = false)
    private Integer wxActiveOtherSp = 0;
    /**
     * 活跃
     */
    @TableField(exist = false)
    private BigDecimal activeUp = new BigDecimal(0);
    /**
     *
     */
    @TableField(exist = false)
    private boolean haveDetail = false;

}
