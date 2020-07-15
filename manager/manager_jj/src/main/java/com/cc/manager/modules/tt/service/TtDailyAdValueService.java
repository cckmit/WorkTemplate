package com.cc.manager.modules.tt.service;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.tt.config.TtConfig;
import com.cc.manager.modules.tt.entity.TtDailyAdValue;
import com.cc.manager.modules.tt.mapper.TtDailyAdValueMapper;
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
 * @author cf
 * @since 2020-07-10
 */
@Service
public class TtDailyAdValueService extends BaseCrudService<TtDailyAdValue, TtDailyAdValueMapper> {

    private TtConfig ttConfig;

    @SneakyThrows
    public PostResult getAdData(JSONObject jsonObject) {
        PostResult postResult = new PostResult();
        String[] timeSplit = jsonObject.getString("times").split("~");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = simpleDateFormat.parse(timeSplit[0] + " 00:00:00");
        Date endDate = simpleDateFormat.parse(timeSplit[1] + " 23:59:59");

        String sessionId = jsonObject.getString("sdessionId");
        ArrayList<String> appIdLists = new ArrayList<>();
        getAppIdLists(sessionId, appIdLists);
        try {
            for (String appId : appIdLists) {
                Map<String, TtDailyAdValue> ttDailyAdValueMap = new HashMap<>(16);
                //今日头条广告数据请求
                String ttBannerUrl = String.format(ttConfig.getTtBannerUrl(), appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
                String ttVideoUrl = String.format(ttConfig.getTtVideoUrl(), appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
                String ttIntUrl = String.format(ttConfig.getTtIntUrl(), appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
                //今日头条极速版广告数据请求
                String ttExtremeVideoUrl = String.format(ttConfig.getTtExtremeVideoUrl(), appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
                String ttExtremeBannerUrl = String.format(ttConfig.getTtExtremeBannerUrl(), appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
                //抖音广告数据请求参数
                String tikTokVideoUrl = String.format(ttConfig.getTikTokVideoUrl(), appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
                //抖音极速版广告数据请求参数
                String tikTokExtremeVideoUrl = String.format(ttConfig.getTikTokExtremeVideoUrl(), appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
                //西瓜视频广告数据请求参数
                String watermelonVideoUrl = String.format(ttConfig.getWatermelonVideoUrl(), appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
                String watermelonBannerUrl = String.format(ttConfig.getWatermelonBannerUrl(), appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));

                getAdValue(appId, "tt", ttDailyAdValueMap, ttBannerUrl, ttVideoUrl, ttIntUrl, sessionId);
                getAdValue(appId, "watermelon", ttDailyAdValueMap, watermelonBannerUrl, watermelonVideoUrl, "", sessionId);
                getAdValue(appId, "tikTok", ttDailyAdValueMap, "", tikTokVideoUrl, "", sessionId);
                getAdValue(appId, "ttExtreme", ttDailyAdValueMap, ttExtremeBannerUrl, ttExtremeVideoUrl, "", sessionId);
                getAdValue(appId, "tikTokExtreme", ttDailyAdValueMap, "", tikTokExtremeVideoUrl, "", sessionId);
                ArrayList<TtDailyAdValue> ttDailyAdValue = new ArrayList<>(ttDailyAdValueMap.values());
                this.saveOrUpdateBatch(ttDailyAdValue);
            }
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            postResult.setCode(2);
            postResult.setMsg("头条广告数据拉取失败！");
        }

        return new PostResult();
    }

    private void getAdValue(String appId, String appType, Map<String, TtDailyAdValue> ttDailyAdValueMap, String ttBannerUrl, String ttVideoUrl, String ttIntUrl, String sessionId) {
        //不同平台拉取数据返回信息
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNotBlank(ttBannerUrl)) {
            String ttBannerBody = HttpRequest.get(ttBannerUrl).header("Content-Type", "application/json").cookie("sdessionid=" + sessionId).execute().body();
            JSONArray bannerData = JSONObject.parseObject(ttBannerBody).getJSONArray("data");
            if (bannerData != null) {
                for (Object datum : bannerData) {
                    TtDailyAdValue ttDailyAdValue = new TtDailyAdValue();
                    JSONObject datumObject = JSONObject.parseObject(datum.toString());
                    ttDailyAdValue.setWxDate(LocalDate.parse(datumObject.getString("date"), fmt));
                    ttDailyAdValue.setWxAppId(appId);
                    ttDailyAdValue.setWxAppType(appType);
                    if (datumObject.getInteger("show") != 0) {
                        ttDailyAdValue.setWxBannerClickrate(new BigDecimal(NumberUtil.div(datumObject.getInteger("click") * 1000, datumObject.getInteger("show") * 10, 2)));
                    }
                    ttDailyAdValue.setWxBannerIncome(datumObject.getBigDecimal("cost"));
                    ttDailyAdValue.setWxBannerShow(datumObject.getInteger("show"));
                    ttDailyAdValueMap.put(datumObject.getString("date") + "-" + appType, ttDailyAdValue);
                }
            }

        }
        if (StringUtils.isNotBlank(ttVideoUrl)) {
            String ttVideoBody = HttpRequest.get(ttVideoUrl).header("Content-Type", "application/json").cookie("sdessionid=" + sessionId).execute().body();
            JSONArray videoData = JSONObject.parseObject(ttVideoBody).getJSONArray("data");
            if (videoData != null) {
                for (Object datum : videoData) {
                    JSONObject datumObject = JSONObject.parseObject(datum.toString());
                    TtDailyAdValue dateAdValue = ttDailyAdValueMap.get(datumObject.getString("date") + "-" + appType);
                    if (dateAdValue != null) {
                        if (datumObject.getInteger("show") != 0) {
                            dateAdValue.setWxVideoClickrate(new BigDecimal(NumberUtil.div(datumObject.getInteger("click") * 1000, datumObject.getInteger("show") * 10, 2)));
                        }
                        dateAdValue.setWxVideoIncome(datumObject.getBigDecimal("cost"));
                        dateAdValue.setWxVideoShow(datumObject.getInteger("show"));
                    } else {
                        TtDailyAdValue ttDailyAdValue = new TtDailyAdValue();
                        ttDailyAdValue.setWxDate(LocalDate.parse(datumObject.getString("date"), fmt));
                        ttDailyAdValue.setWxAppId(appId);
                        ttDailyAdValue.setWxAppType(appType);
                        if (datumObject.getInteger("show") != 0) {
                            ttDailyAdValue.setWxVideoClickrate(NumberUtil.div(datumObject.getInteger("click"), datumObject.getInteger("show"), 2));
                        }
                        ttDailyAdValue.setWxVideoIncome(datumObject.getBigDecimal("cost"));
                        ttDailyAdValue.setWxVideoShow(datumObject.getInteger("show"));
                        ttDailyAdValueMap.put(datumObject.getString("date") + "-" + appType, ttDailyAdValue);
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(ttIntUrl)) {
            String ttIntBody = HttpRequest.get(ttIntUrl).header("Content-Type", "application/json").cookie("sdessionid=" + sessionId).execute().body();
            JSONArray intData = JSONObject.parseObject(ttIntBody).getJSONArray("data");
            if (intData != null) {
                for (Object datum : intData) {
                    JSONObject datumObject = JSONObject.parseObject(datum.toString());
                    TtDailyAdValue dateAdValue = ttDailyAdValueMap.get(datumObject.getString("date") + "-" + appType);
                    if (datumObject.getInteger("show") != 0) {
                        dateAdValue.setWxIntClickrate(new BigDecimal(NumberUtil.div(datumObject.getInteger("click") * 1000, datumObject.getInteger("show") * 10, 2)));
                    }
                    dateAdValue.setWxIntIncome(datumObject.getBigDecimal("cost"));
                    dateAdValue.setWxIntShow(datumObject.getInteger("show"));
                }
            }
        }

    }

    @Override
    public boolean saveOrUpdateBatch(Collection<TtDailyAdValue> entityList, int batchSize) {
        try {
            for (TtDailyAdValue ttDailyAdValue : entityList) {
                this.saveOrUpdate(ttDailyAdValue);
            }
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
        return true;
    }

    @Override
    public boolean saveOrUpdate(TtDailyAdValue entity) {
        LocalDate wxDate = entity.getWxDate();
        String appId = entity.getWxAppId();
        String appType = entity.getWxAppType();
        entity.setInsertTime(LocalDateTime.now());
        QueryWrapper<TtDailyAdValue> ttDailyAdValueQueryWrapper = new QueryWrapper<>();
        ttDailyAdValueQueryWrapper.eq("dateValue", wxDate).eq("appId", appId).eq("appType", appType);
        TtDailyAdValue tableContent = this.getOne(ttDailyAdValueQueryWrapper);
        //数据存在更新，不存在则新增
        if (tableContent != null) {
            UpdateWrapper<TtDailyAdValue> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("dateValue", wxDate).eq("appId", appId).eq("appType", appType);
            return this.update(entity, updateWrapper);
        } else {
            return this.save(entity);
        }
    }

    /**
     * 根据sessionId获取产品appId
     *
     * @param sessionId  sessionId
     * @param appIdLists appIdLists
     */
    private void getAppIdLists(String sessionId, ArrayList<String> appIdLists) {
        String appIdList = ttConfig.getAppIdListUrl();
        String appIdListBody = HttpRequest.get(appIdList).header("Content-Type", "application/json").cookie("sdessionid=" + sessionId).execute().body();
        JSONObject object = JSONObject.parseObject(appIdListBody);
        JSONArray jsonArray = object.getJSONObject("data").getJSONArray("creator_list");
        for (Object o : jsonArray) {
            JSONObject appIdObject = JSONObject.parseObject(o.toString());
            String appId = appIdObject.getString("appid");
            appIdLists.add(appId);
        }
    }

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<TtDailyAdValue> queryWrapper) {

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<TtDailyAdValue> deleteWrapper) {
        return false;
    }

    @Autowired
    public void setTtConfig(TtConfig ttConfig) {
        this.ttConfig = ttConfig;
    }

}
