package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.entity.AdValueWxAdUnit;
import com.cc.manager.modules.fc.entity.MiniGame;
import com.cc.manager.modules.fc.entity.MinitjWx;
import com.cc.manager.modules.fc.mapper.MinitjWxMapper;
import com.cc.manager.modules.fc.mapper.WxDailyVisitTrendMapper;
import com.cc.manager.modules.jj.entity.BuyPay;
import com.cc.manager.modules.jj.entity.Orders;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.service.BuyPayService;
import com.cc.manager.modules.jj.service.OrdersService;
import com.cc.manager.modules.jj.service.WxConfigService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * @author cf
 * @since 2020-05-22
 */
@Service
public class MinitjWxProductDataService extends BaseStatsService<MinitjWx, MinitjWxMapper> {

    private WxConfigService wxConfigService;
    private MiniGameService miniGameService;
    private MinitjWxService minitjWxService;
    private WxDailyVisitTrendMapper wxDailyVisitTrendMapper;
    private BuyPayService buyPayService;
    private AdValueWxAdUnitService adValueWxAdUnitService;
    private OrdersService ordersService;

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<MinitjWx> queryWrapper, StatsListResult statsListResult) {
    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<MinitjWx> entityList, StatsListResult statsListResult) {
        return null;
    }

    @Override
    public StatsListResult getPage(StatsListParam statsListParam) {
        StatsListResult statsListResult = new StatsListResult();

        try {
            String[] times = getTimes(statsListParam);
            ConcurrentHashMap<String, WxConfig> wxConfigMap = getProductMap();
            Map<String, BuyPay> buyPayMap = getBuyPayMap(times);
            Map<String, AdValueWxAdUnit> wxAdUnitMap = getWxAdUnitMap(times);
            String type = "";
            String ddAppId = "";
            List<MinitjWx> productDatas = new ArrayList<>();
            List<MinitjWx> newDatas = new ArrayList<>();
            if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
                JSONObject queryObject = JSONObject.parseObject(statsListParam.getQueryData());
                type = queryObject.getString("type");
                ddAppId = queryObject.getString("id");
                switch (type) {
                    case "0":
                        productDatas = selectMinitjWx(times, ddAppId, wxConfigMap, buyPayMap, wxAdUnitMap);
                        break;
                    case "1":
                        productDatas = selectPersieDeamon(times, ddAppId, wxConfigMap, buyPayMap);
                        break;
                    default:
                        // 如果只选了时间
                        productDatas = selectMinitjWx(times, ddAppId, wxConfigMap, buyPayMap, wxAdUnitMap);
                        List<MinitjWx> list = selectPersieDeamon(times, ddAppId, wxConfigMap, buyPayMap);
                        if (list != null) {
                            if (productDatas != null) {
                                productDatas.addAll(list);
                            }
                        }
                        break;
                }
            } else {
                productDatas = selectMinitjWx(times, null, wxConfigMap, buyPayMap, wxAdUnitMap);
                List<MinitjWx> list = selectPersieDeamon(times, null, wxConfigMap, buyPayMap);
                if (productDatas != null) {
                    if (list != null) {
                        productDatas.addAll(list);
                    }
                }
            }

            for (int i = (statsListParam.getPage() - 1) * statsListParam.getLimit(); i < statsListParam.getPage() * statsListParam.getLimit(); i++) {
                if (productDatas != null && productDatas.size() > i) {
                    newDatas.add(productDatas.get(i));
                }
            }
            statsListResult.setData(JSONArray.parseArray(JSON.toJSONString(newDatas)));
            if (productDatas != null) {
                statsListResult.setCount(productDatas.size());
            }
        } catch (Exception e) {
            statsListResult.setCode(1);
            statsListResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return statsListResult;
    }

    private List<MinitjWx> selectPersieDeamon(String[] times, String ddAppId, ConcurrentHashMap<String, WxConfig> wxConfigMap, Map<String, BuyPay> buyPayMap) {
        List<MinitjWx> productDatas;
        //小程序充值收入map
        Map<String, Orders> programReChargeMap = getProgramReChargeMap();

        // 解析时间
        String start = times[0].replace("-", "").trim();
        String end = times[1].replace("-", "").trim();
        //查询小程序数据
        productDatas = wxDailyVisitTrendMapper.selectVisitTrend(start, end, ddAppId);
        try {
            for (MinitjWx productDatum : productDatas) {
                String wxAppid = productDatum.getWxAppid();
                LocalDate wxDate = productDatum.getWxDate();
                productDatum.setProgramType(1);
                productDatum.setWxRemain2(productDatum.getWxRemain2() != null ? productDatum.getWxRemain2().multiply(new BigDecimal(100)) : new BigDecimal(0));
                WxConfig wxConfig = wxConfigMap.get(wxAppid);
                productDatum.setProductName(wxConfig != null ? wxConfig.getProductName() : "");
                productDatum.setDdAppPlatform(wxConfig != null ? wxConfig.getDdAppPlatform() : "");
                if (productDatum.getWxVisit() != null && productDatum.getWxVisit() != 0 && productDatum.getWxActive() != 0) {
                    productDatum.setWxAvgLogin(new BigDecimal(productDatum.getWxVisit()).divide(new BigDecimal(productDatum.getWxActive()), 2, ROUND_HALF_UP));
                } else {
                    productDatum.setWxAvgLogin(new BigDecimal(0));
                }
                if (productDatum.getWxShareUser() != null && productDatum.getWxShareUser() != 0 && productDatum.getWxActive() != 0) {
                    productDatum.setWxShareRate(new BigDecimal(productDatum.getWxShareUser() * 100).divide(new BigDecimal(productDatum.getWxActive()), 2, ROUND_HALF_UP));
                } else {
                    productDatum.setWxShareRate(new BigDecimal(0));
                }
                //查询小程序是否存在买量数据
                if (buyPayMap != null && !buyPayMap.isEmpty()) {
                    BuyPay buyPay = buyPayMap.get(wxAppid + "_" + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(wxDate));
                    if (buyPay != null) {
                        productDatum.setBuyCost(buyPay.getBuyCost());
                        productDatum.setBuyClickPrice(buyPay.getBuyClickPrice());
                    }
                }
                //查询小程序是否存在充值数据
                if (!programReChargeMap.isEmpty()) {
                    Orders orders = programReChargeMap.get(wxAppid + "_" + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(wxDate));
                    if (orders != null) {
                        productDatum.setRecharge(orders.getDdPrice());
                    }
                }
            }
            return productDatas;
        } catch (Exception e) {
            LOGGER.error("查询小游戏数据处理异常" + ", 详细信息:{}", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    /**
     * 处理充值map
     */
    private Map<String, Orders> getProgramReChargeMap() {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DATE(ddTrans) as ddTrans", "ddAppId", "SUM(ddPrice) as ddPrice");
        queryWrapper.groupBy("DATE(ddTrans)", "ddAppId");
        queryWrapper.eq("ddState", 1);
        List<Orders> orders = this.ordersService.getBaseMapper().selectList(queryWrapper);
        Map<String, Orders> programReChargeMap = new HashMap<>(16);
        orders.forEach(order -> programReChargeMap.put(order.getDdAppId() + "_" + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(order.getDdTrans()), order));
        return programReChargeMap;
    }

    private List<MinitjWx> selectMinitjWx(String[] times, String ddAppId, ConcurrentHashMap<String, WxConfig> wxConfigMap, Map<String, BuyPay> buyPayMap, Map<String, AdValueWxAdUnit> wxAdUnitMap) {
        QueryWrapper<MinitjWx> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("DATE(wx_date)", times[0].trim(), times[1].trim());
        queryWrapper.eq(StringUtils.isNotBlank(ddAppId), "wx_appid", ddAppId);
        // 小游戏的时候查询
        List<MinitjWx> wxDatas = this.minitjWxService.getBaseMapper().selectList(queryWrapper);
        List<MinitjWx> newDataList = new ArrayList<>();
        try {
            for (MinitjWx wxData : wxDatas) {
                WxConfig wxConfig = wxConfigMap.get(wxData.getWxAppid());
                if (wxConfig == null) {
                    continue;
                }
                // 设置data信息
                wxData.setProgramType(wxConfig.getProgramType());
                wxData.setProductName(wxConfig.getProductName());
                wxData.setDdAppPlatform(wxConfig.getDdAppPlatform());
                if (wxAdUnitMap != null && !wxAdUnitMap.isEmpty()) {
                    AdValueWxAdUnit wxAdUnit = wxAdUnitMap.get(wxData.getWxAppid() + "_" + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(wxData.getWxDate()));
                    if (wxAdUnit != null) {
                        wxData.setScreenIncome(new BigDecimal(wxAdUnit.getScreenIncome()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                    } else {
                        wxData.setScreenIncome(new BigDecimal(0));
                    }
                }
                //设置广告数据
                wxData.setAdRevenue(wxData.getWxBannerIncome().add(wxData.getWxVideoIncome()).add(wxData.getScreenIncome() != null ? wxData.getScreenIncome() : new BigDecimal(0)));
                BigDecimal adRevenue = wxData.getAdRevenue();
                Integer wxActive = wxData.getWxActive();
                if (!wxActive.equals(0)) {
                    BigDecimal divide = adRevenue.divide(new BigDecimal(wxActive), 4, 4);
                    //设置活跃up
                    wxData.setActiveUp(divide);
                }
                if (buyPayMap != null && !buyPayMap.isEmpty()) {
                    BuyPay buyPay = buyPayMap.get(wxData.getWxAppid() + "_" + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(wxData.getWxDate()));
                    if (buyPay != null) {
                        wxData.setBuyCost(buyPay.getBuyCost());
                        wxData.setBuyClickPrice(buyPay.getBuyClickPrice());
                        wxData.setWxAdNewPrice(wxData.getWxRegAd().equals(0) ? new BigDecimal(0) : buyPay.getBuyCost().divide(new BigDecimal(wxData.getWxRegAd()), 2, ROUND_HALF_UP));
                    }
                }
                newDataList.add(wxData);
            }
            return newDataList;
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    private Map<String, AdValueWxAdUnit> getWxAdUnitMap(String[] times) {
        Map<String, AdValueWxAdUnit> adValueWxAdUnitMap = new HashMap<>(16);
        QueryWrapper<AdValueWxAdUnit> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("DATE(date)", times[0].trim(), times[1].trim());
        queryWrapper.eq("slotId", "3030046789020061");
        queryWrapper.eq("appSource", "JJ");
        queryWrapper.groupBy("DATE", "appId");
        queryWrapper.select("appId", "SUM(income) AS screenIncome", "date");
        List<AdValueWxAdUnit> adValueWxAdUnits = this.adValueWxAdUnitService.getBaseMapper().selectList(queryWrapper);
        adValueWxAdUnits.forEach(adValueWxAdUnit -> adValueWxAdUnitMap.put(adValueWxAdUnit.getAppId() + "_" + adValueWxAdUnit.getDate(), adValueWxAdUnit));
        return adValueWxAdUnitMap;
    }

    /**
     * g根据时间处理买量数据map
     *
     * @param times times
     * @return Map
     */
    private Map<String, BuyPay> getBuyPayMap(String[] times) {
        Map<String, BuyPay> buyPayMap = new HashMap<>(16);
        QueryWrapper<BuyPay> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("DATE(buy_date)", times[0].trim(), times[1].trim());
        // 小游戏的时候查询
        List<BuyPay> buyPays = this.buyPayService.getBaseMapper().selectList(queryWrapper);
        buyPays.forEach(buyPay -> buyPayMap.put(buyPay.getBuyAppId() + "_" + buyPay.getBuyDate(), buyPay));
        return buyPayMap;

    }

    private ConcurrentHashMap<String, WxConfig> getProductMap() {
        ConcurrentHashMap<String, WxConfig> wxConfigMap = new ConcurrentHashMap<>();
        List<WxConfig> wxConfigEntityList = this.wxConfigService.getCacheEntityList(WxConfig.class);
        List<MiniGame> miniGameEntityList = this.miniGameService.getCacheEntityList(MiniGame.class);

        wxConfigEntityList.forEach(entity -> {
            wxConfigMap.put(entity.getCacheKey(), entity);
            wxConfigMap.put("value", entity);

        });
        miniGameEntityList.forEach(entity -> {
            WxConfig wxConfig = wxConfigMap.get(entity.getCacheKey());
            if (wxConfig == null) {
                WxConfig newWxConfig = new WxConfig();
                newWxConfig.setId(entity.getGameAppid());
                newWxConfig.setProductName(entity.getGameName());
                newWxConfig.setDdAppPlatform(entity.getGameAppplatform());
                newWxConfig.setProgramType(0);
                wxConfigMap.put(entity.getGameAppid(), newWxConfig);
            }
        });
        return wxConfigMap;
    }

    private String[] getTimes(StatsListParam statsListParam) {
        String[] times = new String[2];
        String beginTime = null, endTime = null;
        JSONObject queryObject = JSONObject.parseObject(statsListParam.getQueryData());
        String time = queryObject.getString("times");
        if (StringUtils.isNotBlank(time)) {
            String[] timeRangeArray = StringUtils.split(time, "~");
            beginTime = timeRangeArray[0].trim();
            endTime = timeRangeArray[1].trim();
        }
        beginTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(2));
        times[0] = beginTime;
        endTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        times[1] = endTime;
        return times;
    }


    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

    @Autowired
    public void setMiniGameService(MiniGameService miniGameService) {
        this.miniGameService = miniGameService;
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
    public void setWxDailyVisitTrendMapper(WxDailyVisitTrendMapper wxDailyVisitTrendMapper) {
        this.wxDailyVisitTrendMapper = wxDailyVisitTrendMapper;
    }
}

