package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.entity.MinitjWx;
import com.cc.manager.modules.fc.mapper.MinitjWxMapper;
import com.cc.manager.modules.jj.service.JjAndFcAppConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author cf
 * @since 2020-05-13
 */
@Service
@DS("fc")
public class CopyWxAddDataDetailService extends BaseStatsService<MinitjWx, MinitjWxMapper> {

    private JjAndFcAppConfigService jjAndFcAppConfigService;

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<MinitjWx> queryWrapper, StatsListResult statsListResult) {

        // 初始化查询的起止日期
        this.updateBeginAndEndDate(statsListParam);
        String beginDate = statsListParam.getQueryObject().getString("beginDate");
        String endDate = statsListParam.getQueryObject().getString("endDate");
        String appId = statsListParam.getQueryObject().getString("appId");
        queryWrapper.between("DATE(wx_date)", beginDate, endDate);
        queryWrapper.eq(StringUtils.isNotBlank(appId), "wx_appid", appId);
        queryWrapper.orderBy(true, false, "wx_date");
    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<MinitjWx> entityList, StatsListResult statsListResult) {
        List<MinitjWx> newEntityList = new ArrayList<>();
        for (MinitjWx minitjWx : entityList) {
            // 获取街机和FC的全部app信息
            LinkedHashMap<String, JSONObject> getAllAppMap = this.jjAndFcAppConfigService.getAllAppMap();
            JSONObject appObject = getAllAppMap.get(minitjWx.getWxAppId());
            if (appObject == null) {
                continue;
            } else {
                // 设置data产品信息
                minitjWx.setProgramType(Integer.parseInt(appObject.getString("programType")));
                minitjWx.setProductName(appObject.getString("name"));
                minitjWx.setDdAppPlatform(appObject.getString("platform"));
            }
            //设置广告收益
            minitjWx.setAdRevenue(minitjWx.getWxBannerIncome().add(minitjWx.getWxVideoIncome()));
            //设置VideoECPM
            if (minitjWx.getWxVideoShow() != 0) {
                minitjWx.setVideoECPM((minitjWx.getWxVideoIncome().divide(new BigDecimal(minitjWx.getWxVideoShow()),
                        5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
            } else {
                minitjWx.setVideoECPM(new BigDecimal(0));
            }
            //设置BannerECPM
            if (minitjWx.getWxBannerShow() != 0) {
                minitjWx.setBannerECPM((minitjWx.getWxBannerIncome().divide(new BigDecimal(minitjWx.getWxBannerShow()),
                        5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
            } else {
                minitjWx.setBannerECPM(new BigDecimal(0));
            }
            //设置总收入
            minitjWx.setRevenueCount(minitjWx.getAdRevenue());
            newEntityList.add(minitjWx);
        }
        entityList.clear();
        entityList.addAll(newEntityList);
        return null;
    }

    /**
     * 初始化查询起止时间
     *
     * @param statsListParam 请求参数
     */
    private void updateBeginAndEndDate(StatsListParam statsListParam) {
        String beginDate = null, endDate = null;
        String times = statsListParam.getQueryObject().getString("times");
        if (StringUtils.isNotBlank(times)) {
            String[] timeRangeArray = StringUtils.split(times, "~");
            beginDate = timeRangeArray[0].trim();
            endDate = timeRangeArray[1].trim();
        }
        if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            beginDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(2));
            endDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        }
        statsListParam.getQueryObject().put("beginDate", beginDate);
        statsListParam.getQueryObject().put("endDate", endDate);
    }

    @Autowired
    public void setJjAndFcAppConfigService(JjAndFcAppConfigService jjAndFcAppConfigService) {
        this.jjAndFcAppConfigService = jjAndFcAppConfigService;
    }

}
