package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.entity.MinitjWx;
import com.cc.manager.modules.jj.service.JjAndFcAppConfigService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author cf
 * @since 2020-05-22
 */
@Service
public class MinitjWxActiveResourceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseStatsService.class);

    private MinitjWxService minitjWxService;
    private JjAndFcAppConfigService jjAndFcAppConfigService;

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
        String beginDate = statsListParam.getQueryObject().getString("beginDate");
        String endDate = statsListParam.getQueryObject().getString("endDate");
        try {

            // 获取街机和FC的全部app信息
            LinkedHashMap<String, JSONObject> getAllAppMap = this.jjAndFcAppConfigService.getAllAppMap();
            List<MinitjWx> userResources;
            String ddAppId = statsListParam.getQueryObject().getString("appId");
            //查询数据
            userResources = selectUserResources(beginDate, endDate, ddAppId, getAllAppMap);

            statsListResult.setData(JSONArray.parseArray(JSON.toJSONString(userResources)));
            statsListResult.setTotalRow(null);
            statsListResult.setCount(userResources.size());
        } catch (Exception e) {
            statsListResult.setCode(1);
            statsListResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return statsListResult;
    }

    private List<MinitjWx> selectUserResources(String beginDate, String endDate, String ddAppId, LinkedHashMap<String, JSONObject> getAllAppMap) {

        List<MinitjWx> resourceDataList = this.minitjWxService.list(ddAppId, beginDate, endDate);
        List<MinitjWx> newDataList = new ArrayList<>();
        for (MinitjWx resourceData : resourceDataList) {
            JSONObject appObject = getAllAppMap.get(resourceData.getWxAppId());
            if (appObject == null) {
                continue;
            }
            // 设置data产品信息
            resourceData.setProgramType(Integer.parseInt(appObject.getString("programType")));
            resourceData.setProductName(appObject.getString("name"));
            resourceData.setDdAppPlatform(appObject.getString("platform"));
            //处理用户新增数据
            String wxRegJson = resourceData.getWxRegJson();
            newSourceOther(resourceData, wxRegJson);
            //处理活跃用户来源数据
            String wxActiveSource = resourceData.getWxActiveSource();
            activeSourceDetail(resourceData, wxActiveSource);
            newDataList.add(resourceData);
        }
        return newDataList;
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
            beginDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
            endDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        }
        statsListParam.getQueryObject().put("beginDate", beginDate);
        statsListParam.getQueryObject().put("endDate", endDate);
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

            productData.setWxActiveTaskBarMySp(taskBarMySp == null ? 0 : taskBarMySp);
            productData.setWxActiveFindMySp(findMySp == null ? 0 : findMySp);
            productData.setWxActiveTaskBarRecent(taskBarRecent == null ? 0 : taskBarRecent);
            productData.setWxActiveOtherReturn(otherReturn == null ? 0 : otherReturn);
            productData.setWxActiveOtherSp(otherSp == null ? 0 : otherSp);
            productData.setWxActiveOther(other == null ? 0 : other);
            productData.setWxActiveSearch(search == null ? 0 : search);
            productData.setWxActiveAd(ad == null ? 0 : ad);

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

            productData.setWxRegOther(other == null ? 0 : other);
            productData.setWxRegTaskBarMySp(wxRegTaskBarMySp == null ? 0 : wxRegTaskBarMySp);
            productData.setWxRegFindMySp(wxRegFindMySp == null ? 0 : wxRegFindMySp);
            productData.setWxRegTaskBarRecent(wxRegTaskBarRecent == null ? 0 : wxRegTaskBarRecent);
            productData.setWxRegOtherSp(wxRegOtherSp == null ? 0 : wxRegOtherSp);
            productData.setWxRegOtherReturn(wxRegOtherReturn == null ? 0 : wxRegOtherReturn);
        }
    }

    @Autowired
    public void setMinitjWxService(MinitjWxService minitjWxService) {
        this.minitjWxService = minitjWxService;
    }

    @Autowired
    public void setJjAndFcAppConfigService(JjAndFcAppConfigService jjAndFcAppConfigService) {
        this.jjAndFcAppConfigService = jjAndFcAppConfigService;
    }

}
