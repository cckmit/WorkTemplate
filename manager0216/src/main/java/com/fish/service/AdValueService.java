package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.AdValueMapper;
import com.fish.dao.second.model.AdValue;
import com.fish.dao.second.model.ConfigAdContent;
import com.fish.dao.second.model.ConfigAdPosition;
import com.fish.dao.second.model.ConfigAdSpace;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * TODO
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-10 13:50
 */
@Service
public class AdValueService implements BaseService<AdValue> {

    @Autowired
    AdValueMapper adValueMapper;

    @Autowired
    CacheService cacheService;

    @Override
    public void setDefaultSort(GetParameter parameter) { }

    @Override
    public Class<AdValue> getClassInfo() { return AdValue.class; }

    @Override
    public boolean removeIf(AdValue adValue, JSONObject searchData) { return false; }

    @Override
    public List<AdValue> selectAll(GetParameter parameter) {
        // 1、将页面传递的查询参数处理成mybatis查询参数
        AdValue adValue = new AdValue();

        // 查询起止时间
        int beginDate = 0;
        int endDate = 0;
        if (StringUtils.isNotBlank(parameter.getSearchData())) {
            // 根据页面的查询参数处理成mybatis查询参数
            // {"dateNum":"2020/03/10 - 2020/03/12","appId":"wx75f1c4d8cd887fd6","version":"","adPosition":"3","adSpace":"3","adContent":"11"}
            JSONObject parameterObject = JSONObject.parseObject(parameter.getSearchData());
            if (parameterObject != null) {
                if (StringUtils.isNotBlank(parameterObject.getString("dateNum"))) {
                    String[] dateNumArray = parameterObject.getString("dateNum").split("-");
                    beginDate = Integer.parseInt(dateNumArray[0].trim().replace("/", ""));
                    endDate = Integer.parseInt(dateNumArray[1].trim().replace("/", ""));
                }
                adValue.setAppId(parameterObject.getString("appId"));
                adValue.setVersion(parameterObject.getString("version"));
                String adPosition = parameterObject.getString("adPosition");
                adValue.setAdPositionId(StringUtils.isNotBlank(adPosition) ? Integer.parseInt(adPosition) : 0);
                String adSpace = parameterObject.getString("adSpace");
                adValue.setAdSpaceId(StringUtils.isNotBlank(adSpace) ? Integer.parseInt(adSpace) : 0);
                String adContent = parameterObject.getString("adContent");
                adValue.setAdContentId(StringUtils.isNotBlank(adContent) ? Integer.parseInt(adContent) : 0);
            }
        }
        // 默认查询当日数据
        if (beginDate == 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            beginDate = endDate = Integer.parseInt(dateFormat.format(new Date()));
        }

        // 2、查询
        List<AdValue> list = this.adValueMapper.selectAll(beginDate, endDate, adValue);
        System.out.println("查询结果：" + (list == null ? "无结果" : list.size()));

        // 3、数据处理
        if (list != null) {
            for (AdValue value : list) {
                System.out.println(value.getDateNum());
                ConfigAdPosition configAdPosition = this.cacheService.getConfigAdPositions(value.getAdPositionId());
                value.setPositionName(value.getAdPositionId() + "-" + configAdPosition.getDdName());
                ConfigAdSpace configAdSpace = this.cacheService.getConfigAdSpaces(value.getAdSpaceId());
                value.setSpaceName(value.getAdSpaceId() + "-" + configAdSpace.getDdName());
                ConfigAdContent configAdContent = this.cacheService.getConfigAdContents(value.getAdContentId());
                value.setContentName(value.getAdContentId() + "-" + configAdContent.getDdTargetAppName());
            }
        }
        return list;
    }
}
