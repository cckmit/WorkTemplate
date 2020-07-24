package com.cc.manager.modules.tt.service;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.tt.config.TtConfig;
import com.cc.manager.modules.tt.entity.TtDailyAdValue;
import com.cc.manager.modules.tt.entity.TtDailyValue;
import com.cc.manager.modules.tt.entity.WxConfig;
import com.cc.manager.modules.tt.mapper.TtDailyAdValueMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
@DS("tt")
public class TtDailyAdValueService extends BaseCrudService<TtDailyAdValue, TtDailyAdValueMapper> {

    private TtConfig ttConfig;
    private TtDailyValueService ttDailyValueService;

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
                //解析不同平台数据
                Map<String, TtDailyAdValue> ttDailyAdValueMap = new HashMap<>(16);
                getAdValue(appId, "tt", ttDailyAdValueMap, ttBannerUrl, ttVideoUrl, ttIntUrl, sessionId);
                getAdValue(appId, "watermelon", ttDailyAdValueMap, watermelonBannerUrl, watermelonVideoUrl, "", sessionId);
                getAdValue(appId, "tikTok", ttDailyAdValueMap, "", tikTokVideoUrl, "", sessionId);
                getAdValue(appId, "ttExtreme", ttDailyAdValueMap, ttExtremeBannerUrl, ttExtremeVideoUrl, "", sessionId);
                getAdValue(appId, "tikTokExtreme", ttDailyAdValueMap, "", tikTokExtremeVideoUrl, "", sessionId);
                this.saveOrUpdateBatch(new ArrayList<>(ttDailyAdValueMap.values()));
            }
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            postResult.setCode(2);
            postResult.setMsg("头条广告数据拉取失败！");
        }

        return postResult;
    }

    private void getAdValue(String appId, String appType, Map<String, TtDailyAdValue> ttDailyAdValueMap, String ttBannerUrl, String ttVideoUrl, String ttIntUrl, String sessionId) {
        //不同平台拉取数据返回信息
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //Banner数据处理
        getAdValueBanner(appId, appType, ttDailyAdValueMap, ttBannerUrl, sessionId, fmt);
        //视频数据处理
        getAdValueVideo(appId, appType, ttDailyAdValueMap, ttVideoUrl, sessionId, fmt);
        //插屏数据处理
        getAdValueInt(appId, appType, ttDailyAdValueMap, ttIntUrl, sessionId, fmt);
    }

    private void getAdValueInt(String appId, String appType, Map<String, TtDailyAdValue> ttDailyAdValueMap, String ttIntUrl, String sessionId, DateTimeFormatter fmt) {
        if (StringUtils.isNotBlank(ttIntUrl)) {
            String ttIntBody = HttpRequest.get(ttIntUrl).header("Content-Type", "application/json").cookie("sdessionid=" + sessionId).execute().body();
            JSONArray intData = JSONObject.parseObject(ttIntBody).getJSONArray("data");
            if (intData != null) {
                for (Object datum : intData) {
                    JSONObject datumObject = JSONObject.parseObject(datum.toString());
                    TtDailyAdValue dateAdValue = ttDailyAdValueMap.get(datumObject.getString("date") + "-" + appType);
                    if (dateAdValue != null) {
                        if (datumObject.getInteger("show") != 0) {
                            dateAdValue.setWxIntClickrate(new BigDecimal(NumberUtil.div(datumObject.getInteger("click") * 1000, datumObject.getInteger("show") * 10, 2)));
                        }
                        dateAdValue.setWxIntIncome(datumObject.getBigDecimal("cost"));
                        dateAdValue.setWxIntClickCount(datumObject.getInteger("click"));
                        dateAdValue.setWxIntShow(datumObject.getInteger("show"));

                    } else {
                        TtDailyAdValue ttDailyAdValue = new TtDailyAdValue();
                        ttDailyAdValue.setWxDate(LocalDate.parse(datumObject.getString("date"), fmt));
                        ttDailyAdValue.setWxAppId(appId);
                        ttDailyAdValue.setWxAppType(appType);
                        if (datumObject.getInteger("show") != 0) {
                            ttDailyAdValue.setWxIntClickrate(new BigDecimal(NumberUtil.div(datumObject.getInteger("click") * 1000, datumObject.getInteger("show") * 10, 2)));
                        }
                        ttDailyAdValue.setWxIntIncome(datumObject.getBigDecimal("cost"));
                        ttDailyAdValue.setWxIntClickCount(datumObject.getInteger("click"));
                        ttDailyAdValue.setWxIntShow(datumObject.getInteger("show"));
                        ttDailyAdValueMap.put(datumObject.getString("date") + "-" + appType, ttDailyAdValue);
                    }
                }
            }
        }
    }

    private void getAdValueVideo(String appId, String appType, Map<String, TtDailyAdValue> ttDailyAdValueMap, String ttVideoUrl, String sessionId, DateTimeFormatter fmt) {
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
                        dateAdValue.setWxVideoClickCount(datumObject.getInteger("click"));
                        dateAdValue.setWxVideoShow(datumObject.getInteger("show"));
                    } else {
                        TtDailyAdValue ttDailyAdValue = new TtDailyAdValue();
                        ttDailyAdValue.setWxDate(LocalDate.parse(datumObject.getString("date"), fmt));
                        ttDailyAdValue.setWxAppId(appId);
                        ttDailyAdValue.setWxAppType(appType);
                        if (datumObject.getInteger("show") != 0) {
                            ttDailyAdValue.setWxVideoClickrate(new BigDecimal(NumberUtil.div(datumObject.getInteger("click") * 1000, datumObject.getInteger("show") * 10, 2)));
                        }
                        ttDailyAdValue.setWxVideoIncome(datumObject.getBigDecimal("cost"));
                        ttDailyAdValue.setWxVideoClickCount(datumObject.getInteger("click"));
                        ttDailyAdValue.setWxVideoShow(datumObject.getInteger("show"));
                        ttDailyAdValueMap.put(datumObject.getString("date") + "-" + appType, ttDailyAdValue);
                    }
                }
            }
        }
    }

    private void getAdValueBanner(String appId, String appType, Map<String, TtDailyAdValue> ttDailyAdValueMap, String ttBannerUrl, String sessionId, DateTimeFormatter fmt) {
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
                    ttDailyAdValue.setWxBannerClickCount(datumObject.getInteger("click"));
                    ttDailyAdValue.setWxBannerShow(datumObject.getInteger("show"));
                    ttDailyAdValueMap.put(datumObject.getString("date") + "-" + appType, ttDailyAdValue);
                }
            }
        }
    }

    /**
     * 批量保存
     *
     * @param entityList entityList
     * @param batchSize  batchSize
     * @return 保存结果
     */
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
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("dateValue", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            } else {
                queryWrapper.between("dateValue", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(2)), DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1)));
            }
            String appType = queryObject.getString("appType");
            queryWrapper.eq(StringUtils.isNotBlank(appType), "appType", appType);
            String appId = queryObject.getString("appId");
            queryWrapper.eq(StringUtils.isNotBlank(appId), "appId", appId);
        } else {
            queryWrapper.between("dateValue", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(2)), DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1)));
        }
        queryWrapper.orderByDesc("dateValue");

    }

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<TtDailyAdValue> entityList) {

        Map<String, String> ttDailyValueMap = new HashMap<>(16);
        //查询ttDailyValueService拼接活跃用户数
        getTtDailyValueMap(crudPageParam, ttDailyValueMap);
        //调用街机接口获取产品信息数据
        String wxConfigs = HttpRequest.get(ttConfig.getWxConfigApi()).execute().body();
        HashMap<String, WxConfig> wxConfigHashMap = new HashMap<>(16);
        //获取产品信息Map
        getWxConfigMap(wxConfigs, wxConfigHashMap);
        if (entityList != null) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            List<TtDailyAdValue> newEntityList = new ArrayList<>();
            //对entityList参数处理
            getNewEntityList(entityList, ttDailyValueMap, wxConfigHashMap, newEntityList);
            Map<String, TtDailyAdValue> sumTtDailyAdValueMap = new LinkedHashMap<>(16);
            if (StringUtils.equals("tt", queryObject.getString("platform"))) {
                //选择平台汇总进行数据求和
                dealSumTtDailyAdValueMap(newEntityList, sumTtDailyAdValueMap);
                List<TtDailyAdValue> ttDailyAdValueList = new ArrayList<>(sumTtDailyAdValueMap.values());
                //对ttDailyAdValueList参数处理
                dealTtDailyAdValueList(ttDailyAdValueList);
                entityList.clear();
                entityList.addAll(ttDailyAdValueList);
            } else {
                entityList.clear();
                entityList.addAll(newEntityList);
            }
        }
    }

    /**
     * entityList参数处理
     *
     * @param entityList      entityList
     * @param ttDailyValueMap ttDailyValueMap
     * @param wxConfigHashMap wxConfigHashMap
     * @param newEntityList   newEntityList
     */
    private void getNewEntityList(List<TtDailyAdValue> entityList, Map<String, String> ttDailyValueMap, HashMap<String, WxConfig> wxConfigHashMap, List<TtDailyAdValue> newEntityList) {
        for (TtDailyAdValue ttDailyAdValue : entityList) {
            //获取活跃用户数
            int activeUser = getActiveUser(ttDailyValueMap, ttDailyAdValue);
            ttDailyAdValue.setWxActive(activeUser);
            // 获取街机app信息
            WxConfig wxConfig = wxConfigHashMap.get(ttDailyAdValue.getWxAppId());
            if (wxConfig == null) {
                continue;
            } else {
                // 设置data产品信息
                ttDailyAdValue.setProgramType(wxConfig.getProgramType());
                ttDailyAdValue.setProductName(wxConfig.getProductName());
                ttDailyAdValue.setDdAppPlatform(wxConfig.getDdAppPlatform());
            }
            //设置广告收益
            ttDailyAdValue.setAdRevenue(ttDailyAdValue.getWxBannerIncome().add(ttDailyAdValue.getWxVideoIncome()).add(ttDailyAdValue.getWxIntIncome()));
            //设置VideoECPM
            if (ttDailyAdValue.getWxVideoShow() != 0) {
                ttDailyAdValue.setVideoECPM((ttDailyAdValue.getWxVideoIncome().divide(new BigDecimal(ttDailyAdValue.getWxVideoShow()),
                        5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
            }
            //设置BannerECPM
            if (ttDailyAdValue.getWxBannerShow() != 0) {
                ttDailyAdValue.setBannerECPM((ttDailyAdValue.getWxBannerIncome().divide(new BigDecimal(ttDailyAdValue.getWxBannerShow()),
                        5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
            }
            //设置插屏ECPM
            if (ttDailyAdValue.getWxIntShow() != 0) {
                ttDailyAdValue.setIntECPM((ttDailyAdValue.getWxIntIncome().divide(new BigDecimal(ttDailyAdValue.getWxIntShow()),
                        5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
            }
            //设置总收入
            ttDailyAdValue.setRevenueCount(ttDailyAdValue.getAdRevenue());
            newEntityList.add(ttDailyAdValue);
        }
    }

    /**
     * 汇总List参数处理
     *
     * @param ttDailyAdValueList ttDailyAdValueList
     */
    private void dealTtDailyAdValueList(List<TtDailyAdValue> ttDailyAdValueList) {
        for (TtDailyAdValue ttDailyAdValue : ttDailyAdValueList) {
            if (ttDailyAdValue.getWxVideoShow() != 0) {
                ttDailyAdValue.setWxVideoClickrate(new BigDecimal(NumberUtil.div(ttDailyAdValue.getWxVideoClickCount() * 1000, ttDailyAdValue.getWxVideoShow() * 10, 2)));
                ttDailyAdValue.setVideoECPM((ttDailyAdValue.getWxVideoIncome().divide(new BigDecimal(ttDailyAdValue.getWxVideoShow()),
                        5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
            }
            if (ttDailyAdValue.getWxBannerShow() != 0) {
                ttDailyAdValue.setWxBannerClickrate(new BigDecimal(NumberUtil.div(ttDailyAdValue.getWxBannerClickCount() * 1000, ttDailyAdValue.getWxBannerShow() * 10, 2)));
                ttDailyAdValue.setBannerECPM((ttDailyAdValue.getWxBannerIncome().divide(new BigDecimal(ttDailyAdValue.getWxBannerShow()),
                        5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
            }
            if (ttDailyAdValue.getWxIntShow() != 0) {
                ttDailyAdValue.setWxIntClickrate(new BigDecimal(NumberUtil.div(ttDailyAdValue.getWxIntClickCount() * 1000, ttDailyAdValue.getWxIntShow() * 10, 2)));
                ttDailyAdValue.setIntECPM((ttDailyAdValue.getWxIntIncome().divide(new BigDecimal(ttDailyAdValue.getWxIntShow()),
                        5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
            }
        }
    }

    /**
     * 获取产品活跃用户数
     *
     * @param ttDailyValueMap ttDailyValueMap
     * @param ttDailyAdValue  ttDailyAdValue
     * @return activeUser
     */
    private int getActiveUser(Map<String, String> ttDailyValueMap, TtDailyAdValue ttDailyAdValue) {
        String formatDate = ttDailyAdValue.getWxDate().format(DateTimeFormatter.BASIC_ISO_DATE);
        Map activeUsersMap = JSON.parseObject(ttDailyValueMap.get(formatDate + "-" + ttDailyAdValue.getWxAppId() + "-" + ttDailyAdValue.getWxAppType()));
        int activeUser = 0;
        if (activeUsersMap != null) {
            for (Object mapData : activeUsersMap.values()) {
                activeUser = activeUser + Integer.parseInt(mapData.toString());
            }
        }
        return activeUser;
    }

    /**
     * 平台汇总进行数据求和
     *
     * @param newEntityList        newEntityList
     * @param sumTtDailyAdValueMap sumTtDailyAdValueMap
     */
    private void dealSumTtDailyAdValueMap(List<TtDailyAdValue> newEntityList, Map<String, TtDailyAdValue> sumTtDailyAdValueMap) {
        for (TtDailyAdValue ttDailyAdValue : newEntityList) {
            LocalDate wxDate = ttDailyAdValue.getWxDate();
            String wxAppId = ttDailyAdValue.getWxAppId();
            TtDailyAdValue dailyAdValue = sumTtDailyAdValueMap.get(wxDate + "-" + wxAppId);
            if (dailyAdValue != null) {
                dailyAdValue.setWxActive(dailyAdValue.getWxActive() + ttDailyAdValue.getWxActive());
                dailyAdValue.setWxVideoShow(dailyAdValue.getWxVideoShow() + ttDailyAdValue.getWxVideoShow());
                dailyAdValue.setWxVideoClickCount(dailyAdValue.getWxVideoClickCount() + ttDailyAdValue.getWxVideoClickCount());
                dailyAdValue.setWxVideoIncome(dailyAdValue.getWxVideoIncome().add(ttDailyAdValue.getWxVideoIncome()));
                dailyAdValue.setWxBannerShow(dailyAdValue.getWxBannerShow() + ttDailyAdValue.getWxBannerShow());
                dailyAdValue.setWxBannerClickCount(dailyAdValue.getWxBannerClickCount() + ttDailyAdValue.getWxBannerClickCount());
                dailyAdValue.setWxBannerIncome(dailyAdValue.getWxBannerIncome().add(ttDailyAdValue.getWxBannerIncome()));
                dailyAdValue.setWxIntShow(dailyAdValue.getWxIntShow() + ttDailyAdValue.getWxIntShow());
                dailyAdValue.setWxIntClickCount(dailyAdValue.getWxIntClickCount() + ttDailyAdValue.getWxIntClickCount());
                dailyAdValue.setWxIntIncome(dailyAdValue.getWxIntIncome().add(ttDailyAdValue.getWxIntIncome()));
                dailyAdValue.setAdRevenue(dailyAdValue.getAdRevenue().add(ttDailyAdValue.getAdRevenue()));
                dailyAdValue.setRevenueCount(dailyAdValue.getRevenueCount().add(ttDailyAdValue.getRevenueCount()));
            } else {
                ttDailyAdValue.setWxAppType("ttCount");
                sumTtDailyAdValueMap.put(wxDate + "-" + wxAppId, ttDailyAdValue);
            }
        }
    }

    /**
     * 获取产品信息Map
     *
     * @param wxConfigs       wxConfigs
     * @param wxConfigHashMap wxConfigHashMap
     */
    private void getWxConfigMap(String wxConfigs, HashMap<String, WxConfig> wxConfigHashMap) {
        JSONArray data = JSONObject.parseObject(wxConfigs).getJSONArray("data");
        for (Object datum : data) {
            WxConfig wxConfig = JSONUtil.toBean(datum.toString(), WxConfig.class);
            wxConfigHashMap.put(wxConfig.getId(), wxConfig);
        }
    }

    /**
     * 获取时间-产品对应的活跃用户数
     *
     * @param crudPageParam   crudPageParam
     * @param ttDailyValueMap ttDailyValueMap
     */
    private void getTtDailyValueMap(CrudPageParam crudPageParam, Map<String, String> ttDailyValueMap) {
        String beginDate = "", endDate = "";
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                beginDate = timeRangeArray[0].trim();
                endDate = timeRangeArray[1].trim();
            } else {
                beginDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(2));
                endDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
            }
        }
        QueryWrapper<TtDailyValue> queryTtDailyValue = new QueryWrapper<>();
        List<TtDailyValue> ttDailyValues = ttDailyValueService.list(queryTtDailyValue.between("dateNum", beginDate.replace("-", ""), endDate.replace("-", "")));
        for (TtDailyValue ttDailyValue : ttDailyValues) {
            ttDailyValueMap.put(ttDailyValue.getDateNum() + "-" + ttDailyValue.getAppId() + "-" + ttDailyValue.getAppType(), ttDailyValue.getActiveUsers());
        }
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<TtDailyAdValue> deleteWrapper) {
        return false;
    }

    @Autowired
    public void setTtConfig(TtConfig ttConfig) {
        this.ttConfig = ttConfig;
    }

    @Autowired
    public void setTtDailyValueService(TtDailyValueService ttDailyValueService) {
        this.ttDailyValueService = ttDailyValueService;
    }

}
