package com.fish.service.persieadvalue;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.fourth.mapper.WxDailySummaryMapper;
import com.fish.dao.fourth.model.WxDailySummary;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.service.BaseService;
import com.fish.service.cache.CacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * WxDailySummaryService
 *
 * @author
 * @date
 */
@Service
public class WxDailySummaryService implements BaseService<WxDailySummary> {

    @Autowired
    WxDailySummaryMapper wxDailySummaryMapper;

    @Autowired
    CacheService cacheService;

    @Override
    public void setDefaultSort(GetParameter parameter) {
    }

    @Override
    public Class<WxDailySummary> getClassInfo() {
        return WxDailySummary.class;
    }

    @Override
    public boolean removeIf(WxDailySummary wxDailySummary, JSONObject searchData) {
        if (existValueFalse(searchData.getString("productsName"), wxDailySummary.getAppId())) {
            return true;
        }
        if (existValueFalse(searchData.getString("appId"), wxDailySummary.getAppId())) {
            return true;
        }
        Date date = new Date();

        return (existTimeFalse(date, searchData.getString("date")));
    }

    @Override
    public List<WxDailySummary> selectAll(GetParameter parameter) {
        WxDailySummary wxDailySummary = new WxDailySummary();
        // 查询起止时间
        String format = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now().minusDays(1));
        int beginDate = Integer.parseInt(format);
        int endDate = Integer.parseInt(format);
        if (StringUtils.isNotBlank(parameter.getSearchData())) {
            // 根据页面的查询参数处理成mybatis查询参数
            // {"dateNum":"2020/03/10 - 2020/03/12","appId":"wx75f1c4d8cd887fd6","dataType":""}
            JSONObject parameterObject = JSONObject.parseObject(parameter.getSearchData());
            if (parameterObject != null) {
                if (StringUtils.isNotBlank(parameterObject.getString("dateNum"))) {
                    String[] dateNumArray = parameterObject.getString("dateNum").split("-");
                    beginDate = Integer.parseInt(dateNumArray[0].trim().replace("/", ""));
                    endDate = Integer.parseInt(dateNumArray[1].trim().replace("/", ""));
                }
                wxDailySummary.setAppId(parameterObject.getString("appId"));
            }
        }

        List<WxDailySummary> wxDailySummarys = wxDailySummaryMapper.selectAll(beginDate, endDate, wxDailySummary);
        for (WxDailySummary dailySummary : wxDailySummarys) {
            WxConfig wxConfig = cacheService.getWxConfig(dailySummary.getAppId());
            if (wxConfig != null) {
                //赋值产品名称
                dailySummary.setAppName(wxConfig.getProductName() != null ? wxConfig.getProductName() : "");
            }
        }
        return wxDailySummarys;
    }

}
