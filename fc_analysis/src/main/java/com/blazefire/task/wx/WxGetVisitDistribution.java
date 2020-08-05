package com.blazefire.task.wx;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blazefire.config.WxConfig;
import com.blazefire.dao.second.model.WxVisitDistribution;
import com.blazefire.service.entity.WxAppAccessToken;
import com.blazefire.service.wx.WxVisitDistributionService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * 获取用户小程序访问分布数据
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-31 15:33
 */
@Component
public class WxGetVisitDistribution {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxGetVisitDistribution.class);

    private WxConfig wxConfig;
    private WxVisitDistributionService wxVisitDistributionService;

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
        String dataUrl = this.wxConfig.getVisitDistributionUrl() + "?access_token=" + wxAppAccessToken.getAccessToken();
        JSONObject paramObject = new JSONObject();
        paramObject.put("access_token", wxAppAccessToken.getAccessToken());
        paramObject.put("begin_date", refDate);
        paramObject.put("end_date", refDate);
        String dataRes = HttpUtil.post(dataUrl, paramObject.toJSONString());
        if (StringUtils.isNotBlank(dataRes)) {
            LOGGER.debug(wxAppAccessToken.getAppId() + " -> " + dataRes);
            // {"ref_date":"20200331","list":[{"index":"access_source_session_cnt","item_list":[{"key":22,"value":445},{"key":29,"value":181},{"key":16,"value":4},{"key":36,"value":20},{"key":10,"value":4},{"key":3,"value":41},{"key":2,"value":5},{"key":23,"value":10},{"key":1,"value":89}]},{"index":"access_source_visit_uv","item_list":[{"key":22,"value":345},{"key":29,"value":121},{"key":16,"value":4},{"key":36,"value":10},{"key":10,"value":2},{"key":3,"value":17},{"key":2,"value":5},{"key":23,"value":9},{"key":1,"value":63}]},{"index":"access_staytime_info","item_list":[{"key":8,"value":201},{"key":7,"value":88},{"key":6,"value":102},{"key":5,"value":67},{"key":4,"value":112},{"key":3,"value":66},{"key":2,"value":82},{"key":1,"value":81}]},{"index":"access_depth_info","item_list":[{"key":5,"value":5},{"key":4,"value":38},{"key":3,"value":281},{"key":2,"value":343},{"key":1,"value":132}]}]}
            JSONObject resObject = JSONObject.parseObject(dataRes);
            if (resObject != null && refDate.equals(resObject.getString("ref_date"))) {
                JSONArray listArray = resObject.getJSONArray("list");
                for (int i = 0; i < listArray.size(); i++) {
                    JSONObject listObject = listArray.getJSONObject(i);
                    // 这条数据直接存了，后台获取的时候再做分析
                    WxVisitDistribution wxVisitDistribution = new WxVisitDistribution();
                    wxVisitDistribution.setAppId(wxAppAccessToken.getAppId());
                    wxVisitDistribution.setRefDate(resObject.getString("ref_date"));
                    wxVisitDistribution.setAccessType(listObject.getString("index"));
                    wxVisitDistribution.setAccessValue(listObject.getString("item_list"));
                    // 空数组就不存了
                    if (!"[]".equals(wxVisitDistribution.getAccessValue())) {
                        this.wxVisitDistributionService.insert(wxVisitDistribution);
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
    public void setWxVisitDistributionService(WxVisitDistributionService wxVisitDistributionService) {
        this.wxVisitDistributionService = wxVisitDistributionService;
    }

}
