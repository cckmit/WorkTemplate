package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * @author cf
 * @since 2020-05-22
 */
@Service
public class MinitjWxActiveResourceService extends BaseCrudService<MinitjWx, MinitjWxMapper> {

    private WxConfigService wxConfigService;
    private MiniGameService miniGameService;

    private MinitjWxService minitjWxService;
    private WxAddDataDetailService wxAddDataDetailService;
    private WxDailyVisitTrendService wxDailyVisitTrendService;

    private WxDailyVisitTrendMapper wxDailyVisitTrendMapper;
    private WxDailySummaryService wxDailySummaryService;

    private BuyPayService buyPayService;
    private AdValueWxAdUnitService adValueWxAdUnitService;

    private OrdersService ordersService;


    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<MinitjWx> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(wx_date)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
        } else {
            String beginTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
            String endTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
            queryWrapper.between("DATE(wx_date)", beginTime, endTime);
        }
    }

    /**
     * 分页查询
     *
     * @param crudPageParam 分页请求参数
     */
    @Override
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        CrudPageResult pageResult = new CrudPageResult();
        try {
            Page<MinitjWx> page = new Page<>(crudPageParam.getPage(), crudPageParam.getLimit());
            QueryWrapper<MinitjWx> queryWrapper = new QueryWrapper<>();
            // 更新查询排序条件
            if (StringUtils.isNotBlank(crudPageParam.getOrderBy())) {
                JSONObject orderByObject = JSONObject.parseObject(crudPageParam.getOrderBy());
                orderByObject.forEach((orderByColumn, orderByType) -> {
                    String typeStr = orderByType.toString();
                    if ("ASC".equalsIgnoreCase(typeStr)) {
                        queryWrapper.orderBy(true, true, orderByColumn);
                    } else if ("DESC".equalsIgnoreCase(typeStr)) {
                        queryWrapper.orderBy(true, false, orderByColumn);
                    }
                });
            }
            String[] times = getTimes(crudPageParam);
            ConcurrentHashMap<String, WxConfig> wxConfigMap = getProductMap();
            Map<String, BuyPay> buyPayMap = getBuyPayMap(times);
            Map<String, AdValueWxAdUnit> wxAdUnitMap = getWxAdUnitMap(times);
            String ddAppId = "";
            List<MinitjWx> productDatas;
            if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
                JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
                ddAppId = queryObject.getString("appId");

                // 如果只选了时间
                productDatas = selectMinitjWx(times, ddAppId, wxConfigMap, buyPayMap, wxAdUnitMap);
                List<MinitjWx> list = selectPersieDeamon(times, ddAppId, wxConfigMap, buyPayMap);
                Objects.requireNonNull(productDatas).addAll(list);

            } else {
                productDatas = selectMinitjWx(times, null, wxConfigMap, buyPayMap, wxAdUnitMap);
                List<MinitjWx> list = selectPersieDeamon(times, null, wxConfigMap, buyPayMap);
                productDatas.addAll(list);
            }
            this.updateGetPageWrapper(crudPageParam, queryWrapper);

            if (Objects.nonNull(productDatas)) {
                pageResult.setCount(productDatas.size());
                pageResult.setData(JSONArray.parseArray(JSON.toJSONString(productDatas)));
            }
        } catch (Exception e) {
            pageResult.setCode(1);
            pageResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return pageResult;
    }


    private List<MinitjWx> selectPersieDeamon(String[] times, String appId, ConcurrentHashMap<String, WxConfig> wxConfigMap, Map<String, BuyPay> buyPayMap) {
        List<MinitjWx> productDatas;

        //小程序充值收入map
        Map<String, Orders> programReChargeMap = getProgramReChargeMap();
        // 解析时间
        String start = times[0].trim().replace("-", "").trim();
        String end = times[1].trim().replace("-", "").trim();
        //查询小程序数据
        if (StringUtils.isNotBlank(appId)) {
            productDatas = this.wxDailyVisitTrendMapper.selectVisitTrend(start, end, appId);
        } else {
            productDatas = this.wxDailyVisitTrendMapper.selectVisitTrend(start, end);
        }

        // productDatas = wxDailyVisitTrendMapper.selectVisitTrend(start.replace("/", ""), end.replace("/", ""), ddAppId);
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
        } catch (Exception e) {
            LOGGER.error("查询小游戏数据处理异常" + ", 详细信息:{}", e.getMessage());
            // System.out.println(e.getMessage());
        }
        return productDatas;
    }

    private Map<String, Orders> getProgramReChargeMap() {
        // List<Orders> orders = ordersMapper.queryProgramReCharge();
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        queryWrapper.between("DATE(wx_date)", times[0].trim(), times[1].trim());
        queryWrapper.eq(StringUtils.isNotBlank(ddAppId), "wx_appid", ddAppId);
        // 小游戏的时候查询
        List<MinitjWx> wxDatas = this.minitjWxService.getBaseMapper().selectList(queryWrapper);
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

                String wxRegJson = wxData.getWxRegJson();
                //处理新增其他数据
                newSourceOther(wxData, wxRegJson);
                String wxActiveSource = wxData.getWxActiveSource();
                if (StringUtils.isNotBlank(wxActiveSource)) {
                    //处理活跃用户来源数据
                    activeSourceDetail(wxData, wxActiveSource);
                }

                if (buyPayMap != null && !buyPayMap.isEmpty()) {
                    BuyPay buyPay = buyPayMap.get(wxData.getWxAppid() + "_" + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(wxData.getWxDate()));
                    if (buyPay != null) {
                        wxData.setBuyCost(buyPay.getBuyCost());
                        wxData.setBuyClickPrice(buyPay.getBuyClickPrice());
                        wxData.setWxAdNewPrice(wxData.getWxRegAd().equals(0) ? new BigDecimal(0) : buyPay.getBuyCost().divide(new BigDecimal(wxData.getWxRegAd()), 2, ROUND_HALF_UP));
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return wxDatas;
    }

    /**
     * 处理活跃用户来源信息
     *
     * @param productData    展示实体
     * @param wxActiveSource 用户来源信息
     */
    private void activeSourceDetail(MinitjWx productData, String wxActiveSource) {
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
     * 用户来源明细-新增其他 数据处理
     *
     * @param productData productData
     * @param wxRegJson   wxRegJson属性
     */
    private void newSourceOther(MinitjWx productData, String wxRegJson) {
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

    private Map<String, BuyPay> getBuyPayMap(String[] times) {
        Map<String, BuyPay> buyPayMap = new HashMap<>(16);
        QueryWrapper<BuyPay> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("DATE(buy_date)", times[0].trim(), times[1].trim());
        // 小游戏的时候查询
        List<BuyPay> buyPays = this.buyPayService.getBaseMapper().selectList(queryWrapper);
        //  List<BuyPay> buyPays = buyPayMapper.selectBuyPayByDate(beginTime, endTime);
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
                newWxConfig.setAddId(entity.getGameAppid());
                newWxConfig.setProductName(entity.getGameName());
                newWxConfig.setDdAppPlatform(entity.getGameAppplatform());
                newWxConfig.setProgramType(0);
                wxConfigMap.put(entity.getGameAppid(), newWxConfig);
            }
        });
        return wxConfigMap;
    }

    private String[] getTimes(CrudPageParam crudPageParam) {
        String[] times = new String[2];
        String beginTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        String endTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
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
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<MinitjWx> entityList) {
        for (MinitjWx productData : entityList) {
            // 通过appID查找配置信息
            WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, productData.getWxAppid());
            //过滤非wx_config配置里面的数据
            if (wxConfig != null) {
                //fc数据赋值展示数据
                productData.setProgramType(wxConfig.getProgramType());
                productData.setProductName(wxConfig.getProductName());
            } else {
                MiniGame cacheEntity = this.miniGameService.getCacheEntity(MiniGame.class, productData.getWxAppid());
                if (cacheEntity != null) {
                    productData.setProgramType(0);
                    productData.setProductName(cacheEntity.getGameName());
                } else {
                    productData.setProgramType(0);
                    productData.setProductName("暂未录入的产品");
                }
            }
            productData.setAdRevenue(productData.getWxBannerIncome().add(productData.getWxVideoIncome()));
            BigDecimal adRevenue = productData.getAdRevenue();
            productData.setWxActive(productData.getWxActive());

            Integer wxVideoShow = productData.getWxVideoShow();
            BigDecimal wxVideoIncome = productData.getWxVideoIncome();

            if (wxVideoShow != 0) {
                productData.setVideoECPM((wxVideoIncome.divide(new BigDecimal(wxVideoShow), 5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
            } else {
                productData.setVideoECPM(new BigDecimal(0));
            }
            Integer wxBannerShow = productData.getWxBannerShow();
            BigDecimal wxBannerIncome = productData.getWxBannerIncome();

            if (wxBannerShow != 0) {
                productData.setBannerECPM((wxBannerIncome.divide(new BigDecimal(wxBannerShow), 5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
            } else {
                productData.setBannerECPM(new BigDecimal(0));
            }
            productData.setRevenueCount(adRevenue);
        }

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<MinitjWx> deleteWrapper) {
        return false;
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
    public void setWxDailySummaryService(WxDailySummaryService wxDailySummaryService) {
        this.wxDailySummaryService = wxDailySummaryService;
    }

    @Autowired
    public void setWxAddDataDetailService(WxAddDataDetailService wxAddDataDetailService) {
        this.wxAddDataDetailService = wxAddDataDetailService;
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
