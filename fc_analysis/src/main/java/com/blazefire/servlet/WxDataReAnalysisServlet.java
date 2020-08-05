package com.blazefire.servlet;

import com.alibaba.fastjson.JSONObject;
import com.blazefire.service.AppInfoService;
import com.blazefire.service.entity.WxAppAccessToken;
import com.blazefire.task.WxDataTask;
import com.blazefire.task.ad.AdLogAnalysisTask;
import com.blazefire.task.wx.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 数据分析刷新接口
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-14 21:14
 */
@WebServlet(urlPatterns = "/wxDataReAnalysis", name = "wxDataReAnalysis")
public class WxDataReAnalysisServlet extends BaseServlet {

    private AppInfoService appInfoService;

    private AdLogAnalysisTask adLogAnalysisTask;

    private WxDataTask wxDataTask;

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

    @Override
    protected JSONObject handle(HttpServletRequest request, JSONObject requestObject) {
        // 初始化返回值，默认执行失败
        JSONObject resultObject = new JSONObject();
        resultObject.put("result", "fail");

        if (requestObject == null) {
            resultObject.put("msg", "请求参数无法解析为Json对象！");
            return resultObject;
        }

        String type = requestObject.getString("type");
        if (StringUtils.isBlank(type)) {
            resultObject.put("msg", "缺少必要参数：type！");
            return resultObject;
        }

        List<WxAppAccessToken> wxAppAccessTokenList = this.appInfoService.getWxAppAccessTokenList();
        switch (type) {
            case "ALL":
                this.wxDataTask.getData();
                resultObject.put("result", "success");
                break;
            case "SELF_AD_LOG":
                resultObject = this.adLogAnalysisTask.beginAnalysis(requestObject);
                break;
            case "ADPOS_GENERAL":
            case "ADUNIT_GENERAL":
                resultObject = this.wxAdDataTask.getData(wxAppAccessTokenList, requestObject);
                break;
            case "DAILY_RETAIN":
                resultObject = this.wxGetDailyRetain.getData(wxAppAccessTokenList, requestObject);
                break;
            case "MONTHLY_RETAIN":
                resultObject = this.wxGetMonthlyRetain.getData(wxAppAccessTokenList, requestObject);
                break;
            case "WEEKLY_RETAIN":
                resultObject = this.wxGetWeeklyRetain.getData(wxAppAccessTokenList, requestObject);
                break;
            case "DAILY_SUMMARY":
                resultObject = this.wxGetDailySummary.getData(wxAppAccessTokenList, requestObject);
                break;
            case "DAILY_VISIT_TREND":
                resultObject = this.wxGetDailyVisitTrend.getData(wxAppAccessTokenList, requestObject);
                break;
            case "MONTHLY_VISIT_TREND":
                resultObject = this.wxGetMonthlyVisitTrend.getData(wxAppAccessTokenList, requestObject);
                break;
            case "WEEKLY_VISIT_TREND":
                resultObject = this.wxGetWeeklyVisitTrend.getData(wxAppAccessTokenList, requestObject);
                break;
            case "USER_PORTRAIT":
                resultObject = this.wxGetUserPortrait.getData(wxAppAccessTokenList, requestObject);
                break;
            case "VISIT_DISTRIBUTION":
                resultObject = this.wxGetVisitDistribution.getData(wxAppAccessTokenList, requestObject);
                break;
            case "VISIT_PAGE":
                resultObject = this.wxGetVisitPage.getData(wxAppAccessTokenList, requestObject);
                break;
            default:
                resultObject.put("msg", "未匹配到合适的执行对象！");
                break;
        }
        return resultObject;
    }


    @Autowired
    public void setAppInfoService(AppInfoService appInfoService) {
        this.appInfoService = appInfoService;
    }

    @Autowired
    public void setAdLogAnalysisTask(AdLogAnalysisTask adLogAnalysisTask) {
        this.adLogAnalysisTask = adLogAnalysisTask;
    }

    @Autowired
    public void setWxDataTask(WxDataTask wxDataTask) {
        this.wxDataTask = wxDataTask;
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
