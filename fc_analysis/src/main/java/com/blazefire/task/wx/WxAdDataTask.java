package com.blazefire.task.wx;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blazefire.config.WxConfig;
import com.blazefire.dao.second.model.AdValueWx;
import com.blazefire.service.entity.WxAppAccessToken;
import com.blazefire.service.wx.WxAdValueService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 定时获取微信官方广告数据
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-27 15:13
 */
@Component
public class WxAdDataTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxAdDataTask.class);

    private WxConfig wxConfig;
    private WxAdValueService wxAdValueService;

    /**
     * 获取微信数据
     */
    public void getData(List<WxAppAccessToken> wxAppAccessTokenList) {
        LOGGER.info("执行开始……");
        // 获取昨日时间字符串
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        wxAppAccessTokenList.forEach(wxAppAccessToken -> {
            // 只查询小游戏数据
            if (wxAppAccessToken.getType() == 0 && StringUtils.isNotBlank(wxAppAccessToken.getAccessToken())) {
                this.getPublisherAdposGeneral(yesterday, yesterday, wxAppAccessToken);
                this.getPublisherAdunitGeneral(yesterday, yesterday, wxAppAccessToken);
            }
        });
        LOGGER.info("执行结束！");
    }

    /**
     * 用户手动查询接口
     *
     * @param wxAppAccessTokenList app相关参数列表
     * @param processObject        业务参数对象
     */
    public JSONObject getData(List<WxAppAccessToken> wxAppAccessTokenList, JSONObject processObject) {
        JSONObject resultObject = new JSONObject();
        resultObject.put("result", "fail");

        String startDate = processObject.getString("startDate");
        String endDate = processObject.getString("endDate");
        if (StringUtils.isBlank(startDate)) {
            resultObject.put("msg", "缺少参数：startDate！");
            return resultObject;
        }
        if (StringUtils.isBlank(endDate)) {
            resultObject.put("msg", "缺少参数：endDate！");
            return resultObject;
        }

        wxAppAccessTokenList.forEach(wxAppAccessToken -> {
            if (wxAppAccessToken.getType() == 0 && StringUtils.isNotBlank(wxAppAccessToken.getAccessToken())) {
                if ("ADPOS_GENERAL".equals(processObject.getString("type"))) {
                    this.getPublisherAdposGeneral(startDate, endDate, wxAppAccessToken);
                } else {
                    this.getPublisherAdunitGeneral(startDate, endDate, wxAppAccessToken);
                }
            }
            resultObject.put("result", "success");
        });
        return resultObject;
    }

    /**
     * 获取记录小游戏广告汇总数据
     *
     * @param startDate        开始日期
     * @param endDate          结束日期
     * @param wxAppAccessToken 小程序配置参数
     */
    public void getPublisherAdposGeneral(String startDate, String endDate, WxAppAccessToken wxAppAccessToken) {
        for (String adSoit : this.wxConfig.getAdSoitJson().keySet()) {
            if ("preloadVideo".equals(adSoit) || "grid".equals(adSoit)) {
                continue;
            }
            String adSoitValue = this.wxConfig.getAdSoitJson().getString(adSoit);
            String dataUrl = this.wxConfig.getAdDataUrl() + "?action=publisher_adpos_general&access_token=" + wxAppAccessToken.getAccessToken() + "&page=1&page_size=90&start_date=" + startDate + "&end_date=" + endDate + "&slot_id=" + adSoitValue;
            String dataRes = HttpUtil.get(dataUrl);
            if (StringUtils.isNotBlank(dataRes)) {
                LOGGER.debug(wxAppAccessToken.getAppId() + "|" + adSoit + "|publisher_adpos_general -> " + dataRes);
                // 分析微信广告数据接口
                JSONObject resObject = JSONObject.parseObject(dataRes);
                if (resObject != null && resObject.containsKey("base_resp")) {
                    JSONObject baseResp = resObject.getJSONObject("base_resp");
                    if ("ok".equals(baseResp.getString("err_msg")) && resObject.containsKey("list")) {
                        JSONArray list = resObject.getJSONArray("list");
                        List<AdValueWx> adValueWxList = new ArrayList<>(list.size());
                        for (int i = 0; i < list.size(); i++) {
                            JSONObject wxData = list.getJSONObject(i);
                            AdValueWx adValueWx = new AdValueWx();
                            adValueWx.setAppId(wxAppAccessToken.getAppId());
                            adValueWx.setAppSource(wxAppAccessToken.getSource());
                            adValueWx.setClickCount(wxData.getInteger("click_count"));
                            adValueWx.setClickRate(wxData.getBigDecimal("click_rate"));
                            adValueWx.setDate(wxData.getString("date"));
                            adValueWx.setEcpm(wxData.getBigDecimal("ecpm"));
                            adValueWx.setExposureCount(wxData.getInteger("exposure_count"));
                            adValueWx.setExposureRate(wxData.getBigDecimal("exposure_rate"));
                            adValueWx.setIncome(wxData.getInteger("income"));
                            adValueWx.setReqSuccCount(wxData.getInteger("req_succ_count"));
                            adValueWx.setSlotId(adSoitValue);
                            adValueWxList.add(adValueWx);
                        }
                        if (!adValueWxList.isEmpty()) {
                            this.wxAdValueService.insertAdposGeneral(adValueWxList);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取记录小游戏广告细分数据
     *
     * @param startDate        开始日期
     * @param endDate          结束日期
     * @param wxAppAccessToken 小程序配置参数
     */
    public void getPublisherAdunitGeneral(String startDate, String endDate, WxAppAccessToken wxAppAccessToken) {
        for (String adSoit : this.wxConfig.getAdSoitJson().keySet()) {
            if ("preloadVideo".equals(adSoit) || "grid".equals(adSoit)) {
                continue;
            }
            String adSoitValue = this.wxConfig.getAdSoitJson().getString(adSoit);
            String dataUrl = this.wxConfig.getAdDataUrl() + "?action=publisher_adunit_general&access_token=" + wxAppAccessToken.getAccessToken() + "&page=1&page_size=90&start_date=" + startDate + "&end_date=" + endDate + "&slot_id=" + adSoitValue;
            LOGGER.debug(
                    wxAppAccessToken.getAppId() + "|" + adSoit + "|" + adSoitValue + "|publisher_adunit_general -> " + dataUrl);
            String dataRes = HttpUtil.get(dataUrl);
            if (StringUtils.isNotBlank(dataRes)) {
                LOGGER.debug(wxAppAccessToken.getAppId() + "|" + adSoit + "|publisher_adunit_general -> " + dataRes);
                JSONObject resObject = JSONObject.parseObject(dataRes);
                if (resObject != null && resObject.containsKey("base_resp")) {
                    JSONObject baseResp = resObject.getJSONObject("base_resp");
                    if ("ok".equals(baseResp.getString("err_msg")) && resObject.containsKey("list")) {
                        JSONArray list = resObject.getJSONArray("list");
                        List<AdValueWx> adValueWxList = new ArrayList<>(list.size());
                        for (int i = 0; i < list.size(); i++) {
                            JSONObject wxData = list.getJSONObject(i);
                            AdValueWx adValueWx = new AdValueWx();
                            adValueWx.setAppId(wxAppAccessToken.getAppId());
                            adValueWx.setAppSource(wxAppAccessToken.getSource());
                            adValueWx.setAdUnitId(wxData.getString("ad_unit_id"));
                            adValueWx.setAdUnitName(wxData.getString("ad_unit_name"));
                            JSONObject statItemObject = wxData.getJSONObject("stat_item");
                            adValueWx.setClickCount(statItemObject.getInteger("click_count"));
                            adValueWx.setClickRate(statItemObject.getBigDecimal("click_rate"));
                            adValueWx.setDate(statItemObject.getString("date"));
                            adValueWx.setEcpm(statItemObject.getBigDecimal("ecpm"));
                            adValueWx.setExposureCount(statItemObject.getInteger("exposure_count"));
                            adValueWx.setExposureRate(statItemObject.getBigDecimal("exposure_rate"));
                            adValueWx.setIncome(statItemObject.getInteger("income"));
                            adValueWx.setReqSuccCount(statItemObject.getInteger("req_succ_count"));
                            String slotStr = statItemObject.getString("slot_str");
                            adValueWx.setSlotId(StringUtils.isNotBlank(slotStr) ? slotStr : adSoitValue);
                            adValueWxList.add(adValueWx);
                        }
                        if (!adValueWxList.isEmpty()) {
                            this.wxAdValueService.insertAdunitGeneral(adValueWxList);
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
    public void setWxAdValueService(WxAdValueService wxAdValueService) {
        this.wxAdValueService = wxAdValueService;
    }

}
