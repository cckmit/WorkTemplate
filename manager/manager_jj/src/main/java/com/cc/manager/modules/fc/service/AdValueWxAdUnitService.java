package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.entity.AdValueWxAdUnit;
import com.cc.manager.modules.fc.entity.MiniGame;
import com.cc.manager.modules.fc.mapper.AdValueWxAdUnitMapper;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.service.WxConfigService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 自有广告查询Service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-03 20:22
 */
@Service
@DS("fc")
public class AdValueWxAdUnitService extends BaseStatsService<AdValueWxAdUnit, AdValueWxAdUnitMapper> {

    /**
     * 分组方式 — 时间
     */
    private static final String TIME = "TIME";
    /**
     * 分组方式 — 产品App
     */
    private static final String PRODUCT_APP = "PRODUCT_APP";
    /**
     * 分组方式 — 广告类型
     */
    private static final String AD_TYPE = "AD_TYPE";
    /**
     * 分组方式 — 广告位
     */
    private static final String AD_SPACE = "AD_SPACE";
    /**
     * 分组方式 — 产品+广告类型
     */
    private static final String PRODUCT_APP_TYPE = "PRODUCT_APP_TYPE";
    /**
     * 分组方式 — 产品+广告位
     */
    private static final String PRODUCT_APP_SPACE = "PRODUCT_APP_SPACE";

    private MiniGameService miniGameService;
    private WxConfigService wxConfigService;

    /**
     * 如果getPage中的分组和排序方式不能满足您的要求，请先调用entityQueryWrapper.clear()方法，然后重写自己的wrapper
     *
     * @param statsListParam  查询参数
     * @param queryWrapper    查询wrapper
     * @param statsListResult 查询返回值封装对象
     */
    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<AdValueWxAdUnit> queryWrapper, StatsListResult statsListResult) {

        queryWrapper.eq("appSource", "JJ");
        // 1.1、初始化查询字段列表
        List<String> selectList = Lists.newArrayList("date,sum(reqSuccCount) as reqSuccCount", "sum(exposureCount) as exposureCount",
                "sum(clickCount) as clickCount", "sum(income) / 100 as income", " round(sum(ecpm) / 100,2) as ecpm");
        // 1.2、初始化分组字段列表
        List<String> groupByList = Lists.newArrayList("date");
        // 1.3、初始化数据展示列表
        List<String> showColumnList = Lists.newArrayList("date", "reqSuccCount", "exposureCount", "exposureRate",
                "clickCount", "clickRate", "income", "ecpm", "clickIncome");

        JSONObject queryObject = statsListParam.getQueryObject();
        // 2、初始化日期
        this.initTimeParam(queryObject);
        queryWrapper.between("DATE(date)", queryObject.getString("beginDate"), queryObject.getString("endDate"));

        // 3、更新数据汇总方式，默认按照推广app，即广告内容ID分组
        this.updateParamByGroupType(queryObject, selectList, groupByList, showColumnList);

        // 4、其它可以在查询时过滤的条件赋值
        String appId = queryObject.getString("appId");
        queryWrapper.eq(StringUtils.isNotBlank(appId), "appId", appId);
        String slotId = queryObject.getString("slotId");
        queryWrapper.eq(StringUtils.isNotBlank(slotId), "slotId", slotId);
        String adUnitName = queryObject.getString("adUnitName");
        queryWrapper.eq(StringUtils.isNotBlank(adUnitName), "adUnitName", adUnitName);

        // 将查询字段和分组字段赋值给查询条件
        queryWrapper.select(selectList.toArray(new String[0]));
        queryWrapper.groupBy(groupByList.toArray(new String[0]));
        // 查询展示列表
        statsListResult.setShowColumn(showColumnList);
        statsListParam.setLimit(Integer.MAX_VALUE);
    }

    public List<AdValueWxAdUnit> list(String appId, String beginTime, String endTime) {
        QueryWrapper<AdValueWxAdUnit> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("appId", "SUM(income) AS screenIncome", "date");
        queryWrapper.between("date", beginTime, endTime);
        queryWrapper.eq(StringUtils.isNotBlank(appId), "appId", appId);
        queryWrapper.eq("slotId", "3030046789020061");
        queryWrapper.eq("appSource", "JJ");
        queryWrapper.groupBy("date", "appId");
        return this.list(queryWrapper);
    }

    /**
     * 获取查询日期范围和日期汇总方式
     *
     * @param queryObject 查询数据对象
     */
    private void initTimeParam(JSONObject queryObject) {
        String beginDate = null, endDate = null;
        // 先从查询条件获取
        String timeRange = queryObject.getString("timeRange");
        if (StringUtils.isNotBlank(timeRange)) {
            String[] timeRangeArray = StringUtils.split(timeRange, "~");
            beginDate = StringUtils.replace(timeRangeArray[0].trim(), "-", "");
            endDate = StringUtils.replace(timeRangeArray[1].trim(), "-", "");
        }
        // 如果查询日期为空，赋默认日期为当日
        if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            LocalDateTime localDateTime = LocalDateTime.now();
            String currentDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            beginDate = endDate = currentDate = "20200510";
        }

        queryObject.put("beginDate", beginDate);
        queryObject.put("endDate", endDate);

    }

    /**
     * 根据分组条件更新相关数据
     *
     * @param queryObject    查询参数
     * @param selectList     查询列表
     * @param groupByList    分组列表
     * @param showColumnList 展示数据列表
     */
    private void updateParamByGroupType(JSONObject queryObject, List<String> selectList, List<String> groupByList, List<String> showColumnList) {
        String groupType = queryObject.getString("groupType");
        // 如果分组方式为空，默认按推广app分组
        if (StringUtils.isBlank(groupType)) {
            groupType = PRODUCT_APP_SPACE;
            queryObject.put("groupType", groupType);
        }
        switch (groupType) {
            case PRODUCT_APP:
                selectList.add("appId as appId");
                groupByList.add("appId");
                showColumnList.add("appId");
                showColumnList.add("productName");
                break;
            case AD_TYPE:
                selectList.add("slotId");
                groupByList.add("slotId");
                showColumnList.add("slotId");
                break;
            case AD_SPACE:
                selectList.add("adUnitName");
                groupByList.add("adUnitName");
                showColumnList.add("adUnitName");
                break;
            case PRODUCT_APP_TYPE:
                selectList.add("appId");
                selectList.add("slotId");
                groupByList.add("appId");
                groupByList.add("slotId");
                showColumnList.add("appId");
                showColumnList.add("productName");
                showColumnList.add("slotId");
                break;
            case PRODUCT_APP_SPACE:
                selectList.add("appId");
                selectList.add("adUnitName");
                selectList.add("slotId");
                groupByList.add("appId");
                groupByList.add("adUnitName");
                groupByList.add("slotId");
                showColumnList.add("appId");
                showColumnList.add("productName");
                showColumnList.add("adUnitName");
                showColumnList.add("slotId");
                break;
            default:
                break;
        }
    }

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param statsListParam 查询参数
     * @param entityList     查询数据对象列表
     * @return 合计数据
     */
    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<AdValueWxAdUnit> entityList, StatsListResult statsListResult) {
        JSONObject queryObject = statsListParam.getQueryObject();
        Map<String, AdValueWxAdUnit> adValueWxAdUnitMap = Maps.newHashMap();
        AdValueWxAdUnit totalAdValueWxAdUnit = new AdValueWxAdUnit();
        for (AdValueWxAdUnit adValueWxAdUnit : entityList) {
            // key用来汇判断需要汇总的数据总数据
            String key;
            switch (queryObject.getString("groupType")) {
                case TIME:
                    key = this.rebuildGroupByTime(adValueWxAdUnit);
                    statsListResult.setDetailGroupBy(TIME);
                    break;
                case PRODUCT_APP:
                    key = this.rebuildGroupByProductApp(adValueWxAdUnit);
                    statsListResult.setDetailGroupBy(PRODUCT_APP_SPACE);
                    break;
                case AD_TYPE:
                    key = this.rebuildGroupByAdType(adValueWxAdUnit);
                    statsListResult.setDetailGroupBy(PRODUCT_APP_SPACE);
                    break;
                case AD_SPACE:
                    key = this.rebuildGroupByAdSpace(adValueWxAdUnit);
                    statsListResult.setDetailGroupBy(PRODUCT_APP_SPACE);
                    break;
                case PRODUCT_APP_TYPE:
                    key = this.rebuildGroupByProductAppType(adValueWxAdUnit);
                    statsListResult.setDetailGroupBy(PRODUCT_APP_SPACE);
                    break;
                case PRODUCT_APP_SPACE:
                    key = this.rebuildGroupByProductAppSpace(adValueWxAdUnit);
                    statsListResult.setDetailGroupBy(PRODUCT_APP_SPACE);
                    break;
                default:
                    continue;
            }
            if (adValueWxAdUnitMap.containsKey(key)) {
                AdValueWxAdUnit tempValue = adValueWxAdUnitMap.get(key);
                tempValue.merge(adValueWxAdUnit);
            } else {
                adValueWxAdUnitMap.put(key, adValueWxAdUnit);
            }
            totalAdValueWxAdUnit.merge(adValueWxAdUnit);
        }
        entityList.clear();
        entityList.addAll(adValueWxAdUnitMap.values());
        this.calculateRate(entityList);

        // 处理汇总数据
        totalAdValueWxAdUnit.setDate("汇总");
        // 计算比例
        totalAdValueWxAdUnit.calculateRate();
        // 汇总数据没有详情
        totalAdValueWxAdUnit.setHaveDetail(false);
        return JSONObject.parseObject(JSONObject.toJSONString(totalAdValueWxAdUnit));

    }

    /**
     * 根据时间分组数据处理
     *
     * @param adValueWxAdUnit 广告数据
     * @return 数据汇总key
     */
    private String rebuildGroupByTime(AdValueWxAdUnit adValueWxAdUnit) {
        adValueWxAdUnit.setHaveDetail(false);
        return adValueWxAdUnit.getDate();
    }

    /**
     * 根据产品分组处理数据
     *
     * @param adValueWxAdUnit 广告数据
     * @return 数据汇总key
     */
    private String rebuildGroupByProductApp(AdValueWxAdUnit adValueWxAdUnit) {
        WxConfig cacheEntity = this.wxConfigService.getCacheEntity(WxConfig.class, adValueWxAdUnit.getAppId());
        if (cacheEntity != null) {
            //fc数据赋值展示数据
            adValueWxAdUnit.setProductName(cacheEntity.getProductName());
        } else {
            MiniGame miniGame = this.miniGameService.getCacheEntity(MiniGame.class, adValueWxAdUnit.getAppId());
            if (miniGame != null) {
                adValueWxAdUnit.setProductName(miniGame.getGameName());
            }
        }
        adValueWxAdUnit.setHaveDetail(true);
        return adValueWxAdUnit.getDate() + "_" + adValueWxAdUnit.getAppId();
    }

    /**
     * 根据广告类型分组处理数据
     *
     * @param adValueWxAdUnit 广告数据
     * @return 数据汇总key
     */
    private String rebuildGroupByAdType(AdValueWxAdUnit adValueWxAdUnit) {
        return adValueWxAdUnit.getDate() + "_" + adValueWxAdUnit.getSlotId();
    }

    /**
     * 根据广告位分组处理数据
     *
     * @param adValueWxAdUnit 广告数据
     * @return 数据汇总key
     */
    private String rebuildGroupByAdSpace(AdValueWxAdUnit adValueWxAdUnit) {
        adValueWxAdUnit.setHaveDetail(true);
        return adValueWxAdUnit.getDate() + "_" + adValueWxAdUnit.getAdUnitName();
    }

    /**
     * 根据产品+广告类型分组处理数据
     *
     * @param adValueWxAdUnit 广告数据
     * @return 数据汇总key
     */
    private String rebuildGroupByProductAppType(AdValueWxAdUnit adValueWxAdUnit) {
        WxConfig cacheEntity = this.wxConfigService.getCacheEntity(WxConfig.class, adValueWxAdUnit.getAppId());
        if (cacheEntity != null) {
            //fc数据赋值展示数据
            adValueWxAdUnit.setProductName(cacheEntity.getProductName());
        } else {
            MiniGame miniGame = this.miniGameService.getCacheEntity(MiniGame.class, adValueWxAdUnit.getAppId());
            if (miniGame != null) {
                adValueWxAdUnit.setProductName(miniGame.getGameName());
            }
        }
        adValueWxAdUnit.setHaveDetail(true);
        return adValueWxAdUnit.getDate() + "_" + adValueWxAdUnit.getAppId() + "_" + adValueWxAdUnit.getSlotId();
    }

    /**
     * 根据产品+广告位分组处理数据
     *
     * @param adValueWxAdUnit 广告数据
     * @return 数据汇总key
     */
    private String rebuildGroupByProductAppSpace(AdValueWxAdUnit adValueWxAdUnit) {
        WxConfig cacheEntity = this.wxConfigService.getCacheEntity(WxConfig.class, adValueWxAdUnit.getAppId());
        if (cacheEntity != null) {
            //fc数据赋值展示数据
            adValueWxAdUnit.setProductName(cacheEntity.getProductName());
        } else {
            MiniGame miniGame = this.miniGameService.getCacheEntity(MiniGame.class, adValueWxAdUnit.getAppId());
            if (miniGame != null) {
                adValueWxAdUnit.setProductName(miniGame.getGameName());
            }
        }
        adValueWxAdUnit.setHaveDetail(false);
        return adValueWxAdUnit.getDate() + "_" + adValueWxAdUnit.getAppId() + "_" + adValueWxAdUnit.getSlotId() + "_" + adValueWxAdUnit.getAdUnitName();
    }

    /**
     * 计算比业务比例数据
     *
     * @param adValueList 广告数据列表
     */
    private void calculateRate(Collection<AdValueWxAdUnit> adValueList) {
        adValueList.forEach(AdValueWxAdUnit::calculateRate);
    }

    /**
     * 查询插屏收入汇总数据
     *
     * @return List
     */
    List<AdValueWxAdUnit> queryScreenIncomeByDate(String beginTime, String endTime) {
        QueryWrapper<AdValueWxAdUnit> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("SUM(income) AS screenIncome", "DATE");
        queryWrapper.between("DATE(DATE)", beginTime, endTime);
        queryWrapper.eq("slotID", "3030046789020061").eq("appSource", "JJ");
        queryWrapper.groupBy("DATE");
        return this.list(queryWrapper);
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
