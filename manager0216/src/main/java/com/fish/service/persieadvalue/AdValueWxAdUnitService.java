package com.fish.service.persieadvalue;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.fourth.mapper.AdValueWxAdUnitMapper;
import com.fish.dao.fourth.model.AdValueWxAdUnit;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.service.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Host-0311
 * @date
 */
@Service
public class AdValueWxAdUnitService implements BaseService<AdValueWxAdUnit> {

    @Autowired
    private AdValueWxAdUnitMapper adValueWxAdunitMapper;

    @Autowired
    private WxConfigMapper wxConfigMapper;

    @Override
    public List<AdValueWxAdUnit> selectAll(GetParameter parameter) {
        String isNull = "null";
        AdValueWxAdUnit adValueWxAdUnit = new AdValueWxAdUnit();
        JSONObject search = getSearchData(parameter.getSearchData());
        // 默认查前一天
        String beginTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        String endTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        String groupTimeType = "0";
        String slotId;
        String adUnitName;
        // 分组类型
        String groupByType = "";
        String queryDetail = "0";
        if (search != null) {
            String appId = search.getString("productsName");
            String dateNum = search.getString("dateNum");
            groupTimeType = search.getString("groupTimeType");
            slotId = search.getString("slotId");
            groupByType = search.getString("groupByType");
            adUnitName = search.getString("adUnitName");
            queryDetail = search.getString("queryDetail");
            if (!StringUtils.isBlank(appId) && !appId.equals(isNull)) {
                adValueWxAdUnit.setAppId(appId);
            }
            if (!StringUtils.isBlank(dateNum)) {
                beginTime = dateNum.split("-")[0];
                endTime = dateNum.split("-")[1];
            }
            if (!StringUtils.isBlank(slotId) && !slotId.equals(isNull)) {
                adValueWxAdUnit.setSlotId(slotId);
            }
            if (!StringUtils.isBlank(groupByType) && !groupByType.equals(isNull)) {
                adValueWxAdUnit.setGroupByType(groupByType);
            }
            if (!StringUtils.isBlank(adUnitName) && !adUnitName.equals(isNull)) {
                adValueWxAdUnit.setAdUnitName(adUnitName);
            }
            if (!StringUtils.isBlank(queryDetail)) {
                adValueWxAdUnit.setQueryDetail(queryDetail);
            }else {
                queryDetail = "0";
            }
        }
        adValueWxAdUnit.setBeginTime(beginTime);
        adValueWxAdUnit.setEndTime(endTime);
        if (!StringUtils.isBlank(groupTimeType) && groupTimeType.equals("1")) {
            //汇总查询
            List<AdValueWxAdUnit> adValueWxAdUnits = adValueWxAdunitMapper.queryCollectData(adValueWxAdUnit);
            collectData(adValueWxAdUnits, groupByType, queryDetail);
            return adValueWxAdUnits;
        } else {
            // 按日查询
            List<AdValueWxAdUnit> adValueWxAdUnits = adValueWxAdunitMapper.selectAll(adValueWxAdUnit);
            return adValueWxAdUnits;
        }
    }

    /**
     * 汇总统计
     *
     * @param adValueWxAdUnits
     * @param groupByType
     */
    private void collectData(List<AdValueWxAdUnit> adValueWxAdUnits, String groupByType, String queryDetail) {
        Map<String, WxConfig> wxConfigMap = new HashMap<>();
        boolean isQueryConfig = false;

        // 判断是否查询产品名
        if (groupByType.equals("1") || groupByType.equals("4") || groupByType.equals("5")) {
            isQueryConfig = true;
        }

        if (isQueryConfig) {
            wxConfigMap = getWxConfigMap();
        }

        if (!adValueWxAdUnits.isEmpty() && adValueWxAdUnits.size() > 0) {

            for (AdValueWxAdUnit adValueWxAdUnit : adValueWxAdUnits) {
                // 标记是否查询了详情
                if (queryDetail.equals("1")) {
                    adValueWxAdUnit.setQueryDetail(queryDetail);
                }
                adValueWxAdUnit.setGroupTimeType("1");
                adValueWxAdUnit.setGroupByType(groupByType);
                if (isQueryConfig) {
                    adValueWxAdUnit.setProductName(wxConfigMap.get(adValueWxAdUnit.getAppId()).getProductName());
                }
                // 点击率 = 点击量/曝光量
                Double clickRate = getRate(adValueWxAdUnit.getClickCount(), adValueWxAdUnit.getExposureCount());
                // 曝光率 = 曝光量/拉取量
                Double exposureRate = getRate(adValueWxAdUnit.getExposureCount(), adValueWxAdUnit.getReqSuccCount());
                //单次点击收入 = 收入/点击次数
                double clickIncome = 0L;
                if (adValueWxAdUnit.getClickCount() > 0) {
                    clickIncome = (double) adValueWxAdUnit.getIncome() / adValueWxAdUnit.getClickCount();
                }
                // 点击率
                adValueWxAdUnit.setClickRate(clickRate);
                // 曝光率
                adValueWxAdUnit.setExposureRate(exposureRate);
                // 单次点击收入(四舍五入保留两位小数)
                adValueWxAdUnit.setClickIncome(new BigDecimal(clickIncome).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
        }
    }

    /**
     * 计算比率(四舍五入保留两位小数)
     *
     * @param number1
     * @param number2
     * @return
     */
    private double getRate(int number1, int number2) {
        if (number2 == 0) {
            return 0L;
        } else {
            double rate = (double) number1 / number2 * 100;
            return new BigDecimal(rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<AdValueWxAdUnit> getClassInfo() {
        return null;
    }

    @Override
    public boolean removeIf(AdValueWxAdUnit adValueWxAdunit, JSONObject searchData) {
        if (existValueFalse(searchData.getString("screenName"), adValueWxAdunit.getAdUnitName())) {
            return true;
        }
        return false;
    }

    private Map<String, WxConfig> getWxConfigMap() {
        Map<String, WxConfig> configMap = new HashMap<>();
        List<WxConfig> wxConfigs = wxConfigMapper.selectAll();
        wxConfigs.forEach(wxConfig -> {
            configMap.put(wxConfig.getDdappid(), wxConfig);
        });
        return configMap;
    }
}
