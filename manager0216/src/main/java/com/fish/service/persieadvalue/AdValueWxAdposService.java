package com.fish.service.persieadvalue;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.fourth.mapper.AdValueWxAdposMapper;
import com.fish.dao.fourth.model.AdValueWxAdpos;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.service.BaseService;
import com.fish.service.WxConfigService;
import com.fish.service.cache.CacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @author CF
 * @date
 */
@Service
public class AdValueWxAdposService implements BaseService<AdValueWxAdpos> {

    @Autowired
    AdValueWxAdposMapper adValueWxAdposMapper;

    @Autowired
    CacheService cacheService;
    @Autowired
    WxConfigService wxConfigService;

    @Override
    public void setDefaultSort(GetParameter parameter) {
    }

    @Override
    public Class<AdValueWxAdpos> getClassInfo() {
        return AdValueWxAdpos.class;
    }

    @Override
    public boolean removeIf(AdValueWxAdpos wxAdPos, JSONObject searchData) {
        if (existValueFalse(searchData.getString("productsName"), wxAdPos.getAppId())) {
            return true;
        }
        if (existValueFalse(searchData.getString("appId"), wxAdPos.getAppId())) {
            return true;
        }
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(wxAdPos.getDate());
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return (existTimeFalse(date, searchData.getString("date")));
    }

    @Override
    public List<AdValueWxAdpos> selectAll(GetParameter parameter) {
        List<AdValueWxAdpos> adValueWxAdPos;
        JSONObject search = getSearchData(parameter.getSearchData());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        //判断是否存在查询条件
        if (search != null && "2".equals(search.getString("showType"))) {
            String[] times = getSearchTime(parameter);
            adValueWxAdPos = adValueWxAdposMapper.selectByDate(times[0], times[1]);
        } else {
            adValueWxAdPos = adValueWxAdposMapper.selectAll();
        }
        for (AdValueWxAdpos wxAdPos : adValueWxAdPos) {
            WxConfig wxConfig = wxConfigService.getEntity(WxConfig.class, wxAdPos.getAppId());
            if (wxConfig != null) {
                //赋值产品名称
                wxAdPos.setAppName(wxConfig.getProductName() != null ? wxConfig.getProductName() : "");
            }
        }
        return adValueWxAdPos;
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
