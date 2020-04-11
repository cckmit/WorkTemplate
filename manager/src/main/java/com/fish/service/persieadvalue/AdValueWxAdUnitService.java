package com.fish.service.persieadvalue;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.fourth.mapper.AdValueWxAdUnitMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.fourth.model.AdValueWxAdUnit;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.service.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date date = calendar.getTime();
        String beginTime = dateFormat.format(date);
        String endTime = dateFormat.format(date);
        String slotId;
        String adUnitName = "";
        // 分组类型
        String groupByType = "5";
        String queryDetail = "0";
        if (search != null) {
            String appId = search.getString("productsName");
            String dateNum = search.getString("dateNum");
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
            if (!StringUtils.isBlank(adUnitName) && !adUnitName.equals(isNull)) {
                adValueWxAdUnit.setAdUnitName(adUnitName);
            }
            if (!StringUtils.isBlank(queryDetail)) {
                adValueWxAdUnit.setQueryDetail(queryDetail);
            } else {
                queryDetail = "0";
            }
        }
        adValueWxAdUnit.setGroupByType(groupByType);
        adValueWxAdUnit.setBeginTime(beginTime);
        adValueWxAdUnit.setEndTime(endTime);
        //汇总查询
        List<AdValueWxAdUnit> adValueWxAdUnits = adValueWxAdunitMapper.queryCollectData(adValueWxAdUnit);
        collectData(adValueWxAdUnits, groupByType, queryDetail);
        return adValueWxAdUnits;
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
        if (groupByType.equals("1") || groupByType.equals("4") || groupByType.equals("5") || queryDetail.equals("1")) {
            isQueryConfig = true;
        }

        if (isQueryConfig) {
            wxConfigMap = getWxConfigMap();
        }

        //总数据
        int allReqSuccCount = 0;
        int allExposureCount = 0;
        int allClickCount = 0;
        int allIncome = 0;

        if (!adValueWxAdUnits.isEmpty() && adValueWxAdUnits.size() > 0) {

            for (AdValueWxAdUnit adValueWxAdUnit : adValueWxAdUnits) {
                // 标记是否查询了详情
                allReqSuccCount += adValueWxAdUnit.getReqSuccCount();
                allExposureCount += adValueWxAdUnit.getExposureCount();
                allClickCount += adValueWxAdUnit.getClickCount();
                allIncome += adValueWxAdUnit.getIncome();

                adValueWxAdUnit.setQueryDetail(queryDetail);
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
                    clickIncome = (double) adValueWxAdUnit.getIncome() / adValueWxAdUnit.getClickCount() / 100;
                }
                double ecpm = 0L;
                if (adValueWxAdUnit.getExposureCount() > 0) {
                    ecpm = (double) adValueWxAdUnit.getIncome() / adValueWxAdUnit.getExposureCount() * 10;
                }
                // 点击率
                adValueWxAdUnit.setClickRate(clickRate);
                // 曝光率
                adValueWxAdUnit.setExposureRate(exposureRate);
                // 单次点击收入(四舍五入保留两位小数)
                adValueWxAdUnit.setClickIncome(new BigDecimal(clickIncome).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                adValueWxAdUnit.setEcpm(new BigDecimal(ecpm).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
            AdValueWxAdUnit valueWxAdUnit = new AdValueWxAdUnit();
            valueWxAdUnit.setReqSuccCount(allReqSuccCount);
            valueWxAdUnit.setExposureCount(allExposureCount);
            valueWxAdUnit.setClickCount(allClickCount);
            valueWxAdUnit.setIncome(allIncome);
            // 点击率 = 点击量/曝光量
            Double allClickRate = getRate(allClickCount, allExposureCount);
            // 曝光率 = 曝光量/拉取量
            Double allExposureRate = getRate(allExposureCount, allReqSuccCount);
            double allEcpm = 0L;
            if (allExposureCount > 0) {
                allEcpm = (double) allIncome / allExposureCount * 10;
            }
            //单次点击收入 = 收入/点击次数
            double allClickIncome = 0L;
            if (allClickCount > 0) {
                allClickIncome = (double) allIncome / allClickCount / 100;
            }
            valueWxAdUnit.setClickRate(allClickRate);
            valueWxAdUnit.setExposureRate(allExposureRate);
            valueWxAdUnit.setClickIncome(new BigDecimal(allClickIncome).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            valueWxAdUnit.setEcpm(new BigDecimal(allEcpm).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            adValueWxAdUnits.add(valueWxAdUnit);
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
