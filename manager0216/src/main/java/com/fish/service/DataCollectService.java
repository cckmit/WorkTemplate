package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.BuyPayMapper;
import com.fish.dao.primary.mapper.ProductDataMapper;
import com.fish.dao.primary.model.BuyPay;
import com.fish.dao.second.mapper.WxConfigMapper;
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

    /**
     * 查询汇总数据
     *
     * @param parameter
     * @return
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
        return dataCollects;
    }

    /**
     * 计算买量数据
     *
     * @param dataCollects
     * @param buyPayMap
     */
    private void countBuyData(List<DataCollect> dataCollects, Map<String, BuyPay> buyPayMap) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        dataCollects.forEach(dataCollect -> {
            if (dataCollect == null) {
                return;
            }
            if (buyPayMap != null && buyPayMap.size() > 0) {
                if (dataCollect != null) {
                    BuyPay buyPay = buyPayMap.get(format.format(dataCollect.getWxDate()));
                    if (buyPay != null) {
                        dataCollect.setBuyPay(buyPay.getBuyCost());
                    }
                }
            }
            dataCollect.setBannerIncomeCount(dataCollect.getBannerIncomeCount().setScale(2, BigDecimal.ROUND_HALF_UP));
            dataCollect.setVideoIncomeCount(dataCollect.getVideoIncomeCount().setScale(2, BigDecimal.ROUND_HALF_UP));
            BigDecimal rate = new BigDecimal((double) dataCollect.getShareUserCount() * 100 / dataCollect.getActiveCount())
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            dataCollect.setShareRateCount(rate);
            dataCollect.setAdRevenueCount(dataCollect.getAdRevenueCount().setScale(2, BigDecimal.ROUND_HALF_UP));
            dataCollect.setRevenueCount(dataCollect.getRevenueCount().setScale(2, BigDecimal.ROUND_HALF_UP));
        });
    }

    /**
     * 计算小程序和小游戏的汇总数据
     *
     * @param dataCollects
     * @param dataCollectMap
     */
    private List<DataCollect> countProgramAndMititjWx(List<DataCollect> dataCollects, Map<String, DataCollect> dataCollectMap) {
        dataCollects.forEach(dataCollect -> {
            DataCollect collect = dataCollectMap.get(DateFormatUtils.format(dataCollect.getWxDate(), "yyyy-MM-dd"));
            if (collect != null) {
                collect.setProductCount(dataCollect.getProductCount() + collect.getProductCount());
                collect.setNewCount(dataCollect.getNewCount() + collect.getNewCount());
                collect.setActiveCount(dataCollect.getActiveCount() + collect.getActiveCount());
                collect.setVideoIncomeCount(dataCollect.getVideoIncomeCount().add(collect.getVideoIncomeCount()));
                collect.setBannerIncomeCount(dataCollect.getBannerIncomeCount().add(collect.getBannerIncomeCount()));
                collect.setAdRevenueCount(collect.getAdRevenueCount().add(dataCollect.getAdRevenueCount()));
                collect.setShareCount(dataCollect.getShareCount() + collect.getShareCount());
                collect.setShareUserCount(dataCollect.getShareUserCount() + collect.getShareUserCount());
                collect.setRevenueCount(collect.getRevenueCount().add(dataCollect.getRevenueCount() != null ? dataCollect.getRevenueCount() : new BigDecimal(0)));
            }
        });
        return new ArrayList<>(dataCollectMap.values());
    }

    /**
     * 查询小游戏汇总数据（最终数据）
     * 生成map集合
     *
     * @param beginTime
     * @param endTime
     * @param type      选择的类型
     * @return
     */
    private Map<String, DataCollect> queryMinitjWxStatis(String beginTime, String endTime, String type) {
        // 获取配置
        Map<String, WxConfig> wxConfigMap = getWxConfigMap();
        // 查询列表
        List<MinitjWx> minitjWxes = minitjWxMapper.queryMinitjWxByDate(beginTime, endTime);
        // 生成map集合
        Map<String, List<MinitjWx>> minitjWxListMap = new HashMap<>(16);
        minitjWxListMap = getMinitjListMap(minitjWxes, wxConfigMap, type);
        return countMinitWxData(minitjWxListMap);
    }

    /**
     * 统计小游戏数据
     *
     * @param minitjListMap
     * @return
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
                //广告总收入
                Double adRevenueCount = 0.0;
                //总收入
                Double revenueCount = 0.0;
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
                } catch (ParseException e) {
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
     * @param minitjWxes
     * @param wxConfigMap
     * @param type
     * @return
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
     * @return
     */
    private List<DataCollect> queryProgramStatis(String beginTime, String endTime) {
        List<DataCollect> dataCollects = new ArrayList<>();
        List<ProductData> productData = productDataMapper.queryProgramByDate(beginTime, endTime);
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
            // 视频收入
            dataCollect.setVideoIncomeCount(productData1.getWxVideoIncome());
            // Banner收入
            dataCollect.setBannerIncomeCount(productData1.getWxBannerIncome());
            //广告收入
            dataCollect.setAdRevenueCount(productData1.getAdRevenue());
            // 分享人数
            dataCollect.setShareUserCount(productData1.getWxShareUser());
            // 分享次数
            dataCollect.setShareCount(productData1.getWxShareCount());
            // 总收入
            dataCollect.setRevenueCount(productData1.getRevenueCount());
            dataCollects.add(dataCollect);
        });
        return dataCollects;
    }

    /**
     * 查询买量数据
     *
     * @return
     */
    private Map<String, BuyPay> queryBuPayByDate(String beginTime, String endTime, String type) {
        Map<String, BuyPay> map = new HashMap<>(16);
        List<BuyPay> buyPays = buyPayMapper.queryByPayCollectByDate(beginTime, endTime, type);
        if (!buyPays.isEmpty()) {
            buyPays.forEach(buyPay -> {
                map.put(buyPay.getBuyDate(), buyPay);
            });
        }
        return map;
    }

    /**
     * 判断时间
     *
     * @param parameter
     * @return
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
     * @return
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
