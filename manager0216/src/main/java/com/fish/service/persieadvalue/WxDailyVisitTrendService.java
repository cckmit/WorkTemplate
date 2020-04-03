package com.fish.service.persieadvalue;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.fourth.mapper.WxDailyVisitTrendMapper;
import com.fish.dao.fourth.model.WxDailyVisitTrend;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.service.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WxDailyVisitTrendService implements BaseService<WxDailyVisitTrend> {

    @Autowired
    private WxDailyVisitTrendMapper wxDailyVisitTrendMapper;
    @Autowired
    private WxConfigMapper wxConfigMapper;

    @Override
    public List<WxDailyVisitTrend> selectAll(GetParameter parameter) {

        JSONObject search = getSearchData(parameter.getSearchData());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        WxDailyVisitTrend wxDailyVisitTrend = new WxDailyVisitTrend();
        String beginTime = sdf.format(new Date());
        String endTimee = sdf.format(new Date());
        String queryType = "daily";
        String appId;
        if (search != null) {
            String type = search.getString("queryType");
            if (!StringUtils.isBlank(type)) {
                queryType = type;
            }
            String dateNum = search.getString("dateNum");
            if (!StringUtils.isBlank(dateNum)) {
                beginTime = dateNum.split("-")[0];
                endTimee = dateNum.split("-")[1];
            }
            appId = search.getString("productName");
            if (!StringUtils.isBlank(appId)) {
                wxDailyVisitTrend.setAppId(appId);
            }
        }
        wxDailyVisitTrend.setQueryType(queryType);
        wxDailyVisitTrend.setBeginTime(beginTime);
        wxDailyVisitTrend.setEndTime(endTimee);

        List<WxDailyVisitTrend> wxDailyVisitTrends = wxDailyVisitTrendMapper.selectAll(wxDailyVisitTrend);
        if (!wxDailyVisitTrends.isEmpty()) {
            // 查询wx_config所有数据
            Map<String, WxConfig> wxConfigMap = getWxConfigMap();
            // 由于有些appId wx_config表里不存在，这里不做关联查询
            wxDailyVisitTrends.forEach(wxDailyVisitTrend1 -> {
                WxConfig wxConfig = wxConfigMap.get(wxDailyVisitTrend1.getAppId());
                if (wxConfig != null) {
                    wxDailyVisitTrend1.setProductName(wxConfig.getProductName());
                }
            });
        }
        return wxDailyVisitTrends;
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<WxDailyVisitTrend> getClassInfo() {
        return null;
    }

    @Override
    public boolean removeIf(WxDailyVisitTrend wxDailyVisitTrend, JSONObject searchData) {
        return false;
    }

    private Map<String, WxConfig> getWxConfigMap() {
        Map<String, WxConfig> wxConfigMap = new HashMap<>();
        List<WxConfig> wxConfigs = wxConfigMapper.selectAll();
        wxConfigs.forEach(wxConfig -> {
            wxConfigMap.put(wxConfig.getDdappid(), wxConfig);
        });
        return wxConfigMap;
    }

}
