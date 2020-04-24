package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.fourth.mapper.AdValueMapper;
import com.fish.dao.fourth.model.AdValue;
import com.fish.dao.second.model.*;
import com.fish.protocols.GetParameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
    WxConfigService wxConfigService;
    @Autowired
    ConfigAdPositionService configAdPositionService;
    @Autowired
    ConfigAdSpaceService configAdSpaceService;
    @Autowired
    ConfigAdContentService configAdContentService;

    @Override
    public void setDefaultSort(GetParameter parameter) {
    }

    @Override
    public Class<AdValue> getClassInfo() {
        return AdValue.class;
    }

    @Override
    public boolean removeIf(AdValue adValue, JSONObject searchData) {
        if (existValueFalse(searchData.getString("adPosition"), adValue.getAdPositionId())) {
            return true;
        }
        if (existValueFalse(searchData.getString("adSpace"), adValue.getAdSpaceId())) {
            return true;
        }
        if (existValueFalse(searchData.getString("productsName"), adValue.getAppId())) {
            return true;
        }
        if (existValueFalse(searchData.getString("type"), adValue.getAppPlatform())) {
            return true;
        }
        return (existValueFalse(searchData.getString("adContent"), adValue.getAdContentId()));
    }

    @Override
    public List<AdValue> selectAll(GetParameter parameter) {
        // 1、将页面传递的查询参数处理成mybatis查询参数
        AdValue adValue = new AdValue();

        // 查询起止时间
        int beginDate = 0;
        int endDate = 0;
        // 分组类型
        String groupByType = "adContent";
        String queryDetail = "0";
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
                String adPosition = parameterObject.getString("adPosition");
                adValue.setAdPositionId(StringUtils.isNotBlank(adPosition) ? Integer.parseInt(adPosition) : 0);
                String adSpace = parameterObject.getString("adSpace");
                adValue.setAdSpaceId(StringUtils.isNotBlank(adSpace) ? Integer.parseInt(adSpace) : 0);
                String adContent = parameterObject.getString("adContent");
                adValue.setAdContentId(StringUtils.isNotBlank(adContent) ? Integer.parseInt(adContent) : 0);
                adValue.setGroupByType(groupByType);
                if (!StringUtils.isBlank(queryDetail)) {
                    adValue.setQueryDetail(queryDetail);
                }
            }
        }
        // 默认查询当日数据
        if (beginDate == 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            beginDate = endDate = Integer.parseInt(dateFormat.format(new Date()));
        }

        // 2、查询
        List<AdValue> list = this.adValueMapper.selectAll(beginDate, endDate, adValue);
        ConcurrentHashMap<String, WxConfig> wxConfigMap = wxConfigService.getAll(WxConfig.class);
        ConcurrentHashMap<String, ConfigAdPosition> configAdPosMap = configAdPositionService.getAll(ConfigAdPosition.class);
        ConcurrentHashMap<String, ConfigAdSpace> configAdSpaceMap = configAdSpaceService.getAll(ConfigAdSpace.class);
        ConcurrentHashMap<String, ConfigAdContent> configAdContentMap = configAdContentService.getAll(ConfigAdContent.class);
        // 3、数据处理
        if (list != null) {
            try {
                for (AdValue value : list) {
                    WxConfig wxConfig = wxConfigMap.get(value.getAppId());
                    if (StringUtils.isNotBlank(wxConfig.getProductName())) {
                        value.setAppName(wxConfig.getProductName());
                        value.setAppPlatform(wxConfig.getDdAppPlatform());
                    }
                    ConfigAdPosition configAdPosition = configAdPosMap.get(String.valueOf(value.getAdPositionId()));
                    if (configAdPosition != null) {
                        value.setPositionName(value.getAdPositionId() + "-" + configAdPosition.getDdName());
                    } else {
                        value.setPositionName(value.getAdPositionId() + " - 未匹配");
                    }

                    if (value.getAdSpaceId() > 0) {
                        ConfigAdSpace configAdSpace = configAdSpaceMap.get(String.valueOf(value.getAdSpaceId()));
                        if (configAdSpace != null) {
                            value.setSpaceName(value.getAdSpaceId() + "-" + configAdSpace.getDdName());
                        } else {
                            value.setSpaceName(value.getAdSpaceId() + " - 未匹配");
                        }
                        ConfigAdContent configAdContent = configAdContentMap.get(String.valueOf(value.getAdContentId()));
                        if (configAdContent != null) {
                            value.setContentName(value.getAdContentId() + "-" + configAdContent.getDdTargetAppName());
                        } else {
                            value.setContentName(value.getAdContentId() + " - 未匹配");
                        }
                    } else {
                        value.setSpaceName("微信");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
