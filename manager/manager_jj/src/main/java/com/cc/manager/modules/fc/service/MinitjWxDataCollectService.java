package com.cc.manager.modules.fc.service;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.entity.*;
import com.cc.manager.modules.jj.entity.BuyPay;
import com.cc.manager.modules.jj.entity.Orders;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.service.BuyPayService;
import com.cc.manager.modules.jj.service.OrdersService;
import com.cc.manager.modules.jj.service.WxConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author cf
 * @since 2020-05-22
 */
@Service
public class MinitjWxDataCollectService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(MinitjWxDataCollectService.class);


    private WxDailyVisitTrendService wxDailyVisitTrendService;
    private WxDailySummaryService wxDailySummaryService;
    private MinitjWxService minitjWxService;

    private AdValueWxAdUnitService adValueWxAdUnitService;
    private OrdersService orderService;
    private BuyPayService buyPayService;
    private WxConfigService wxConfigService;

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

        List<DataCollect> dataCollects;
        try {
            String programType = statsListParam.getQueryObject().getString("programType");
            if (StringUtils.isBlank(programType)) {
                dataCollects = queryProgramStats(beginDate, endDate);
                Map<String, DataCollect> queryGameStats = queryMinitjWxStatis(beginDate, endDate, programType);
                // 合并小游戏和小程序数据
                if (CollectionUtils.isEmpty(dataCollects)) {
                    dataCollects = new ArrayList<>(queryGameStats.values());
                } else {
                    if (MapUtil.isNotEmpty(queryGameStats)) {
                        dataCollects = countProgramAndGame(dataCollects, queryGameStats);
                    }
                }
            } else {
                if ("1".equals(programType)) {
                    // 小程序查询
                    dataCollects = queryProgramStats(beginDate, endDate);
                } else {
                    // 小游戏查询
                    Map<String, DataCollect> queryGameStats = queryMinitjWxStatis(beginDate, endDate, programType);
                    dataCollects = new ArrayList<>(queryGameStats.values());
                }
            }

            // 查询买量数据
            Map<String, BuyPay> buyPayMap = queryBuPayByDate(beginDate, endDate, programType);
            // 追加买量数据和分享率
            countBuyData(dataCollects, buyPayMap);

            // 查询插屏总收入
            Map<String, AdValueWxAdUnit> screenIncomeMap = queryAdValueWxAdUnitDate(beginDate, endDate);
            // 追加插屏总收入
            countScreenIncomeData(dataCollects, screenIncomeMap);

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

    /**
     * 追加插屏总收入
     *
     * @param dataCollects    结果合集
     * @param screenIncomeMap 插屏收入Map
     */
    private void countScreenIncomeData(List<DataCollect> dataCollects, Map<String, AdValueWxAdUnit> screenIncomeMap) {
        dataCollects.forEach(dataCollect -> {
            if (dataCollect == null) {
                return;
            }
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (screenIncomeMap != null && screenIncomeMap.size() > 0) {
                AdValueWxAdUnit adValueWxAdUnit = screenIncomeMap.get(dateTimeFormatter.format(dataCollect.getWxDate()));
                if (adValueWxAdUnit != null) {
                    BigDecimal screenIncome = new BigDecimal(adValueWxAdUnit.getScreenIncome()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    dataCollect.setScreenIncomeCount(screenIncome);
                    dataCollect.setAdRevenueCount(dataCollect.getAdRevenueCount().add(screenIncome));
                    dataCollect.setRevenueCount(dataCollect.getRevenueCount().add(screenIncome));
                }
            }
        });
    }

    /**
     * 根据时间查询插屏总收入
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 查询插屏收入Map结果
     */
    private Map<String, AdValueWxAdUnit> queryAdValueWxAdUnitDate(String beginTime, String endTime) {
        Map<String, AdValueWxAdUnit> map = new HashMap<>(16);
        List<AdValueWxAdUnit> adValueWxAdUnits = adValueWxAdUnitService.queryScreenIncomeByDate(beginTime, endTime);
        if (!adValueWxAdUnits.isEmpty()) {
            adValueWxAdUnits.forEach(adValueWxAdUnit -> map.put(adValueWxAdUnit.getDate(), adValueWxAdUnit));
        }
        return map;
    }

    /**
     * 计算买量数据
     *
     * @param dataCollects 结果合集
     * @param buyPayMap    买量数据Map
     */
    private void countBuyData(List<DataCollect> dataCollects, Map<String, BuyPay> buyPayMap) {
        dataCollects.forEach(dataCollect -> {
            if (dataCollect == null) {
                return;
            }
            if (MapUtil.isNotEmpty(buyPayMap)) {
                BuyPay buyPay = buyPayMap.get(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dataCollect.getWxDate()));
                if (buyPay != null) {
                    dataCollect.setBuyPay(buyPay.getBuyCost());
                }
            }
            dataCollect.setBannerIncomeCount(dataCollect.getBannerIncomeCount() != null ? dataCollect.getBannerIncomeCount().setScale(2, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0));
            dataCollect.setVideoIncomeCount(dataCollect.getVideoIncomeCount() != null ? dataCollect.getVideoIncomeCount().setScale(2, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0));
            if (dataCollect.getShareUserCount() != null) {
                BigDecimal rate = new BigDecimal((double) dataCollect.getShareUserCount() * 100 / dataCollect.getActiveCount())
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                dataCollect.setShareRateCount(rate);
            }
            dataCollect.setAdRevenueCount(dataCollect.getAdRevenueCount() != null ? dataCollect.getAdRevenueCount().setScale(2, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0));
            dataCollect.setRevenueCount(dataCollect.getRevenueCount() != null ? dataCollect.getRevenueCount().setScale(2, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0));
        });
    }

    /**
     * 计算小程序和小游戏的汇总数据
     *
     * @param dataCollects   小程序数据集合
     * @param dataCollectMap 小游戏Map
     */
    private List<DataCollect> countProgramAndGame(List<DataCollect> dataCollects, Map<String, DataCollect> dataCollectMap) {
        dataCollects.forEach(dataCollect -> {
            DataCollect collect = dataCollectMap.get(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dataCollect.getWxDate()));
            if (collect != null) {
                collect.setProductCount(dataCollect.getProductCount() + collect.getProductCount());
                collect.setNewCount(dataCollect.getNewCount() + collect.getNewCount());
                collect.setActiveCount(dataCollect.getActiveCount() + collect.getActiveCount());
                collect.setVideoIncomeCount(dataCollect.getVideoIncomeCount() != null ? collect.getVideoIncomeCount().add(collect.getVideoIncomeCount()) : collect.getVideoIncomeCount());
                collect.setBannerIncomeCount(dataCollect.getBannerIncomeCount() != null ? dataCollect.getBannerIncomeCount().add(collect.getBannerIncomeCount()) : collect.getBannerIncomeCount());
                collect.setAdRevenueCount(collect.getAdRevenueCount().add(dataCollect.getAdRevenueCount() != null ? dataCollect.getAdRevenueCount() : new BigDecimal(0)));
                collect.setShareCount((dataCollect.getShareCount() != null ? dataCollect.getShareCount() : 0) + (collect.getShareCount() != null ? collect.getShareCount() : 0));
                collect.setShareUserCount((dataCollect.getShareUserCount() != null ? dataCollect.getShareUserCount() : 0) + (collect.getShareUserCount() != null ? collect.getShareUserCount() : 0));
                collect.setRechargeCount(dataCollect.getRechargeCount() != null ? dataCollect.getRechargeCount() : new BigDecimal(0));
                collect.setRevenueCount(collect.getRevenueCount().add(dataCollect.getRevenueCount() != null ? dataCollect.getRevenueCount() : new BigDecimal(0)));
            } else {
                dataCollectMap.put(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dataCollect.getWxDate()), dataCollect);
            }
        });
        return new ArrayList<>(dataCollectMap.values());
    }

    /**
     * 查询小游戏汇总数据（最终数据）
     * 生成map集合
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @param type      选择的类型
     * @return 汇总结果
     */
    private Map<String, DataCollect> queryMinitjWxStatis(String beginTime, String endTime, String type) {
        LinkedHashMap<String, JSONObject> allAppMap = new LinkedHashMap<>();
        List<WxConfig> wxConfigEntityList = this.wxConfigService.getCacheEntityList(WxConfig.class);
        wxConfigEntityList.forEach(wxConfig -> {
            JSONObject jsonObject = new JSONObject();
            // 平台
            jsonObject.put("platform", wxConfig.getDdAppPlatform());
            // 类型
            jsonObject.put("programType", wxConfig.getProgramType());
            // 名称
            jsonObject.put("name", wxConfig.getProductName());
            allAppMap.put(wxConfig.getCacheKey(), jsonObject);
        });
        // 查询列表
        List<MinitjWx> minitjWxes = this.minitjWxService.list(null, beginTime, endTime);
        // 生成map集合
        Map<String, List<MinitjWx>> minitjWxListMap;
        minitjWxListMap = getMinitjListMap(minitjWxes, allAppMap, type);
        return countMinitWxData(minitjWxListMap);
    }

    /**
     * 统计小游戏数据
     *
     * @param minitjListMap 小游戏数据集合
     * @return 计算后数据
     */
    private Map<String, DataCollect> countMinitWxData(Map<String, List<MinitjWx>> minitjListMap) {
        Map<String, DataCollect> dataCollectMap = new HashMap<>(16);
        // 循环map
        if (MapUtil.isNotEmpty(minitjListMap)) {
            for (String wxDate : minitjListMap.keySet()) {
                DataCollect dataCollect = new DataCollect();
                //新增数量
                Integer newCount = 0;
                //活跃总人数
                Integer activeCount = 0;
                // 视频收入
                Double videoIncomeCount = 0.0;
                //banner总收入
                Double bannerIncomeCount = 0.0;
                //分享人数
                Integer shareUserCount = 0;
                //分享次数
                Integer shareCount = 0;
                // 循环list
                for (MinitjWx minitj : minitjListMap.get(wxDate)) {
                    newCount += minitj.getWxNew();
                    activeCount += minitj.getWxActive();
                    videoIncomeCount += minitj.getWxVideoIncome().doubleValue();
                    bannerIncomeCount += minitj.getWxBannerIncome().doubleValue();
                    shareUserCount += minitj.getWxShareUser();
                    shareCount += minitj.getWxShareCount();
                }
                dataCollect.setWxDate(LocalDate.parse(wxDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                dataCollect.setProductCount(minitjListMap.get(wxDate).size());
                dataCollect.setNewCount(newCount);
                dataCollect.setActiveCount(activeCount);
                dataCollect.setVideoIncomeCount(new BigDecimal(videoIncomeCount).setScale(2, BigDecimal.ROUND_HALF_UP));
                dataCollect.setBannerIncomeCount(new BigDecimal(bannerIncomeCount).setScale(2, BigDecimal.ROUND_HALF_UP));
                dataCollect.setShareUserCount(shareUserCount);
                dataCollect.setShareCount(shareCount);
                dataCollect.setAdRevenueCount(new BigDecimal(videoIncomeCount + bannerIncomeCount));
                dataCollect.setRevenueCount(dataCollect.getAdRevenueCount());
                dataCollectMap.put(wxDate, dataCollect);
            }
        }
        return dataCollectMap;
    }

    /**
     * 生成map集合
     *
     * @param minitjWxes  小游戏数据集合
     * @param wxConfigMap 配置列表
     * @param type        查询类型
     * @return Map集合
     */
    private Map<String, List<MinitjWx>> getMinitjListMap(List<MinitjWx> minitjWxes, LinkedHashMap<String, JSONObject> wxConfigMap, String type) {
        Map<String, List<MinitjWx>> minitjWxListMap = new HashMap<>(16);
        for (MinitjWx minitjWx : minitjWxes) {
            JSONObject jsonObject = wxConfigMap.get(minitjWx.getWxAppId());
            // 如果查不到配置不处理
            if (jsonObject == null) {
                continue;
            } else {
                // 如果类型不为空
                if (StringUtils.isNotBlank(type)) {
                    // 判断类型：如果该条数据的类型不符合不做处理
                    if (!StringUtils.equals(String.valueOf(jsonObject.getString("programType")), type)) {
                        continue;
                    }
                }
            }
            String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(minitjWx.getWxDate());
            List<MinitjWx> miniWxDate = minitjWxListMap.get(date);
            if (CollectionUtils.isEmpty(miniWxDate)) {
                miniWxDate = new ArrayList<>();
                miniWxDate.add(minitjWx);
                minitjWxListMap.put(date, miniWxDate);
            } else {
                miniWxDate.add(minitjWx);
                minitjWxListMap.put(date, miniWxDate);
            }
        }
        return minitjWxListMap;
    }

    /**
     * 统计小程序汇总数据
     *
     * @return 汇总数据
     */
    private List<DataCollect> queryProgramStats(String beginTime, String endTime) {
        List<DataCollect> dataCollects = new ArrayList<>();

        //将日期数据放入先得set，防止循环时漏掉数据
        LinkedHashSet<String> appAndDateSet = new LinkedHashSet<>();
        //查询小程序数据
        Map<String, WxDailyVisitTrend> wxDailyVisitTrendMap = new HashMap<>(16);
        List<WxDailyVisitTrend> wxDailyVisitTrendList = wxDailyVisitTrendService.sumList(beginTime, endTime);
        if (wxDailyVisitTrendList != null) {
            wxDailyVisitTrendList.forEach(wxDailyVisitTrend -> {
                String key = wxDailyVisitTrend.getRefDate();
                appAndDateSet.add(key);
                wxDailyVisitTrendMap.put(key, wxDailyVisitTrend);
            });
        }
        Map<String, WxDailySummary> wxDailySummaryMap = new HashMap<>(16);
        List<WxDailySummary> wxDailySummaryList = wxDailySummaryService.sumList(beginTime, endTime);
        if (wxDailySummaryList != null) {
            wxDailySummaryList.forEach(wxDailySummary -> {
                String key = wxDailySummary.getRefDate();
                appAndDateSet.add(key);
                wxDailySummaryMap.put(key, wxDailySummary);
            });
        }
        Map<String, Orders> programReChargeMap = getProgramReChargeMap();
        for (String appAndDate : appAndDateSet) {
            DataCollect dataCollect = new DataCollect();
            StringBuilder dateString = new StringBuilder(appAndDate);
            dateString.insert(4, "-").insert(7, "-");
            LocalDate wxDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dataCollect.setWxDate(wxDate);
            WxDailyVisitTrend wxDailyVisitTrend = wxDailyVisitTrendMap.get(appAndDate);
            if (wxDailyVisitTrend != null && wxDailyVisitTrend.getVisitPv() != 0) {
                dataCollect.setProductCount(wxDailyVisitTrend.getProductCount());
            } else {
                dataCollect.setProductCount(0);
            }
            if (wxDailyVisitTrend != null && wxDailyVisitTrend.getVisitUvNew() != 0) {
                dataCollect.setNewCount(wxDailyVisitTrend.getVisitUvNew());
            } else {
                dataCollect.setNewCount(0);
            }
            if (wxDailyVisitTrend != null && wxDailyVisitTrend.getVisitUv() != 0) {
                dataCollect.setActiveCount(wxDailyVisitTrend.getVisitUv());
            } else {
                dataCollect.setActiveCount(0);
            }
            if (wxDailyVisitTrend != null && wxDailyVisitTrend.getVisitUv() != 0) {
                dataCollect.setActiveCount(wxDailyVisitTrend.getVisitUv());
            } else {
                dataCollect.setActiveCount(0);
            }
            WxDailySummary wxDailySummary = wxDailySummaryMap.get(appAndDate);
            if (wxDailySummary != null && wxDailySummary.getShareUv() != 0) {
                dataCollect.setShareUserCount(wxDailySummary.getShareUv());
            } else {
                dataCollect.setShareUserCount(0);
            }
            if (wxDailySummary != null && wxDailySummary.getSharePv() != 0) {
                dataCollect.setShareCount(wxDailySummary.getSharePv());
            } else {
                dataCollect.setShareCount(0);
            }

            Orders orders = programReChargeMap.get(appAndDate);
            if (orders != null) {
                //充值
                dataCollect.setRechargeCount(orders.getDdPrice());
                //总收入
                dataCollect.setRevenueCount(orders.getDdPrice());
            } else {
                dataCollect.setRechargeCount(new BigDecimal(0));
            }
            dataCollects.add(dataCollect);

        }

        return dataCollects;
    }

    /**
     * @return 获取小程序充值金额Map
     */
    private Map<String, Orders> getProgramReChargeMap() {
        List<Orders> orders = orderService.queryProgramReChargeCount();
        Map<String, Orders> programReChargeMap = new HashMap<>(16);
        orders.forEach(order -> programReChargeMap.put(DateTimeFormatter.ofPattern("yyyyMMdd").format(order.getDdTrans()), order));
        return programReChargeMap;
    }

    /**
     * 查询买量数据
     *
     * @return 返回买量数据Map
     */
    private Map<String, BuyPay> queryBuPayByDate(String beginTime, String endTime, String type) {
        Map<String, BuyPay> map = new HashMap<>(16);
        List<BuyPay> buyPays = buyPayService.queryByPayCollectByDate(beginTime, endTime, type);
        if (CollectionUtils.isNotEmpty(buyPays)) {
            buyPays.forEach(buyPay -> map.put(buyPay.getBuyDate(), buyPay));
        }
        return map;
    }


    @Autowired
    public void setWxDailySummaryService(WxDailySummaryService wxDailySummaryService) {
        this.wxDailySummaryService = wxDailySummaryService;
    }

    @Autowired
    public void setWxDailyVisitTrendService(WxDailyVisitTrendService wxDailyVisitTrendService) {
        this.wxDailyVisitTrendService = wxDailyVisitTrendService;
    }

    @Autowired
    public void setMinitjWxService(MinitjWxService minitjWxService) {
        this.minitjWxService = minitjWxService;
    }

    @Autowired
    public void setAdValueWxAdUnitService(AdValueWxAdUnitService adValueWxAdUnitService) {
        this.adValueWxAdUnitService = adValueWxAdUnitService;
    }

    @Autowired
    public void setOrdersService(OrdersService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setBuyPayService(BuyPayService buyPayService) {
        this.buyPayService = buyPayService;
    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

}
