package com.cc.manager.modules.tt.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.tt.config.TtConfig;
import com.cc.manager.modules.tt.entity.QqDailyAdValue;
import com.cc.manager.modules.tt.entity.TtDailyAdValue;
import com.cc.manager.modules.tt.entity.TtDailyValue;
import com.cc.manager.modules.tt.entity.WxConfig;
import com.cc.manager.modules.tt.mapper.QqDailyAdValueMapper;
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
 * @since 2020-07-27
 */
@Service
@DS("tt")
public class QqDailyAdValueService extends BaseCrudService<QqDailyAdValue, QqDailyAdValueMapper> {

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

            //qq广告数据请求
            String qqAdRequest = String.format(ttConfig.getQqAdRequest(), appId);

            String adData = HttpRequest.post(ttConfig.getQqAdUrl()).header("Content-Type", "application/json").cookie("quid=b171a71b368c2be5d1d0529a309788b4; qticket=4d15da1407d8e14cc3e1acdefcda9a67").body("{\"ftimeBegin\":20200723,\"ftimeEnd\":20200723,\"appid\":\"1110459368\",\"needSubPosData\":0,\"channelType\":0}").execute().body();

            JSONArray data = JSONObject.parseObject(adData).getJSONObject("data").getJSONArray("AdDataDailyList");
            Map<String, QqDailyAdValue> map = new HashMap<>(16);
            for (Object datum : data) {
                System.out.println("我是data----" + datum.toString());

                JSONObject object = JSONObject.parseObject(datum.toString());
                String productId = object.getString("appid");
                String time = object.getString("ftime");
                String adType = object.getString("adType");
                if (StringUtils.equals("1", adType)) {
                    QqDailyAdValue qqDailyAdValue = map.get(time + "-" + productId);
                    if (qqDailyAdValue != null) {
                        qqDailyAdValue.setWxBannerShow(Integer.parseInt(object.getString("exposure")));
                        qqDailyAdValue.setWxBannerClickCount(Integer.parseInt(object.getString("click")));
                        qqDailyAdValue.setWxBannerClickrate(new BigDecimal(object.getString("clickRate")).setScale(2, RoundingMode.HALF_UP));
                        qqDailyAdValue.setWxBannerIncome(new BigDecimal(object.getString("revenue")).setScale(2, RoundingMode.HALF_UP));
                        qqDailyAdValue.setBannerECPM(new BigDecimal(object.getString("cpm")).setScale(2, RoundingMode.HALF_UP));

                    } else {
                        QqDailyAdValue newDailyAdValue = new QqDailyAdValue();
                        newDailyAdValue.setWxAppId(productId);
                        newDailyAdValue.setWxDate(LocalDate.parse(time));
                        newDailyAdValue.setWxBannerShow(Integer.parseInt(object.getString("exposure")));
                        newDailyAdValue.setWxBannerClickCount(Integer.parseInt(object.getString("click")));
                        newDailyAdValue.setWxBannerClickrate(new BigDecimal(object.getString("clickRate")).setScale(2, RoundingMode.HALF_UP));
                        newDailyAdValue.setWxBannerIncome(new BigDecimal(object.getString("revenue")).setScale(2, RoundingMode.HALF_UP));
                        newDailyAdValue.setBannerECPM(new BigDecimal(object.getString("cpm")).setScale(2, RoundingMode.HALF_UP));
                        map.put(time + "-" + productId,newDailyAdValue);
                    }
                }
                if (StringUtils.equals("2", adType)) {
                    QqDailyAdValue qqDailyAdValue = map.get(time + "-" + productId);
                    if (qqDailyAdValue != null) {
                        qqDailyAdValue.setWxVideoShow(Integer.parseInt(object.getString("exposure")));
                        qqDailyAdValue.setWxVideoClickCount(Integer.parseInt(object.getString("click")));
                        qqDailyAdValue.setWxVideoClickrate(new BigDecimal(object.getString("clickRate")).setScale(2, RoundingMode.HALF_UP));
                        qqDailyAdValue.setWxVideoIncome(new BigDecimal(object.getString("revenue")).setScale(2, RoundingMode.HALF_UP));
                        qqDailyAdValue.setVideoECPM(new BigDecimal(object.getString("cpm")).setScale(2, RoundingMode.HALF_UP));

                    } else {
                        QqDailyAdValue newDailyAdValue = new QqDailyAdValue();
                        newDailyAdValue.setWxAppId(productId);
                        newDailyAdValue.setWxDate(LocalDate.parse(time));
                        newDailyAdValue.setWxVideoShow(Integer.parseInt(object.getString("exposure")));
                        newDailyAdValue.setWxVideoClickCount(Integer.parseInt(object.getString("click")));
                        newDailyAdValue.setWxVideoClickrate(new BigDecimal(object.getString("clickRate")).setScale(2, RoundingMode.HALF_UP));
                        newDailyAdValue.setWxVideoIncome(new BigDecimal(object.getString("revenue")).setScale(2, RoundingMode.HALF_UP));
                        newDailyAdValue.setVideoECPM(new BigDecimal(object.getString("cpm")).setScale(2, RoundingMode.HALF_UP));
                        map.put(time + "-" + productId,newDailyAdValue);

                    }
                }
            }
            this.saveOrUpdateBatch(map.values());
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            postResult.setCode(2);
            postResult.setMsg("头条广告数据拉取失败！");
        }

        return postResult;
    }

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<QqDailyAdValue> queryWrapper) {
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

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<QqDailyAdValue> deleteWrapper) {
        return false;
    }

    /**
     * 批量保存
     *
     * @param entityList entityList
     * @param batchSize  batchSize
     * @return 保存结果
     */
    @Override
    public boolean saveOrUpdateBatch(Collection<QqDailyAdValue> entityList, int batchSize) {
        try {
            for (QqDailyAdValue qqDailyAdValue : entityList) {
                this.saveOrUpdate(qqDailyAdValue);
            }
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
        return true;
    }

    @Override
    public boolean saveOrUpdate(QqDailyAdValue entity) {
        LocalDate wxDate = entity.getWxDate();
        String appId = entity.getWxAppId();
        String appType = entity.getWxAppType();
        entity.setInsertTime(LocalDateTime.now());
        QueryWrapper<QqDailyAdValue> qqDailyAdValueQueryWrapper = new QueryWrapper<>();
        qqDailyAdValueQueryWrapper.eq("dateValue", wxDate).eq("appId", appId).eq("appType", appType);
        QqDailyAdValue qqDailyAdValue = this.getOne(qqDailyAdValueQueryWrapper);
        //数据存在更新，不存在则新增
        if (qqDailyAdValue != null) {
            UpdateWrapper<QqDailyAdValue> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("dateValue", wxDate).eq("appId", appId).eq("appType", appType);
            return this.update(entity, updateWrapper);
        } else {
            return this.save(entity);
        }
    }


    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<QqDailyAdValue> entityList) {

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
            //  getNewEntityList(entityList, ttDailyValueMap, wxConfigHashMap, newEntityList);
            Map<String, TtDailyAdValue> sumTtDailyAdValueMap = new LinkedHashMap<>(16);
            if (StringUtils.equals("tt", queryObject.getString("platform"))) {
                //选择平台汇总进行数据求和
                dealSumTtDailyAdValueMap(newEntityList, sumTtDailyAdValueMap);
                List<TtDailyAdValue> ttDailyAdValueList = new ArrayList<>(sumTtDailyAdValueMap.values());
                //对ttDailyAdValueList参数处理
                //  dealTtDailyAdValueList(ttDailyAdValueList);
                entityList.clear();
                // entityList.addAll(ttDailyAdValueList);
            } else {
                entityList.clear();
                //    entityList.addAll(newEntityList);
            }
        }
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

    @Autowired
    public void setTtConfig(TtConfig ttConfig) {
        this.ttConfig = ttConfig;
    }

    @Autowired
    public void setTtDailyValueService(TtDailyValueService ttDailyValueService) {
        this.ttDailyValueService = ttDailyValueService;
    }

}
