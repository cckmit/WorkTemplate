package com.cc.manager.modules.tt.service;

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
import com.cc.manager.modules.tt.entity.TtDailyAdValue;
import com.cc.manager.modules.tt.entity.TtDailyValue;
import com.cc.manager.modules.tt.mapper.TtDailyValueMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author: CF
 * @date 2020/7/2 14:52
 */
@Service
@DS("fc")
public class TtDailyValueService extends BaseCrudService<TtDailyValue, TtDailyValueMapper> {
    private WxConfigService wxConfigService;

    @SneakyThrows
    public PostResult getAdData(JSONObject jsonObject) {
        String[] timeSplit = jsonObject.getString("times").split("~");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = simpleDateFormat.parse(timeSplit[0] + " 00:00:00");
        Date endDate = simpleDateFormat.parse(timeSplit[1] + " 23:59:59");

        String sdessionId = jsonObject.getString("sdessionId");
        String appId = "tt03bf75029c85e975";
        Map<String, TtDailyAdValue> ttDailyAdValueMap = new HashMap<>(16);
        //,20037,10037,30037,80038,80043,40042,20038,16038,10038,30038,28042
        String ttBannerUrl = String.format("https://microapp.bytedance.com/api/v1/app/%s/ad/overview?start=%s&end=%s&type=80037", appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
        String ttVideoUrl = String.format("https://microapp.bytedance.com/api/v1/app/%s/ad/overview?start=%s&end=%s&type=80038", appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
        String ttIntUrl = String.format("https://microapp.bytedance.com/api/v1/app/%s/ad/overview?start=%s&end=%s&type=80043", appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
        //不同平台拉取数据返回信息
        String ttBannerBody = HttpRequest.get(ttBannerUrl).header("Content-Type", "application/json").cookie("sdessionid=87d318bccacce1a2b8643a68126ba575").execute().body();
        JSONArray bannerData = JSONObject.parseObject(ttBannerBody).getJSONArray("data");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Object datum : bannerData) {
            TtDailyAdValue ttDailyAdValue = new TtDailyAdValue();
            JSONObject datumObject = JSONObject.parseObject(datum.toString());
            ttDailyAdValue.setWxDate(LocalDate.parse(datumObject.getString("date"), fmt));
            ttDailyAdValue.setWxAppId(appId);
            ttDailyAdValue.setWxAppType("tt");
            ttDailyAdValue.setWxBannerClickrate(new BigDecimal(datumObject.getInteger("click") / datumObject.getInteger("show")));
            ttDailyAdValue.setWxBannerIncome(datumObject.getBigDecimal("cost"));
            ttDailyAdValue.setWxBannerShow(datumObject.getInteger("show"));
            ttDailyAdValueMap.put(datumObject.getString("date") + "-" + "tt", ttDailyAdValue);
        }
        String ttVideoBody = HttpRequest.get(ttVideoUrl).header("Content-Type", "application/json").cookie("sdessionid=87d318bccacce1a2b8643a68126ba575").execute().body();
        JSONArray videoData = JSONObject.parseObject(ttVideoBody).getJSONArray("data");
        for (Object datum : videoData) {
            JSONObject datumObject = JSONObject.parseObject(datum.toString());
            TtDailyAdValue dateAdValue = ttDailyAdValueMap.get(datumObject.getString("date") + "-" + "tt");
            dateAdValue.setWxVideoClickrate(new BigDecimal(datumObject.getInteger("click") / datumObject.getInteger("show")));
            dateAdValue.setWxVideoIncome(datumObject.getBigDecimal("cost"));
            dateAdValue.setWxVideoShow(datumObject.getInteger("show"));
        }
        String ttIntBody = HttpRequest.get(ttIntUrl).header("Content-Type", "application/json").cookie("sdessionid=87d318bccacce1a2b8643a68126ba575").execute().body();
        JSONArray intData = JSONObject.parseObject(ttIntBody).getJSONArray("data");
        for (Object datum : intData) {
            JSONObject datumObject = JSONObject.parseObject(datum.toString());
            TtDailyAdValue dateAdValue = ttDailyAdValueMap.get(datumObject.getString("date") + "-" + "tt");
            dateAdValue.setWxIntClickrate(new BigDecimal(datumObject.getInteger("click") / datumObject.getInteger("show")));
            dateAdValue.setWxIntIncome(datumObject.getBigDecimal("cost"));
            dateAdValue.setWxIntShow(datumObject.getInteger("show"));
        }
        System.out.println(ttDailyAdValueMap.toString());
        return null;
    }

    @SneakyThrows
    public PostResult getData(JSONObject jsonObject) {
        String[] timeSplit = jsonObject.getString("times").split("~");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = simpleDateFormat.parse(timeSplit[0] + " 00:00:00");
        Date endDate = simpleDateFormat.parse(timeSplit[1] + " 00:00:00");
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        String sdessionId = jsonObject.getString("sdessionId");
        String appId = "tt03bf75029c85e975";
        //今日头条数据请求参数
        String tt = String.format("{\"period\":{\"interval\":\"day\",\"range\":[%s,%s],\"type\":\"range\"},\"hostId\":13,\"queryEvents\":[{\"label\":\"打开次数\",\"eventName\":\"mp_launch\",\"resultType\":{\"type\":\"events\"},\"groupBy\":[\"scene\"],\"attrs\":[{\"attr\":\"_app_name\",\"content_type\":\"event_param\",\"op\":\"=\",\"type\":\"string\",\"value\":[\"news_article\"]}]}],\"filters\":[]}", startTime / 1000, endTime / 1000);
        String ttNew = String.format("{\"period\":{\"interval\":\"day\",\"range\":[%s,%s],\"type\":\"range\"},\"hostId\":13,\"queryEvents\":[{\"label\":\"新增用户数\",\"eventName\":\"mp_launch\",\"resultType\":{\"type\":\"event_users\"},\"groupBy\":[\"scene\"],\"attrs\":[{\"attr\":\"_app_name\",\"content_type\":\"event_param\",\"op\":\"=\",\"type\":\"string\",\"value\":[\"news_article\"]}]}],\"filters\":[{\"name\":\"新用户\",\"attr\":\"user_is_new\",\"op\":\"=\",\"value\":[1]}]}", startTime / 1000, endTime / 1000);
        String ttActive = String.format("{\"period\":{\"interval\":\"day\",\"range\":[%s,%s],\"type\":\"range\"},\"hostId\":13,\"queryEvents\":[{\"label\":\"活跃用户数\",\"eventName\":\"mp_launch\",\"resultType\":{\"type\":\"event_users\"},\"groupBy\":[\"scene\"],\"attrs\":[{\"attr\":\"_app_name\",\"content_type\":\"event_param\",\"op\":\"=\",\"type\":\"string\",\"value\":[\"news_article\"]}]}],\"filters\":[]}", startTime / 1000, endTime / 1000);
        //西瓜视频数据请求参数
        String watermelon = String.format("{\"period\":{\"interval\":\"day\",\"range\":[%s,%s],\"type\":\"range\"},\"hostId\":32,\"queryEvents\":[{\"label\":\"打开次数\",\"eventName\":\"mp_launch\",\"resultType\":{\"type\":\"events\"},\"groupBy\":[\"scene\"],\"attrs\":[{\"attr\":\"_app_name\",\"content_type\":\"event_param\",\"op\":\"=\",\"type\":\"string\",\"value\":[\"video_article\"]}]}],\"filters\":[]}", startTime / 1000, endTime / 1000);
        String watermelonNew = String.format("{\"period\":{\"interval\":\"day\",\"range\":[%s,%s],\"type\":\"range\"},\"hostId\":32,\"queryEvents\":[{\"label\":\"新增用户数\",\"eventName\":\"mp_launch\",\"resultType\":{\"type\":\"event_users\"},\"groupBy\":[\"scene\"],\"attrs\":[{\"attr\":\"_app_name\",\"content_type\":\"event_param\",\"op\":\"=\",\"type\":\"string\",\"value\":[\"video_article\"]}]}],\"filters\":[{\"name\":\"新用户\",\"attr\":\"user_is_new\",\"op\":\"=\",\"value\":[1]}]}", startTime / 1000, endTime / 1000);
        String watermelonActive = String.format("{\"period\":{\"interval\":\"day\",\"range\":[%s,%s],\"type\":\"range\"},\"hostId\":32,\"queryEvents\":[{\"label\":\"活跃用户数\",\"eventName\":\"mp_launch\",\"resultType\":{\"type\":\"event_users\"},\"groupBy\":[\"scene\"],\"attrs\":[{\"attr\":\"_app_name\",\"content_type\":\"event_param\",\"op\":\"=\",\"type\":\"string\",\"value\":[\"video_article\"]}]}],\"filters\":[]}", startTime / 1000, endTime / 1000);
        //抖音数据请求参数
        String tikTok = String.format("{\"period\":{\"interval\":\"day\",\"range\":[%s,%s],\"type\":\"range\"},\"hostId\":1128,\"queryEvents\":[{\"label\":\"打开次数\",\"eventName\":\"mp_launch\",\"resultType\":{\"type\":\"events\"},\"groupBy\":[\"scene\"],\"attrs\":[{\"attr\":\"_app_name\",\"content_type\":\"event_param\",\"op\":\"=\",\"type\":\"string\",\"value\":[\"aweme\"]}]}],\"filters\":[]}", startTime / 1000, endTime / 1000);
        String tikTokNew = String.format("{\"period\":{\"interval\":\"day\",\"range\":[%s,%s],\"type\":\"range\"},\"hostId\":1128,\"queryEvents\":[{\"label\":\"新增用户数\",\"eventName\":\"mp_launch\",\"resultType\":{\"type\":\"event_users\"},\"groupBy\":[\"scene\"],\"attrs\":[{\"attr\":\"_app_name\",\"content_type\":\"event_param\",\"op\":\"=\",\"type\":\"string\",\"value\":[\"aweme\"]}]}],\"filters\":[{\"name\":\"新用户\",\"attr\":\"user_is_new\",\"op\":\"=\",\"value\":[1]}]}", startTime / 1000, endTime / 1000);
        String tikTokActive = String.format("{\"period\":{\"interval\":\"day\",\"range\":[%s,%s],\"type\":\"range\"},\"hostId\":1128,\"queryEvents\":[{\"label\":\"活跃用户数\",\"eventName\":\"mp_launch\",\"resultType\":{\"type\":\"event_users\"},\"groupBy\":[\"scene\"],\"attrs\":[{\"attr\":\"_app_name\",\"content_type\":\"event_param\",\"op\":\"=\",\"type\":\"string\",\"value\":[\"aweme\"]}]}],\"filters\":[]}", startTime / 1000, endTime / 1000);
        //今日头条极速版数据请求参数
        String ttExtreme = String.format("{\"period\":{\"interval\":\"day\",\"range\":[%s,%s],\"type\":\"range\"},\"hostId\":35,\"queryEvents\":[{\"label\":\"打开次数\",\"eventName\":\"mp_launch\",\"resultType\":{\"type\":\"events\"},\"groupBy\":[\"scene\"],\"attrs\":[{\"attr\":\"_app_name\",\"content_type\":\"event_param\",\"op\":\"=\",\"type\":\"string\",\"value\":[\"news_article_lite\"]}]}],\"filters\":[]}", startTime / 1000, endTime / 1000);
        String ttExtremeNew = String.format("{\"period\":{\"interval\":\"day\",\"range\":[%s,%s],\"type\":\"range\"},\"hostId\":35,\"queryEvents\":[{\"label\":\"新增用户数\",\"eventName\":\"mp_launch\",\"resultType\":{\"type\":\"event_users\"},\"groupBy\":[\"scene\"],\"attrs\":[{\"attr\":\"_app_name\",\"content_type\":\"event_param\",\"op\":\"=\",\"type\":\"string\",\"value\":[\"news_article_lite\"]}]}],\"filters\":[{\"name\":\"新用户\",\"attr\":\"user_is_new\",\"op\":\"=\",\"value\":[1]}]}", startTime / 1000, endTime / 1000);
        String ttExtremeActive = String.format("{\"period\":{\"interval\":\"day\",\"range\":[%s,%s],\"type\":\"range\"},\"hostId\":35,\"queryEvents\":[{\"label\":\"活跃用户数\",\"eventName\":\"mp_launch\",\"resultType\":{\"type\":\"event_users\"},\"groupBy\":[\"scene\"],\"attrs\":[{\"attr\":\"_app_name\",\"content_type\":\"event_param\",\"op\":\"=\",\"type\":\"string\",\"value\":[\"news_article_lite\"]}]}],\"filters\":[]}", startTime / 1000, endTime / 1000);
        //抖音极速版数据请求参数
        String tikTokExtreme = String.format("{\"period\":{\"interval\":\"day\",\"range\":[%s,%s],\"type\":\"range\"},\"hostId\":2329,\"queryEvents\":[{\"label\":\"打开次数\",\"eventName\":\"mp_launch\",\"resultType\":{\"type\":\"events\"},\"groupBy\":[\"scene\"],\"attrs\":[{\"attr\":\"_app_name\",\"content_type\":\"event_param\",\"op\":\"=\",\"type\":\"string\",\"value\":[\"aweme_lite\"]}]}],\"filters\":[]}", startTime / 1000, endTime / 1000);
        String tikTokExtremeNew = String.format("{\"period\":{\"interval\":\"day\",\"range\":[%s,%s],\"type\":\"range\"},\"hostId\":2329,\"queryEvents\":[{\"label\":\"新增用户数\",\"eventName\":\"mp_launch\",\"resultType\":{\"type\":\"event_users\"},\"groupBy\":[\"scene\"],\"attrs\":[{\"attr\":\"_app_name\",\"content_type\":\"event_param\",\"op\":\"=\",\"type\":\"string\",\"value\":[\"aweme_lite\"]}]}],\"filters\":[{\"name\":\"新用户\",\"attr\":\"user_is_new\",\"op\":\"=\",\"value\":[1]}]}", startTime / 1000, endTime / 1000);
        String tikTokExtremeActive = String.format("{\"period\":{\"interval\":\"day\",\"range\":[%s,%s],\"type\":\"range\"},\"hostId\":2329,\"queryEvents\":[{\"label\":\"活跃用户数\",\"eventName\":\"mp_launch\",\"resultType\":{\"type\":\"event_users\"},\"groupBy\":[\"scene\"],\"attrs\":[{\"attr\":\"_app_name\",\"content_type\":\"event_param\",\"op\":\"=\",\"type\":\"string\",\"value\":[\"aweme_lite\"]}]}],\"filters\":[]}", startTime / 1000, endTime / 1000);

        String url = String.format("https://microapp.bytedance.com/api/v1/app/%s/source_analysis", appId);
        //不同平台拉取数据返回信息
        String ttBody = HttpRequest.post(url).header("Content-Type", "application/json").cookie("sdessionid=87d318bccacce1a2b8643a68126ba575").body(tt).execute().body();
        String watermelonBody = HttpRequest.post(url).header("Content-Type", "application/json").cookie("sdessionid=87d318bccacce1a2b8643a68126ba575").body(watermelon).execute().body();
        String tikTokBody = HttpRequest.post(url).header("Content-Type", "application/json").cookie("sdessionid=87d318bccacce1a2b8643a68126ba575").body(tikTok).execute().body();
        String ttExtremeBody = HttpRequest.post(url).header("Content-Type", "application/json").cookie("sdessionid=87d318bccacce1a2b8643a68126ba575").body(ttExtreme).execute().body();
        String tikTokExtremeBody = HttpRequest.post(url).header("Content-Type", "application/json").cookie("sdessionid=87d318bccacce1a2b8643a68126ba575").body(tikTokExtreme).execute().body();

        Map<String, TtDailyValue> ttDailyValueMap = new HashMap<>(16);
        System.out.println(ttBody);
        List<TtDailyValue> ttList = getTtDailyValues(appId, "tt", ttNew, ttActive, url, ttBody, ttDailyValueMap);
        List<TtDailyValue> watermelonList = getTtDailyValues(appId, "watermelon", watermelonNew, watermelonActive, url, watermelonBody, ttDailyValueMap);
        List<TtDailyValue> tikTokList = getTtDailyValues(appId, "tikTok", tikTokNew, tikTokActive, url, tikTokBody, ttDailyValueMap);
        List<TtDailyValue> ttExtremeList = getTtDailyValues(appId, "ttExtreme", ttExtremeNew, ttExtremeActive, url, ttExtremeBody, ttDailyValueMap);
        List<TtDailyValue> tikTokExtremeList = getTtDailyValues(appId, "tikTokExtreme", tikTokExtremeNew, tikTokExtremeActive, url, tikTokExtremeBody, ttDailyValueMap);
        ArrayList<TtDailyValue> allTtDailyValue = new ArrayList<>();
        allTtDailyValue.addAll(ttList);
        allTtDailyValue.addAll(watermelonList);
        allTtDailyValue.addAll(tikTokList);
        allTtDailyValue.addAll(ttExtremeList);
        allTtDailyValue.addAll(tikTokExtremeList);
        this.saveOrUpdateBatch(allTtDailyValue);
        return new PostResult();
    }

    private List<TtDailyValue> getTtDailyValues(String appId, String appType, String ttNew, String ttActive, String url, String ttBody, Map<String, TtDailyValue> ttDailyValueMap) {
        JSONObject data = JSONObject.parseObject(ttBody).getJSONObject("data");
        JSONArray series = data.getJSONArray("series");
        JSONObject xAxis = data.getJSONObject("xAxis");
        JSONArray xAxisData = xAxis.getJSONArray("data");
        for (int i = 0; i < xAxisData.size(); i++) {
            Integer date = xAxisData.getInteger(i);
            TtDailyValue ttDailyValue = new TtDailyValue();
            ttDailyValue.setDateNum(date);
            ttDailyValue.setAppId(appId);
            ttDailyValue.setAppType(appType);
            ttDailyValue.setAppPlatform("字节游戏");
            Map<String, Integer> keyDataMap = new HashMap<>(16);
            for (Object seriesString : series) {
                JSONObject seriesObject = JSONObject.parseObject(seriesString.toString());
                String groupByKey = seriesObject.getString("group_by_key");
                Integer seriesData = seriesObject.getJSONArray("data").getInteger(i);
                keyDataMap.put(groupByKey, seriesData);
            }
            String map = JSONUtils.toJSONString(keyDataMap);
            ttDailyValue.setOpenTimes(map);
            ttDailyValueMap.put(date + "-" + appType, ttDailyValue);
        }
        //新增用户数请求数据返回值
        String ttNewBody = HttpRequest.post(url).header("Content-Type", "application/json").cookie("sdessionid=87d318bccacce1a2b8643a68126ba575").body(ttNew).execute().body();
        JSONObject dataNew = JSONObject.parseObject(ttNewBody).getJSONObject("data");
        JSONArray seriesNew = dataNew.getJSONArray("series");
        JSONObject xAxisNew = dataNew.getJSONObject("xAxis");
        JSONArray xAxisDataNew = xAxisNew.getJSONArray("data");
        for (int i = 0; i < xAxisDataNew.size(); i++) {
            String dateNew = xAxisDataNew.getString(i);
            Map<String, Integer> keyDataMap = new HashMap<>(16);
            for (Object seriesString : seriesNew) {
                JSONObject seriesObject = JSONObject.parseObject(seriesString.toString());
                String groupByKey = seriesObject.getString("group_by_key");
                Integer seriesData = seriesObject.getJSONArray("data").getInteger(i);
                keyDataMap.put(groupByKey, seriesData);
            }
            String newUsersMap = JSONUtils.toJSONString(keyDataMap);
            TtDailyValue ttDailyValue = ttDailyValueMap.get(dateNew + "-" + appType);
            ttDailyValue.setNewUsers(newUsersMap);
        }
        //活跃用户数请求数据返回值
        String ttActiveBody = HttpRequest.post(url).header("Content-Type", "application/json").cookie("sdessionid=87d318bccacce1a2b8643a68126ba575").body(ttActive).execute().body();
        System.out.println("我是新增：" + ttActiveBody);
        JSONObject dataActive = JSONObject.parseObject(ttActiveBody).getJSONObject("data");
        JSONArray seriesActive = dataActive.getJSONArray("series");
        JSONObject xAxisActive = dataActive.getJSONObject("xAxis");
        JSONArray xAxisDataActive = xAxisActive.getJSONArray("data");
        for (int i = 0; i < xAxisDataActive.size(); i++) {
            String dateNew = xAxisDataActive.getString(i);
            Map<String, Integer> keyDataMap = new HashMap<>(16);
            for (Object seriesString : seriesActive) {
                JSONObject seriesObject = JSONObject.parseObject(seriesString.toString());
                String groupByKey = seriesObject.getString("group_by_key");
                Integer seriesData = seriesObject.getJSONArray("data").getInteger(i);
                keyDataMap.put(groupByKey, seriesData);
            }
            String activeUsersMap = JSONUtils.toJSONString(keyDataMap);
            TtDailyValue ttDailyValue = ttDailyValueMap.get(dateNew + "-" + appType);
            ttDailyValue.setActiveUsers(activeUsersMap);
        }
        System.out.println(ttDailyValueMap.toString());
        return new ArrayList<>(ttDailyValueMap.values());
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
        Integer dateNum = entity.getDateNum();
        String appId = entity.getAppId();
        String appType = entity.getAppType();
        entity.setInsertTime(LocalDateTime.now());
        QueryWrapper<TtDailyValue> ttDailyValueQueryWrapper = new QueryWrapper<>();
        ttDailyValueQueryWrapper.eq("dateNum", dateNum).eq("appId", appId).eq("appType", appType);
        TtDailyValue tableContent = this.getOne(ttDailyValueQueryWrapper);
        //数据存在更新，不存在则新增
        if (tableContent != null) {
            UpdateWrapper<TtDailyValue> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("dateNum", dateNum).eq("appId", appId).eq("appType", appType);
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

        }
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
            String ddAppId = ttDailyValue.getAppId();
            WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, ddAppId);
            if (wxConfig != null) {
                ttDailyValue.setAppName(wxConfig.getProductName());
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

}
