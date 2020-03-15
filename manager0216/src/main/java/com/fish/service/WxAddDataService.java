package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ProductDataMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.WxConfig;
import com.fish.dao.third.mapper.MinitjWxMapper;
import com.fish.dao.third.model.MinitjWx;
import com.fish.dao.third.model.ProductData;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @description: 微信广告数据明细
 * @create:
 */
@Service
public class WxAddDataService implements BaseService<ProductData> {

    @Autowired
    WxConfigMapper wxConfigMapper;

    @Autowired
    MinitjWxMapper minitjWxMapper;

    @Autowired
    CacheService cacheService;
    @Autowired
    ProductDataMapper productDataMapper;

    /**
     * 查询微信广告数据
     *
     * @param parameter
     * @return
     */
    @Override
    public List<ProductData> selectAll(GetParameter parameter) {
        Map<String, WxConfig> wxConfigMap = new HashMap<>(16);
        // 查询配置
        List<WxConfig> wxConfigs = cacheService.getAllWxConfig();
        // 将配置信息存到map集合，key为appId,value 为整个配置信息
        wxConfigs.forEach(wxConfig -> {
            wxConfigMap.put(wxConfig.getDdappid(), wxConfig);
        });
        return selectMinitjWx(parameter, wxConfigMap);
    }

    /**
     * 微信广告数据
     *
     * @param parameter
     * @param wxConfigMap
     * @return
     */
    private List<ProductData> selectMinitjWx(GetParameter parameter, Map<String, WxConfig> wxConfigMap) {
        List<ProductData> productDatas = new ArrayList<>();
        JSONObject search = getSearchData(parameter.getSearchData());
        // 查询的开始时间和结束时间,默认查询前一天数据
        String ddAppId = "";
        String localDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        String beginTime = localDate;
        String endTime = localDate;
        // 如果没有搜索条件
        if (search != null) {
            //输入搜索条件
            ddAppId = search.getString("ddappid");
            String times = search.getString("times");
            if (StringUtils.isNotBlank(times)) {
                beginTime = times.split("-")[0];
                endTime = times.split("-")[1];
            }
        }
        // 获取小游戏数据
        List<MinitjWx> WxDatas = minitjWxMapper.searchDatas(ddAppId, beginTime, endTime);
        WxDatas.forEach(minitjWx -> {
            // 通过appID查找配置信息
            WxConfig wxConfig = wxConfigMap.get(minitjWx.getWxAppid());
            //过滤非wx_config配置里面的数据
            if (wxConfig != null) {
                //fc数据赋值展示数据
                ProductData productData = new ProductData();
                productData.setProgramType(wxConfig.getProgramType());
                productData.setProductName(wxConfig.getProductName());
                productData.setMinitjWx(minitjWx);
                productData.setAdRevenue(minitjWx.getWxBannerIncome().add(minitjWx.getWxVideoIncome()));
                BigDecimal adRevenue = productData.getAdRevenue();
                productData.setWxActive(productData.getMinitjWx().getWxActive());
                BeanUtils.copyProperties(productData.getMinitjWx(), productData);
                Integer wxVideoShow = minitjWx.getWxVideoShow();
                BigDecimal wxVideoIncome = minitjWx.getWxVideoIncome();
                productData.setWxVideoShow(wxVideoShow);
                productData.setWxVideoClickrate(minitjWx.getWxVideoClickrate());
                productData.setWxVideoIncome(wxVideoIncome);
                if (wxVideoShow != 0) {
                    productData.setVideoECPM((wxVideoIncome.divide(new BigDecimal(wxVideoShow), 5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
                } else {
                    productData.setVideoECPM(new BigDecimal(0));
                }
                Integer wxBannerShow = minitjWx.getWxBannerShow();
                BigDecimal wxBannerIncome = minitjWx.getWxBannerIncome();
                productData.setWxBannerShow(wxBannerShow);
                productData.setWxBannerClickrate(minitjWx.getWxBannerClickrate());
                productData.setWxBannerIncome(wxBannerIncome);
                if (wxBannerShow != 0) {
                    productData.setBannerECPM((wxBannerIncome.divide(new BigDecimal(wxBannerShow), 5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
                } else {
                    productData.setBannerECPM(new BigDecimal(0));
                }
                productData.setRevenueCount(adRevenue);
                BeanUtils.copyProperties(productData.getMinitjWx(), productData);
                try {
                    if (!checkObjFieldIsNull(productData.getMinitjWx())) {
                        productDatas.add(productData);
                    }
                } catch (IllegalAccessException e) {
                    LOGGER.error("查询微信广告数据失败" + ", 详细信息:{}", e.getMessage());
                }
            }
        });
        return productDatas;
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
                }
            }
            return flag;
        }
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

}
