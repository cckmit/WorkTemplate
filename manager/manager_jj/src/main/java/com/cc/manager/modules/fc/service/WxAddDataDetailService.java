package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.entity.MinitjWx;
import com.cc.manager.modules.jj.service.JjAndFcAppConfigService;
import com.cc.manager.modules.tt.entity.TtDailyAdValue;
import com.cc.manager.modules.tt.service.TtDailyAdValueService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author cf
 * @since 2020-05-13
 */
@Service
@DS("fc")
public class WxAddDataDetailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxAddDataDetailService.class);
    private JjAndFcAppConfigService jjAndFcAppConfigService;
    private MinitjWxService minitjWxService;
    private TtDailyAdValueService ttDailyAdValueService;

    /**
     * 查询产品数据，重写默认的分页查询方法
     * TODO 当前查询暂不分页
     *
     * @param statsListParam 查询参数
     * @return 查询结果
     */
    public StatsListResult getPage(StatsListParam statsListParam) {
        StatsListResult statsListResult = new StatsListResult();
        // 判断请求参数是否为空，并进行初始化
        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            statsListParam.setQueryObject(JSONObject.parseObject(statsListParam.getQueryData()));
        }
        if (Objects.isNull(statsListParam.getQueryObject())) {
            statsListParam.setQueryObject(new JSONObject());
        }
        // 初始化查询的起止日期
        this.updateBeginAndEndDate(statsListParam);
        String appId = statsListParam.getQueryObject().getString("appId");
        String beginDate = statsListParam.getQueryObject().getString("beginDate");
        String endDate = statsListParam.getQueryObject().getString("endDate");
        try {

            // 查询返回结果
            List<MinitjWx> minitjWxList = new ArrayList<>();

            // 根据查询类型判断，0表示小游戏，1表示小程序，为空表示全部
            String appType = statsListParam.getQueryObject().getString("appType");
            if (StringUtils.isNotBlank(appType)) {
                QueryWrapper<TtDailyAdValue> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq(StringUtils.isNotBlank(appId), "wx_appid", appId);
                queryWrapper.eq("wx_app_type", appType);
                queryWrapper.between("wx_date", beginDate, endDate);
                List<TtDailyAdValue> ttAdValueList = ttDailyAdValueService.list(queryWrapper);
                ArrayList<MinitjWx> list = new ArrayList<>();
                for (TtDailyAdValue ttDailyAdValue : ttAdValueList) {
                    MinitjWx minitjWx = new MinitjWx();
                    BeanUtils.copyProperties(ttDailyAdValue, minitjWx);
                    list.add(minitjWx);
                }
                minitjWxList.addAll(list);
            } else {
                List<MinitjWx> wxAdValueList = this.minitjWxService.list(appId, beginDate, endDate);
                if (Objects.nonNull(wxAdValueList)) {
                    this.rebuildStatsListResult(statsListParam, wxAdValueList, statsListResult);
                    minitjWxList.addAll(wxAdValueList);
                }
            }
            // TODO 先不进行分组
            statsListResult.setData(JSONArray.parseArray(JSON.toJSONString(minitjWxList)));
            statsListResult.setTotalRow(null);
            statsListResult.setCount(minitjWxList.size());
        } catch (Exception e) {
            statsListResult.setCode(2);
            statsListResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return statsListResult;
    }

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
            //beginDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(2));
            beginDate = endDate = "2020-06-29";
        }
        statsListParam.getQueryObject().put("beginDate", beginDate);
        statsListParam.getQueryObject().put("endDate", endDate);
    }

    @Autowired
    public void setJjAndFcAppConfigService(JjAndFcAppConfigService jjAndFcAppConfigService) {
        this.jjAndFcAppConfigService = jjAndFcAppConfigService;
    }

    @Autowired
    public void setMinitjWxService(MinitjWxService minitjWxService) {
        this.minitjWxService = minitjWxService;
    }

    @Autowired
    public void setTtDailyAdValueService(TtDailyAdValueService ttDailyAdValueService) {
        this.ttDailyAdValueService = ttDailyAdValueService;
    }

}
