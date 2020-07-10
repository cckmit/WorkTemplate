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
import com.cc.manager.modules.tt.entity.TtDailyAdValue;
import com.cc.manager.modules.tt.mapper.TtDailyAdValueMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
    @SneakyThrows
    public PostResult getAdData(JSONObject jsonObject) {
        String[] timeSplit = jsonObject.getString("times").split("~");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = simpleDateFormat.parse(timeSplit[0] + " 00:00:00");
        Date endDate = simpleDateFormat.parse(timeSplit[1] + " 23:59:59");

        String sdessionId = jsonObject.getString("sdessionId");
        String appId = "tt03bf75029c85e975";
        Map<String, TtDailyAdValue> ttDailyAdValueMap = new HashMap<>(16);

        String ttBannerUrl = String.format("https://microapp.bytedance.com/api/v1/app/%s/ad/overview?start=%s&end=%s&type=80037", appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
        String ttVideoUrl = String.format("https://microapp.bytedance.com/api/v1/app/%s/ad/overview?start=%s&end=%s&type=80038", appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
        String ttIntUrl = String.format("https://microapp.bytedance.com/api/v1/app/%s/ad/overview?start=%s&end=%s&type=80043", appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));

        String ttExtremeVideoUrl = String.format("https://microapp.bytedance.com/api/v1/app/%s/ad/overview?start=%s&end=%s&type=20038", appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
        String ttExtremeBannerUrl = String.format("https://microapp.bytedance.com/api/v1/app/%s/ad/overview?start=%s&end=%s&type=20037", appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));

        String tikTokVideoUrl = String.format("https://microapp.bytedance.com/api/v1/app/%s/ad/overview?start=%s&end=%s&type=40042", appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));

        String tikTokExtremeVideoUrl = String.format("https://microapp.bytedance.com/api/v1/app/%s/ad/overview?start=%s&end=%s&type=28042", appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));

        String watermelonVideoUrl = String.format("https://microapp.bytedance.com/api/v1/app/%s/ad/overview?start=%s&end=%s&type=10038", appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
        String watermelonBannerUrl = String.format("https://microapp.bytedance.com/api/v1/app/%s/ad/overview?start=%s&end=%s&type=10037", appId, simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));

        getAdValue(appId, "tt", ttDailyAdValueMap, ttBannerUrl, ttVideoUrl, ttIntUrl);
        getAdValue(appId, "watermelon", ttDailyAdValueMap, watermelonBannerUrl, watermelonVideoUrl, "");
        getAdValue(appId, "tikTok", ttDailyAdValueMap, "", tikTokVideoUrl, "");
        getAdValue(appId, "ttExtreme", ttDailyAdValueMap, ttExtremeBannerUrl, ttExtremeVideoUrl, "");
        getAdValue(appId, "tikTokExtreme", ttDailyAdValueMap, "", tikTokExtremeVideoUrl, "");
        ArrayList<TtDailyAdValue> ttDailyAdValue = new ArrayList<>(ttDailyAdValueMap.values());
        this.saveOrUpdateBatch(ttDailyAdValue);
        return new PostResult();
    }

    private void getAdValue(String appId, String appType, Map<String, TtDailyAdValue> ttDailyAdValueMap, String ttBannerUrl, String ttVideoUrl, String ttIntUrl) {
        //不同平台拉取数据返回信息
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNotBlank(ttBannerUrl)) {
            String ttBannerBody = HttpRequest.get(ttBannerUrl).header("Content-Type", "application/json").cookie("sdessionid=87d318bccacce1a2b8643a68126ba575").execute().body();
            JSONArray bannerData = JSONObject.parseObject(ttBannerBody).getJSONArray("data");
            for (Object datum : bannerData) {
                TtDailyAdValue ttDailyAdValue = new TtDailyAdValue();
                JSONObject datumObject = JSONObject.parseObject(datum.toString());
                ttDailyAdValue.setWxDate(LocalDate.parse(datumObject.getString("date"), fmt));
                ttDailyAdValue.setWxAppId(appId);
                ttDailyAdValue.setWxAppType(appType);
                if (datumObject.getInteger("show") != 0) {
                    double div = NumberUtil.div(datumObject.getInteger("click") * 1000, datumObject.getInteger("show") * 10, 2);
                    ttDailyAdValue.setWxVideoClickrate(new BigDecimal(div));
                }
                ttDailyAdValue.setWxBannerIncome(datumObject.getBigDecimal("cost"));
                ttDailyAdValue.setWxBannerShow(datumObject.getInteger("show"));
                ttDailyAdValueMap.put(datumObject.getString("date") + "-" + appType, ttDailyAdValue);
            }
        }
        String ttVideoBody = HttpRequest.get(ttVideoUrl).header("Content-Type", "application/json").cookie("sdessionid=87d318bccacce1a2b8643a68126ba575").execute().body();
        JSONArray videoData = JSONObject.parseObject(ttVideoBody).getJSONArray("data");
        for (Object datum : videoData) {
            JSONObject datumObject = JSONObject.parseObject(datum.toString());
            TtDailyAdValue dateAdValue = ttDailyAdValueMap.get(datumObject.getString("date") + "-" + appType);
            if (dateAdValue != null) {
                if (datumObject.getInteger("show") != 0) {
                    double div = NumberUtil.div(datumObject.getInteger("click") * 1000, datumObject.getInteger("show") * 10, 2);
                    dateAdValue.setWxVideoClickrate(new BigDecimal(div));
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
        if (StringUtils.isNotBlank(ttIntUrl)) {
            String ttIntBody = HttpRequest.get(ttIntUrl).header("Content-Type", "application/json").cookie("sdessionid=87d318bccacce1a2b8643a68126ba575").execute().body();
            JSONArray intData = JSONObject.parseObject(ttIntBody).getJSONArray("data");
            for (Object datum : intData) {
                JSONObject datumObject = JSONObject.parseObject(datum.toString());
                TtDailyAdValue dateAdValue = ttDailyAdValueMap.get(datumObject.getString("date") + "-" + appType);
                if (datumObject.getInteger("show") != 0) {
                    double div = NumberUtil.div(datumObject.getInteger("click") * 1000, datumObject.getInteger("show") * 10, 2);
                    dateAdValue.setWxVideoClickrate(new BigDecimal(div));
                }
                dateAdValue.setWxIntIncome(datumObject.getBigDecimal("cost"));
                dateAdValue.setWxIntShow(datumObject.getInteger("show"));
            }
        }
        System.out.println(ttDailyAdValueMap.toString());
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
        ttDailyAdValueQueryWrapper.eq("wx_date", wxDate).eq("wx_appid", appId).eq("wx_app_type", appType);
        TtDailyAdValue tableContent = this.getOne(ttDailyAdValueQueryWrapper);
        //数据存在更新，不存在则新增
        if (tableContent != null) {
            UpdateWrapper<TtDailyAdValue> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("wx_date", wxDate).eq("wx_appid", appId).eq("wx_app_type", appType);
            return this.update(entity, updateWrapper);
        } else {
            return this.save(entity);
        }
    }

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<TtDailyAdValue> queryWrapper) {

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<TtDailyAdValue> deleteWrapper) {
        return false;
    }
}
