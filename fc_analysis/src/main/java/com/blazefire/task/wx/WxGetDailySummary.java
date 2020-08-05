package com.blazefire.task.wx;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blazefire.config.WxConfig;
import com.blazefire.dao.second.model.WxDailySummary;
import com.blazefire.service.entity.WxAppAccessToken;
import com.blazefire.service.wx.WxDailySummaryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * 获取用户访问小程序月留存
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-31 15:32
 */
@Component
public class WxGetDailySummary {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxGetDailySummary.class);

    private WxConfig wxConfig;
    private WxDailySummaryService wxDailySummaryService;

    /**
     * 开始查询数据
     */
    public void getData(List<WxAppAccessToken> wxAppAccessTokenList) {
        LOGGER.info("执行开始……");
        // 获取昨日时间字符串
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());

        for (WxAppAccessToken wxAppAccessToken : wxAppAccessTokenList) {
            // 只查询小程序的分析数据
            if (wxAppAccessToken.getType() == 1 && StringUtils.isNotBlank(wxAppAccessToken.getAccessToken())) {
                this.getData(yesterday, wxAppAccessToken);
            }
        }
        LOGGER.info("执行结束！");
    }

    /**
     * 查询指定App时间段数据，手动查询数据接口
     *
     * @param processObject 查询参数对象
     */
    public JSONObject getData(List<WxAppAccessToken> wxAppAccessTokenList, JSONObject processObject) {
        JSONObject resultObject = new JSONObject();
        String refDate = processObject.getString("refDate");
        if (StringUtils.isBlank(refDate)) {
            resultObject.put("result", "fail");
            resultObject.put("msg", "缺少参数：refDate！");
        } else {
            wxAppAccessTokenList.forEach(wxAppAccessToken -> {
                if (wxAppAccessToken.getType() == 1 && StringUtils.isNotBlank(wxAppAccessToken.getAccessToken())) {
                    this.getData(refDate, wxAppAccessToken);
                }
            });
            resultObject.put("result", "success");
        }
        return resultObject;
    }

    /**
     * 查询微信相关数据
     *
     * @param refDate          查询日期
     * @param wxAppAccessToken 小游戏/小程序信息
     */
    private void getData(String refDate, WxAppAccessToken wxAppAccessToken) {
        String dataUrl = this.wxConfig.getDailySummaryUrl() + "?access_token=" + wxAppAccessToken.getAccessToken();
        JSONObject paramObject = new JSONObject();
        paramObject.put("access_token", wxAppAccessToken.getAccessToken());
        paramObject.put("begin_date", refDate);
        paramObject.put("end_date", refDate);
        String dataRes = HttpUtil.post(dataUrl, paramObject.toJSONString());
        if (StringUtils.isNotBlank(dataRes)) {
            LOGGER.debug(wxAppAccessToken.getAppId() + " -> " + dataRes);
            // {"list":[{"ref_date":"20200331","visit_total":16744,"share_pv":29,"share_uv":19}]}
            JSONObject resObject = JSONObject.parseObject(dataRes);
            if (resObject != null) {
                JSONArray list = resObject.getJSONArray("list");
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        JSONObject dataObject = list.getJSONObject(i);
                        WxDailySummary wxDailySummary = new WxDailySummary();
                        wxDailySummary.setAppId(wxAppAccessToken.getAppId());
                        wxDailySummary.setRefDate(dataObject.getString("ref_date"));
                        wxDailySummary.setVisitTotal(dataObject.getInteger("visit_total"));
                        wxDailySummary.setSharePv(dataObject.getInteger("share_pv"));
                        wxDailySummary.setShareUv(dataObject.getInteger("share_uv"));
                        this.wxDailySummaryService.insert(wxDailySummary);
                    }
                }
            }
        }
    }

    @Autowired
    public void setWxConfig(WxConfig wxConfig) {
        this.wxConfig = wxConfig;
    }

    @Autowired
    public void setWxDailySummaryService(WxDailySummaryService wxDailySummaryService) {
        this.wxDailySummaryService = wxDailySummaryService;
    }

}
