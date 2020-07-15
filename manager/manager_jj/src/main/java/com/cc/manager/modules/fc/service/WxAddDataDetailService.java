package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.entity.MinitjWx;
import com.cc.manager.modules.jj.service.JjAndFcAppConfigService;
import com.cc.manager.modules.tt.entity.TtDailyAdValue;
import com.cc.manager.modules.tt.entity.TtDailyValue;
import com.cc.manager.modules.tt.service.TtDailyAdValueService;
import com.cc.manager.modules.tt.service.TtDailyValueService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author cf
 * @since 2020-07-10
 */
@Service
@DS("fc")
public class WxAddDataDetailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxAddDataDetailService.class);

    private JjAndFcAppConfigService jjAndFcAppConfigService;
    private MinitjWxService minitjWxService;
    private TtDailyAdValueService ttDailyAdValueService;
    private TtDailyValueService ttDailyValueService;

    /**
     * 查询产品数据，重写默认的分页查询方法
     *
     * @param statsListParam 查询参数
     * @return 查询结果
     */
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
        String appId = statsListParam.getQueryObject().getString("appId");
        String beginDate = statsListParam.getQueryObject().getString("beginDate");
        String endDate = statsListParam.getQueryObject().getString("endDate");
        //获取当前日期活跃用户map
        Map<String, String> mapTtDailyValue = new HashMap<>(16);
        mapTtDailyValue(mapTtDailyValue, beginDate, endDate);
        try {
            // 查询返回结果
            List<MinitjWx> minitjWxList = new ArrayList<>();
            // App类型下拉框
            String appType = statsListParam.getQueryObject().getString("appType");
            Boolean belongTt;
            belongTt = belongAppType(appId);
            QueryWrapper<TtDailyAdValue> queryWrapper = new QueryWrapper<>();
            queryWrapper.between("dateValue", beginDate, endDate);
            if (StringUtils.isNotBlank(appType) || belongTt) {
                queryWrapper.eq(StringUtils.isNotBlank(appId), "appId", appId);
                queryWrapper.eq(StringUtils.isNotBlank(appType), "appType", appType);
                List<TtDailyAdValue> ttAdValueList = ttDailyAdValueService.list(queryWrapper);
                ArrayList<MinitjWx> list = new ArrayList<>();
                convertMinitjWx(mapTtDailyValue, ttAdValueList, list);
                this.rebuildStatsListResult(list);
                minitjWxList.addAll(list);
            } else if (StringUtils.isNotBlank(appId) && !belongTt) {
                List<MinitjWx> wxAdValueList = this.minitjWxService.list(appId, beginDate, endDate);
                this.rebuildStatsListResult(wxAdValueList);
                minitjWxList.addAll(wxAdValueList);
            } else {
                List<TtDailyAdValue> ttAdValueList = ttDailyAdValueService.list(queryWrapper);
                ArrayList<MinitjWx> list = new ArrayList<>();
                convertMinitjWx(mapTtDailyValue, ttAdValueList, list);
                List<MinitjWx> wxAdValueList = this.minitjWxService.list(appId, beginDate, endDate);
                this.rebuildStatsListResult(wxAdValueList);
                this.rebuildStatsListResult(list);
                minitjWxList.addAll(wxAdValueList);
                minitjWxList.addAll(list);
            }
            statsListResult.setData(JSONArray.parseArray(JSON.toJSONString(minitjWxList)));
            statsListResult.setTotalRow(null);
            statsListResult.setCount(minitjWxList.size());
        } catch (Exception e) {
            statsListResult.setCode(2);
            statsListResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return statsListResult;
    }

    /**
     * 转化TtDailyAdValue为MinitjWx展示
     *
     * @param mapTtDailyValue mapTtDailyValue
     * @param ttAdValueList   ttAdValueList
     * @param list            list
     */
    private void convertMinitjWx(Map<String, String> mapTtDailyValue, List<TtDailyAdValue> ttAdValueList, ArrayList<MinitjWx> list) {
        for (TtDailyAdValue ttDailyAdValue : ttAdValueList) {
            String formatDate = ttDailyAdValue.getWxDate().format(DateTimeFormatter.BASIC_ISO_DATE);
            String activeUsers = mapTtDailyValue.get(formatDate + "-" + ttDailyAdValue.getWxAppId() + "-" + ttDailyAdValue.getWxAppType());
            Map activeUsersMap = JSON.parseObject(activeUsers);
            int activeUser = 0;
            if (activeUsersMap != null) {
                for (Object mapData : activeUsersMap.values()) {
                    activeUser = activeUser + Integer.parseInt(mapData.toString());
                }
            }
            MinitjWx minitjWx = new MinitjWx();
            BeanUtils.copyProperties(ttDailyAdValue, minitjWx);
            minitjWx.setWxActive(activeUser);
            list.add(minitjWx);
        }
    }

    /**
     * 查询活跃用户数据
     *
     * @param mapTtDailyValue mapTtDailyValue
     * @param beginDate       beginDate
     * @param endDate         endDate
     */
    private void mapTtDailyValue(Map<String, String> mapTtDailyValue, String beginDate, String endDate) {
        QueryWrapper<TtDailyValue> queryTtDailyValue = new QueryWrapper<>();
        List<TtDailyValue> ttDailyValues = ttDailyValueService.list(queryTtDailyValue.between("dateNum", beginDate.replace("-", ""), endDate.replace("-", "")));
        for (TtDailyValue ttDailyValue : ttDailyValues) {
            mapTtDailyValue.put(ttDailyValue.getDateNum() + "-" + ttDailyValue.getAppId() + "-" + ttDailyValue.getAppType(), ttDailyValue.getActiveUsers());
        }
    }

    /**
     * appID平台来源状态
     *
     * @param appId appId
     * @return Boolean
     */
    private Boolean belongAppType(String appId) {
        if (StringUtils.isBlank(appId)) {
            return false;
        } else {
            LinkedHashMap<String, JSONObject> getAllAppMap = this.jjAndFcAppConfigService.getAllAppMap();
            return "tt".equals(getAllAppMap.get(appId).getString("platform"));
        }
    }

    protected void rebuildStatsListResult(List<MinitjWx> entityList) {
        if (entityList != null) {
            List<MinitjWx> newEntityList = new ArrayList<>();
            for (MinitjWx minitjWx : entityList) {
                // 获取街机和FC的全部app信息
                LinkedHashMap<String, JSONObject> getAllAppMap = this.jjAndFcAppConfigService.getAllAppMap();
                JSONObject appObject = getAllAppMap.get(minitjWx.getWxAppId());
                if (appObject == null) {
                    continue;
                } else {
                    // 设置data产品信息
                    minitjWx.setProgramType(Integer.parseInt(appObject.getString("programType")));
                    minitjWx.setProductName(appObject.getString("name"));
                    minitjWx.setDdAppPlatform(appObject.getString("platform"));
                }
                //设置广告收益
                minitjWx.setAdRevenue(minitjWx.getWxBannerIncome().add(minitjWx.getWxVideoIncome()).add(minitjWx.getWxIntIncome()));
                //设置VideoECPM
                if (minitjWx.getWxVideoShow() != 0) {
                    minitjWx.setVideoECPM((minitjWx.getWxVideoIncome().divide(new BigDecimal(minitjWx.getWxVideoShow()),
                            5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
                }
                //设置BannerECPM
                if (minitjWx.getWxBannerShow() != 0) {
                    minitjWx.setBannerECPM((minitjWx.getWxBannerIncome().divide(new BigDecimal(minitjWx.getWxBannerShow()),
                            5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
                }
                //设置插屏ECPM
                if (minitjWx.getWxIntShow() != 0) {
                    minitjWx.setIntECPM((minitjWx.getWxIntIncome().divide(new BigDecimal(minitjWx.getWxIntShow()),
                            5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
                }
                //设置总收入
                minitjWx.setRevenueCount(minitjWx.getAdRevenue());
                newEntityList.add(minitjWx);
            }
            entityList.clear();
            entityList.addAll(newEntityList);
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
            beginDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(2));
            endDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        }
        statsListParam.getQueryObject().put("beginDate", beginDate);
        statsListParam.getQueryObject().put("endDate", endDate);
    }

    @Autowired
    public void setJjAndFcAppConfigService(JjAndFcAppConfigService jjAndFcAppConfigService) {
        this.jjAndFcAppConfigService = jjAndFcAppConfigService;
    }

    @Autowired
    public void setMinitjWxService(MinitjWxService minitjWxService) {
        this.minitjWxService = minitjWxService;
    }

    @Autowired
    public void setTtDailyAdValueService(TtDailyAdValueService ttDailyAdValueService) {
        this.ttDailyAdValueService = ttDailyAdValueService;
    }

    @Autowired
    public void setTtDailyValueService(TtDailyValueService ttDailyValueService) {
        this.ttDailyValueService = ttDailyValueService;
    }

}
