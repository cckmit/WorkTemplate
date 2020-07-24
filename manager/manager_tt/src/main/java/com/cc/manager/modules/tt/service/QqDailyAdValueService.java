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
public class QqDailyAdValueService extends BaseCrudService<TtDailyAdValue, TtDailyAdValueMapper> {

    private TtConfig ttConfig;
    private TtDailyValueService ttDailyValueService;

    @SneakyThrows
    public PostResult getAdData(JSONObject jsonObject) {
        PostResult postResult = new PostResult();
        String[] timeSplit = jsonObject.getString("times").split("~");
        String quid = jsonObject.getString("quid");
        String qTicket = jsonObject.getString("qticket");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = simpleDateFormat.parse(timeSplit[0]);
        Date endDate = simpleDateFormat.parse(timeSplit[1]);

        try {
            String appId = "1110459368";

            //今日头条广告数据请求
            String qqAdRequest = String.format(ttConfig.getQqAdRequest(), appId);

            String adData = HttpRequest.post(ttConfig.getQqAdUrl()).header("Content-Type", "application/json").cookie("quid=b171a71b368c2be5d1d0529a309788b4; qticket=4d15da1407d8e14cc3e1acdefcda9a67").body("{\"ftimeBegin\":20200723,\"ftimeEnd\":20200723,\"appid\":\"1110459368\",\"needSubPosData\":0,\"channelType\":0}").execute().body();

            JSONArray data = JSONObject.parseObject(adData).getJSONObject("data").getJSONArray("AdDataDailyList");
            for (Object datum : data) {
                System.out.println("我是data----"+datum.toString());
            }
            this.saveOrUpdateBatch(null);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            postResult.setCode(2);
            postResult.setMsg("头条广告数据拉取失败！");
        }

        return postResult;
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
