package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.entity.AdValue;
import com.cc.manager.modules.fc.mapper.AdValueMapper;
import com.cc.manager.modules.jj.entity.ConfigAdContent;
import com.cc.manager.modules.jj.entity.ConfigAdPosition;
import com.cc.manager.modules.jj.entity.ConfigAdSpace;
import com.cc.manager.modules.jj.entity.ConfigAdType;
import com.cc.manager.modules.jj.service.ConfigAdContentService;
import com.cc.manager.modules.jj.service.ConfigAdPositionService;
import com.cc.manager.modules.jj.service.ConfigAdSpaceService;
import com.cc.manager.modules.jj.service.ConfigAdTypeService;
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
import java.util.Objects;

/**
 * 自有广告查询Service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-03 20:22
 */
@Service
@DS("fc")
public class AdValueService extends BaseStatsService<AdValue, AdValueMapper> {

    /**
     * 分组方式 — 推广App
     */
    private static final String TARGET_APP = "TARGET_APP";
    /**
     * 分组方式 — 运营App
     */
    private static final String PRODUCT_APP = "PRODUCT_APP";
    /**
     * 分组方式 — 广告位置
     */
    private static final String AD_POSITION = "AD_POSITION";
    /**
     * 分组方式 — 广告位
     */
    private static final String AD_SPACE = "AD_SPACE";
    /**
     * 分组方式 — 广告内容
     */
    private static final String AD_CONTENT = "AD_CONTENT";
    /**
     * 分组方式 — 推广产品+位置
     */
    private static final String TARGET_APP_POSITION = "TARGET_APP_POSITION";
    /**
     * 分组方式 — 位置+内容
     */
    private static final String POSITION_CONTENT = "POSITION_CONTENT";
    /**
     * 分组方式 — 推广产品+位置+内容
     */
    private static final String TARGET_APP_POSITION_CONTENT = "TARGET_APP_POSITION_CONTENT";
    private ConfigAdPositionService configAdPositionService;
    private ConfigAdSpaceService configAdSpaceService;
    private ConfigAdContentService configAdContentService;
    private ConfigAdTypeService configAdTypeService;

    /**
     * 如果getPage中的分组和排序方式不能满足您的要求，请先调用entityQueryWrapper.clear()方法，然后重写自己的wrapper
     *
     * @param statsListParam  查询参数
     * @param queryWrapper    查询wrapper
     * @param statsListResult 查询返回值封装对象
     */
    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<AdValue> queryWrapper, StatsListResult statsListResult) {
        // 1.1、初始化查询字段列表
        List<String> selectList = Lists.newArrayList("sum(showNum) as showNum", "sum(clickNum) as clickNum",
                "sum(promoteShowNum) as promoteShowNum", "sum(promoteClickNum) as promoteClickNum",
                "sum(targetShowNum) as targetShowNum");
        // 1.2、初始化分组字段列表
        List<String> groupByList = Lists.newArrayList("timeValue");
        // 1.3、初始化数据展示列表
        List<String> showColumnList = Lists.newArrayList("timeValue", "showNum", "clickNum", "clickRate",
                "promoteShowNum", "promoteClickNum", "promoteClickRate", "targetShowNum");

        JSONObject queryObject = statsListParam.getQueryObject();
        // 2、初始化日期和时间汇总方式
        this.initTimeParam(queryObject);
        selectList.add("left(hourNum, " + queryObject.getString("timeGroupType") + ") as timeValue");
        queryWrapper.between("left(hourNum, 8)", queryObject.getString("beginDate"), queryObject.getString("endDate"));

        // 3、更新数据汇总方式，默认按照推广app，即广告内容ID分组
        this.updateParamByGroupType(queryObject, selectList, groupByList, showColumnList);

        // 4、其它可以在查询时过滤的条件赋值
        String productAppId = queryObject.getString("productAppId");
        queryWrapper.eq(StringUtils.isNotBlank(productAppId), "appId", productAppId);
        String adPositionId = queryObject.getString("adPositionId");
        queryWrapper.eq(StringUtils.isNotBlank(adPositionId), "adPositionId", adPositionId);
        String adSpaceId = queryObject.getString("adSpaceId");
        queryWrapper.eq(StringUtils.isNotBlank(adSpaceId), "adSpaceId", adSpaceId);
        // 默认查询排除微信
        queryWrapper.gt("adContentId", 0);


        // 将查询字段和分组字段赋值给查询条件
        queryWrapper.select(selectList.toArray(new String[0]));
        queryWrapper.groupBy(groupByList.toArray(new String[0]));
        // 查询展示列表
        statsListResult.setShowColumn(showColumnList);
        //
        statsListParam.setLimit(Integer.MAX_VALUE);
    }

    /**
     * 获取查询日期范围和日期汇总方式
     *
     * @param queryObject 查询数据对象
     */
    private void initTimeParam(JSONObject queryObject) {
        String beginDate = null, endDate = null, timeGroupType;
        // 先从查询条件获取
        String timeRange = queryObject.getString("timeRange");
        if (StringUtils.isNotBlank(timeRange)) {
            String[] timeRangeArray = StringUtils.split(timeRange, "~");
            beginDate = StringUtils.replace(timeRangeArray[0].trim(), "-", "");
            endDate = StringUtils.replace(timeRangeArray[1].trim(), "-", "");
        }
        timeGroupType = queryObject.getString("timeGroupType");

        // 如果查询日期为空，赋默认日期为当日
        if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            LocalDateTime localDateTime = LocalDateTime.now();
            String currentDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            beginDate = endDate = currentDate = "20200508";
        }

        // 如果汇总方式为空，默认按日分组，即取数据值前8位
        if (StringUtils.isBlank(timeGroupType)) {
            timeGroupType = "8";
        }

        queryObject.put("beginDate", beginDate);
        queryObject.put("endDate", endDate);
        queryObject.put("timeGroupType", timeGroupType);
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
            groupType = TARGET_APP;
            queryObject.put("groupType", groupType);
        }
        switch (groupType) {
            case TARGET_APP:
                selectList.add("adContentId");
                groupByList.add("adContentId");
                showColumnList.add("targetAppName");
                break;
            case PRODUCT_APP:
                selectList.add("appId as productAppId");
                groupByList.add("appId");
                showColumnList.add("productAppName");
                break;
            case AD_POSITION:
                selectList.add("adPositionId");
                groupByList.add("adPositionId");
                showColumnList.add("adPositionName");
                break;
            case AD_SPACE:
                selectList.add("adSpaceId");
                groupByList.add("adSpaceId");
                showColumnList.add("adSpaceName");
                break;
            case AD_CONTENT:
                selectList.add("adContentId");
                groupByList.add("adContentId");
                showColumnList.add("targetAppName");
                showColumnList.add("adContentName");
                break;
            case TARGET_APP_POSITION:
                selectList.add("adPositionId");
                selectList.add("adContentId");
                groupByList.add("adPositionId");
                groupByList.add("adContentId");
                showColumnList.add("adPositionName");
                showColumnList.add("targetAppName");
                break;
            case TARGET_APP_POSITION_CONTENT:
                selectList.add("adPositionId");
                selectList.add("adContentId");
                groupByList.add("adPositionId");
                groupByList.add("adContentId");
                showColumnList.add("adPositionName");
                showColumnList.add("adContentName");
                showColumnList.add("targetAppName");
                break;
            case POSITION_CONTENT:
                selectList.add("adPositionId");
                selectList.add("adContentId");
                groupByList.add("adPositionId");
                groupByList.add("adContentId");
                showColumnList.add("adPositionName");
                showColumnList.add("adContentName");
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
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<AdValue> entityList, StatsListResult statsListResult) {
        JSONObject queryObject = statsListParam.getQueryObject();
        Map<String, AdValue> adValueMap = Maps.newHashMap();
        AdValue totalAdValue = new AdValue();

        for (AdValue adValue : entityList) {
            // 将yyyyMMdd(HH)格式的时间处理成yyyy-MM-dd( HH)格式
            String timeValue = StringUtils.substring(adValue.getTimeValue(), 0, 4) + "-" + StringUtils.substring(adValue.getTimeValue(), 4, 6) + "-" + StringUtils.substring(adValue.getTimeValue(), 6, 8);
            if (StringUtils.equals("10", queryObject.getString("timeGroupType"))) {
                timeValue += " " + StringUtils.substring(adValue.getTimeValue(), 8, 10);
            }
            adValue.setTimeValue(timeValue);

            // 通过广告位ID关联的推广appId参数过滤查询条件中的推广App参数
            ConfigAdContent configAdContent = this.configAdContentService.getCacheEntity(ConfigAdContent.class, String.valueOf(adValue.getAdContentId()));
            String queryParamTargetAppId = queryObject.getString("targetAppId");
            if (Objects.nonNull(configAdContent)) {
                if (StringUtils.isNotBlank(queryParamTargetAppId) && !StringUtils.equals(configAdContent.getTargetAppId(), queryParamTargetAppId)) {
                    continue;
                }
            }

            // key用来汇判断需要汇总的数据总数据
            String key;
            switch (queryObject.getString("groupType")) {
                case TARGET_APP:
                    key = this.rebuildGroupByTargetApp(adValue, configAdContent);
                    statsListResult.setDetailGroupBy(TARGET_APP_POSITION_CONTENT);
                    break;
                case PRODUCT_APP:
                    key = this.rebuildGroupByProductApp(adValue);
                    statsListResult.setDetailGroupBy(TARGET_APP);
                    break;
                case AD_POSITION:
                    key = this.rebuildGroupByAdPosition(adValue);
                    statsListResult.setDetailGroupBy(TARGET_APP_POSITION_CONTENT);
                    break;
                case AD_SPACE:
                    key = this.rebuildGroupByAdSpace(adValue);
                    statsListResult.setDetailGroupBy(AD_CONTENT);
                    break;
                case AD_CONTENT:
                    key = this.rebuildGroupByAdContent(adValue, configAdContent);
                    statsListResult.setDetailGroupBy(TARGET_APP_POSITION);
                    break;
                case TARGET_APP_POSITION:
                    key = this.rebuildGroupByTargetAppPosition(adValue, configAdContent);
                    statsListResult.setDetailGroupBy(TARGET_APP_POSITION_CONTENT);
                    break;
                case POSITION_CONTENT:
                    key = this.rebuildGroupByPositionContent(adValue, configAdContent);
                    statsListResult.setDetailGroupBy(TARGET_APP_POSITION_CONTENT);
                    break;
                case TARGET_APP_POSITION_CONTENT:
                    key = this.rebuildGroupByTargetAppPositionContent(adValue, configAdContent);
                    break;
                default:
                    continue;
            }
            if (adValueMap.containsKey(key)) {
                AdValue tempValue = adValueMap.get(key);
                tempValue.merge(adValue);
            } else {
                adValueMap.put(key, adValue);
            }
            totalAdValue.merge(adValue);
        }
        entityList.clear();
        entityList.addAll(adValueMap.values());
        this.calculateRate(entityList);

        // 处理汇总数据
        totalAdValue.setTimeValue("汇总");
        // 计算比例
        totalAdValue.calculateRate();
        // 汇总数据没有详情
        totalAdValue.setHaveDetail(false);
        return JSONObject.parseObject(JSONObject.toJSONString(totalAdValue));
    }

    /**
     * 根据推广App分组数据处理
     *
     * @param adValue         广告数据
     * @param configAdContent 广告内容
     * @return 数据汇总key
     */
    private String rebuildGroupByTargetApp(AdValue adValue, ConfigAdContent configAdContent) {
        adValue.setTargetAppId(configAdContent.getTargetAppId());
        adValue.setTargetAppName(configAdContent.getTargetAppName());
        return adValue.getTimeValue() + "_" + configAdContent.getTargetAppId();
    }

    /**
     * 根据运营App分组处理数据
     *
     * @param adValue 广告数据
     * @return 数据汇总key
     */
    private String rebuildGroupByProductApp(AdValue adValue) {
        adValue.setProductAppName(adValue.getProductAppId() + " 名称");
        return adValue.getTimeValue() + "_" + adValue.getProductAppId();
    }

    /**
     * 根据广告位置分组处理数据
     *
     * @param adValue 广告数据
     * @return 数据汇总key
     */
    private String rebuildGroupByAdPosition(AdValue adValue) {
        ConfigAdPosition configAdPosition = this.configAdPositionService.getCacheEntity(ConfigAdPosition.class, String.valueOf(adValue.getAdPositionId()));
        adValue.setAdPositionName(adValue.getAdPositionId() + "_" + configAdPosition.getName());
        return adValue.getTimeValue() + "_" + adValue.getAdPositionId();
    }

    /**
     * 根据广告位分组处理数据
     *
     * @param adValue 广告数据
     * @return 数据汇总key
     */
    private String rebuildGroupByAdSpace(AdValue adValue) {
        ConfigAdSpace configAdSpace = this.configAdSpaceService.getCacheEntity(ConfigAdSpace.class, String.valueOf(adValue.getAdSpaceId()));
        adValue.setAdSpaceName(adValue.getAdSpaceId() + "_" + configAdSpace.getName());
        return adValue.getTimeValue() + "_" + adValue.getAdSpaceId();
    }

    /**
     * 根据分组处理数据
     *
     * @param adValue         广告数据
     * @param configAdContent 广告内容
     * @return 数据汇总key
     */
    private String rebuildGroupByAdContent(AdValue adValue, ConfigAdContent configAdContent) {
        adValue.setTargetAppId(configAdContent.getTargetAppId());
        adValue.setTargetAppName(configAdContent.getTargetAppName());
        ConfigAdType configAdType = this.configAdTypeService.getCacheEntity(ConfigAdType.class, String.valueOf(configAdContent.getAdType()));
        adValue.setAdContentName(configAdContent.getId() + " - " + configAdType.getName());
        return adValue.getTimeValue() + "_" + adValue.getAdContentId();
    }

    /**
     * 根据分组处理数据
     *
     * @param adValue         广告数据
     * @param configAdContent 广告内容
     * @return 数据汇总key
     */
    private String rebuildGroupByTargetAppPosition(AdValue adValue, ConfigAdContent configAdContent) {
        adValue.setTargetAppId(configAdContent.getTargetAppId());
        adValue.setTargetAppName(configAdContent.getTargetAppName());
        ConfigAdPosition configAdPosition = this.configAdPositionService.getCacheEntity(ConfigAdPosition.class, String.valueOf(adValue.getAdPositionId()));
        adValue.setAdPositionName(configAdPosition.getName());
        return adValue.getTimeValue() + "_" + adValue.getAdPositionId() + "_" + configAdContent.getTargetAppId();
    }

    /**
     * 根据分组处理数据
     *
     * @param adValue         广告数据
     * @param configAdContent 广告内容
     * @return 数据汇总key
     */
    private String rebuildGroupByTargetAppPositionContent(AdValue adValue, ConfigAdContent configAdContent) {
        adValue.setTargetAppId(configAdContent.getTargetAppId());
        adValue.setTargetAppName(configAdContent.getTargetAppName());
        ConfigAdType configAdType = this.configAdTypeService.getCacheEntity(ConfigAdType.class, String.valueOf(configAdContent.getAdType()));
        adValue.setAdContentName(configAdContent.getId() + " - " + configAdType.getName());
        ConfigAdPosition configAdPosition = this.configAdPositionService.getCacheEntity(ConfigAdPosition.class, String.valueOf(adValue.getAdPositionId()));
        adValue.setAdPositionName(configAdPosition.getName());
        adValue.setHaveDetail(false);
        return adValue.getTimeValue() + "_" + adValue.getAdPositionId() + "_" + adValue.getAdContentId();
    }

    /**
     * 根据分组处理数据
     *
     * @param adValue         广告数据
     * @param configAdContent 广告内容
     * @return 数据汇总key
     */
    private String rebuildGroupByPositionContent(AdValue adValue, ConfigAdContent configAdContent) {
        ConfigAdType configAdType = this.configAdTypeService.getCacheEntity(ConfigAdType.class, String.valueOf(configAdContent.getAdType()));
        adValue.setAdContentName(configAdContent.getId() + " - " + configAdContent.getTargetAppName() + " - " + configAdType.getName());
        ConfigAdPosition configAdPosition = this.configAdPositionService.getCacheEntity(ConfigAdPosition.class, String.valueOf(adValue.getAdPositionId()));
        adValue.setAdPositionName(configAdPosition.getName());
        return adValue.getTimeValue() + "_" + adValue.getAdPositionId() + "_" + adValue.getAdContentId();
    }

    /**
     * 计算比业务比例数据
     *
     * @param adValueList 广告数据列表
     */
    private void calculateRate(Collection<AdValue> adValueList) {
        adValueList.forEach(AdValue::calculateRate);
    }

    @Autowired
    public void setConfigAdPositionService(ConfigAdPositionService configAdPositionService) {
        this.configAdPositionService = configAdPositionService;
    }

    @Autowired
    public void setConfigAdSpaceService(ConfigAdSpaceService configAdSpaceService) {
        this.configAdSpaceService = configAdSpaceService;
    }

    @Autowired
    public void setConfigAdContentService(ConfigAdContentService configAdContentService) {
        this.configAdContentService = configAdContentService;
    }

    @Autowired
    public void setConfigAdTypeService(ConfigAdTypeService configAdTypeService) {
        this.configAdTypeService = configAdTypeService;
    }

}
