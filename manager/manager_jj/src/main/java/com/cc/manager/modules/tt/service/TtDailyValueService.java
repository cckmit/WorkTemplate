package com.cc.manager.modules.tt.service;

import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.service.WxConfigService;
import com.cc.manager.modules.tt.config.TtConfig;
import com.cc.manager.modules.tt.entity.TtDailyValue;
import com.cc.manager.modules.tt.mapper.TtDailyValueMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author: CF
 * @date 2020/7/2
 */
@Service
@DS("fc")
public class TtDailyValueService extends BaseCrudService<TtDailyValue, TtDailyValueMapper> {

    private TtConfig ttConfig;
    private WxConfigService wxConfigService;

    @SneakyThrows
    public PostResult getData(JSONObject jsonObject) {
        PostResult postResult = new PostResult();
        String[] timeSplit = jsonObject.getString("times").split("~");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long startTime = simpleDateFormat.parse(timeSplit[0] + " 00:00:00").getTime();
        long endTime = simpleDateFormat.parse(timeSplit[1] + " 00:00:00").getTime();
        String sessionId = jsonObject.getString("sdessionId");
        ArrayList<String> appIdLists = new ArrayList<>();
        getAppIdLists(sessionId, appIdLists);
        try {
            for (String appId : appIdLists) {
                //今日头条数据请求参数
                String tt = String.format(ttConfig.getTtRequest(), startTime / 1000, endTime / 1000);
                String ttNew = String.format(ttConfig.getTtNewRequest(), startTime / 1000, endTime / 1000);
                String ttActive = String.format(ttConfig.getTtActiveRequest(), startTime / 1000, endTime / 1000);
                //西瓜视频数据请求参数
                String watermelon = String.format(ttConfig.getWatermelonRequest(), startTime / 1000, endTime / 1000);
                String watermelonNew = String.format(ttConfig.getWatermelonNewRequest(), startTime / 1000, endTime / 1000);
                String watermelonActive = String.format(ttConfig.getWatermelonActiveRequest(), startTime / 1000, endTime / 1000);
                //抖音数据请求参数
                String tikTok = String.format(ttConfig.getTikTokRequest(), startTime / 1000, endTime / 1000);
                String tikTokNew = String.format(ttConfig.getTikTokNewRequest(), startTime / 1000, endTime / 1000);
                String tikTokActive = String.format(ttConfig.getTikTokActiveRequest(), startTime / 1000, endTime / 1000);
                //今日头条极速版数据请求参数
                String ttExtreme = String.format(ttConfig.getTtExtremeRequest(), startTime / 1000, endTime / 1000);
                String ttExtremeNew = String.format(ttConfig.getTtExtremeNewRequest(), startTime / 1000, endTime / 1000);
                String ttExtremeActive = String.format(ttConfig.getTtExtremeActiveRequest(), startTime / 1000, endTime / 1000);
                //抖音极速版数据请求参数
                String tikTokExtreme = String.format(ttConfig.getTikTokExtremeRequest(), startTime / 1000, endTime / 1000);
                String tikTokExtremeNew = String.format(ttConfig.getTikTokExtremeNewRequest(), startTime / 1000, endTime / 1000);
                String tikTokExtremeActive = String.format(ttConfig.getTikTokExtremeActiveRequest(), startTime / 1000, endTime / 1000);

                String url = String.format(ttConfig.getUrl(), appId);
                //不同平台拉取数据返回信息
                String ttBody = HttpRequest.post(url).header("Content-Type", "application/json").cookie("sdessionid=" + sessionId).body(tt).execute().body();
                String watermelonBody = HttpRequest.post(url).header("Content-Type", "application/json").cookie("sdessionid=" + sessionId).body(watermelon).execute().body();
                String tikTokBody = HttpRequest.post(url).header("Content-Type", "application/json").cookie("sdessionid=" + sessionId).body(tikTok).execute().body();
                String ttExtremeBody = HttpRequest.post(url).header("Content-Type", "application/json").cookie("sdessionid=" + sessionId).body(ttExtreme).execute().body();
                String tikTokExtremeBody = HttpRequest.post(url).header("Content-Type", "application/json").cookie("sdessionid=" + sessionId).body(tikTokExtreme).execute().body();

                Map<String, TtDailyValue> ttDailyValueMap = new HashMap<>(16);
                ArrayList<TtDailyValue> allTtDailyValue = new ArrayList<>();
                List<TtDailyValue> ttList = getTtDailyValues(appId, "tt", ttNew, ttActive, url, ttBody, ttDailyValueMap, sessionId);
                List<TtDailyValue> ttExtremeList = getTtDailyValues(appId, "ttExtreme", ttExtremeNew, ttExtremeActive, url, ttExtremeBody, ttDailyValueMap, sessionId);
                List<TtDailyValue> watermelonList = getTtDailyValues(appId, "watermelon", watermelonNew, watermelonActive, url, watermelonBody, ttDailyValueMap, sessionId);
                List<TtDailyValue> tikTokList = getTtDailyValues(appId, "tikTok", tikTokNew, tikTokActive, url, tikTokBody, ttDailyValueMap, sessionId);
                List<TtDailyValue> tikTokExtremeList = getTtDailyValues(appId, "tikTokExtreme", tikTokExtremeNew, tikTokExtremeActive, url, tikTokExtremeBody, ttDailyValueMap, sessionId);
                if (ttList != null) {
                    allTtDailyValue.addAll(ttList);
                }
                if (ttExtremeList != null) {
                    allTtDailyValue.addAll(ttExtremeList);
                }
                if (watermelonList != null) {
                    allTtDailyValue.addAll(watermelonList);
                }
                if (tikTokList != null) {
                    allTtDailyValue.addAll(tikTokList);
                }
                if (tikTokExtremeList != null) {
                    allTtDailyValue.addAll(tikTokExtremeList);
                }
                this.saveOrUpdateBatch(allTtDailyValue);
            }
        } catch (HttpException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            postResult.setCode(2);
            postResult.setMsg("头条数据拉取失败！");
        }
        return postResult;
    }

    /**
     * 根据sessionId获取产品appId
     *
     * @param sessionId  sessionId
     * @param appIdLists appIdLists
     */
    private void getAppIdLists(String sessionId, ArrayList<String> appIdLists) {
        String appIdListUrl = ttConfig.getAppIdListUrl();
        String appIdListBody = HttpRequest.get(appIdListUrl).header("Content-Type", "application/json").cookie("sdessionid=" + sessionId).execute().body();
        JSONObject object = JSONObject.parseObject(appIdListBody);
        JSONArray creatorList = object.getJSONObject("data").getJSONArray("creator_list");
        for (Object data : creatorList) {
            JSONObject appIdObject = JSONObject.parseObject(data.toString());
            String appId = appIdObject.getString("appid");
            appIdLists.add(appId);
        }
    }

    private List<TtDailyValue> getTtDailyValues(String appId, String appType, String ttNew, String ttActive, String url, String ttBody, Map<String, TtDailyValue> ttDailyValueMap, String sessionId) {
        JSONObject data = JSONObject.parseObject(ttBody).getJSONObject("data");
        if (data != null) {
            //解析头条data数据
            JSONArray xAxisData = data.getJSONObject("xAxis").getJSONArray("data");
            for (int i = 0; i < xAxisData.size(); i++) {
                Integer date = xAxisData.getInteger(i);
                TtDailyValue ttDailyValue = new TtDailyValue();
                ttDailyValue.setDateNum(date);
                ttDailyValue.setAppId(appId);
                ttDailyValue.setAppType(appType);
                ttDailyValue.setAppPlatform("字节游戏");
                Map<String, Integer> keyDataMap = new HashMap<>(16);
                for (Object seriesString : data.getJSONArray("series")) {
                    JSONObject seriesObject = JSONObject.parseObject(seriesString.toString());
                    keyDataMap.put(seriesObject.getString("group_by_key"), seriesObject.getJSONArray("data").getInteger(i));
                }
                ttDailyValue.setOpenTimes(JSONUtils.toJSONString(keyDataMap));
                ttDailyValueMap.put(date + "-" + appType, ttDailyValue);
            }
            //新增用户数请求数据返回值
            String ttNewBody = HttpRequest.post(url).header("Content-Type", "application/json").cookie("sdessionid=" + sessionId).body(ttNew).execute().body();
            JSONObject dataNew = JSONObject.parseObject(ttNewBody).getJSONObject("data");
            JSONArray xAxisDataNew = dataNew.getJSONObject("xAxis").getJSONArray("data");
            for (int i = 0; i < xAxisDataNew.size(); i++) {
                String dateNew = xAxisDataNew.getString(i);
                Map<String, Integer> keyDataMap = new HashMap<>(16);
                for (Object seriesString : dataNew.getJSONArray("series")) {
                    JSONObject seriesObject = JSONObject.parseObject(seriesString.toString());
                    keyDataMap.put(seriesObject.getString("group_by_key"), seriesObject.getJSONArray("data").getInteger(i));
                }
                TtDailyValue ttDailyValue = ttDailyValueMap.get(dateNew + "-" + appType);
                ttDailyValue.setNewUsers(JSONUtils.toJSONString(keyDataMap));
            }
            //活跃用户数请求数据返回值
            String ttActiveBody = HttpRequest.post(url).header("Content-Type", "application/json").cookie("sdessionid=" + sessionId).body(ttActive).execute().body();
            JSONObject dataActive = JSONObject.parseObject(ttActiveBody).getJSONObject("data");
            JSONArray xAxisDataActive = dataActive.getJSONObject("xAxis").getJSONArray("data");
            for (int i = 0; i < xAxisDataActive.size(); i++) {
                String dateNew = xAxisDataActive.getString(i);
                Map<String, Integer> keyDataMap = new HashMap<>(16);
                for (Object seriesString : dataActive.getJSONArray("series")) {
                    JSONObject seriesObject = JSONObject.parseObject(seriesString.toString());
                    keyDataMap.put(seriesObject.getString("group_by_key"), seriesObject.getJSONArray("data").getInteger(i));
                }
                TtDailyValue ttDailyValue = ttDailyValueMap.get(dateNew + "-" + appType);
                ttDailyValue.setActiveUsers(JSONUtils.toJSONString(keyDataMap));
            }

            return new ArrayList<>(ttDailyValueMap.values());
        }
        return null;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<TtDailyValue> entityList, int batchSize) {
        try {
            for (TtDailyValue ttDailyValue : entityList) {
                this.saveOrUpdate(ttDailyValue);
            }
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
        return true;
    }

    @Override
    public boolean saveOrUpdate(TtDailyValue entity) {
        entity.setInsertTime(LocalDateTime.now());
        QueryWrapper<TtDailyValue> ttDailyValueQueryWrapper = new QueryWrapper<>();
        ttDailyValueQueryWrapper.eq("dateNum", entity.getDateNum()).eq("appId", entity.getAppId()).eq("appType", entity.getAppType());
        TtDailyValue tableContent = this.getOne(ttDailyValueQueryWrapper);
        //数据存在更新，不存在则新增
        if (tableContent != null) {
            UpdateWrapper<TtDailyValue> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("dateNum", entity.getDateNum()).eq("appId", entity.getAppId()).eq("appType", entity.getAppType());
            return this.update(entity, updateWrapper);
        } else {
            return this.save(entity);
        }
    }

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<TtDailyValue> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("dateNum", timeRangeArray[0].replace("-", "").trim(), timeRangeArray[1].replace("-", "").trim());
            }
            String appType = queryObject.getString("appType");
            queryWrapper.eq(StringUtils.isNotBlank(appType), "appType", appType);
            String appId = queryObject.getString("appId");
            queryWrapper.eq(StringUtils.isNotBlank(appId), "appId", appId);
        }
        queryWrapper.orderByDesc("dateNum");
    }

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<TtDailyValue> entityList) {
        for (TtDailyValue ttDailyValue : entityList) {
            WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, ttDailyValue.getAppId());
            if (wxConfig != null) {
                ttDailyValue.setAppName(wxConfig.getProductName());
            } else {
                ttDailyValue.setAppName(ttDailyValue.getAppId());
            }
        }
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<TtDailyValue> deleteWrapper) {
        return false;
    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

    @Autowired
    public void setTtConfig(TtConfig ttConfig) {
        this.ttConfig = ttConfig;
    }

}
