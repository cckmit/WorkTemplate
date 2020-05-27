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
public class WxAddDataDetailService extends BaseStatsService<MinitjWx, MinitjWxMapper> {

    private WxConfigService wxConfigService;
    private MiniGameService miniGameService;

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<MinitjWx> queryWrapper, StatsListResult statsListResult) {
        String beginTime =null, endTime =null;
        JSONObject queryObject = statsListParam.getQueryObject();
        String appId = queryObject.getString("id");
        queryWrapper.eq(StringUtils.isNotBlank(appId), "wx_appid", appId);
        String times = queryObject.getString("times");
        if (StringUtils.isNotBlank(times)) {
            String[] timeRangeArray = StringUtils.split(times, "~");
            beginTime = timeRangeArray[0].trim();
            endTime = timeRangeArray[1].trim();
        }
        if(StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)){
            beginTime =DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(2));
            endTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        }
        queryWrapper.between("DATE(wx_date)", beginTime, endTime);
        queryWrapper.orderBy(true, false, "wx_date");
    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<MinitjWx> entityList, StatsListResult statsListResult) {
        entityList.forEach(minitjWx -> {
            // 通过appID查找配置信息
            WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, minitjWx.getWxAppid());
            //过滤非wx_config配置里面的数据
            if (wxConfig != null) {
                //fc数据赋值展示数据
                minitjWx.setProgramType(wxConfig.getProgramType());
                minitjWx.setProductName(wxConfig.getProductName());
            } else {
                MiniGame miniGame = this.miniGameService.getCacheEntity(MiniGame.class, minitjWx.getWxAppid());
                if (miniGame != null) {
                    minitjWx.setProgramType(0);
                    minitjWx.setProductName(miniGame.getGameName());
                }
            }
            minitjWx.setAdRevenue(minitjWx.getWxBannerIncome().add(minitjWx.getWxVideoIncome()));
            BigDecimal adRevenue = minitjWx.getAdRevenue();
            Integer wxVideoShow = minitjWx.getWxVideoShow();
            BigDecimal wxVideoIncome = minitjWx.getWxVideoIncome();
            minitjWx.setWxVideoShow(wxVideoShow);
            if (wxVideoShow != 0) {
                minitjWx.setVideoECPM((wxVideoIncome.divide(new BigDecimal(wxVideoShow), 5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
            } else {
                minitjWx.setVideoECPM(new BigDecimal(0));
            }
            Integer wxBannerShow = minitjWx.getWxBannerShow();
            BigDecimal wxBannerIncome = minitjWx.getWxBannerIncome();
            if (wxBannerShow != 0) {
                minitjWx.setBannerECPM((wxBannerIncome.divide(new BigDecimal(wxBannerShow), 5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
            } else {
                minitjWx.setBannerECPM(new BigDecimal(0));
            }
            minitjWx.setRevenueCount(adRevenue);

        });
        return null;
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
