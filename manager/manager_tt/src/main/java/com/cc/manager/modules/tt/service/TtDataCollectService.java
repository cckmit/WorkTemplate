package com.cc.manager.modules.tt.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.tt.entity.TtDailyAdValue;
import com.cc.manager.modules.tt.entity.TtDailyValue;
import com.cc.manager.modules.tt.entity.TtDataCollect;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.cc.manager.common.mvc.BaseController.LOGGER;

/**
 * 头条剪切板广告数据
 *
 * @author cf
 * @since 2020-07-21
 */
@Service
public class TtDataCollectService {


    private TtDailyValueService ttDailyValueService;
    private TtDailyAdValueService ttDailyAdValueService;

    public StatsListResult getPage(StatsListParam statsListParam) {
        StatsListResult statsListResult = new StatsListResult();
        // 判断请求参数是否为空，并进行初始化
        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            statsListParam.setQueryObject(JSONObject.parseObject(statsListParam.getQueryData()));
        }
        if (Objects.isNull(statsListParam.getQueryObject())) {
            statsListParam.setQueryObject(new JSONObject());
        }
        // 初始化查询的起止日期
        this.updateBeginAndEndDate(statsListParam);
        String beginDate = statsListParam.getQueryObject().getString("beginDate");
        String endDate = statsListParam.getQueryObject().getString("endDate");

        List<TtDataCollect> dataCollects;
        try {
            Map<String, TtDataCollect> dataCollectMap = new TreeMap<>();
            Map<String, TtDailyAdValue> ttDailyAdValueMap = queryDailyAdValueCount(beginDate, endDate);
            Map<String, TtDailyValue> ttDailyValueMap = queryDailyValueCount(beginDate, endDate);
            dataCollects = mergeValue(ttDailyAdValueMap, ttDailyValueMap, dataCollectMap);
            // TODO 先不进行分组
            statsListResult.setData(JSONArray.parseArray(JSON.toJSONString(dataCollects)));
            statsListResult.setTotalRow(null);
            statsListResult.setCount(dataCollects.size());
        } catch (Exception e) {
            statsListResult.setCode(1);
            statsListResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return statsListResult;
    }

    @SneakyThrows
    private List<TtDataCollect> mergeValue(Map<String, TtDailyAdValue> ttDailyAdValueMap, Map<String, TtDailyValue> ttDailyValueMap, Map<String, TtDataCollect> dataCollectMap) {
        Collection<TtDailyValue> values = ttDailyValueMap.values();
        ArrayList<TtDailyValue> ttDailyValues = new ArrayList<>(values);
        SimpleDateFormat orFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (TtDailyValue ttDailyValue : ttDailyValues) {
            TtDataCollect dataCollect = new TtDataCollect();
            dataCollect.setWxDate(newFormat.format(orFormat.parse(ttDailyValue.getDateNum().toString())));
            dataCollect.setProductCount(ttDailyValue.getProductCount());
            dataCollect.setNewCount(ttDailyValue.getNewCount());
            dataCollect.setActiveCount(ttDailyValue.getActiveCount());
            TtDailyAdValue ttDailyAdValue = ttDailyAdValueMap.get(ttDailyValue.getDateNum().toString());
            if (ttDailyAdValue != null) {
                dataCollect.setRevenueCount(ttDailyAdValue.getAdRevenue());
                dataCollect.setAdRevenueCount(ttDailyAdValue.getAdRevenue());
                dataCollect.setVideoIncomeCount(ttDailyAdValue.getWxVideoIncome());
                dataCollect.setBannerIncomeCount(ttDailyAdValue.getWxBannerIncome());
                dataCollect.setScreenIncomeCount(ttDailyAdValue.getWxIntIncome());
            }
            dataCollectMap.put(ttDailyValue.getDateNum().toString(), dataCollect);
        }
        return new ArrayList<>(dataCollectMap.values());
    }

    private Map<String, TtDailyValue> queryDailyValueCount(String beginDate, String endDate) {
        QueryWrapper<TtDailyValue> ttDailyValueQueryWrapper = new QueryWrapper<>();
        ttDailyValueQueryWrapper.between("dateNum", beginDate.replace("-", "").trim(), endDate.replace("-", "").trim());
        List<TtDailyValue> ttDailyValueList = ttDailyValueService.list(ttDailyValueQueryWrapper);
        Map<String, TtDailyValue> sumTtDailyValueMap = new HashMap<>(16);
        dealSumTtDailyValueMap(ttDailyValueList, sumTtDailyValueMap);
        return sumTtDailyValueMap;
    }

    private void dealSumTtDailyValueMap(List<TtDailyValue> ttDailyValueList, Map<String, TtDailyValue> sumTtDailyValueMap) {
        HashSet<String> appId = new HashSet<>();
        for (TtDailyValue ttDailyValue : ttDailyValueList) {
            Integer dateNum = ttDailyValue.getDateNum();
            Integer newCount = 0;
            newCount = getUsers(JSONObject.parseObject(ttDailyValue.getNewUsers()), newCount);
            ttDailyValue.setNewCount(newCount);
            Integer activeCount = 0;
            activeCount = getUsers(JSONObject.parseObject(ttDailyValue.getActiveUsers()), activeCount);
            ttDailyValue.setActiveCount(activeCount);
            appId.add(ttDailyValue.getAppId());
            TtDailyValue dailyValue = sumTtDailyValueMap.get(dateNum.toString());
            if (dailyValue != null) {
                dailyValue.setProductCount(appId.size());
                dailyValue.setNewCount(dailyValue.getNewCount() + ttDailyValue.getNewCount());
                dailyValue.setActiveCount(dailyValue.getActiveCount() + ttDailyValue.getActiveCount());
            } else {
                sumTtDailyValueMap.put(dateNum.toString(), ttDailyValue);
            }
        }
    }

    /**
     * 获取汇总用户数
     *
     * @param ttDailyUsers ttDailyUsers
     * @param count        count
     * @return 用户数
     */
    private Integer getUsers(JSONObject ttDailyUsers, Integer count) {
        Map<String, Integer> params = new HashMap<>(16);
        if (ttDailyUsers != null) {
            for (Map.Entry<String, Object> entry : ttDailyUsers.entrySet()) {
                params.put(entry.getKey(), Integer.parseInt(entry.getValue().toString()));
            }
            for (Integer integer : new ArrayList<>(params.values())) {
                count = count + integer;
            }
        }
        return count;
    }

    private Map<String, TtDailyAdValue> queryDailyAdValueCount(String beginDate, String endDate) {
        QueryWrapper<TtDailyAdValue> ttDailyAdValueQueryWrapper = new QueryWrapper<>();
        ttDailyAdValueQueryWrapper.between("dateValue", beginDate, endDate);
        List<TtDailyAdValue> ttDailyAdValueList = ttDailyAdValueService.list(ttDailyAdValueQueryWrapper);
        Map<String, TtDailyAdValue> sumTtDailyAdValueMap = new HashMap<>(16);
        dealSumTtDailyAdValueMap(ttDailyAdValueList, sumTtDailyAdValueMap);
        return sumTtDailyAdValueMap;
    }

    /**
     * 汇总进行数据求和
     *
     * @param ttDailyAdValueList   ttDailyAdValueList
     * @param sumTtDailyAdValueMap sumTtDailyAdValueMap
     */
    private void dealSumTtDailyAdValueMap(List<TtDailyAdValue> ttDailyAdValueList, Map<String, TtDailyAdValue> sumTtDailyAdValueMap) {
        for (TtDailyAdValue ttDailyAdValue : ttDailyAdValueList) {
            LocalDate wxDate = ttDailyAdValue.getWxDate();
            ttDailyAdValue.setAdRevenue(ttDailyAdValue.getWxBannerIncome().add(ttDailyAdValue.getWxVideoIncome()).add(ttDailyAdValue.getWxIntIncome()));
            ttDailyAdValue.setRevenueCount(ttDailyAdValue.getAdRevenue());
            TtDailyAdValue dailyAdValue = sumTtDailyAdValueMap.get(DateTimeFormatter.ofPattern("yyyyMMdd").format(wxDate));
            if (dailyAdValue != null) {
                dailyAdValue.setWxVideoShow(dailyAdValue.getWxVideoShow() + ttDailyAdValue.getWxVideoShow());
                dailyAdValue.setWxVideoClickCount(dailyAdValue.getWxVideoClickCount() + ttDailyAdValue.getWxVideoClickCount());
                dailyAdValue.setWxVideoIncome(dailyAdValue.getWxVideoIncome().add(ttDailyAdValue.getWxVideoIncome()));
                dailyAdValue.setWxBannerShow(dailyAdValue.getWxBannerShow() + ttDailyAdValue.getWxBannerShow());
                dailyAdValue.setWxBannerClickCount(dailyAdValue.getWxBannerClickCount() + ttDailyAdValue.getWxBannerClickCount());
                dailyAdValue.setWxBannerIncome(dailyAdValue.getWxBannerIncome().add(ttDailyAdValue.getWxBannerIncome()));
                dailyAdValue.setWxIntShow(dailyAdValue.getWxIntShow() + ttDailyAdValue.getWxIntShow());
                dailyAdValue.setWxIntClickCount(dailyAdValue.getWxIntClickCount() + ttDailyAdValue.getWxIntClickCount());
                dailyAdValue.setWxIntIncome(dailyAdValue.getWxIntIncome().add(ttDailyAdValue.getWxIntIncome()));
                dailyAdValue.setAdRevenue(dailyAdValue.getAdRevenue().add(ttDailyAdValue.getAdRevenue()));
            } else {
                sumTtDailyAdValueMap.put(DateTimeFormatter.ofPattern("yyyyMMdd").format(wxDate), ttDailyAdValue);
            }
        }
    }

    /**
     * 初始化查询起止时间
     *
     * @param statsListParam 请求参数
     */
    private void updateBeginAndEndDate(StatsListParam statsListParam) {
        String beginDate = null, endDate = null;
        String times = statsListParam.getQueryObject().getString("times");
        if (StringUtils.isNotBlank(times)) {
            String[] timeRangeArray = StringUtils.split(times, "~");
            beginDate = timeRangeArray[0].trim();
            endDate = timeRangeArray[1].trim();
        }
        if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            beginDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(13));
            endDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        }
        statsListParam.getQueryObject().put("beginDate", beginDate);
        statsListParam.getQueryObject().put("endDate", endDate);
    }

    @Autowired
    public void setTtDailyValueService(TtDailyValueService ttDailyValueService) {
        this.ttDailyValueService = ttDailyValueService;
    }

    @Autowired
    public void setTtDailyAdValueService(TtDailyAdValueService ttDailyAdValueService) {
        this.ttDailyAdValueService = ttDailyAdValueService;
    }
}
