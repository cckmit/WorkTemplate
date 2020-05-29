package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.entity.MiniGame;
import com.cc.manager.modules.fc.entity.MinitjWx;
import com.cc.manager.modules.fc.mapper.MinitjWxMapper;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.service.WxConfigService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * @author cf
 * @since 2020-05-13
 */
@Service
public class DataCollectService extends BaseStatsService<MinitjWx, MinitjWxMapper> {

    private WxConfigService wxConfigService;
    private MiniGameService miniGameService;

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<MinitjWx> queryWrapper, StatsListResult statsListResult) {
        String beginTime = null;
        String endTime = null;
        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(statsListParam.getQueryData());
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                beginTime = timeRangeArray[0].trim();
                endTime = timeRangeArray[1].trim();
            }
        } else {
            beginTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
            endTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        }
        queryWrapper.between("DATE(wx_date)", beginTime, endTime);
    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<MinitjWx> entityList, StatsListResult statsListResult) {
        for (MinitjWx productData : entityList) {
            // 通过appID查找配置信息
            WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, productData.getWxAppId());
            //过滤非wx_config配置里面的数据
            if (wxConfig != null) {
                //fc数据赋值展示数据
                productData.setProgramType(wxConfig.getProgramType());
                productData.setProductName(wxConfig.getProductName());
            } else {
                MiniGame cacheEntity = this.miniGameService.getCacheEntity(MiniGame.class, productData.getWxAppId());
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
        return null;
    }

    @Override
    public StatsListResult getPage(StatsListParam statsListParam) {
        StatsListResult statsListResult = new StatsListResult();
        // 判断请求参数是否为空
        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            statsListParam.setQueryObject(JSONObject.parseObject(statsListParam.getQueryData()));
        }
        if (Objects.isNull(statsListParam.getQueryObject())) {
            statsListParam.setQueryObject(new JSONObject());
        }
        try {

//            if (Objects.nonNull()) {
//                statsListResult.setData(JSONArray.parseArray(JSON.toJSONString()));
//                statsListResult.setCount(.size());
//            }
        } catch (Exception e) {
            statsListResult.setCode(1);
            statsListResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return statsListResult;
    }

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


    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

    @Autowired
    public void setMiniGameService(MiniGameService miniGameService) {
        this.miniGameService = miniGameService;
    }


}
