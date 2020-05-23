package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.fc.entity.MiniGame;
import com.cc.manager.modules.fc.entity.MinitjWx;
import com.cc.manager.modules.fc.mapper.MinitjWxMapper;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.service.WxConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author cf
 * @since 2020-05-13
 */
@Service
public class MinitjWxService extends BaseCrudService<MinitjWx, MinitjWxMapper> {

    private WxConfigService wxConfigService;
    private MiniGameService miniGameService;

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

}
