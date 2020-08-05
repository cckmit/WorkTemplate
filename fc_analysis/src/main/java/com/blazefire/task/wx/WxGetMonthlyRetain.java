package com.blazefire.task.wx;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blazefire.config.WxConfig;
import com.blazefire.dao.second.model.WxMonthlyRetain;
import com.blazefire.service.entity.WxAppAccessToken;
import com.blazefire.service.wx.WxMonthlyRetainService;
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
public class WxGetMonthlyRetain {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxGetMonthlyRetain.class);

    private WxConfig wxConfig;
    private WxMonthlyRetainService wxMonthlyRetainService;

    /**
     * 开始查询数据
     */
    public void getData(List<WxAppAccessToken> wxAppAccessTokenList) {
        LOGGER.info("执行开始……");
        // 获取昨天所在月份的月首日和月末日
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        int currentDate = calendar.get(Calendar.DAY_OF_MONTH);
        // 如果今天是1号，则查询上月相关数据
        if (currentDate == 1) {
            calendar.add(Calendar.DATE, -1);
            String endDate = dateFormat.format(calendar.getTime());
            calendar.set(Calendar.DAY_OF_MONTH, 1);
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
     * 用户手动查询接口
     *
     * @param wxAppAccessTokenList app相关参数列表
     * @param processObject        业务参数对象
     */
    public JSONObject getData(List<WxAppAccessToken> wxAppAccessTokenList, JSONObject processObject) {
        JSONObject resultObject = new JSONObject();
        String refDate = processObject.getString("refDate");
        if (StringUtils.isBlank(refDate)) {
            resultObject.put("result", "fail");
            resultObject.put("msg", "缺少参数：refDate！");
        } else {
            // 根据传入的月份计算日期起止
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            int year = Integer.parseInt(refDate.substring(0, 4));
            int month = Integer.parseInt(refDate.substring(4, 6));
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            // 结束日期
            String endDate = dateFormat.format(calendar.getTime());
            // 开始日期
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String beginDate = dateFormat.format(calendar.getTime());

            wxAppAccessTokenList.forEach(wxAppAccessToken -> {
                if (wxAppAccessToken.getType() == 1 && StringUtils.isNotBlank(wxAppAccessToken.getAccessToken())) {
                    this.getData(beginDate, endDate, wxAppAccessToken);
                }
            });
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
        String dataUrl = this.wxConfig.getMonthlyRetainUrl() + "?access_token=" + wxAppAccessToken.getAccessToken();
        JSONObject paramObject = new JSONObject();
        paramObject.put("access_token", wxAppAccessToken.getAccessToken());
        paramObject.put("begin_date", beginDate);
        paramObject.put("end_date", endDate);
        String dataRes = HttpUtil.post(dataUrl, paramObject.toJSONString());
        if (StringUtils.isNotBlank(dataRes)) {
            LOGGER.debug(wxAppAccessToken.getAppId() + " -> " + dataRes);
            // {"ref_date":"202003","visit_uv_new":[{"key":0,"value":177}],"visit_uv":[{"key":0,"value":197}]}
            JSONObject resObject = JSONObject.parseObject(dataRes);
            if (resObject != null && resObject.containsKey("ref_date")) {
                WxMonthlyRetain wxMonthlyRetainNew = getMonthlyRetain(wxAppAccessToken, resObject, "visit_uv_new");
                WxMonthlyRetain wxMonthlyRetain = getMonthlyRetain(wxAppAccessToken, resObject, "visit_uv");
                this.wxMonthlyRetainService.insert(wxMonthlyRetainNew);
                this.wxMonthlyRetainService.insert(wxMonthlyRetain);
            }
        }
    }

    /**
     * 根据微信返回值生成数据对象
     *
     * @param wxAppAccessToken 小程序信息
     * @param resObject        微信返回值对象
     * @param visitUv          解析
     */
    private WxMonthlyRetain getMonthlyRetain(WxAppAccessToken wxAppAccessToken, JSONObject resObject, String visitUv) {
        JSONArray visitUvArray = resObject.getJSONArray(visitUv);
        if (visitUvArray.isEmpty()) {
            return null;
        }

        WxMonthlyRetain wxMonthlyRetain = new WxMonthlyRetain(wxAppAccessToken.getAppId(), resObject.getString("ref_date"),
                visitUv);
        for (int i = 0; i < visitUvArray.size(); i++) {
            JSONObject visitUvObject = visitUvArray.getJSONObject(i);
            int value = visitUvObject.getInteger("value");
            switch (visitUvObject.getInteger("key")) {
                case 0:
                    wxMonthlyRetain.setMonth0(value);
                    break;
                case 1:
                    wxMonthlyRetain.setMonth1(value);
                    break;
                case 2:
                    wxMonthlyRetain.setMonth2(value);
                    break;
                case 3:
                    wxMonthlyRetain.setMonth3(value);
                    break;
                case 4:
                    wxMonthlyRetain.setMonth4(value);
                    break;
                case 5:
                    wxMonthlyRetain.setMonth5(value);
                    break;
                case 6:
                    wxMonthlyRetain.setMonth6(value);
                    break;
                case 7:
                    wxMonthlyRetain.setMonth7(value);
                    break;
                default:
                    break;
            }
        }
        return wxMonthlyRetain;
    }

    @Autowired
    public void setWxConfig(WxConfig wxConfig) {
        this.wxConfig = wxConfig;
    }

    @Autowired
    public void setWxMonthlyRetainService(WxMonthlyRetainService wxMonthlyRetainService) {
        this.wxMonthlyRetainService = wxMonthlyRetainService;
    }

}
