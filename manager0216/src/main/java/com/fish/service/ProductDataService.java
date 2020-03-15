package com.fish.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.BuyPayMapper;
import com.fish.dao.primary.mapper.ProductDataMapper;
import com.fish.dao.primary.model.BuyPay;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.WxConfig;
import com.fish.dao.third.mapper.MinitjWxMapper;
import com.fish.dao.third.model.MinitjWx;
import com.fish.dao.third.model.ProductData;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.cache.CacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    CacheService cacheService;
    @Autowired
    ProductDataMapper productDataMapper;

    @Autowired
    BuyPayMapper buyPayMapper;

    /**
     * 获取微信配置表
     *
     * @return
     */
    private Map<String, WxConfig> getWxConfigMap() {
        Map<String, WxConfig> wxConfigMap = new HashMap<>();
        List<WxConfig> allWxConfig = cacheService.getAllWxConfig();
        allWxConfig.forEach(wxConfig -> {
            wxConfigMap.put(wxConfig.getDdappid(), wxConfig);
        });
        return wxConfigMap;
    }

    @Override
    public List<ProductData> selectAll(GetParameter parameter) {
        Map<String, WxConfig> wxConfigMap = getWxConfigMap();
        Map<String, BuyPay> buyPayMap = getBuyPayMap(parameter);
        List<ProductData> productDatas = new ArrayList<ProductData>();
        JSONObject search = getSearchData(parameter.getSearchData());
        if (search != null) {
            //搜索
            String ddAppId = search.getString("ddappid");
            String type = search.getString("type");
            // 先判断有没有根据appId查
            if (!StringUtils.isBlank(ddAppId)) {
                WxConfig wxConfig = wxConfigMap.get(ddAppId);
                if (wxConfig.getProgramType() == 0) {
                    productDatas = selectMinitjWx(parameter, ddAppId, wxConfigMap, buyPayMap);
                } else {
                    productDatas = selectPersieDeamon(parameter, ddAppId, buyPayMap);
                }
            } else {
                switch (type) {
                    case "0":
                        productDatas = selectMinitjWx(parameter, ddAppId, wxConfigMap, buyPayMap);
                        break;
                    case "1":
                        productDatas = selectPersieDeamon(parameter, ddAppId, buyPayMap);
                        break;
                    default:
                        // 如果只选了时间
                        productDatas = selectMinitjWx(parameter, ddAppId, wxConfigMap, buyPayMap);
                        List<ProductData> list = selectPersieDeamon(parameter, ddAppId, buyPayMap);
                        productDatas.addAll(list);
                        break;
                }
            }
        } else {
            productDatas = selectMinitjWx(parameter, null, wxConfigMap, buyPayMap);
            List<ProductData> list = selectPersieDeamon(parameter, null, buyPayMap);
            productDatas.addAll(list);
        }
        return productDatas;
    }

    /**
     * 查询小程序数据
     *
     * @param parameter
     * @param ddAppId
     * @return
     */
    private List<ProductData> selectPersieDeamon(GetParameter parameter, String ddAppId, Map<String, BuyPay> buyPayMap) {
        List<ProductData> productDatas = new ArrayList<ProductData>();
        JSONObject search = getSearchData(parameter.getSearchData());
        // 选小程序的时候查询
        StringBuilder sqlProgram = new StringBuilder("select * from program_entering where 1 = 1");
        String times = "";
        if (search != null) {
            times = search.getString("times");
        }
        if (!StringUtils.isBlank(times)) {
            String[] split = times.split("-");
            sqlProgram.append(" and wx_date >=" + "'").append(split[0].trim()).append("'");
            sqlProgram.append(" and wx_date <=" + "'").append(split[1].trim()).append("'");
        } else {
            sqlProgram.append(" and wx_date =" + "'").append(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1))).append("'");
        }
        if (!StringUtils.isBlank(ddAppId)) {
            sqlProgram.append(" and wx_appid =" + "'").append(ddAppId).append("'");
        }
        productDatas = productDataMapper.searchData(sqlProgram.toString());
        for (ProductData productDatum : productDatas) {
            String wxAppid = productDatum.getWxAppid();
            Date wxDate = productDatum.getWxDate();
            //查询小程序是否存在买量数据
            if (!buyPayMap.isEmpty()) {
                BuyPay buyPay = buyPayMap.get(wxAppid);
                if (buyPay != null) {
                    productDatum.setBuyCost(buyPay.getBuyCost());
                    productDatum.setBuyClickPrice(buyPay.getBuyClickPrice());
                }
            }
        }
        return productDatas;
    }

    /**
     * 查询小游戏数据
     *
     * @param parameter
     * @param ddAppId   前台输入框传的ddAppId
     * @return
     */
    private List<ProductData> selectMinitjWx(GetParameter parameter, String ddAppId, Map<String, WxConfig> wxConfigMap, Map<String, BuyPay> buyPayMap) {
        List<ProductData> productDatas = new ArrayList<>();
        JSONObject search = getSearchData(parameter.getSearchData());
        // 小游戏的时候查询
        StringBuilder sql = new StringBuilder("select * from minitj_wx where 1 = 1");
        String times = "";
        if (search != null) {
            times = search.getString("times");
        }
        if (!StringUtils.isBlank(times)) {
            String[] split = times.split("-");
            sql.append(" and wx_date >=" + "'").append(split[0].trim()).append("'");
            sql.append(" and wx_date <=" + "'").append(split[1].trim()).append("'");
        } else {
            String time = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
            sql.append(" and wx_date =" + "'").append(time).append("'");
        }
        if (!StringUtils.isBlank(ddAppId)) {
            sql.append(" and wx_appid =" + "'").append(ddAppId).append("'");
        }
        List<MinitjWx> WxDatas = minitjWxMapper.searchData(sql.toString());
        for (MinitjWx wxData : WxDatas) {
            WxConfig wxConfig = wxConfigMap.get(wxData.getWxAppid());
            if (wxConfig == null) {
                continue;
            }
            ProductData productData = new ProductData();
            productData.setMinitjWx(wxData);
            // 设置data信息
            productData.setProgramType(wxConfig.getProgramType());
            productData.setProductName(wxConfig.getProductName());
            productData.setProductName(wxConfig.getProductName());
            productData.setAdRevenue(wxData.getWxBannerIncome().add(wxData.getWxVideoIncome()));
            BigDecimal adRevenue = productData.getAdRevenue();
            Integer wxActive = wxData.getWxActive();
            if (!wxActive.equals(0)) {
                BigDecimal divide = adRevenue.divide(new BigDecimal(wxActive), 4, ROUND_HALF_UP);
                productData.setActiveUp(divide.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            String wxRegJson = wxData.getWxRegJson();
            JSONObject jsonObject = JSONObject.parseObject(wxRegJson);
            if (jsonObject != null) {
                Integer other = (Integer) jsonObject.get("其他");
                if (other != null) {
                    productData.setWxRegOther(other);
                } else {
                    productData.setWxRegOther(0);
                }
            } else {
                productData.setWxRegOther(0);
            }
            // 判断小游戏是否有买量数据
            if (!buyPayMap.isEmpty()) {
                BuyPay buyPay = buyPayMap.get(wxData.getWxAppid());
                if (buyPay != null) {
                    productData.setBuyCost(buyPay.getBuyCost());
                    productData.setBuyClickPrice(buyPay.getBuyClickPrice());
                }
            }
            BeanUtils.copyProperties(wxData, productData);
            productDatas.add(productData);
        }
        return productDatas;
    }

    /**
     * 获取买量数据
     *
     * @param parameter
     * @return
     */
    private Map<String, BuyPay> getBuyPayMap(GetParameter parameter) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String beginTime = format.format(new Date());
        String endTime = format.format(new Date());
        JSONObject search = getSearchData(parameter.getSearchData());
        if (search != null) {
            String times = search.getString("times");
            if (!StringUtils.isBlank(times)) {
                beginTime = times.split("-")[0];
                endTime = times.split("-")[1];
            }
        }
        Map<String, BuyPay> buyPayMap = new HashMap<String, BuyPay>(16);
        List<BuyPay> buyPays = buyPayMapper.selectBuyPayByDate(beginTime, endTime);
        buyPays.forEach(buyPay -> {
            buyPayMap.put(buyPay.getBuyAppId(), buyPay);
        });
        return buyPayMap;
    }

    /**
     * 新增小程序产品信息
     *
     * @param productData
     * @return
     */
    public int insert(ProductData productData) {
        int insertProgramData;
        productData.setInsertTime(new Timestamp(System.currentTimeMillis()));
        insertProgramData = productDataMapper.insert(productData);
        return insertProgramData;
    }

    /**
     * 更新小程序产品信息
     *
     * @param productData
     * @return
     */
    public int updateByPrimaryKeySelective(ProductData productData) {
        int update;
        productData.setInsertTime(new Timestamp(System.currentTimeMillis()));
        update = productDataMapper.updateByPrimaryKey(productData);
        return update;
    }

    /**
     * 上传小程序产品信息
     *
     * @param record
     * @return
     */
    public int insertExcel(JSONObject record) {
        String context = record.getString("context");
        System.out.println("context :" + context);
        context = context.substring(1, context.length() - 1);
        try {
            JSONArray param = new JSONArray(Collections.singletonList(context));
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            List<ProductData> lists = new ArrayList<>();
            for (int i = 0; i < param.size(); i++) {
                String singleData = param.get(i).toString();

                String singleString = singleData.substring(1, singleData.length() - 1);
                System.out.println("我是singleString :" + singleString);
                String[] split = singleString.split("], ");
                for (int j = 0; j < split.length; j++) {
                    if (j != 0 && j < split.length) {
                        String single = split[j].substring(1);
                        String[] singleSplit = single.split(",");
                        Map<String, String> mapSingle = new HashMap<>();
                        ProductData productData = new ProductData();
                        for (int x = 0; x < singleSplit.length; x++) {
                            switch (x) {
                                case 0:
                                    mapSingle.put("wx_date", singleSplit[x].trim());
                                    break;
                                case 1:
                                    mapSingle.put("wx_appid", singleSplit[x].trim());
                                    break;
                                case 2:
                                    mapSingle.put("product_name", singleSplit[x].trim());
                                    break;
                                case 3:
                                    mapSingle.put("product_type", singleSplit[x].trim());
                                    break;
                                case 4:
                                    mapSingle.put("wx_new", singleSplit[x].trim());
                                    break;
                                case 5:
                                    mapSingle.put("wx_active", singleSplit[x].trim());
                                    break;
                                case 6:
                                    mapSingle.put("wx_visit", singleSplit[x].trim());
                                    break;
                                case 7:
                                    mapSingle.put("recharge", singleSplit[x].trim());
                                    break;
                                case 8:
                                    mapSingle.put("ad_revenue", singleSplit[x].trim());
                                    break;
                                case 9:
                                    mapSingle.put("wx_video_income", singleSplit[x].trim());
                                    break;

                                case 10:
                                    mapSingle.put("wx_banner_income", singleSplit[x].trim());
                                    break;
                                case 11:
                                    mapSingle.put("active_up", singleSplit[x].trim());
                                    break;
                                case 12:
                                    mapSingle.put("wx_share_user", singleSplit[x].trim());
                                    break;
                                case 13:
                                    mapSingle.put("wx_share_count", singleSplit[x].trim());
                                    break;
                                case 14:
                                    mapSingle.put("wx_share_rate", singleSplit[x].trim());
                                    break;
                                case 15:
                                    mapSingle.put("wx_remain2", singleSplit[x].trim());
                                    break;
                                case 16:
                                    mapSingle.put("wx_avg_login", singleSplit[x].trim());
                                    break;
                                case 17:
                                    mapSingle.put("wx_avg_online", singleSplit[x].trim());
                                    break;
                                default:
                                    break;
                            }
                        }
                        String wxDate = mapSingle.get("wx_date");
                        String wxAppid = mapSingle.get("wx_appid");
                        String productName = mapSingle.get("product_name");
                        String productType = mapSingle.get("product_type");
                        String wxNew = mapSingle.get("wx_new");
                        String wxActive = mapSingle.get("wx_active");
                        String wxVisit = mapSingle.get("wx_visit");
                        String recharge = mapSingle.get("recharge");
                        String adRevenue = mapSingle.get("ad_revenue");
                        String wxVideoIncome = mapSingle.get("wx_video_income");
                        String wxBannerIncome = mapSingle.get("wx_banner_income");
                        String activeUp = mapSingle.get("active_up");
                        String wxShareUser = mapSingle.get("wx_share_user");
                        String wxShareCount = mapSingle.get("wx_share_count");
                        String wxShareRate = mapSingle.get("wx_share_rate");
                        String wxRemain2 = mapSingle.get("wx_remain2");
                        String wxAvgLogin = mapSingle.get("wx_avg_login");
                        String wxAvgOnline = mapSingle.get("wx_avg_online");
                        if (StringUtils.isNotBlank(wxDate)) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = null;
                            try {
                                date = simpleDateFormat.parse(wxDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            productData.setWxDate(date);
                            productData.setWxAppid(wxAppid);
                            productData.setProductName(productName);
                            productData.setProgramType(Integer.valueOf(productType));
                            productData.setWxNew(Integer.valueOf(wxNew));
                            productData.setWxActive(Integer.valueOf(wxActive));
                            productData.setWxVisit(Integer.valueOf(wxVisit));
                            if (recharge != null) {
                                productData.setRecharge(new BigDecimal(recharge));
                            } else {
                                productData.setRecharge(new BigDecimal(0));
                            }
                            productData.setAdRevenue(new BigDecimal(adRevenue));
                            productData.setWxVideoIncome(new BigDecimal(wxVideoIncome));
                            productData.setWxBannerIncome(new BigDecimal(wxBannerIncome));
                            productData.setActiveUp(new BigDecimal(activeUp));
                            productData.setWxShareUser(Integer.valueOf(wxShareUser));
                            productData.setWxShareCount(Integer.valueOf(wxShareCount));
                            productData.setWxShareRate(new BigDecimal(wxShareRate));
                            productData.setWxRemain2(new BigDecimal(wxRemain2));
                            productData.setWxAvgLogin(new BigDecimal(wxAvgLogin));
                            productData.setWxAvgOnline(new BigDecimal(wxAvgOnline));
                            productData.setInsertTime(new Timestamp(System.currentTimeMillis()));
                            lists.add(productData);
                        }
                    }
                }
                productDataMapper.insertBatch(lists);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * java反射机制判断对象所有属性是否全部为空
     *
     * @param obj 对象参数
     * @return 返回属性名称
     */
    private boolean checkObjFieldIsNull(Object obj) throws IllegalAccessException {

        boolean flag = false;
        if (obj == null) {
            return true;
        } else {
            for (Field f : obj.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.get(obj) == null) {
                    flag = true;
                    return flag;
                }
            }
            return flag;
        }
    }

    public int insert(MinitjWx record) {

        return minitjWxMapper.insert(record);
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
        ArrayList<ProductData> searchDatas = new ArrayList<>();
        String sql = "select * from minitj_wx where 1 = 1";
        StringBuilder SQL = new StringBuilder(sql);
        if (StringUtils.isBlank(beginDate) && StringUtils.isBlank(endDate) && StringUtils.isBlank(productName)) {
            List<ProductData> productData = selectAll(parameter);
            filterData(productData, parameter);
            setDefaultSort(parameter);
            return template(productData, parameter);
        }
        if (StringUtils.isNotBlank(beginDate)) {
            SQL.append(" and wx_date >=" + "'").append(beginDate).append("'");
        }
        if (StringUtils.isNotBlank(endDate)) {
            SQL.append(" and wx_date <=" + "'").append(endDate).append("'");
        }
        if (StringUtils.isNotBlank(productName)) {
            SQL.append(" and wx_appid =" + "'").append(productName).append("'");

        }
        //根据搜索条件查询
        searchDatas = searchQuery(SQL.toString());

        filterData(searchDatas, parameter);
        setDefaultSort(parameter);
        return template(searchDatas, parameter);
    }

    /**
     * 根据搜索条件查询
     *
     * @param sql
     * @return
     */
    private ArrayList<ProductData> searchQuery(String sql) {
        ArrayList<ProductData> searchDatas = new ArrayList<>();
        List<MinitjWx> WxDatas = minitjWxMapper.searchData(sql);
        for (MinitjWx wxData : WxDatas) {
            ProductData productData = new ProductData();
            String appId = wxData.getWxAppid();
            WxConfig wxConfig = cacheService.getWxConfig(appId);
            BuyPay buyPay = buyPayMapper.selectByAppIdAndDate(wxData.getWxDate().toString(), appId);
            if (wxConfig != null) {
                String ddName = wxConfig.getProductName();
                productData.setProductName(ddName);
                productData.setMinitjWx(wxData);
                productData.setAdRevenue(wxData.getWxBannerIncome().add(wxData.getWxVideoIncome()));
                BigDecimal adRevenue = productData.getAdRevenue();
                Integer wxActive = productData.getMinitjWx().getWxActive();
                try {
                    if (!wxActive.equals(0)) {
                        BigDecimal divide = adRevenue.divide(new BigDecimal(wxActive), 4, ROUND_HALF_UP);
                        productData.setActiveUp(divide.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    String wxRegJson = productData.getMinitjWx().getWxRegJson();
                    JSONObject jsonObject = null;
                    jsonObject = JSONObject.parseObject(wxRegJson);
                    if (jsonObject != null) {
                        Integer other = (Integer) jsonObject.get("其他");
                        if (other != null) {
                            productData.setWxRegOther(other);
                        } else {
                            productData.setWxRegOther(0);
                        }
                    } else {
                        productData.setWxRegOther(0);
                    }
                    if (buyPay != null) {
                        productData.setBuyCost(buyPay.getBuyCost());
                        productData.setBuyClickPrice(buyPay.getBuyClickPrice());
                    }
                    BeanUtils.copyProperties(productData.getMinitjWx(), productData);
                } catch (Exception e) {
                    LOGGER.error("ProductDataService异常" + ", 详细信息:{}", e.getMessage());
                }
                searchDatas.add(productData);
            }
        }
        return searchDatas;
    }

    /**
     * 删除产品数据小程序部分
     *
     * @param jsonObject
     * @return
     */
    public PostResult delete(JSONObject jsonObject) {
        PostResult result = new PostResult();
        String deleteIds = jsonObject.getString("deleteIds");
        String wxDate = jsonObject.getString("wxDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(wxDate);
            int delete = this.productDataMapper.deleteByPrimaryKey(deleteIds, date);
            if (delete <= 0) {
                result.setSuccessed(false);
                result.setMsg("操作失败，修改广告内容失败！");
            }
        } catch (ParseException e) {
            LOGGER.error("删除小程序产品数据异常" + ", 详细信息:{}", e.getMessage());
        }
        return result;
    }
}
