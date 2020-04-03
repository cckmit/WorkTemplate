package com.fish.service.persieadvalue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.fourth.mapper.AdValueWxAdposMapper;
import com.fish.dao.fourth.mapper.WxVisitDistributionMapper;
import com.fish.dao.fourth.model.AdValueWxAdpos;
import com.fish.dao.fourth.model.WxVisitDistribution;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.service.BaseService;
import com.fish.service.cache.CacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @date
 */
@Service
public class WxDistributionService implements BaseService<WxVisitDistribution> {

    @Autowired
    WxVisitDistributionMapper wxVisitDistributionMapper;

    @Autowired
    CacheService cacheService;

    @Override
    public void setDefaultSort(GetParameter parameter) {
    }

    @Override
    public Class<WxVisitDistribution> getClassInfo() {
        return WxVisitDistribution.class;
    }

    @Override
    public boolean removeIf(WxVisitDistribution wxAdPos, JSONObject searchData) {
        if (existValueFalse(searchData.getString("productsName"), wxAdPos.getAppId())) {
            return true;
        }
        if (existValueFalse(searchData.getString("appId"), wxAdPos.getAppId())) {
            return true;
        }
        return false;
    }

    @Override
    public List<WxVisitDistribution> selectAll(GetParameter parameter) {
        List<WxVisitDistribution> wxVisitDistributions = wxVisitDistributionMapper.selectAll();
        for (WxVisitDistribution wxVisitDistribution : wxVisitDistributions) {
            String accessValue = wxVisitDistribution.getAccessValue();
            JSONArray newValueArray = new JSONArray();
            JSONArray accessValueArray = JSONArray.parseArray(accessValue);
            if (accessValueArray != null) {
                for (int i = 0; i < accessValueArray.size(); i++) {
                    JSONObject jsonObject = accessValueArray.getJSONObject(i);
                    String key = jsonObject.getString("key");
                    String value = jsonObject.getString("value");
                }
            }
        }
        return null;
    }

    private String[] getSearchTime(GetParameter parameter) {
        String[] times = new String[2];
        String timeStr = "";
        JSONObject search = getSearchData(parameter.getSearchData());
        if (search != null) {
            timeStr = search.getString("date");
        }
        if (StringUtils.isNotBlank(timeStr)) {
            times[0] = timeStr.split("-")[0].trim();
            times[1] = timeStr.split("-")[1].trim();
        } else {
            String format = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
            times[0] = format;
            times[1] = format;
        }
        return times;
    }
}
