package com.fish.service.persieadvalue;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.fourth.mapper.WxDailyDetainMapper;
import com.fish.dao.fourth.model.WxDailyDetain;
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
 * WxDailyDetainService
 *
 * @author
 * @date
 */
@Service
public class WxDailyDetainService implements BaseService<WxDailyDetain> {

    @Autowired
    WxDailyDetainMapper WxDailyDetainMapper;

    @Autowired
    CacheService cacheService;

    @Override
    public void setDefaultSort(GetParameter parameter) {
    }

    @Override
    public Class<WxDailyDetain> getClassInfo() {
        return WxDailyDetain.class;
    }

    @Override
    public boolean removeIf(WxDailyDetain wxDailyDetain, JSONObject searchData) {
        if (existValueFalse(searchData.getString("productsName"), wxDailyDetain.getAppId())) {
            return true;
        }
        if (existValueFalse(searchData.getString("appId"), wxDailyDetain.getAppId())) {
            return true;
        }
        Date date = new Date();

        return (existTimeFalse(date, searchData.getString("date")));
    }

    @Override
    public List<WxDailyDetain> selectAll(GetParameter parameter) {
        WxDailyDetain wxDailyDetain = new WxDailyDetain();

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
                wxDailyDetain.setAppId(parameterObject.getString("appId"));
                wxDailyDetain.setDataType(parameterObject.getString("dataType"));
            }
        }

        List<WxDailyDetain> wxDailyDetains = WxDailyDetainMapper.selectAll(beginDate, endDate, wxDailyDetain);
        for (WxDailyDetain dailyDetain : wxDailyDetains) {
            WxConfig wxConfig = cacheService.getWxConfig(dailyDetain.getAppId());
            if (wxConfig != null) {
                //赋值产品名称
                dailyDetain.setAppName(wxConfig.getProductName() != null ? wxConfig.getProductName() : "");
            }
        }
        return wxDailyDetains;
    }

}
