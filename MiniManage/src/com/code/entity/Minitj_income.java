package com.code.entity;

import com.annotation.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Vector;

/**
 * minitj_wx 实体类
 * 2019-02-21 xuweihua
 */

@Entity
@Page
@TableName("minitj_wx")
public class Minitj_income {
    @PrimaryKey
    @Column(name = "wx_appid")
    @Comments(name = "游戏id")
    public String wx_appid;

    @ReadOnly
    @Column(name = "game_name")
    @Comments(name = "产品名称")
    public String game_name;

    @Comments(name = "总收入")
    public BigDecimal totalIncome;
    @Comments(name = "视频请求次数")
    public int runtime_videoreq;

    @Comments(name = "视频ECPM")
    public BigDecimal videoEcpm;

    @Comments(name = "banner ECPM")
    public BigDecimal bannerEcpm;


    @Comments(name = "插屏曝光量")
    public BigDecimal wx_screen_show;

    @Comments(name = "插屏点击率")
    public BigDecimal wx_screen_clickrate;

    @Comments(name = "插屏收入")
    public BigDecimal wx_screen_income;

    @Comments(name = "screen ECPM")
    public BigDecimal screenEcpm;

    @Column(name = "wx_date")
    @Comments(name = "日期")
    public String wx_date;
    @Column(name = "wx_new")
    @Comments(name = "注册用户数")
    public int wx_new;
    @Column(name = "wx_active")
    @Comments(name = "活跃用户数")
    public int wx_active;
    @Column(name = "wx_visit")
    @Comments(name = "访问用户数")
    public int wx_visit;
    @Column(name = "wx_avg_login")
    @Comments(name = "人均登录次数")
    public BigDecimal wx_avg_login;
    @Column(name = "wx_avg_online")
    @Comments(name = "人均在线时长")
    public BigDecimal wx_avg_online;
    @Column(name = "wx_remain2")
    @Comments(name = "注册次留")
    public BigDecimal wx_remain2;
    @Column(name = "wx_video_show")
    @Comments(name = "视频曝光量")
    public int wx_video_show;
    @Column(name = "wx_video_clickrate")
    @Comments(name = "视频点击率")
    public BigDecimal wx_video_clickrate;
    @Column(name = "wx_video_income")
    @Comments(name = "视频收入")
    public BigDecimal wx_video_income;
    @Column(name = "wx_banner_show")
    @Comments(name = "bannel曝光量")
    public int wx_banner_show;
    @Column(name = "wx_banner_clickrate")
    @Comments(name = "bannel点击率")
    public BigDecimal wx_banner_clickrate;
    @Column(name = "wx_banner_income")
    @Comments(name = "bannel收入")
    public BigDecimal wx_banner_income;
    @Column(name = "wx_reg_ad")
    @Comments(name = "注册用户来自广告占比")
    public int wx_reg_ad;
    @Column(name = "wx_reg_jump")
    @Comments(name = "注册用户来自跳转占比")
    public int wx_reg_jump;
    @Column(name = "wx_reg_search")
    @Comments(name = "注册用户来自搜索占比")
    public int wx_reg_search;
    @Column(name = "wx_reg_app")
    @Comments(name = "注册用户来自app占比")
    public int wx_reg_app;
    @Column(name = "wx_reg_code")
    @Comments(name = "注册用户来自二维码占比")
    public int wx_reg_code;
    @Column(name = "wx_reg_session")
    @Comments(name = "注册用户来自会话占比")
    public int wx_reg_session;
    @Column(name = "wx_active_women")
    @Comments(name = "活跃女性占比")
    public BigDecimal wx_active_women;
    @Column(name = "wx_share_user")
    @Comments(name = "分享人数")
    public int wx_share_user;
    @Column(name = "wx_share_count")
    @Comments(name = "分享次数")
    public int wx_share_count;
    @Column(name = "wx_share_rate")
    @Comments(name = "分享率")
    public BigDecimal wx_share_rate;
    @Column(name = "wx_updatetime")
    @Comments(name = "更新时间")
    public String wx_updatetime;

    public static void addSumLine(Vector<Minitj_income> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        Minitj_income income = new Minitj_income();
        int runtime_videoreq = 0,
                wx_video_show = 0,
                wx_banner_show = 0;
        BigDecimal wx_banner_income = new BigDecimal(0);
        BigDecimal totalIncome = new BigDecimal(0);
        BigDecimal wx_video_clickrate = new BigDecimal(0);
        BigDecimal videoEcpm = new BigDecimal(0);
        BigDecimal wx_banner_clickrate = new BigDecimal(0);
        BigDecimal bannerEcpm = new BigDecimal(0);
        BigDecimal wx_video_income = new BigDecimal(0);


        BigDecimal wx_screen_show = new BigDecimal(0);
        BigDecimal wx_screen_clickrate = new BigDecimal(0);
        BigDecimal wx_screen_income = new BigDecimal(0);
        BigDecimal screenEcpm = new BigDecimal(0);

        for (Minitj_income minitj_income : list) {
            if (minitj_income.totalIncome == null) minitj_income.totalIncome = new BigDecimal(0);
            totalIncome = totalIncome.add(minitj_income.totalIncome);
            runtime_videoreq += minitj_income.runtime_videoreq;
            wx_video_show += minitj_income.wx_video_show;
            wx_banner_show += minitj_income.wx_banner_show;
            wx_banner_income = wx_banner_income.add(minitj_income.wx_banner_income);
            wx_video_clickrate = wx_video_clickrate.add(minitj_income.wx_video_clickrate);
            if (minitj_income.videoEcpm == null) minitj_income.videoEcpm = new BigDecimal(0);
            videoEcpm = videoEcpm.add(minitj_income.videoEcpm);
            wx_banner_clickrate = wx_banner_clickrate.add(minitj_income.wx_banner_clickrate);
            if (minitj_income.bannerEcpm == null) minitj_income.bannerEcpm = new BigDecimal(0);
            bannerEcpm = bannerEcpm.add(minitj_income.bannerEcpm);
            if (minitj_income.wx_video_income == null) minitj_income.wx_video_income = new BigDecimal(0);
            wx_video_income = wx_video_income.add(minitj_income.wx_video_income);

            if (minitj_income.wx_screen_clickrate != null) {
                wx_screen_clickrate = wx_screen_clickrate.add(minitj_income.wx_screen_clickrate);
            }
            if (minitj_income.wx_screen_show == null) minitj_income.wx_screen_show = new BigDecimal(0);
            wx_screen_show = wx_screen_show.add(minitj_income.wx_screen_show);
            if (minitj_income.wx_screen_income == null) minitj_income.wx_screen_income = new BigDecimal(0);
            wx_screen_income = wx_screen_income.add(minitj_income.wx_screen_income);
            if (minitj_income.screenEcpm == null) minitj_income.screenEcpm = new BigDecimal(0);
            screenEcpm = screenEcpm.add(minitj_income.screenEcpm);

            income.wx_date = "总计";
            income.game_name = "-";
            income.wx_active = 0;
            income.totalIncome = totalIncome;
            income.runtime_videoreq = runtime_videoreq;
            income.wx_video_show = wx_video_show;
            income.wx_banner_show = wx_banner_show;
            income.wx_banner_income = wx_banner_income;
            BigDecimal size = new BigDecimal(list.size());
            income.wx_video_clickrate = wx_video_clickrate.divide(size, 2, BigDecimal.ROUND_HALF_UP);
            income.videoEcpm = videoEcpm.divide(size, 2, BigDecimal.ROUND_HALF_UP);
            income.wx_banner_clickrate = wx_banner_clickrate.divide(size, 2, BigDecimal.ROUND_HALF_UP);
            income.bannerEcpm = bannerEcpm.divide(size, 2, BigDecimal.ROUND_HALF_UP);
            income.wx_video_income = wx_video_income;

            income.wx_screen_show = wx_screen_show;
            income.wx_screen_clickrate = wx_screen_clickrate.divide(size, 2, BigDecimal.ROUND_HALF_UP);
            income.wx_screen_income = wx_screen_income;
            income.screenEcpm = screenEcpm.divide(size, 2, BigDecimal.ROUND_HALF_UP);
        }
        list.add(income);
    }
}
