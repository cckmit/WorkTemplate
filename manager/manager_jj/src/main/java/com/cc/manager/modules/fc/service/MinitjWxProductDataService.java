package com.cc.manager.modules.fc.service;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.entity.*;
import com.cc.manager.modules.jj.service.JjAndFcAppConfigService;
import com.cc.manager.modules.jj.entity.BuyPay;
import com.cc.manager.modules.jj.entity.Orders;
import com.cc.manager.modules.jj.service.BuyPayService;
import com.cc.manager.modules.jj.service.OrdersService;
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
public class MinitjWxProductDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinitjWxProductDataService.class);

    private MinitjWxService minitjWxService;
    private BuyPayService buyPayService;
    private AdValueWxAdUnitService adValueWxAdUnitService;
    private OrdersService ordersService;
    private JjAndFcAppConfigService jjAndFcAppConfigService;
    private WxDailyVisitTrendService wxDailyVisitTrendService;
    private WxDailySummaryService wxDailySummaryService;
    private WxDailyRetainService wxDailyRetainService;

    /**
     * 查询产品数据，重写默认的分页查询方法
     * TODO 当前查询暂不分页
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

        try {
            // 获取街机和FC的全部app信息
            LinkedHashMap<String, JSONObject> getAllAppMap = this.jjAndFcAppConfigService.getAllAppMap();

            // 查询购买数据
            Map<String, BuyPay> buyPayMap = new HashMap<>(16);
            List<BuyPay> buyPayList = this.buyPayService.list(appId, beginDate, endDate);
            if (buyPayList != null) {
                buyPayList.forEach(buyPay -> buyPayMap.put(buyPay.getBuyAppId() + "_" + buyPay.getBuyDate().replace("-", ""), buyPay));
            }

            // 查询微信官方广告数据
            Map<String, AdValueWxAdUnit> adValueWxAdUnitMap = new HashMap<>(16);
            List<AdValueWxAdUnit> adValueWxAdUnitList = this.adValueWxAdUnitService.list(appId, beginDate, endDate);
            if (adValueWxAdUnitList != null) {
                adValueWxAdUnitList.forEach(adValueWxAdUnit -> adValueWxAdUnitMap.put(adValueWxAdUnit.getAppId() + "_" + adValueWxAdUnit.getDate().replace("-", ""), adValueWxAdUnit));
            }

            // 查询返回结果
            List<MinitjWx> minitjWxList = new ArrayList<>();

            // 根据查询类型判断，0表示小游戏，1表示小程序，为空表示全部
            String programType = statsListParam.getQueryObject().getString("programType");
            if (StringUtils.equals("0", programType)) {
                minitjWxList.addAll(listGameMinitjWx(appId, beginDate, endDate, getAllAppMap, adValueWxAdUnitMap, buyPayMap));
            } else if (StringUtils.equals("1", programType)) {
                minitjWxList.addAll(listProgramMinitjWx(appId, beginDate, endDate, getAllAppMap, buyPayMap));
            } else {
                minitjWxList.addAll(listGameMinitjWx(appId, beginDate, endDate, getAllAppMap, adValueWxAdUnitMap, buyPayMap));
                minitjWxList.addAll(listProgramMinitjWx(appId, beginDate, endDate, getAllAppMap, buyPayMap));
            }

            // 对整个数据进行过滤、汇总
            // TODO 先不进行分组
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
            beginDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
            endDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        }
        statsListParam.getQueryObject().put("beginDate", beginDate);
        statsListParam.getQueryObject().put("endDate", endDate);
    }

    /**
     * 获取小游戏产品数据
     *
     * @param appId       appId
     * @param beginDate   开始日期
     * @param endDate     结束日期
     * @param allAppMap   app信息
     * @param buyPayMap   购买数据
     * @param wxAdUnitMap 广告数据
     * @return 产品数据列表
     */
    private List<MinitjWx> listGameMinitjWx(String appId, String beginDate, String endDate, LinkedHashMap<String, JSONObject> allAppMap, Map<String, AdValueWxAdUnit> wxAdUnitMap, Map<String, BuyPay> buyPayMap) {
        List<MinitjWx> newDataList = new ArrayList<>();
        List<MinitjWx> minitjWxList = this.minitjWxService.list(appId, beginDate, endDate);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (minitjWxList == null) {
            return new ArrayList<>();
        }
        try {
            for (MinitjWx minitjWx : minitjWxList) {
                JSONObject appObject = allAppMap.get(minitjWx.getWxAppId());
                if (appObject == null) {
                    continue;
                }
                // 设置data信息
                minitjWx.setProgramType(appObject.getInteger("programType"));
                minitjWx.setProductName(appObject.getString("name"));
                minitjWx.setDdAppPlatform(appObject.getString("platform"));

                AdValueWxAdUnit wxAdUnit = wxAdUnitMap.get(minitjWx.getWxAppId() + "_" + dateTimeFormatter.format(minitjWx.getWxDate()).replace("-", ""));
                if (wxAdUnit != null) {
                    minitjWx.setScreenIncome(new BigDecimal(wxAdUnit.getScreenIncome()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                } else {
                    minitjWx.setScreenIncome(new BigDecimal(0));
                }

                minitjWx.setAdRevenue(minitjWx.getWxBannerIncome().add(minitjWx.getWxVideoIncome()).add(minitjWx.getScreenIncome() != null ? minitjWx.getScreenIncome() : new BigDecimal(0)));

                if (minitjWx.getWxActive() != 0) {
                    BigDecimal divide = minitjWx.getAdRevenue().divide(new BigDecimal(minitjWx.getWxActive()), 4, 4);
                    //设置活跃up
                    minitjWx.setActiveUp(divide);
                }

                // 录入购买数据
                BuyPay buyPay = buyPayMap.get(minitjWx.getWxAppId() + "_" + dateTimeFormatter.format(minitjWx.getWxDate()).replace("-", ""));
                if (buyPay != null) {
                    minitjWx.setBuyCost(buyPay.getBuyCost());
                    minitjWx.setBuyClickPrice(buyPay.getBuyClickPrice());
                    minitjWx.setWxAdNewPrice(minitjWx.getWxRegAd().equals(0) ? new BigDecimal(0) : buyPay.getBuyCost().divide(new BigDecimal(minitjWx.getWxRegAd()), 2));
                }
                newDataList.add(minitjWx);
            }
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return newDataList;
    }

    /**
     * 获取小程序产品数据
     *
     * @param appId     appId
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param allAppMap 全部应用列表
     * @param buyPayMap 购买数据记录
     * @return 小程序产品数据列表
     */
    private List<MinitjWx> listProgramMinitjWx(String appId, String beginDate, String endDate, LinkedHashMap<String, JSONObject> allAppMap, Map<String, BuyPay> buyPayMap) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        //小程序充值收入map
        List<Orders> orderList = this.ordersService.list(appId, beginDate, endDate);
        Map<String, Orders> orderMap = new HashMap<>(16);
        orderList.forEach(order -> orderMap.put(order.getDdAppId() + "_" + dateTimeFormatter.format(order.getDdTrans()), order));

        /**
         * 将查询出的app+"_"+日期数据放入先得set，防止循环时漏掉数据
         */
        LinkedHashSet<String> appAndDateSet = new LinkedHashSet<>();
        //查询小程序数据
        Map<String, WxDailyVisitTrend> wxDailyVisitTrendMap = new HashMap<>(16);
        List<WxDailyVisitTrend> wxDailyVisitTrendList = this.wxDailyVisitTrendService.list(appId, beginDate, endDate);
        if (wxDailyVisitTrendList != null) {
            wxDailyVisitTrendList.forEach(wxDailyVisitTrend -> {
                String key = wxDailyVisitTrend.getAppId() + "_" + wxDailyVisitTrend.getRefDate();
                appAndDateSet.add(key);
                wxDailyVisitTrendMap.put(key, wxDailyVisitTrend);
            });
        }
        Map<String, WxDailySummary> wxDailySummaryMap = new HashMap<>(16);
        List<WxDailySummary> wxDailySummaryList = this.wxDailySummaryService.list(appId, beginDate, endDate);
        if (wxDailySummaryList != null) {
            wxDailySummaryList.forEach(wxDailySummary -> {
                String key = wxDailySummary.getAppId() + "_" + wxDailySummary.getRefDate();
                appAndDateSet.add(key);
                wxDailySummaryMap.put(key, wxDailySummary);
            });
        }
        Map<String, WxDailyRetain> wxDailyRetainMap = new HashMap<>(16);
        List<WxDailyRetain> wxDailyRetainList = this.wxDailyRetainService.list(appId, beginDate, endDate);
        if (wxDailyRetainList != null) {
            wxDailyRetainList.forEach(wxDailyRetain -> {
                String key = wxDailyRetain.getAppId() + "_" + wxDailyRetain.getRefDate();
                appAndDateSet.add(key);
                wxDailyRetainMap.put(key, wxDailyRetain);
            });
        }

        List<MinitjWx> minitjWxList = new ArrayList<>();
        try {
            for (String appAndDate : appAndDateSet) {
                String wxAppId = StringUtils.split(appAndDate, "_")[0];
                MinitjWx minitjWx = new MinitjWx();
                minitjWx.setWxAppId(wxAppId);
                String date = StringUtils.split(appAndDate, "_")[1];
                StringBuilder dateString = new StringBuilder(date);
                dateString.insert(4, "-").insert(7, "-");
                minitjWx.setWxDate(LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                JSONObject appObject = allAppMap.get(wxAppId);
                minitjWx.setProductName(appObject != null ? appObject.getString("name") : "");
                minitjWx.setDdAppPlatform(appObject != null ? appObject.getString("platform") : "");
                minitjWx.setProgramType(1);

                WxDailyVisitTrend wxDailyVisitTrend = wxDailyVisitTrendMap.get(appAndDate);
                if (wxDailyVisitTrend != null && wxDailyVisitTrend.getVisitPv() != 0) {
                    minitjWx.setWxNew(wxDailyVisitTrend.getVisitUvNew());
                    minitjWx.setWxVisit(wxDailyVisitTrend.getVisitPv());
                    minitjWx.setWxActive(wxDailyVisitTrend.getVisitUv());
                    minitjWx.setWxAvgLogin(NumberUtil.round(NumberUtil.div(wxDailyVisitTrend.getVisitPv(), wxDailyVisitTrend.getVisitUv()), 2));
                    minitjWx.setWxAvgOnline(new BigDecimal(wxDailyVisitTrend.getStayTimeUv()).setScale(0, BigDecimal.ROUND_HALF_UP));
                } else {
                    minitjWx.setWxNew(wxDailyVisitTrend == null ? 0 : wxDailyVisitTrend.getVisitUvNew());
                    minitjWx.setWxVisit(0);
                    minitjWx.setWxActive(wxDailyVisitTrend == null ? 0 : wxDailyVisitTrend.getVisitUv());
                    minitjWx.setWxAvgLogin(new BigDecimal(0));
                    minitjWx.setWxAvgOnline(wxDailyVisitTrend == null ? new BigDecimal(0) :
                            new BigDecimal(wxDailyVisitTrend.getStayTimeUv()).setScale(0, BigDecimal.ROUND_HALF_UP));
                }

                WxDailySummary wxDailySummary = wxDailySummaryMap.get(appAndDate);
                if (wxDailySummary != null && wxDailySummary.getShareUv() != 0) {
                    minitjWx.setWxShareUser(wxDailySummary.getShareUv());
                    minitjWx.setWxShareCount(wxDailySummary.getSharePv());
                    if (minitjWx.getWxActive() != 0) {
                        minitjWx.setWxShareRate(NumberUtil.round(NumberUtil.div(100 * wxDailySummary.getShareUv(), (float) minitjWx.getWxActive()), 2));
                    } else {
                        minitjWx.setWxShareRate(new BigDecimal(0));
                    }
                } else {
                    minitjWx.setWxShareUser(0);
                    minitjWx.setWxShareCount(wxDailySummary == null ? 0 : wxDailySummary.getSharePv());
                    minitjWx.setWxShareRate(new BigDecimal(0));
                }

                WxDailyRetain wxDailyRetain = wxDailyRetainMap.get(appAndDate);
                if (wxDailyRetain != null && wxDailyRetain.getDay0() != 0) {
                    minitjWx.setWxRemain2(NumberUtil.round(NumberUtil.div(100 * wxDailyRetain.getDay1(), wxDailyRetain.getDay0()), 2));
                } else {
                    minitjWx.setWxRemain2(new BigDecimal(0));
                }

                //查询小程序是否存在买量数据
                BuyPay buyPay = buyPayMap.get(appAndDate);
                if (buyPay != null) {
                    minitjWx.setBuyCost(buyPay.getBuyCost());
                    minitjWx.setBuyClickPrice(buyPay.getBuyClickPrice());
                }

                //查询小程序是否存在充值数据
                Orders orders = orderMap.get(appAndDate);
                if (orders != null) {
                    minitjWx.setRecharge(orders.getDdPrice());
                }
                minitjWxList.add(minitjWx);
            }
        } catch (Exception e) {
            LOGGER.error("查询小游戏数据处理异常" + ", 详细信息:{}", ExceptionUtils.getStackTrace(e));
        }
        return minitjWxList;
    }

    /**
     * TODO 统一的构造appId和日期拼接方法，有空统一替换
     *
     * @return aa
     */
    private String buildMapKey(String appId, String date) {
        return appId.trim() + "_" + date.trim();
    }

    @Autowired
    public void setMinitjWxService(MinitjWxService minitjWxService) {
        this.minitjWxService = minitjWxService;
    }

    @Autowired
    public void setBuyPayService(BuyPayService buyPayService) {
        this.buyPayService = buyPayService;
    }

    @Autowired
    public void setAdValueWxAdUnitService(AdValueWxAdUnitService adValueWxAdUnitService) {
        this.adValueWxAdUnitService = adValueWxAdUnitService;
    }

    @Autowired
    public void setOrdersService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @Autowired
    public void setWxDailyVisitTrendService(WxDailyVisitTrendService wxDailyVisitTrendService) {
        this.wxDailyVisitTrendService = wxDailyVisitTrendService;
    }

    @Autowired
    public void setJjAndFcAppConfigService(JjAndFcAppConfigService jjAndFcAppConfigService) {
        this.jjAndFcAppConfigService = jjAndFcAppConfigService;
    }

    @Autowired
    public void setWxDailySummaryService(WxDailySummaryService wxDailySummaryService) {
        this.wxDailySummaryService = wxDailySummaryService;
    }

    @Autowired
    public void setWxDailyRetainService(WxDailyRetainService wxDailyRetainService) {
        this.wxDailyRetainService = wxDailyRetainService;
    }

}

