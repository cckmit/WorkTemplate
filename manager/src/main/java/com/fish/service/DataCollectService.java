package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.fourth.mapper.AdValueWxAdUnitMapper;
import com.fish.dao.fourth.mapper.AdValueWxAdposMapper;
import com.fish.dao.fourth.mapper.WxDailyVisitTrendMapper;
import com.fish.dao.fourth.model.AdValueWxAdUnit;
import com.fish.dao.fourth.model.AdValueWxAdpos;
import com.fish.dao.primary.mapper.BuyPayMapper;
import com.fish.dao.primary.mapper.ProductDataMapper;
import com.fish.dao.primary.model.BuyPay;
import com.fish.dao.second.mapper.OrdersMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.Orders;
import com.fish.dao.second.model.WxConfig;
import com.fish.dao.third.mapper.MinitjWxMapper;
import com.fish.dao.third.model.DataCollect;
import com.fish.dao.third.model.MinitjWx;
import com.fish.dao.third.model.ProductData;
import com.fish.protocols.GetParameter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataCollectService implements BaseService<DataCollect> {

    @Autowired
    WxConfigMapper wxConfigMapper;

    @Autowired
    MinitjWxMapper minitjWxMapper;
    @Autowired
    BuyPayMapper buyPayMapper;
    @Autowired
    ProductDataMapper productDataMapper;
    @Autowired
    AdValueWxAdUnitMapper adValueWxAdUnitMapper;
    @Autowired
    WxDailyVisitTrendMapper wxDailyVisitTrendMapper;
    @Autowired
    AdValueWxAdposMapper adValueWxAdposMapper;
    @Autowired
    OrdersMapper ordersMapper;

    /**
     * 查询汇总数据
     *
     * @param parameter 参数
     * @return 查询结果
     */
    @Override
    public List<DataCollect> selectAll(GetParameter parameter) {
        String[] times = getTimes(parameter);
        JSONObject search = getSearchData(parameter.getSearchData());
        String type = "";
        List<DataCollect> dataCollects = new ArrayList<>();
        if (search != null) {
            type = search.getString("type");
        }
        if (StringUtils.isBlank(type)) {
            dataCollects = queryProgramStatis(times[0], times[1]);
            Map<String, DataCollect> dataCollectMap = queryMinitjWxStatis(times[0], times[1], type);
            // 合并小游戏和小程序数据
            if (!dataCollectMap.isEmpty() && !dataCollects.isEmpty()) {
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
        return dataCollects;
    }

    /**
     * 追加插屏总收入
     *
     * @param dataCollects    结果合集
     * @param screenIncomeMap 插屏收入Map
     */
    private void countScreenIncomeData(List<DataCollect> dataCollects, Map<String, AdValueWxAdUnit> screenIncomeMap) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        dataCollects.forEach(dataCollect -> {
            if (dataCollect == null) {
                return;
            }
            if (screenIncomeMap != null && screenIncomeMap.size() > 0) {
                AdValueWxAdUnit adValueWxAdUnit = screenIncomeMap.get(format.format(dataCollect.getWxDate()));
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
        List<AdValueWxAdUnit> adValueWxAdUnits = adValueWxAdUnitMapper.queryScreenIncomeByDate(beginTime, endTime);
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        dataCollects.forEach(dataCollect -> {
            if (dataCollect == null) {
                return;
            }
            if (buyPayMap != null && buyPayMap.size() > 0) {
                BuyPay buyPay = buyPayMap.get(format.format(dataCollect.getWxDate()));
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
            DataCollect collect = dataCollectMap.get(DateFormatUtils.format(dataCollect.getWxDate(), "yyyy-MM-dd"));
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
        List<MinitjWx> minitjWxes = minitjWxMapper.queryMinitjWxByDate(beginTime, endTime);
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
                try {
                    dataCollect.setWxDate(new SimpleDateFormat("yyyy-MM-dd").parse(str));
                } catch (ParseException ignored) {
                }
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, List<MinitjWx>> minitjWxListMap = new HashMap<>(16);
        for (MinitjWx minitjWx : minitjWxes) {
            WxConfig wxConfig = wxConfigMap.get(minitjWx.getWxAppid());
            // 如果查不到配置不处理
            if (wxConfig == null) {
                continue;
            } else {
                // 如果类型不为空
                if (!StringUtils.isBlank(type)) {
                    // 判断类型：如果该条数据的类型不符合不做处理
                    if (!String.valueOf(wxConfig.getProgramType()).equals(type)) {
                        continue;
                    }
                }
            }
            String date = format.format(minitjWx.getWxDate());
            List<MinitjWx> minitjWxes1 = minitjWxListMap.get(date);
            if (minitjWxes1 == null || minitjWxes1.isEmpty()) {
                minitjWxes1 = new ArrayList<>();
                minitjWxes1.add(minitjWx);
                minitjWxListMap.put(date, minitjWxes1);
            } else {
                minitjWxes1.add(minitjWx);
                minitjWxListMap.put(date, minitjWxes1);
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

        List<ProductData> productData = wxDailyVisitTrendMapper.selectVisitTrendSummary(beginTime, endTime);
        Map<String, Orders> programReChargeMap = getProgramReChargeMap();
        productData.forEach(productData1 -> {
            DataCollect dataCollect = new DataCollect();
            // 日期
            dataCollect.setWxDate(productData1.getWxDate());
            // 总数量
            dataCollect.setProductCount(productData1.getProductCount());
            //新增
            dataCollect.setNewCount(productData1.getWxNew());
            // 活跃
            dataCollect.setActiveCount(productData1.getWxActive());
           /* // 视频收入
            dataCollect.setVideoIncomeCount(productData1.getWxVideoIncome());
            // Banner收入
            dataCollect.setBannerIncomeCount(productData1.getWxBannerIncome());
            //广告收入
            dataCollect.setAdRevenueCount(productData1.getAdRevenue());*/
            // 分享人数
            dataCollect.setShareUserCount(productData1.getWxShareUser());
            // 分享次数
            dataCollect.setShareCount(productData1.getWxShareCount());
            Orders orders = programReChargeMap.get(new SimpleDateFormat("yyyy-MM-dd").format(productData1.getWxDate()));
            if (orders != null) {
                //充值
                dataCollect.setRechargeCount(orders.getDdprice());
                //总收入
                dataCollect.setRevenueCount(orders.getDdprice());
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
        List<Orders> orders = ordersMapper.queryProgramReChargeCount();
        Map<String, Orders> programReChargeMap = new HashMap<>(16);
        orders.forEach(order -> programReChargeMap.put(new SimpleDateFormat("yyyy-MM-dd").format(order.getDdtrans()), order));
        return programReChargeMap;
    }

    /**
     * 存放广告收入map--暂未使用
     */
    private Map<String, AdValueWxAdpos> getWxAdIncomeMap(String start, String end, String ddAppId) {
        List<AdValueWxAdpos> adValueWxAdPos = adValueWxAdposMapper.selectDataCollectTypeIncome(start, end, ddAppId);
        Map<String, AdValueWxAdpos> wxAdpPosMap = new HashMap<>(16);
        for (AdValueWxAdpos adValueWxAdPo : adValueWxAdPos) {
            String date = adValueWxAdPo.getDate();
            AdValueWxAdpos adValueWxAdpos = wxAdpPosMap.get(date);
            if (adValueWxAdpos == null) {
                switch (adValueWxAdPo.getSlotId()) {
                    case "8040321819858439":
                        adValueWxAdPo.setWxBannerIncome(new BigDecimal(adValueWxAdPo.getIncome()));
                        break;
                    case "1030436212907001":
                        adValueWxAdPo.setWxVideoIncome(new BigDecimal(adValueWxAdPo.getIncome()));
                        break;
                    case "3030046789020061":
                        adValueWxAdPo.setScreenIncome(new BigDecimal(adValueWxAdPo.getIncome()));
                        break;
                    case "7070083760581921":
                        break;
                    default:
                        break;
                }
                wxAdpPosMap.put(date, adValueWxAdPo);
            } else {
                switch (adValueWxAdPo.getSlotId()) {
                    case "8040321819858439":
                        adValueWxAdpos.setWxBannerIncome(new BigDecimal(adValueWxAdPo.getIncome()));
                        break;
                    case "1030436212907001":
                        adValueWxAdpos.setWxVideoIncome(new BigDecimal(adValueWxAdPo.getIncome()));
                        break;
                    case "3030046789020061":
                        adValueWxAdpos.setScreenIncome(new BigDecimal(adValueWxAdPo.getIncome()));
                        break;
                    case "7070083760581921":
                        break;
                    default:
                        break;
                }
            }
        }
        return wxAdpPosMap;
    }


    /**
     * 查询买量数据
     *
     * @return 返回买量数据Map
     */
    private Map<String, BuyPay> queryBuPayByDate(String beginTime, String endTime, String type) {
        Map<String, BuyPay> map = new HashMap<>(16);
        List<BuyPay> buyPays = buyPayMapper.queryByPayCollectByDate(beginTime, endTime, type);
        if (!buyPays.isEmpty()) {
            buyPays.forEach(buyPay -> map.put(buyPay.getBuyDate(), buyPay));
        }
        return map;
    }

    /**
     * 判断时间
     *
     * @param parameter parameter
     * @return 返回格式化时间
     */
    private String[] getTimes(GetParameter parameter) {
        String[] times = new String[2];
        JSONObject search = getSearchData(parameter.getSearchData());
        String beginTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        String endTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        if (search != null) {
            if (!StringUtils.isBlank(search.getString("beginDate"))) {
                beginTime = search.getString("beginDate");
            }
            if (!StringUtils.isBlank(search.getString("endDate"))) {
                endTime = search.getString("endDate");
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
        List<WxConfig> wxConfigs = wxConfigMapper.selectAll();
        wxConfigs.forEach(wxConfig -> {
            if (wxConfig.getProgramType() != 2) {
                wxConfigMap.put(wxConfig.getDdappid(), wxConfig);
            }
        });
        return wxConfigMap;
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null) {
            return;
        }
        parameter.setSort("wxDate");
        parameter.setOrder("desc");
    }

    @Override
    public Class<DataCollect> getClassInfo() {
        return DataCollect.class;
    }

    @Override
    public boolean removeIf(DataCollect productData, JSONObject searchData) {
        return false;
    }
}
