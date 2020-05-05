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
import com.fish.dao.third.model.MinitjWx;
import com.fish.dao.third.model.ProductData;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * 数据统计-产品数据详情
 *
 * @author
 * @date
 */
@Service
public class ProductDataService implements BaseService<ProductData> {

    @Autowired
    WxConfigMapper wxConfigMapper;

    @Autowired
    MinitjWxMapper minitjWxMapper;

    @Autowired
    AdValueWxAdUnitMapper adValueWxAdUnitMapper;
    @Autowired
    WxConfigService wxConfigService;
    @Autowired
    ProductDataMapper productDataMapper;

    @Autowired
    BuyPayMapper buyPayMapper;

    @Autowired
    WxDailyVisitTrendMapper wxDailyVisitTrendMapper;

    @Autowired
    AdValueWxAdposMapper adValueWxAdposMapper;

    @Autowired
    OrdersMapper ordersMapper;

    /**
     * 获取微信配置表
     *
     * @return 配置结果集
     *//*
    private Map<String, WxConfig> getWxConfigMap() {
        Map<String, WxConfig> wxConfigMap = new HashMap<>();
        List<WxConfig> allWxConfig = cacheService.getAllWxConfig();
        allWxConfig.forEach(wxConfig -> {
            wxConfigMap.put(wxConfig.getDdappid(), wxConfig);
        });
        return wxConfigMap;
    }*/

    @Override
    public List<ProductData> selectAll(GetParameter parameter) {
        ConcurrentHashMap<String, WxConfig> wxConfigMap = this.wxConfigService.getAll(WxConfig.class);
        //Map<String, WxConfig> wxConfigMap = getWxConfigMap();
        Map<String, BuyPay> buyPayMap = getBuyPayMap(parameter);
        Map<String, AdValueWxAdUnit> wxAdUnitMap = getWxAdUnitMap(parameter);
        List<ProductData> productDatas;
        JSONObject search = getSearchData(parameter.getSearchData());
        if (search != null) {
            //搜索
            String ddAppId = search.getString("ddappid");
            String type = search.getString("type");
            switch (type) {
                case "0":
                    productDatas = selectMinitjWx(parameter, ddAppId, wxConfigMap, buyPayMap, wxAdUnitMap);
                    break;
                case "1":
                    productDatas = selectPersieDeamon(parameter, ddAppId, wxConfigMap, buyPayMap);
                    break;
                default:
                    // 如果只选了时间
                    productDatas = selectMinitjWx(parameter, ddAppId, wxConfigMap, buyPayMap, wxAdUnitMap);
                    List<ProductData> list = selectPersieDeamon(parameter, ddAppId, wxConfigMap, buyPayMap);
                    productDatas.addAll(list);
                    break;
            }
        } else {
            productDatas = selectMinitjWx(parameter, null, wxConfigMap, buyPayMap, wxAdUnitMap);
            List<ProductData> list = selectPersieDeamon(parameter, null, wxConfigMap, buyPayMap);
            productDatas.addAll(list);
        }
        return productDatas;
    }


    /**
     * 查询小程序数据
     *
     * @param parameter   搜索参数
     * @param ddAppId     appId
     * @param wxConfigMap 配置Map
     * @param buyPayMap   买量Map
     * @return 结果集合
     */
    private List<ProductData> selectPersieDeamon(GetParameter parameter, String ddAppId, Map<String, WxConfig> wxConfigMap,
                                                 Map<String, BuyPay> buyPayMap) {
        List<ProductData> productDatas;
        // 解析时间
        String[] times = getSearchTime(parameter);
        String start = times[0];
        String end = times[1];

        //获取广告收入map--暂未使用
        Map<String, AdValueWxAdpos> wxAdpPosMap = getWxAdIncomeMap(start, end, ddAppId);
        //小程序充值收入map
        Map<String, Orders> programReChargeMap = getProgramReChargeMap();
        //查询小程序数据
        productDatas = wxDailyVisitTrendMapper.selectVisitTrend(start.replace("/", ""), end.replace("/", ""), ddAppId);
        try {
            for (ProductData productDatum : productDatas) {
                String wxAppid = productDatum.getWxAppid();
                Date wxDate = productDatum.getWxDate();
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
                    BuyPay buyPay = buyPayMap.get(wxAppid + "_" + new SimpleDateFormat("yyyy-MM-dd").format(wxDate));
                    if (buyPay != null) {
                        productDatum.setBuyCost(buyPay.getBuyCost());
                        productDatum.setBuyClickPrice(buyPay.getBuyClickPrice());
                    }
                }
                //查询小程序是否存在充值数据
                if (!programReChargeMap.isEmpty()) {
                    Orders orders = programReChargeMap.get(wxAppid + "_" + new SimpleDateFormat("yyyy-MM-dd").format(wxDate));
                    if (orders != null) {
                        productDatum.setRecharge(orders.getDdprice());
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("查询小游戏数据处理异常" + ", 详细信息:{}", e.getMessage());
            // System.out.println(e.getMessage());
        }
        return productDatas;
    }

    /**
     * 小程序充值存放map
     */
    private Map<String, Orders> getProgramReChargeMap() {
        List<Orders> orders = ordersMapper.queryProgramReCharge();
        Map<String, Orders> programReChargeMap = new HashMap<>(16);
        orders.forEach(order -> programReChargeMap.put(order.getDdappid() + "_" + new SimpleDateFormat("yyyy-MM-dd").format(order.getDdtrans()), order));
        return programReChargeMap;
    }

    /**
     * 存放广告收入map
     */
    private Map<String, AdValueWxAdpos> getWxAdIncomeMap(String start, String end, String ddAppId) {
        List<AdValueWxAdpos> adValueWxAdPos = adValueWxAdposMapper.selectTypeIncomeByDate(start, end, ddAppId);
        Map<String, AdValueWxAdpos> wxAdpPosMap = new HashMap<>(16);
        for (AdValueWxAdpos adValueWxAdPo : adValueWxAdPos) {
            String appId = adValueWxAdPo.getAppId();
            String date = adValueWxAdPo.getDate();
            AdValueWxAdpos adValueWxAdpos = wxAdpPosMap.get(appId + "_" + date);
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
            if (adValueWxAdpos == null) {
                wxAdpPosMap.put(appId + "_" + date, adValueWxAdPo);
            }
        }
        return wxAdpPosMap;
    }

    /**
     * 判断时间
     *
     * @param parameter parameter
     * @return 返回格式化时间
     */
    private String[] getSearchTime(GetParameter parameter) {

        String[] times = new String[2];
        String timeStr = "";
        JSONObject search = getSearchData(parameter.getSearchData());
        if (search != null) {
            timeStr = search.getString("times");
        }
        if (StringUtils.isNotBlank(timeStr)) {
            times[0] = timeStr.split("-")[0].trim();
            times[1] = timeStr.split("-")[1].trim();
        } else {
            String format = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
            times[0] = format;
            times[1] = format;
        }
        return times;
    }

    /**
     * 查询小游戏数据
     *
     * @param parameter parameter
     * @param ddAppId   前台输入框传的ddAppId
     * @return 小游戏数据集合
     */
    private List<ProductData> selectMinitjWx(GetParameter parameter, String ddAppId, Map<String, WxConfig> wxConfigMap,
                                             Map<String, BuyPay> buyPayMap, Map<String, AdValueWxAdUnit> wxAdUnitMap) {
        List<ProductData> productDatas = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] times = getSearchTime(parameter);
        String start = times[0];
        String end = times[1];
        // 小游戏的时候查询
        List<MinitjWx> wxDatas = minitjWxMapper.searchDatas(ddAppId, start, end);
        try {
            for (MinitjWx wxData : wxDatas) {
                WxConfig wxConfig = wxConfigMap.get(wxData.getWxAppid());
                if (wxConfig == null) {
                    continue;
                }
                ProductData productData = new ProductData();
                productData.setMinitjWx(wxData);
                // 设置data信息
                productData.setProgramType(wxConfig.getProgramType());
                productData.setProductName(wxConfig.getProductName());
                productData.setDdAppPlatform(wxConfig.getDdAppPlatform());
                if (wxAdUnitMap != null && !wxAdUnitMap.isEmpty()) {
                    AdValueWxAdUnit wxAdUnit = wxAdUnitMap.get(wxData.getWxAppid() + "_" + dateFormat.format(wxData.getWxDate()));
                    if (wxAdUnit != null) {
                        productData.setScreenIncome(new BigDecimal(wxAdUnit.getScreenIncome()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                    } else {
                        productData.setScreenIncome(new BigDecimal(0));
                    }
                }
                //设置广告数据
                productData.setAdRevenue(wxData.getWxBannerIncome().add(wxData.getWxVideoIncome()).add(productData.getScreenIncome() != null ? productData.getScreenIncome() : new BigDecimal(0)));
                BigDecimal adRevenue = productData.getAdRevenue();
                Integer wxActive = wxData.getWxActive();
                if (!wxActive.equals(0)) {
                    BigDecimal divide = adRevenue.divide(new BigDecimal(wxActive), 4, 4);
                    //设置活跃up
                    productData.setActiveUp(divide);
                }
                String wxRegJson = wxData.getWxRegJson();
                //处理新增其他数据
                newSourceOther(productData, wxRegJson);

                String wxActiveSource = wxData.getWxActiveSource();
                if (StringUtils.isNotBlank(wxActiveSource)) {
                    //处理活跃用户来源数据
                    activeSourceDetail(productData, wxActiveSource);
                }
                if (buyPayMap != null && !buyPayMap.isEmpty()) {
                    BuyPay buyPay = buyPayMap.get(wxData.getWxAppid() + "_" + dateFormat.format(wxData.getWxDate()));
                    if (buyPay != null) {
                        productData.setBuyCost(buyPay.getBuyCost());
                        productData.setBuyClickPrice(buyPay.getBuyClickPrice());
                        productData.setWxAdNewPrice(wxData.getWxRegAd().equals(0) ? new BigDecimal(0) : buyPay.getBuyCost().divide(new BigDecimal(wxData.getWxRegAd()), 2, ROUND_HALF_UP));
                    }
                }
                //赋值相同属性
                BeanUtils.copyProperties(wxData, productData);
                productDatas.add(productData);
            }
        } catch (Exception e) {
            LOGGER.error("查询小游戏数据处理异常" + ", 详细信息:{}", e.getMessage());
        }
        return productDatas;
    }

    /**
     * 处理活跃用户来源信息
     *
     * @param productData    展示实体
     * @param wxActiveSource 用户来源信息
     */
    private void activeSourceDetail(ProductData productData, String wxActiveSource) {
        JSONObject jsonObject = JSONObject.parseObject(wxActiveSource);
        if (jsonObject != null) {
            Integer taskBarMySp = (Integer) jsonObject.get("任务栏-我的小程序");
            Integer findMySp = (Integer) jsonObject.get("发现-小程序");
            Integer taskBarRecent = (Integer) jsonObject.get("任务栏-最近使用");
            Integer otherReturn = (Integer) jsonObject.get("其他小程序返回");
            Integer otherSp = (Integer) jsonObject.get("其他小程序");
            Integer other = (Integer) jsonObject.get("其他");
            Integer search = (Integer) jsonObject.get("搜索");
            //广告
            Integer ad = (Integer) jsonObject.get("微信广告");
            if (taskBarMySp != null) {
                productData.setWxActiveTaskBarMySp(taskBarMySp);
            } else {
                productData.setWxActiveTaskBarMySp(0);
            }
            if (findMySp != null) {
                productData.setWxActiveFindMySp(findMySp);
            } else {
                productData.setWxActiveFindMySp(0);
            }
            if (taskBarRecent != null) {
                productData.setWxActiveTaskBarRecent(taskBarRecent);
            } else {
                productData.setWxActiveTaskBarRecent(0);
            }
            if (otherReturn != null) {
                productData.setWxActiveOtherReturn(otherReturn);
            } else {
                productData.setWxActiveOtherReturn(0);
            }
            if (otherSp != null) {
                productData.setWxActiveOtherSp(otherSp);
            } else {
                productData.setWxActiveOtherSp(0);
            }
            if (other != null) {
                productData.setWxActiveOther(other);
            } else {
                productData.setWxActiveOther(0);
            }
            if (search != null) {
                productData.setWxActiveSearch(search);
            } else {
                productData.setWxActiveSearch(0);
            }
            if (ad != null) {
                productData.setWxActiveAd(ad);
            } else {
                productData.setWxActiveAd(0);
            }
        }
    }

    /**
     * 获取买量数据
     *
     * @param parameter 参数
     * @return 买量数据Map
     */
    private Map<String, BuyPay> getBuyPayMap(GetParameter parameter) {
        String[] times = getSearchTime(parameter);
        String beginTime = times[0];
        String endTime = times[1];
        Map<String, BuyPay> buyPayMap = new HashMap<>(16);
        List<BuyPay> buyPays = buyPayMapper.selectBuyPayByDate(beginTime, endTime);
        buyPays.forEach(buyPay -> buyPayMap.put(buyPay.getBuyAppId() + "_" + buyPay.getBuyDate(), buyPay));
        return buyPayMap;
    }

    /**
     * 获取插屏收入
     *
     * @param parameter 参数
     * @return 3030046789020061插屏
     */
    private Map<String, AdValueWxAdUnit> getWxAdUnitMap(GetParameter parameter) {
        String[] times = getSearchTime(parameter);
        String beginTime = times[0];
        String endTime = times[1];
        Map<String, AdValueWxAdUnit> adValueWxAdUnitMap = new HashMap<>(16);
        List<AdValueWxAdUnit> adValueWxAdUnits = adValueWxAdUnitMapper.selectAllScreenIncome(beginTime, endTime);
        adValueWxAdUnits.forEach(adValueWxAdUnit -> adValueWxAdUnitMap.put(adValueWxAdUnit.getAppId() + "_" + adValueWxAdUnit.getDate(), adValueWxAdUnit));
        return adValueWxAdUnitMap;
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
    public Class<ProductData> getClassInfo() {
        return ProductData.class;
    }

    @Override
    public boolean removeIf(ProductData productData, JSONObject searchData) {
        return false;
    }

    /**
     * 页面搜索查询
     *
     * @param beginDate   开始日期
     * @param endDate     结束日期
     * @param productName 产品名称
     * @param parameter   参数
     * @return 返回结果
     */
    public GetResult searchProductData(String beginDate, String endDate, String productName, GetParameter parameter) {
        ArrayList<ProductData> searchDatas;
        if (StringUtils.isBlank(beginDate) && StringUtils.isBlank(endDate) && StringUtils.isBlank(productName)) {
            List<ProductData> productData = selectAll(parameter);
            filterData(productData, parameter);
            setDefaultSort(parameter);
            return template(productData, parameter);
        }
        //根据搜索条件查询
        searchDatas = searchQuery(productName, beginDate, endDate);
        filterData(searchDatas, parameter);
        setDefaultSort(parameter);
        return template(searchDatas, parameter);
    }

    /**
     * 根据搜索条件查询
     *
     * @param wxAppId appId
     * @return 查询结果
     */
    private ArrayList<ProductData> searchQuery(String wxAppId, String beginTime, String endTime) {
        ArrayList<ProductData> searchDatas = new ArrayList<>();
        if (StringUtils.isBlank(beginTime) && StringUtils.isBlank(endTime)) {
            String format = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
            beginTime = format;
            endTime = format;
        }
        List<MinitjWx> wxDatas = minitjWxMapper.searchDatas(wxAppId, beginTime, endTime);
        ConcurrentHashMap<String, WxConfig> wxConfigMap = wxConfigService.getAll(WxConfig.class);
        for (MinitjWx wxData : wxDatas) {
            ProductData productData = new ProductData();
            String appId = wxData.getWxAppid();
            WxConfig wxConfig = wxConfigMap.get(appId);
            BuyPay buyPay = buyPayMapper.selectByAppIdAndDate(wxData.getWxDate().toString(), appId);
            if (wxConfig != null) {
                String ddName = wxConfig.getProductName();
                productData.setProductName(ddName);
                productData.setDdAppPlatform(wxConfig.getDdAppPlatform());
                productData.setMinitjWx(wxData);
                productData.setAdRevenue(wxData.getWxBannerIncome().add(wxData.getWxVideoIncome()));
                BigDecimal adRevenue = productData.getAdRevenue();
                Integer wxActive = productData.getMinitjWx().getWxActive();
                try {
                    if (!wxActive.equals(0)) {
                        BigDecimal divide = adRevenue.divide(new BigDecimal(wxActive), 4, 4);
                        productData.setActiveUp(divide);
                    }
                    String wxRegJson = productData.getMinitjWx().getWxRegJson();
                    newSourceOther(productData, wxRegJson);
                    if (buyPay != null) {
                        productData.setBuyCost(buyPay.getBuyCost());
                        productData.setBuyClickPrice(buyPay.getBuyClickPrice());
                    }
                    BeanUtils.copyProperties(productData.getMinitjWx(), productData);
                } catch (Exception e) {
                    LOGGER.error("ProductDataService异常" + ", 详细信息:{}", e.getMessage());
                }
                String wxActiveSource = wxData.getWxActiveSource();
                if (StringUtils.isNotBlank(wxActiveSource)) {
                    activeSourceDetail(productData, wxActiveSource);
                }
                searchDatas.add(productData);
            }
        }
        return searchDatas;
    }

    /**
     * 用户来源明细-新增其他 数据处理
     *
     * @param productData productData
     * @param wxRegJson   wxRegJson属性
     */
    private void newSourceOther(ProductData productData, String wxRegJson) {
        JSONObject jsonObject = JSONObject.parseObject(wxRegJson);
        if (jsonObject != null) {
            Integer other = (Integer) jsonObject.get("其他");
            Integer wxRegTaskBarMySp = (Integer) jsonObject.get("任务栏-我的小程序");
            Integer wxRegFindMySp = (Integer) jsonObject.get("发现-我的小程序");
            Integer wxRegTaskBarRecent = (Integer) jsonObject.get("任务栏-最近使用");
            Integer wxRegOtherSp = (Integer) jsonObject.get("其他小程序");
            Integer wxRegOtherReturn = (Integer) jsonObject.get("其他小程序返回");
            if (other != null) {
                productData.setWxRegOther(other);
            } else {
                productData.setWxRegOther(0);
            }

            if (wxRegTaskBarMySp != null) {
                productData.setWxRegTaskBarMySp(wxRegTaskBarMySp);
            } else {
                productData.setWxRegTaskBarMySp(0);
            }
            if (wxRegFindMySp != null) {
                productData.setWxRegFindMySp(wxRegFindMySp);
            } else {
                productData.setWxRegFindMySp(0);
            }
            if (wxRegTaskBarRecent != null) {
                productData.setWxRegTaskBarRecent(wxRegTaskBarRecent);
            } else {
                productData.setWxRegTaskBarRecent(0);
            }
            if (wxRegOtherSp != null) {
                productData.setWxRegOtherSp(wxRegOtherSp);
            } else {
                productData.setWxRegOtherSp(0);
            }
            if (wxRegOtherReturn != null) {
                productData.setWxRegOtherReturn(wxRegOtherReturn);
            } else {
                productData.setWxRegOtherReturn(0);
            }
        }
    }

}
