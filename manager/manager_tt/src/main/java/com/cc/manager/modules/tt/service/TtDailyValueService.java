package com.cc.manager.modules.tt.service;

import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.tt.config.TtConfig;
import com.cc.manager.modules.tt.entity.TransferData;
import com.cc.manager.modules.tt.entity.TtDailyValue;
import com.cc.manager.modules.tt.entity.TtValueMapping;
import com.cc.manager.modules.tt.entity.WxConfig;
import com.cc.manager.modules.tt.mapper.TtDailyValueMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author CF
 * @date 2020/7/2
 */
@Service
@DS("tt")
public class TtDailyValueService extends BaseStatsService<TtDailyValue, TtDailyValueMapper> {

    private static ConcurrentHashMap<String, String> TIME_CACHE_MAP = new ConcurrentHashMap<>();
    private TtConfig ttConfig;
    private TtDailyAdValueService ttDailyAdValueService;
    private TtValueMappingService ttValueMappingService;

    public CrudObjectResult getAllData(JSONObject jsonObject) {
        CrudObjectResult postResult = new CrudObjectResult();
        ConcurrentHashMap<String, TransferData> exportMap = new ConcurrentHashMap<>();

        this.getData(jsonObject, exportMap);
        this.ttDailyAdValueService.getAdData(jsonObject, exportMap);
        //处理传输数据并发送
        ArrayList<TransferData> transferDataList = new ArrayList<>(exportMap.values());
        for (TransferData transferData : transferDataList) {
            String activeUsers = transferData.getActiveUsers();
            Map activeUserMap = (Map) JSON.parse(activeUsers);
            int activeUser = 0;
            for (Object mapKey : activeUserMap.keySet()) {
                QueryWrapper<TtValueMapping> queryWrapper = new QueryWrapper<>();
                TtValueMapping groupByKey = this.ttValueMappingService.getOne(queryWrapper.eq("groupByKey", mapKey));
                if (groupByKey != null && StringUtils.equals(groupByKey.getGroupByValue(), "搜索")) {
                    activeUser += Integer.parseInt(activeUserMap.get(mapKey.toString()).toString());
                }
            }
            transferData.setActiveUserNum(activeUser);
        }
        System.out.println("list打印开始--" + transferDataList + "--打印结束");
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("data",transferDataList);
      //  String resMessage = HttpRequest.post("http://192.168.1.123:20002/externalEngineer/yesterday/task").header("Content-Type", "application/json").body(jsonObject1.toJSONString()).execute().body();
      //  System.out.println("返回值:"+resMessage);
        return postResult;
    }

    @SneakyThrows
    public CrudObjectResult getData(JSONObject jsonObject, ConcurrentHashMap<String, TransferData> exportMap) {
        CrudObjectResult postResult = new CrudObjectResult();
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
                Map<String, TtDailyValue> ttDailyValueMap = new HashMap<>(16);
                getTtDailyValues(appId, "tt", tt, ttNew, ttActive, url, ttDailyValueMap, sessionId, exportMap);
                getTtDailyValues(appId, "ttExtreme", ttExtreme, ttExtremeNew, ttExtremeActive, url, ttDailyValueMap, sessionId, null);
                getTtDailyValues(appId, "watermelon", watermelon, watermelonNew, watermelonActive, url, ttDailyValueMap, sessionId, null);
                getTtDailyValues(appId, "tikTok", tikTok, tikTokNew, tikTokActive, url, ttDailyValueMap, sessionId, null);
                getTtDailyValues(appId, "tikTokExtreme", tikTokExtreme, tikTokExtremeNew, tikTokExtremeActive, url, ttDailyValueMap, sessionId, null);

                this.saveOrUpdateBatch(new ArrayList<>(ttDailyValueMap.values()));
            }

            TIME_CACHE_MAP.put("time", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
            postResult.setData(JSON.parseObject(JSON.toJSONString(TIME_CACHE_MAP)));
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
        JSONArray creatorList = JSONObject.parseObject(appIdListBody).getJSONObject("data").getJSONArray("creator_list");
        for (Object data : creatorList) {
            String appId = JSONObject.parseObject(data.toString()).getString("appid");
            appIdLists.add(appId);
        }
    }

    private void getTtDailyValues(String appId, String appType, String tt, String ttNew, String ttActive, String url, Map<String, TtDailyValue> ttDailyValueMap, String sessionId, Map<String, TransferData> exportMap) {
        String ttBody = HttpRequest.post(url).header("Content-Type", "application/json").cookie("sdessionid=" + sessionId).body(tt).execute().body();
        //打开次数数请求数据返回值
        JSONObject data = JSONObject.parseObject(ttBody).getJSONObject("data");
        if (data != null) {
            JSONArray xAxisData = data.getJSONObject("xAxis").getJSONArray("data");
            for (int i = 0; i < xAxisData.size(); i++) {
                Integer date = xAxisData.getInteger(i);
                TtDailyValue ttDailyValue = new TtDailyValue();
                ttDailyValue.setDateNum(date);
                ttDailyValue.setAppId(appId);
                ttDailyValue.setAppType(appType);
                ttDailyValue.setAppPlatform("字节游戏");
                Map<String, Integer> keyDataMap = new HashMap<>(16);
                dealSeriesArray(data, i, keyDataMap);
                ttDailyValue.setOpenTimes(JSONUtils.toJSONString(keyDataMap));
                ttDailyValueMap.put(date + "-" + appType, ttDailyValue);
            }
            //新增用户数请求数据返回值
            String ttNewBody = HttpRequest.post(url).header("Content-Type", "application/json").cookie("sdessionid=" + sessionId).body(ttNew).execute().body();
            JSONObject dataNew = JSONObject.parseObject(ttNewBody).getJSONObject("data");
            if (dataNew != null) {
                JSONArray xAxisDataNew = dataNew.getJSONObject("xAxis").getJSONArray("data");
                for (int i = 0; i < xAxisDataNew.size(); i++) {
                    String dateNew = xAxisDataNew.getString(i);
                    Map<String, Integer> keyDataMap = new HashMap<>(16);
                    dealSeriesArray(dataNew, i, keyDataMap);
                    TtDailyValue ttDailyValue = ttDailyValueMap.get(dateNew + "-" + appType);
                    ttDailyValue.setNewUsers(JSONUtils.toJSONString(keyDataMap));
                }
            }
            //活跃用户数请求数据返回值
            String ttActiveBody = HttpRequest.post(url).header("Content-Type", "application/json").cookie("sdessionid=" + sessionId).body(ttActive).execute().body();
            JSONObject dataActive = JSONObject.parseObject(ttActiveBody).getJSONObject("data");
            if (dataActive != null) {
                JSONArray xAxisDataActive = dataActive.getJSONObject("xAxis").getJSONArray("data");
                for (int i = 0; i < xAxisDataActive.size(); i++) {
                    String dateNew = xAxisDataActive.getString(i);
                    Map<String, Integer> keyDataMap = new HashMap<>(16);
                    dealSeriesArray(dataActive, i, keyDataMap);
                    TtDailyValue ttDailyValue = ttDailyValueMap.get(dateNew + "-" + appType);
                    ttDailyValue.setActiveUsers(JSONUtils.toJSONString(keyDataMap));
                    if (StringUtils.equals(ttDailyValue.getAppType(), "tt")) {
                        TransferData transferData = new TransferData();
                        transferData.setDateValue(ttDailyValue.getDateNum().toString());
                        transferData.setAppId(ttDailyValue.getAppId());
                        transferData.setActiveUsers(ttDailyValue.getActiveUsers());
                        exportMap.put(ttDailyValue.getAppId() + "-" + ttDailyValue.getDateNum(), transferData);
                    }
                }
            }
        }
    }

    /**
     * 解析series
     *
     * @param data       data
     * @param i          索引
     * @param keyDataMap keyDataMap
     */
    private void dealSeriesArray(JSONObject data, int i, Map<String, Integer> keyDataMap) {
        for (Object seriesString : data.getJSONArray("series")) {
            JSONObject seriesObject = JSONObject.parseObject(seriesString.toString());
            keyDataMap.put(seriesObject.getString("group_by_key"), seriesObject.getJSONArray("data").getInteger(i));
        }
    }

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<TtDailyValue> queryWrapper, StatsListResult statsListResult) {
        this.updateBeginAndEndDate(statsListParam);
        String beginDate = statsListParam.getQueryObject().getString("beginDate");
        String endDate = statsListParam.getQueryObject().getString("endDate");
        queryWrapper.between("dateNum", beginDate, endDate);
        String appId = statsListParam.getQueryObject().getString("appId");
        queryWrapper.eq(StringUtils.isNotBlank(appId), "appId", appId);
        String appType = statsListParam.getQueryObject().getString("appType");
        queryWrapper.eq(StringUtils.isNotBlank(appType), "appType", appType);
        queryWrapper.orderByDesc("dateNum");
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
            beginDate = timeRangeArray[0].replace("-", "").trim();
            endDate = timeRangeArray[1].replace("-", "").trim();
        }
        if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            beginDate = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now().minusDays(2));
            endDate = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now().minusDays(1));
        }
        statsListParam.getQueryObject().put("beginDate", beginDate);
        statsListParam.getQueryObject().put("endDate", endDate);
    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<TtDailyValue> entityList, StatsListResult statsListResult) {
        String wxConfigs = HttpRequest.get(ttConfig.getWxConfigApi()).execute().body();
        HashMap<String, WxConfig> wxConfigHashMap = new HashMap<>(16);
        JSONArray data = JSONObject.parseObject(wxConfigs).getJSONArray("data");
        for (Object datum : data) {
            WxConfig wxConfig = JSONUtil.toBean(datum.toString(), WxConfig.class);
            wxConfigHashMap.put(wxConfig.getId(), wxConfig);
        }
        for (TtDailyValue ttDailyValue : entityList) {
            WxConfig wxConfig = wxConfigHashMap.get(ttDailyValue.getAppId());
            if (wxConfig != null) {
                ttDailyValue.setAppName(wxConfig.getProductName());
            } else {
                ttDailyValue.setAppName(ttDailyValue.getAppId());
            }
        }

        Map<String, TtDailyValue> ttDailyValueMap = new LinkedHashMap<>(16);
        JSONObject queryObject = JSONObject.parseObject(statsListParam.getQueryData());
        if (queryObject != null) {
            String appType = queryObject.getString("appCollect");
            if (StringUtils.equals("ttCount", appType)) {
                //计算平台汇总数据
                appDataCollect(entityList, ttDailyValueMap);
                List<TtDailyValue> newEntityList = new ArrayList<>(ttDailyValueMap.values());
                entityList.clear();
                entityList.addAll(newEntityList);
            }
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

    /**
     * 计算平台汇总数据
     *
     * @param entityList      entityList
     * @param ttDailyValueMap ttDailyValueMap
     */
    private void appDataCollect(List<TtDailyValue> entityList, Map<String, TtDailyValue> ttDailyValueMap) {
        for (TtDailyValue ttDailyValue : entityList) {
            Integer dateNum = ttDailyValue.getDateNum();
            String appId = ttDailyValue.getAppId();
            TtDailyValue dailyValue = ttDailyValueMap.get(dateNum + "-" + appId);
            if (dailyValue != null) {
                Integer newCount = 0;
                newCount = getUsers(JSONObject.parseObject(ttDailyValue.getNewUsers()), newCount);
                newCount = getUsers(JSONObject.parseObject(dailyValue.getNewUsers()), newCount);
                JSONObject jsonNewUsers = new JSONObject(16);
                jsonNewUsers.put("0000", newCount);
                dailyValue.setNewUsers(jsonNewUsers.toJSONString());

                Integer activeCount = 0;
                activeCount = getUsers(JSONObject.parseObject(ttDailyValue.getActiveUsers()), activeCount);
                activeCount = getUsers(JSONObject.parseObject(dailyValue.getActiveUsers()), activeCount);
                JSONObject jsonActiveUsers = new JSONObject(16);
                jsonActiveUsers.put("0000", activeCount);
                dailyValue.setActiveUsers(jsonActiveUsers.toString());
            } else {
                ttDailyValue.setAppType("ttCount");
                ttDailyValueMap.put(dateNum + "-" + appId, ttDailyValue);
            }
        }
    }

    /**
     * 获取汇总用户数
     *
     * @param ttDailyUsers ttDailyUsers
     * @param count        count
     * @return 用户数
     */
    private Integer getUsers(JSONObject ttDailyUsers, Integer count) {
        Map<String, Integer> params = new HashMap<>(16);
        if (ttDailyUsers != null) {
            for (Map.Entry<String, Object> entry : ttDailyUsers.entrySet()) {
                params.put(entry.getKey(), Integer.parseInt(entry.getValue().toString()));
            }
            for (Integer integer : new ArrayList<>(params.values())) {
                count = count + integer;
            }
        }
        return count;
    }

    @Autowired
    public void setTtConfig(TtConfig ttConfig) {
        this.ttConfig = ttConfig;
    }

    @Autowired
    public void setTtDailyAdValueService(TtDailyAdValueService ttDailyAdValueService) {
        this.ttDailyAdValueService = ttDailyAdValueService;
    }

    @Autowired
    public void setTtValueMappingService(TtValueMappingService ttValueMappingService) {
        this.ttValueMappingService = ttValueMappingService;
    }

}
