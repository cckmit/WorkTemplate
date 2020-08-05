package com.blazefire.task.wx;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blazefire.config.WxConfig;
import com.blazefire.dao.second.model.WxVisitTrend;
import com.blazefire.service.entity.WxAppAccessToken;
import com.blazefire.service.wx.WxVisitTrendService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 获取用户访问小程序数据周趋势
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-31 15:33
 */
@Component
public class WxGetWeeklyVisitTrend {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxGetWeeklyVisitTrend.class);

    private WxConfig wxConfig;
    private WxVisitTrendService wxVisitTrendService;

    /**
     * 开始查询数据
     */
    public void getData(List<WxAppAccessToken> wxAppAccessTokenList) {
        LOGGER.info("执行开始……");
        // 每周一查询上周的数据
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        if (week == Calendar.MONDAY) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            calendar.add(Calendar.DATE, -1);
            String endDate = dateFormat.format(calendar.getTime());
            calendar.add(Calendar.DATE, -6);
            String beginDate = dateFormat.format(calendar.getTime());

            for (WxAppAccessToken wxAppAccessToken : wxAppAccessTokenList) {
                // 只查询小程序的分析数据
                if (wxAppAccessToken.getType() == 1 && StringUtils.isNotBlank(wxAppAccessToken.getAccessToken())) {
                    this.getData(beginDate, endDate, wxAppAccessToken);
                }
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
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            try {
                Date date = dateFormat.parse(refDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                // 获取周一和周日
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                String beginDate = dateFormat.format(calendar.getTime());
                calendar.add(Calendar.DATE, 6);
                String endDate = dateFormat.format(calendar.getTime());

                wxAppAccessTokenList.forEach(wxAppAccessToken -> {
                    // 只查询小程序的分析数据
                    if (wxAppAccessToken.getType() == 1 && StringUtils.isNotBlank(wxAppAccessToken.getAccessToken())) {
                        this.getData(beginDate, endDate, wxAppAccessToken);
                    }
                });
                resultObject.put("result", "success");
            } catch (ParseException e) {
                resultObject.put("msg", "日期格式错误：refDate！");
            }
            resultObject.put("result", "success");
        }
        return resultObject;
    }

    /**
     * 查询微信相关数据
     *
     * @param beginDate        开始日期
     * @param endDate          结束日期
     * @param wxAppAccessToken 小游戏/小程序信息
     */
    private void getData(String beginDate, String endDate, WxAppAccessToken wxAppAccessToken) {
        String dataUrl = this.wxConfig.getWeeklyVisitTrendUrl() + "?access_token=" + wxAppAccessToken.getAccessToken();
        JSONObject paramObject = new JSONObject();
        paramObject.put("access_token", wxAppAccessToken.getAccessToken());
        paramObject.put("begin_date", beginDate);
        paramObject.put("end_date", endDate);
        String dataRes = HttpUtil.post(dataUrl, paramObject.toJSONString());
        if (StringUtils.isNotBlank(dataRes)) {
            LOGGER.debug(wxAppAccessToken.getAppId() + " -> " + dataRes);
            JSONObject resObject = JSONObject.parseObject(dataRes);
            if (resObject != null) {
                JSONArray list = resObject.getJSONArray("list");
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        JSONObject dataObject = list.getJSONObject(i);
                        WxVisitTrend wxVisitTrend = new WxVisitTrend(wxAppAccessToken.getAppId());
                        wxVisitTrend.setRefDate(dataObject.getString("ref_date"));
                        wxVisitTrend.setSessionCnt(dataObject.getInteger("session_cnt"));
                        wxVisitTrend.setVisitPv(dataObject.getInteger("visit_pv"));
                        wxVisitTrend.setVisitUv(dataObject.getInteger("visit_uv"));
                        wxVisitTrend.setVisitUvNew(dataObject.getInteger("visit_uv_new"));
                        wxVisitTrend.setStayTimeUv(dataObject.getDouble("stay_time_uv"));
                        wxVisitTrend.setStayTimeSession(dataObject.getDouble("stay_time_session"));
                        wxVisitTrend.setVisitDepth(dataObject.getDouble("visit_depth"));
                        this.wxVisitTrendService.insertWeekly(wxVisitTrend);
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
    public void setWxVisitTrendService(WxVisitTrendService wxVisitTrendService) {
        this.wxVisitTrendService = wxVisitTrendService;
    }

}
