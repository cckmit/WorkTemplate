package com.blazefire.task.wx;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.blazefire.config.WxConfig;
import com.blazefire.dao.second.model.WxUserPortrait;
import com.blazefire.service.entity.WxAppAccessToken;
import com.blazefire.service.wx.WxUserPortraitService;
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
 * 获取小程序新增或活跃用户的画像分布数据
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-31 15:33
 */
@Component
public class WxGetUserPortrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxGetUserPortrait.class);

    private WxConfig wxConfig;
    private WxUserPortraitService wxUserPortraitService;

    /**
     * 开始查询数据
     */
    public void getData(List<WxAppAccessToken> wxAppAccessTokenList) {
        LOGGER.info("执行开始……");
        // 获取昨日时间字符串
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String endDate = dateFormat.format(calendar.getTime());
        // 分别查询昨天、最近7天、最近30天的数据，即beginDate分别与endDate相差0、6，29天
        // 查询7天数据的起始日期
        calendar.add(Calendar.DATE, -6);
        String beginDate7 = dateFormat.format(calendar.getTime());
        // 查询30天数据的起始日期
        calendar.add(Calendar.DATE, -23);
        String beginDate30 = dateFormat.format(calendar.getTime());

        for (WxAppAccessToken wxAppAccessToken : wxAppAccessTokenList) {
            // 只查询小程序的分析数据
            if (wxAppAccessToken.getType() == 1 && StringUtils.isNotBlank(wxAppAccessToken.getAccessToken())) {
                this.getData(1, endDate, endDate, wxAppAccessToken);
                this.getData(7, beginDate7, endDate, wxAppAccessToken);
                this.getData(30, beginDate30, endDate, wxAppAccessToken);
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
        resultObject.put("result", "fail");
        String refDate = processObject.getString("refDate");
        if (StringUtils.isBlank(refDate)) {
            resultObject.put("msg", "缺少参数：refDate！");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            try {
                Date date = dateFormat.parse(refDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                // 分别查询前一天、最近7天、最近30天的数据，即beginDate分别与endDate相差0、6，29天
                // 查询7天数据的起始日期
                calendar.add(Calendar.DATE, -6);
                String beginDate7 = dateFormat.format(calendar.getTime());
                // 查询30天数据的起始日期
                calendar.add(Calendar.DATE, -23);
                String beginDate30 = dateFormat.format(calendar.getTime());
                wxAppAccessTokenList.forEach(wxAppAccessToken -> {
                    // 只查询小程序的分析数据
                    if (wxAppAccessToken.getType() == 1 && StringUtils.isNotBlank(wxAppAccessToken.getAccessToken())) {
                        this.getData(1, refDate, refDate, wxAppAccessToken);
                        this.getData(7, beginDate7, refDate, wxAppAccessToken);
                        this.getData(30, beginDate30, refDate, wxAppAccessToken);
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
     * @param dateType         时间范围类型
     * @param beginDate        开始日期
     * @param endDate          结束日期
     * @param wxAppAccessToken 小游戏/小程序信息
     */
    private void getData(int dateType, String beginDate, String endDate, WxAppAccessToken wxAppAccessToken) {
        String dataUrl = this.wxConfig.getUserPortraitUrl() + "?access_token=" + wxAppAccessToken.getAccessToken();
        JSONObject paramObject = new JSONObject();
        paramObject.put("access_token", wxAppAccessToken.getAccessToken());
        paramObject.put("begin_date", beginDate);
        paramObject.put("end_date", endDate);
        String dataRes = HttpUtil.post(dataUrl, paramObject.toJSONString());
        if (StringUtils.isNotBlank(dataRes)) {
            LOGGER.debug(wxAppAccessToken.getAppId() + " -> " + dataRes);
            JSONObject dataObject = JSONObject.parseObject(dataRes);
            if (dataObject != null) {
                String refDate = dataObject.getString("ref_date");
                if (StringUtils.isNotBlank(refDate)) {
                    JSONObject visitUvNew = dataObject.getJSONObject("visit_uv_new");
                    for (String key : visitUvNew.keySet()) {
                        String portraitValue = visitUvNew.getString(key);
                        if (!"[]".equals(portraitValue)) {
                            WxUserPortrait wxUserPortrait = new WxUserPortrait(wxAppAccessToken.getAppId(), refDate, dateType,
                                    "visit_uv_new");
                            wxUserPortrait.setPortraitType(key);
                            wxUserPortrait.setPortraitValue(portraitValue);
                            this.wxUserPortraitService.insert(wxUserPortrait);
                        }
                    }
                    JSONObject visitUv = dataObject.getJSONObject("visit_uv");
                    for (String key : visitUv.keySet()) {
                        String portraitValue = visitUvNew.getString(key);
                        if (!"[]".equals(portraitValue)) {
                            WxUserPortrait wxUserPortrait = new WxUserPortrait(wxAppAccessToken.getAppId(), refDate, dateType,
                                    "visit_uv");
                            wxUserPortrait.setPortraitType(key);
                            wxUserPortrait.setPortraitValue(portraitValue);
                            this.wxUserPortraitService.insert(wxUserPortrait);
                        }
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
    public void setWxUserPortraitService(WxUserPortraitService wxUserPortraitService) {
        this.wxUserPortraitService = wxUserPortraitService;
    }

}
