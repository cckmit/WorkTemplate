package com.blazefire.task;

import com.blazefire.service.AppInfoService;
import com.blazefire.service.entity.WxAppAccessToken;
import com.blazefire.task.wx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 获取微信数据定时任务
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 18:55
 */
@Component
public class WxDataTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxDataTask.class);

    private AppInfoService appInfoService;

    /**
     * 微信广告数据查询
     */
    private WxAdDataTask wxAdDataTask;

    private WxGetDailyRetain wxGetDailyRetain;
    private WxGetMonthlyRetain wxGetMonthlyRetain;
    private WxGetWeeklyRetain wxGetWeeklyRetain;
    private WxGetDailySummary wxGetDailySummary;
    private WxGetDailyVisitTrend wxGetDailyVisitTrend;
    private WxGetMonthlyVisitTrend wxGetMonthlyVisitTrend;
    private WxGetWeeklyVisitTrend wxGetWeeklyVisitTrend;
    private WxGetUserPortrait wxGetUserPortrait;
    private WxGetVisitDistribution wxGetVisitDistribution;
    private WxGetVisitPage wxGetVisitPage;

    @Scheduled(cron = "0 30 9-13 * * ? ")
    public void getData() {
        LOGGER.info("获取微信数据定时任务执行开始……");
        // 获取全部小游戏/小程序
        List<WxAppAccessToken> wxAppAccessTokenList = this.appInfoService.getWxAppAccessTokenList();

        this.wxAdDataTask.getData(wxAppAccessTokenList);

        this.wxGetDailyRetain.getData(wxAppAccessTokenList);
        this.wxGetMonthlyRetain.getData(wxAppAccessTokenList);
        this.wxGetWeeklyRetain.getData(wxAppAccessTokenList);
        this.wxGetDailySummary.getData(wxAppAccessTokenList);
        this.wxGetDailyVisitTrend.getData(wxAppAccessTokenList);
        this.wxGetMonthlyVisitTrend.getData(wxAppAccessTokenList);
        this.wxGetWeeklyVisitTrend.getData(wxAppAccessTokenList);
        this.wxGetUserPortrait.getData(wxAppAccessTokenList);
        this.wxGetVisitDistribution.getData(wxAppAccessTokenList);
        this.wxGetVisitPage.getData(wxAppAccessTokenList);

        LOGGER.info("获取微信数据定时任务执行结束……");
    }

    @Scheduled(cron = "0 0 13 * * ? ")
    public void checkData() {

    }

    @Autowired
    public void setAppInfoService(AppInfoService appInfoService) {
        this.appInfoService = appInfoService;
    }

    @Autowired
    public void setWxAdDataTask(WxAdDataTask wxAdDataTask) {
        this.wxAdDataTask = wxAdDataTask;
    }

    @Autowired
    public void setWxGetDailyRetain(WxGetDailyRetain wxGetDailyRetain) {
        this.wxGetDailyRetain = wxGetDailyRetain;
    }

    @Autowired
    public void setWxGetMonthlyRetain(WxGetMonthlyRetain wxGetMonthlyRetain) {
        this.wxGetMonthlyRetain = wxGetMonthlyRetain;
    }

    @Autowired
    public void setWxGetWeeklyRetain(WxGetWeeklyRetain wxGetWeeklyRetain) {
        this.wxGetWeeklyRetain = wxGetWeeklyRetain;
    }

    @Autowired
    public void setWxGetDailySummary(WxGetDailySummary wxGetDailySummary) {
        this.wxGetDailySummary = wxGetDailySummary;
    }

    @Autowired
    public void setWxGetDailyVisitTrend(WxGetDailyVisitTrend wxGetDailyVisitTrend) {
        this.wxGetDailyVisitTrend = wxGetDailyVisitTrend;
    }

    @Autowired
    public void setWxGetMonthlyVisitTrend(WxGetMonthlyVisitTrend wxGetMonthlyVisitTrend) {
        this.wxGetMonthlyVisitTrend = wxGetMonthlyVisitTrend;
    }

    @Autowired
    public void setWxGetWeeklyVisitTrend(WxGetWeeklyVisitTrend wxGetWeeklyVisitTrend) {
        this.wxGetWeeklyVisitTrend = wxGetWeeklyVisitTrend;
    }

    @Autowired
    public void setWxGetUserPortrait(WxGetUserPortrait wxGetUserPortrait) {
        this.wxGetUserPortrait = wxGetUserPortrait;
    }

    @Autowired
    public void setWxGetVisitDistribution(WxGetVisitDistribution wxGetVisitDistribution) {
        this.wxGetVisitDistribution = wxGetVisitDistribution;
    }

    @Autowired
    public void setWxGetVisitPage(WxGetVisitPage wxGetVisitPage) {
        this.wxGetVisitPage = wxGetVisitPage;
    }

}
