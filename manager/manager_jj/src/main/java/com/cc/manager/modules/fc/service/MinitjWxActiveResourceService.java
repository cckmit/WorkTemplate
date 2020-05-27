package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cf
 * @since 2020-05-22
 */
@Service
public class MinitjWxActiveResourceService extends BaseStatsService<MinitjWx, MinitjWxMapper> {

    private WxConfigService wxConfigService;
    private MiniGameService miniGameService;

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<MinitjWx> queryWrapper, StatsListResult statsListResult) {

    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<MinitjWx> entityList, StatsListResult statsListResult) {
        return null;
    }

    @Override
    public StatsListResult getPage(StatsListParam statsListParam) {
        StatsListResult statsListResult = new StatsListResult();
        try {
            String[] times = getTimes(statsListParam);
            ConcurrentHashMap<String, WxConfig> wxConfigMap = getProductMap();
            String ddAppId = "";
            List<MinitjWx> userResources;
            List<MinitjWx> newUserResources = new ArrayList<>();
            if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
                JSONObject queryObject = JSONObject.parseObject(statsListParam.getQueryData());
                ddAppId = queryObject.getString("appId");
            }
            userResources = selectUserResources(times, ddAppId, wxConfigMap);
            for (int i = (statsListParam.getPage() - 1) * statsListParam.getLimit(); i < statsListParam.getPage() * statsListParam.getLimit(); i++) {
                if (userResources.size() > i) {
                    newUserResources.add(userResources.get(i));
                }
            }
            statsListResult.setData(JSONArray.parseArray(JSON.toJSONString(newUserResources)));
            statsListResult.setCount(userResources.size());
        } catch (Exception e) {
            statsListResult.setCode(1);
            statsListResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return statsListResult;
    }

    private List<MinitjWx> selectUserResources(String[] times, String ddAppId, ConcurrentHashMap<String, WxConfig> wxConfigMap) {
        QueryWrapper<MinitjWx> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("DATE(wx_date)", times[0].trim(), times[1].trim());
        queryWrapper.eq(StringUtils.isNotBlank(ddAppId), "wx_appid", ddAppId);
        // 小游戏的时候查询
        List<MinitjWx> resourceDataList = this.mapper.selectList(queryWrapper);
        List<MinitjWx> newDataList = new ArrayList<>();
        for (MinitjWx resourceData : resourceDataList) {
            WxConfig wxConfig = wxConfigMap.get(resourceData.getWxAppid());
            if (wxConfig == null) {
                continue;
            }
            // 设置data产品信息
            resourceData.setProgramType(wxConfig.getProgramType());
            resourceData.setProductName(wxConfig.getProductName());
            resourceData.setDdAppPlatform(wxConfig.getDdAppPlatform());
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


    /**
     * 获取产品集合map
     *
     * @return ConcurrentHashMap
     */
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

    private String[] getTimes(StatsListParam statsListParam) {
        String[] times = new String[2];
        String beginTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        String endTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
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
