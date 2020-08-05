package com.blazefire.task.wx;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blazefire.config.WxConfig;
import com.blazefire.dao.second.model.WxDailyRetain;
import com.blazefire.service.entity.WxAppAccessToken;
import com.blazefire.service.wx.WxDailyRetainService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 获取用户访问小程序日留存
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-31 15:31
 */
@Component
public class WxGetDailyRetain {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxGetDailyRetain.class);

    private WxConfig wxConfig;
    private WxDailyRetainService wxDailyRetainService;

    /**
     * 开始查询数据
     */
    public void getData(List<WxAppAccessToken> wxAppAccessTokenList) {
        LOGGER.info("执行开始……");
        // 获取昨日时间字符串
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        List<String> dateList = new ArrayList<>(30);
        for (int i = 1; i <= 30; i++) {
            calendar.add(Calendar.DATE, -1);
            dateList.add(dateFormat.format(calendar.getTime()));
        }

        for (WxAppAccessToken wxAppAccessToken : wxAppAccessTokenList) {
            // 只查询小程序的分析数据
            if (wxAppAccessToken.getType() == 1 && StringUtils.isNotBlank(wxAppAccessToken.getAccessToken())) {
                dateList.forEach(date -> this.getData(date, wxAppAccessToken));
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
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateFormat.parse(refDate));
                List<String> dateList = new ArrayList<>(30);
                for (int i = 0; i < 30; i++) {
                    calendar.add(Calendar.DATE, -1);
                    dateList.add(dateFormat.format(calendar.getTime()));
                }

                wxAppAccessTokenList.forEach(wxAppAccessToken -> {
                    if (wxAppAccessToken.getType() == 1 && StringUtils.isNotBlank(wxAppAccessToken.getAccessToken())) {
                        dateList.forEach(date -> this.getData(date, wxAppAccessToken));
                    }
                });
                resultObject.put("result", "success");
            } catch (ParseException e) {
                resultObject.put("msg", "日期格式错误：refDate！");
            }
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
        String dataUrl = this.wxConfig.getDailyRetainUrl() + "?access_token=" + wxAppAccessToken.getAccessToken();
        JSONObject paramObject = new JSONObject();
        paramObject.put("access_token", wxAppAccessToken.getAccessToken());
        paramObject.put("begin_date", refDate);
        paramObject.put("end_date", refDate);
        String dataRes = HttpUtil.post(dataUrl, paramObject.toJSONString());
        if (StringUtils.isNotBlank(dataRes)) {
            LOGGER.debug(wxAppAccessToken.getAppId() + "|" + refDate + " -> " + dataRes);
            // {"ref_date":"20200331","visit_uv_new":[{"key":0,"value":6}],"visit_uv":[{"key":0,"value":6}]}
            JSONObject resObject = JSONObject.parseObject(dataRes);
            if (resObject != null && refDate.equals(resObject.getString("ref_date"))) {
                WxDailyRetain wxDailyRetainNew = getDailyRetain(refDate, wxAppAccessToken, resObject, "visit_uv_new");
                WxDailyRetain wxDailyRetain = getDailyRetain(refDate, wxAppAccessToken, resObject, "visit_uv");
                this.wxDailyRetainService.insert(wxDailyRetainNew);
                this.wxDailyRetainService.insert(wxDailyRetain);
            }
        }
    }

    /**
     * 根据微信返回值生成数据对象
     *
     * @param refDate          查询日期
     * @param wxAppAccessToken 小程序信息
     * @param resObject        微信返回值对象
     * @param visitUv          解析
     */
    private WxDailyRetain getDailyRetain(String refDate, WxAppAccessToken wxAppAccessToken, JSONObject resObject, String visitUv) {
        JSONArray visitUvArray = resObject.getJSONArray(visitUv);
        if (visitUvArray.isEmpty()) {
            return null;
        }

        WxDailyRetain wxDailyRetain = new WxDailyRetain(wxAppAccessToken.getAppId(), refDate, visitUv);
        for (int i = 0; i < visitUvArray.size(); i++) {
            JSONObject visitUvObject = visitUvArray.getJSONObject(i);
            int value = visitUvObject.getInteger("value");
            switch (visitUvObject.getInteger("key")) {
                case 0:
                    wxDailyRetain.setDay0(value);
                    break;
                case 1:
                    wxDailyRetain.setDay1(value);
                    break;
                case 2:
                    wxDailyRetain.setDay2(value);
                    break;
                case 3:
                    wxDailyRetain.setDay3(value);
                    break;
                case 4:
                    wxDailyRetain.setDay4(value);
                    break;
                case 5:
                    wxDailyRetain.setDay5(value);
                    break;
                case 6:
                    wxDailyRetain.setDay6(value);
                    break;
                case 7:
                    wxDailyRetain.setDay7(value);
                    break;
                case 14:
                    wxDailyRetain.setDay14(value);
                    break;
                case 30:
                    wxDailyRetain.setDay30(value);
                    break;
                default:
                    break;
            }
        }
        return wxDailyRetain;
    }

    @Autowired
    public void setWxConfig(WxConfig wxConfig) {
        this.wxConfig = wxConfig;
    }

    @Autowired
    public void setWxDailyRetainService(WxDailyRetainService wxDailyRetainService) {
        this.wxDailyRetainService = wxDailyRetainService;
    }

}
