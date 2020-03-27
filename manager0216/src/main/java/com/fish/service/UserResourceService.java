package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.WxConfig;
import com.fish.dao.third.mapper.MinitjWxMapper;
import com.fish.dao.third.model.MinitjWx;
import com.fish.dao.third.model.ProductData;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ROUND_HALF_UP;

@Service
public class UserResourceService implements BaseService<ProductData> {

    @Autowired
    WxConfigMapper wxConfigMapper;

    @Autowired
    MinitjWxMapper minitjWxMapper;

    @Override
    public List<ProductData> selectAll(GetParameter parameter) {
        ArrayList<ProductData> productDatas = new ArrayList<>();
        List<WxConfig> wxConfigs = wxConfigMapper.selectAll();
        for (WxConfig wxConfig : wxConfigs) {
            if (wxConfig.getProgramType() == 0) {
                ProductData productData = new ProductData();
                productData.setProductName(wxConfig.getProductName());
                String ddAppId = wxConfig.getDdappid();
                LocalDate beforeDate = LocalDate.now().minusDays(2);
                String localDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(beforeDate);
                try {
                    MinitjWx minitjWx = minitjWxMapper.selectByPrimaryKey(ddAppId, localDate);
                    if (minitjWx != null) {
                        productData.setMinitjWx(minitjWx);
                        productData.setAdRevenue(minitjWx.getWxBannerIncome().add(minitjWx.getWxVideoIncome()));
                        BigDecimal adRevenue = productData.getAdRevenue();
                        Integer wxActive = productData.getMinitjWx().getWxActive();
                        BigDecimal divide = adRevenue.divide(new BigDecimal(wxActive), 4, ROUND_HALF_UP);
                        productData.setActiveUp(divide.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                } catch (Exception e) {
                    LOGGER.error("查询UserResourceService失败" + ", 详细信息:{}", e.getMessage());
                }
                productDatas.add(productData);
            }
        }
        return productDatas;
    }

    public int insert(MinitjWx record) {

        return minitjWxMapper.insert(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {
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
