package com.cc.manager.modules.fc.service;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.entity.AdValueWxAdUnit;
import com.cc.manager.modules.fc.entity.DataCollect;
import com.cc.manager.modules.fc.entity.MiniGame;
import com.cc.manager.modules.fc.entity.MinitjWx;
import com.cc.manager.modules.fc.mapper.MinitjWxMapper;
import com.cc.manager.modules.jj.entity.BuyPay;
import com.cc.manager.modules.jj.entity.Orders;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.service.BuyPayService;
import com.cc.manager.modules.jj.service.OrdersService;
import com.cc.manager.modules.jj.service.WxConfigService;
import org.apache.commons.collections.CollectionUtils;
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

/**
 * @author cf
 * @since 2020-05-22
 */
@Service
public class MinitjWxDataCollectService extends BaseStatsService<MinitjWx, MinitjWxMapper> {

    private WxConfigService wxConfigService;
    private MiniGameService miniGameService;

    private WxDailyVisitTrendService wxDailyVisitTrendService;
    private MinitjWxService minitjWxService;

    private AdValueWxAdUnitService adValueWxAdUnitService;
    private OrdersService orderService;
    private BuyPayService buyPayService;

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
            List<DataCollect> dataCollects;
            List<DataCollect> newDataCollects = new ArrayList<>();
            String[] times = getTimes(statsListParam);
            String type = "";

            JSONObject queryObject = JSONObject.parseObject(statsListParam.getQueryData());
            if (queryObject != null) {
                type = queryObject.getString("type");
            }
            if (StringUtils.isBlank(type)) {
                dataCollects = queryProgramStatis(times[0], times[1]);
                Map<String, DataCollect> dataCollectMap = queryMinitjWxStatis(times[0], times[1], type);
                // 合并小游戏和小程序数据
                if (MapUtil.isNotEmpty(dataCollectMap) && CollectionUtils.isNotEmpty(dataCollects)) {
                    dataCollects = countProgramAndMititjWx(dataCollects, dataCollectMap);
                } else {
                    if (dataCollects.isEmpty()) {
                        dataCollects = new ArrayList<>(dataCollectMap.values());
                    }
                }
            } else {
                if ("1".equals(type)) {
                    // 小程序查询
                    dataCollects = queryProgramStatis(times[0], times[1]);
                } else {
                    // 小游戏查询
                    Map<String, DataCollect> dataCollectMap = queryMinitjWxStatis(times[0], times[1], type);
                    dataCollects = new ArrayList<>(dataCollectMap.values());
                }
            }

            // 查询买量数据
            Map<String, BuyPay> buyPayMap = queryBuPayByDate(times[0], times[1], type);
            // 追加买量数据和分享率
            countBuyData(dataCollects, buyPayMap);

            // 查询插屏总收入
            Map<String, AdValueWxAdUnit> screenIncomeMap = queryAdValueWxAdUnitDate(times[0], times[1]);
            // 追加插屏总收入
            countScreenIncomeData(dataCollects, screenIncomeMap);
            int page = statsListParam.getPage();
            int limit = statsListParam.getLimit();

            for (int i = (page - 1) * limit; i < page * limit; i++) {
                if (dataCollects.size() > i) {
                    newDataCollects.add(dataCollects.get(i));
                }
            }
            statsListResult.setData(JSONArray.parseArray(JSON.toJSONString(newDataCollects)));
            statsListResult.setCount(dataCollects.size());
        } catch (Exception e) {
            statsListResult.setCode(1);
            statsListResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return statsListResult;
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
            if (screenIncomeMap != null && screenIncomeMap.size() > 0) {
                AdValueWxAdUnit adValueWxAdUnit = screenIncomeMap.get(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dataCollect.getWxDate()));
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
            if (buyPayMap != null && buyPayMap.size() > 0) {
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
    private List<DataCollect> countProgramAndMititjWx(List<DataCollect> dataCollects, Map<String, DataCollect> dataCollectMap) {
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
        // 获取配置
        Map<String, WxConfig> wxConfigMap = getWxConfigMap();
        // 查询列表
        List<MinitjWx> minitjWxes = minitjWxService.queryMinitjWxByDate(beginTime, endTime);
        // 生成map集合
        Map<String, List<MinitjWx>> minitjWxListMap;
        minitjWxListMap = getMinitjListMap(minitjWxes, wxConfigMap, type);
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
        if (!minitjListMap.isEmpty()) {
            for (String str : minitjListMap.keySet()) {
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
                for (MinitjWx minitj : minitjListMap.get(str)) {
                    newCount += minitj.getWxNew();
                    activeCount += minitj.getWxActive();
                    videoIncomeCount += minitj.getWxVideoIncome().doubleValue();
                    bannerIncomeCount += minitj.getWxBannerIncome().doubleValue();
                    shareUserCount += minitj.getWxShareUser();
                    shareCount += minitj.getWxShareCount();
                }
                dataCollect.setWxDate(LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                dataCollect.setProductCount(minitjListMap.get(str).size());
                dataCollect.setNewCount(newCount);
                dataCollect.setActiveCount(activeCount);
                dataCollect.setVideoIncomeCount(new BigDecimal(videoIncomeCount).setScale(2, BigDecimal.ROUND_HALF_UP));
                dataCollect.setBannerIncomeCount(new BigDecimal(bannerIncomeCount).setScale(2, BigDecimal.ROUND_HALF_UP));
                dataCollect.setShareUserCount(shareUserCount);
                dataCollect.setShareCount(shareCount);
                dataCollect.setAdRevenueCount(new BigDecimal(videoIncomeCount + bannerIncomeCount));
                dataCollect.setRevenueCount(dataCollect.getAdRevenueCount());
                dataCollectMap.put(str, dataCollect);
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
    private Map<String, List<MinitjWx>> getMinitjListMap(List<MinitjWx> minitjWxes, Map<String, WxConfig> wxConfigMap, String type) {
        Map<String, List<MinitjWx>> minitjWxListMap = new HashMap<>(16);
        for (MinitjWx minitjWx : minitjWxes) {
            WxConfig wxConfig = wxConfigMap.get(minitjWx.getWxAppid());
            // 如果查不到配置不处理
            if (wxConfig == null) {
                continue;
            } else {
                // 如果类型不为空
                if (StringUtils.isNotBlank(type)) {
                    // 判断类型：如果该条数据的类型不符合不做处理
                    if (!StringUtils.equals(String.valueOf(wxConfig.getProgramType()), type)) {
                        continue;
                    }
                }
            }
            String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(minitjWx.getWxDate());
            List<MinitjWx> miniWxDate = minitjWxListMap.get(date);
            if (miniWxDate == null || miniWxDate.isEmpty()) {
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
    private List<DataCollect> queryProgramStatis(String beginTime, String endTime) {
        List<DataCollect> dataCollects = new ArrayList<>();

        List<MinitjWx> productData = wxDailyVisitTrendService.selectVisitTrendSummary(beginTime, endTime);
        Map<String, Orders> programReChargeMap = getProgramReChargeMap();
        productData.forEach(appletData -> {
            DataCollect dataCollect = new DataCollect();
            // 日期
            dataCollect.setWxDate(appletData.getWxDate());
            // 总数量
            dataCollect.setProductCount(appletData.getProductCount());
            //新增
            dataCollect.setNewCount(appletData.getWxNew());
            // 活跃
            dataCollect.setActiveCount(appletData.getWxActive());
            // 分享人数
            dataCollect.setShareUserCount(appletData.getWxShareUser());
            // 分享次数
            dataCollect.setShareCount(appletData.getWxShareCount());
            Orders orders = programReChargeMap.get(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(appletData.getWxDate()));
            if (orders != null) {
                //充值
                dataCollect.setRechargeCount(orders.getDdPrice());
                //总收入
                dataCollect.setRevenueCount(orders.getDdPrice());
            } else {
                dataCollect.setRechargeCount(new BigDecimal(0));
            }
            dataCollects.add(dataCollect);
        });
        return dataCollects;
    }

    /**
     * @return 获取小程序充值金额Map
     */
    private Map<String, Orders> getProgramReChargeMap() {
        List<Orders> orders = orderService.queryProgramReChargeCount();
        Map<String, Orders> programReChargeMap = new HashMap<>(16);
        orders.forEach(order -> programReChargeMap.put(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(order.getDdTrans()), order));
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
        if (!buyPays.isEmpty()) {
            buyPays.forEach(buyPay -> map.put(buyPay.getBuyDate(), buyPay));
        }
        return map;
    }

    /**
     * 判断时间
     *
     * @param statsListParam statsListParam
     * @return 返回格式化时间
     */
    private String[] getTimes(StatsListParam statsListParam) {
        String[] times = new String[2];
        String beginTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(14));
        String endTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(statsListParam.getQueryData());
            String time = queryObject.getString("times");
            if (StringUtils.isNotBlank(time)) {
                String[] timeRangeArray = StringUtils.split(time, "~");
                beginTime = timeRangeArray[0].trim();
                endTime = timeRangeArray[1].trim();
            }
        }
        times[0] = beginTime;
        times[1] = endTime;
        return times;
    }

    /**
     * 获取配置Map
     * 排除公众号配置
     *
     * @return 配置Map
     */
    private Map<String, WxConfig> getWxConfigMap() {
        Map<String, WxConfig> wxConfigMap = new HashMap<>(16);
        List<WxConfig> wxConfigs = this.wxConfigService.getCacheEntityList(WxConfig.class);
        wxConfigs.forEach(wxConfig -> {
            if (wxConfig.getProgramType() != 2) {
                wxConfigMap.put(wxConfig.getAddId(), wxConfig);
            }
        });
        List<MiniGame> miniGameEntityList = this.miniGameService.getCacheEntityList(MiniGame.class);
        miniGameEntityList.forEach(entity -> {
            WxConfig wxConfig = wxConfigMap.get(entity.getCacheKey());
            if (wxConfig == null) {
                WxConfig newWxConfig = new WxConfig();
                newWxConfig.setAddId(entity.getGameAppid());
                newWxConfig.setProductName(entity.getGameName());
                newWxConfig.setDdAppPlatform(entity.getGameAppplatform());
                newWxConfig.setProgramType(0);
                wxConfigMap.put(entity.getGameAppid(), newWxConfig);
            }
        });
        return wxConfigMap;
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

}
