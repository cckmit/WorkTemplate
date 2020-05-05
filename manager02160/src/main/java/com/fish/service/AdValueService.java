package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.fourth.mapper.AdValueMapper;
import com.fish.dao.fourth.model.AdValue;
import com.fish.dao.second.model.*;
import com.fish.protocols.GetParameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-10 13:50
 */
@Service
public class AdValueService implements BaseService<AdValue> {

    @Autowired
    AdValueMapper adValueMapper;
    @Autowired
    WxConfigService wxConfigService;
    @Autowired
    ConfigAdPositionService configAdPositionService;
    @Autowired
    ConfigAdSpaceService configAdSpaceService;
    @Autowired
    ConfigAdContentService configAdContentService;
    @Autowired
    ConfigAdTypeService configAdTypeService;

    private ConcurrentHashMap<String, WxConfig> wxConfigMap = new ConcurrentHashMap<String, WxConfig>();
    private ConcurrentHashMap<String, ConfigAdPosition> configAdPosMap = new ConcurrentHashMap<String, ConfigAdPosition>();
    private ConcurrentHashMap<String, ConfigAdSpace> configAdSpaceMap = new ConcurrentHashMap<String, ConfigAdSpace>();
    private ConcurrentHashMap<String, ConfigAdContent> configAdContentMap = new ConcurrentHashMap<String, ConfigAdContent>();

    @Override
    public void setDefaultSort(GetParameter parameter) {
    }

    @Override
    public Class<AdValue> getClassInfo() {
        return AdValue.class;
    }

    @Override
    public boolean removeIf(AdValue adValue, JSONObject searchData) {
        return false;
    }

    @Override
    public List<AdValue> selectAll(GetParameter parameter) {
        //缓存预先加载
        wxConfigMap = wxConfigService.getAll(WxConfig.class);
        configAdPosMap = configAdPositionService.getAll(ConfigAdPosition.class);
        configAdSpaceMap = configAdSpaceService.getAll(ConfigAdSpace.class);
        configAdContentMap = configAdContentService.getAll(ConfigAdContent.class);
        // 1、将页面传递的查询参数处理成mybatis查询参数
        AdValue adValue = new AdValue();
        // 设置查询起止时间
        int beginDate = 0;
        int endDate = 0;
        if (StringUtils.isNotBlank(parameter.getSearchData())) {
            // 根据页面的查询参数处理成mybatis查询参数
            // {"dateNum":"2020/03/10 - 2020/03/12","appId":"wx75f1c4d8cd887fd6","version":"","adPosition":"3","adSpace":"3","adContent":"11"}
            JSONObject parameterObject = JSONObject.parseObject(parameter.getSearchData());
            if (parameterObject != null) {
                if (StringUtils.isNotBlank(parameterObject.getString("dateNum"))) {
                    String[] dateNumArray = parameterObject.getString("dateNum").split("-");
                    beginDate = Integer.parseInt(dateNumArray[0].trim().replace("/", ""));
                    endDate = Integer.parseInt(dateNumArray[1].trim().replace("/", ""));
                }
            }
        }
        // 默认查询当日数据
        if (beginDate == 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            beginDate = endDate = Integer.parseInt(dateFormat.format(new Date()));
        }
        // 2、查询
        List<AdValue> list = this.adValueMapper.selectAll(beginDate, endDate, adValue);
        List<AdValue> adValues = new ArrayList<>();
        // 3、数据处理
        try {
            if (list != null) {
                if (StringUtils.isNotBlank(parameter.getSearchData())) {
                    JSONObject parameterObject = JSONObject.parseObject(parameter.getSearchData());
                    if (parameterObject != null) {
                        //queryDetail="1"区分查询明细
                        if (!"1".equals(parameterObject.getString("queryDetail"))) {
                            String groupByType = parameterObject.getString("groupByType");
                            //根据分组内容执行不同方法处理
                            switch (groupByType) {
                                case "time":
                                    adValues = selectTimeData(list, parameterObject);
                                    break;
                                case "ddTargetAppName":
                                    adValues = selectTargetAppNameData(list, parameterObject);
                                    break;
                                case "adPosition":
                                    adValues = selectAdPositionData(list, parameterObject);
                                    break;
                                case "productName":
                                    adValues = selectProductNameData(list, parameterObject);
                                    break;
                                case "adSpace":
                                    adValues = selectAdSpaceData(list, parameterObject);
                                    break;
                                case "adContent":
                                    adValues = selectAdContentData(list, parameterObject);
                                    break;
                                case "all":
                                    adValues = selectAll(list, parameterObject);
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            //查询明细
                            adValues = selectDetailData(beginDate, endDate, parameterObject);
                        }
                    } else {
                        adValues = selectTargetAppNameData(list, null);
                    }
                } else {
                    //默认进入
                    adValues = selectTargetAppNameData(list, null);
                }
            }
        } catch (Exception e) {

        }
        //数据汇总
        //collectData(adValues);
        return adValues;
    }

    /**
     * 数据汇总 求和 排序出现bug节后解决
     *
     * @param adValues 求和数据
     */
    private void collectData(List<AdValue> adValues) {
        //总数据
        int showNum = 0;
        int clickNum = 0;
        int promoteShowNum = 0;
        int promoteClickNum = 0;
        int targetShowNum = 0;
        if (adValues.size() > 0) {
            for (AdValue adValue : adValues) {
                showNum += adValue.getShowNum();
                clickNum += adValue.getClickNum();
                promoteShowNum += adValue.getPromoteShowNum();
                promoteClickNum += adValue.getPromoteClickNum();
                targetShowNum += adValue.getTargetShowNum();
            }

        }
        AdValue AdValue = new AdValue();
        AdValue.setAppPlatform("-");
        AdValue.setShowNum(showNum);
        AdValue.setClickNum(clickNum);
        AdValue.setPromoteShowNum(promoteShowNum);
        AdValue.setPromoteClickNum(promoteClickNum);
        AdValue.setTargetShowNum(targetShowNum);
        adValues.add(AdValue);
    }

    /**
     * 查询明细
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return List
     */
    private List<AdValue> selectDetailData(int beginDate, int endDate, JSONObject parameterObject) {
        ConcurrentHashMap<String, ConfigAdType> configAdTypeMap = configAdTypeService.getAll(ConfigAdType.class);
        AdValue selectAdValue = new AdValue();
        String groupByType = parameterObject.getString("groupByType");
        String appId = parameterObject.getString("appId");
        String adPositionId = parameterObject.getString("adPosition");
        String adSpace = parameterObject.getString("adSpace");
        String adContent = parameterObject.getString("adContent");
        String targetAppId = parameterObject.getString("ddTargetAppId");
        String contentId = parameterObject.getString("contentIds");
        selectAdValue.setAppId(appId);
        selectAdValue.setAdPositionId(Integer.parseInt(adPositionId));
        selectAdValue.setAdSpaceId(Integer.parseInt(adSpace));
        selectAdValue.setAdContentId(Integer.parseInt(adContent));
        selectAdValue.setGroupByType(groupByType);
        if (StringUtils.isNotBlank(targetAppId)) {
            List<Integer> contentIds = configAdContentService.adContentMapper.selectContentIdByDdTargetAppId(targetAppId);
            if (contentIds.size() > 0) {
                selectAdValue.setContentIds(StringUtils.join(contentIds.toArray(), ","));
            }
        }
        if (StringUtils.isNotBlank(contentId) && !"null".equals(contentId)) {
            selectAdValue.setContentIds(contentId);
        }
        List<AdValue> list = this.adValueMapper.queryDetail(beginDate, endDate, selectAdValue);
        try {
            for (AdValue value : list) {
                value.setGroupByType(selectAdValue.getGroupByType());
                value.setQueryDetail("1");
                if (value.getAppId() != null) {
                    WxConfig wxConfig = wxConfigMap.get(value.getAppId());
                    if (wxConfig != null) {
                        value.setAppName(wxConfig.getProductName());
                        value.setAppPlatform(wxConfig.getDdAppPlatform());
                    }
                }
                ConfigAdPosition configAdPosition = configAdPosMap.get(String.valueOf(value.getAdPositionId()));
                if (configAdPosition != null) {
                    value.setPositionName(value.getAdPositionId() + "-" + configAdPosition.getDdName());
                } else {
                    value.setPositionName(value.getAdPositionId() + " - 未匹配");
                }
                if (value.getAdSpaceId() > 0) {
                    ConfigAdSpace configAdSpace = configAdSpaceMap.get(String.valueOf(value.getAdSpaceId()));
                    if (configAdSpace != null) {
                        value.setSpaceName(value.getAdSpaceId() + "-" + configAdSpace.getDdName());
                        value.setAdTypeName(configAdTypeMap.get(String.valueOf(configAdSpace.getDdAdType())).getDdName());
                    } else {
                        value.setSpaceName(value.getAdSpaceId() + " - 未匹配");
                    }
                    ConfigAdContent configAdContent = configAdContentMap.get(String.valueOf(value.getAdContentId()));
                    if (configAdContent != null) {
                        value.setContentName(value.getAdContentId() + "-" + configAdContent.getDdTargetAppName());
                        value.setAdTypeName(configAdTypeMap.get(String.valueOf(configAdContent.getDdAdType())).getDdName());
                        value.setDdTargetAppId(configAdContent.getDdTargetAppId());
                        value.setDdTargetAppName(configAdContent.getDdTargetAppName());
                    } else {
                        value.setContentName(value.getAdContentId() + " - 未匹配");
                    }
                } else {
                    value.setSpaceName("微信");
                }
                if (value.getAdContentId() > 0) {
                    ConfigAdContent configAdContent = configAdContentMap.get(String.valueOf(value.getAdContentId()));
                    if (configAdContent != null) {
                        value.setAdTypeName(configAdTypeMap.get(String.valueOf(configAdContent.getDdAdType())).getDdName());
                        value.setContentName(value.getAdContentId() + "-" + configAdContent.getDdTargetAppName());
                        value.setDdTargetAppId(configAdContent.getDdTargetAppId());
                        value.setDdTargetAppName(configAdContent.getDdTargetAppName());
                    } else {
                        value.setContentName(value.getAdContentId() + " - 未匹配");
                    }
                } else {
                    value.setContentName("微信");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 按时间分组查询
     *
     * @param list            基础数据
     * @param parameterObject 查询条件
     * @return 符合数据
     */
    private List<AdValue> selectTimeData(List<AdValue> list, JSONObject parameterObject) {
        //map用于过滤相同key进行同key次数求和求和
        Map<String, AdValue> map = new HashMap<String, AdValue>(16);
        for (AdValue value : list) {
            if (parameterObject != null) {
                String appId = parameterObject.getString("appId");
                if (StringUtils.isNotBlank(appId)) {
                    if (!appId.equals(value.getAppId())) {
                        continue;
                    }
                }
//                String appPlatform = parameterObject.getString("appPlatform");
//                if (StringUtils.isNotBlank(appPlatform)) {
//                    if(!appPlatform.equals(value.getAppPlatform())){
//                        continue;
//                    }
//                } 按平台条件查询
                String adPosition = parameterObject.getString("adPosition");
                if (StringUtils.isNotBlank(adPosition)) {
                    if (!adPosition.equals(String.valueOf(value.getAdPositionId()))) {
                        continue;
                    }
                }
                String adSpace = parameterObject.getString("adSpace");
                if (StringUtils.isNotBlank(adSpace)) {
                    if (!adSpace.equals(String.valueOf(value.getAdSpaceId()))) {
                        continue;
                    }
                }
                String adContent = parameterObject.getString("adContent");
                if (StringUtils.isNotBlank(adContent)) {
                    if (!adContent.equals(String.valueOf(value.getAdContentId()))) {
                        continue;
                    }
                }
                String targetAppId = parameterObject.getString("ddTargetAppId");
                if (StringUtils.isNotBlank(targetAppId)) {
                    ConfigAdContent configAdContent = configAdContentMap.get(String.valueOf(value.getAdContentId()));
                    if (configAdContent != null) {
                        if (!targetAppId.equals(configAdContent.getDdTargetAppId())) {
                            continue;
                        }
                    }
                }
            }
            AdValue adValue = map.get(String.valueOf(value.getHourNum()));
            if (adValue != null) {
                adValue.setShowNum(adValue.getShowNum() + value.getShowNum());
                adValue.setClickNum(adValue.getClickNum() + value.getClickNum());
                adValue.setPromoteShowNum(adValue.getPromoteShowNum() + value.getPromoteShowNum());
                adValue.setPromoteClickNum(adValue.getPromoteClickNum() + value.getPromoteClickNum());
                adValue.setTargetShowNum(adValue.getTargetShowNum() + value.getTargetShowNum());
            } else {
                value.setGroupByType("time");
                value.setQueryDetail("0");
                map.put(String.valueOf(value.getHourNum()), value);
            }
        }
        return new ArrayList<>(map.values());
    }

    /**
     * 推广名称分组数据处理方式
     *
     * @param list            基础数据
     * @param parameterObject 查询条件
     * @return 符合数据
     */
    private List<AdValue> selectTargetAppNameData(List<AdValue> list, JSONObject parameterObject) {
        //map用于过滤相同key进行同key次数求和求和
        Map<String, AdValue> map = new HashMap<String, AdValue>(16);
        for (AdValue value : list) {
            //排除不符合搜索条件数据
            if (parameterObject != null) {
                String appId = parameterObject.getString("appId");
                if (StringUtils.isNotBlank(appId)) {
                    if (!appId.equals(value.getAppId())) {
                        continue;
                    }
                }
//                String appPlatform = parameterObject.getString("appPlatform");
//                if (StringUtils.isNotBlank(appPlatform)) {
//                    if(!appPlatform.equals(value.getAppPlatform())){
//                        continue;
//                    }
//                }查询平台下个版本补全
                String adPosition = parameterObject.getString("adPosition");
                if (StringUtils.isNotBlank(adPosition)) {
                    if (!adPosition.equals(String.valueOf(value.getAdPositionId()))) {
                        continue;
                    }
                }
                String adSpace = parameterObject.getString("adSpace");
                if (StringUtils.isNotBlank(adSpace)) {
                    if (!adSpace.equals(String.valueOf(value.getAdSpaceId()))) {
                        continue;
                    }
                }
                String adContent = parameterObject.getString("adContent");
                if (StringUtils.isNotBlank(adContent)) {
                    if (!adContent.equals(String.valueOf(value.getAdContentId()))) {
                        continue;
                    }
                }
                String targetAppId = parameterObject.getString("ddTargetAppId");
                if (StringUtils.isNotBlank(targetAppId)) {
                    ConfigAdContent configAdContent = configAdContentMap.get(String.valueOf(value.getAdContentId()));
                    if (configAdContent != null) {
                        if (!targetAppId.equals(configAdContent.getDdTargetAppId())) {
                            continue;
                        }
                    }
                }
            }
            //获取广告内容，按每天将推广App名称一致数据求和
            ConfigAdContent configAdContent = configAdContentMap.get(String.valueOf(value.getAdContentId()));
            if (configAdContent != null) {
                String ddTargetAppId = configAdContent.getDdTargetAppId();
                value.setDdTargetAppId(ddTargetAppId);
                AdValue adValue = map.get(ddTargetAppId + "-" + value.getHourNum());
                if (adValue != null) {
                    adValue.setDdTargetAppId(ddTargetAppId);
                    adValue.setDdTargetAppName(configAdContent.getDdTargetAppName());
                    adValue.setShowNum(adValue.getShowNum() + value.getShowNum());
                    adValue.setClickNum(adValue.getClickNum() + value.getClickNum());
                    adValue.setPromoteShowNum(adValue.getPromoteShowNum() + value.getPromoteShowNum());
                    adValue.setPromoteClickNum(adValue.getPromoteClickNum() + value.getPromoteClickNum());
                    adValue.setTargetShowNum(adValue.getTargetShowNum() + value.getTargetShowNum());
                } else {
                    //此处再次判断条件为了查询详情的时候有错误数据进入
                    if (parameterObject != null) {
                        //将搜索条件赋值到返回数据属性为了查询详细信息使用
                        String appId = parameterObject.getString("appId");
                        if (StringUtils.isNotBlank(appId)) {
                            value.setAppId(appId);
                        } else {
                            value.setAppId("");
                        }
                        String adPosition = parameterObject.getString("adPosition");
                        if (StringUtils.isNotBlank(adPosition)) {
                            value.setAdPositionId(Integer.parseInt(adPosition));
                        } else {
                            value.setAdPositionId(0);
                        }
                        String adSpace = parameterObject.getString("adSpace");
                        if (StringUtils.isNotBlank(adSpace)) {
                            value.setAdSpaceId(Integer.parseInt(adSpace));
                        } else {
                            value.setAdSpaceId(0);
                        }
                        String adContent = parameterObject.getString("adContent");
                        if (StringUtils.isNotBlank(adContent)) {
                            value.setAdContentId(Integer.parseInt(adContent));
                        } else {
                            value.setAdContentId(0);
                        }
                        String targetAppId = parameterObject.getString("ddTargetAppId");
                        if (StringUtils.isNotBlank(targetAppId)) {
                            List<Integer> contentIds = configAdContentService.adContentMapper.selectContentIdByDdTargetAppId(targetAppId);
                            if (contentIds.size() > 0) {
                                value.setContentIds(StringUtils.join(contentIds.toArray(), ","));
                            }
                        }
                    }else{
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                        int defaultDate = Integer.parseInt(dateFormat.format(new Date()));
                        value.setHourNum(defaultDate);
                    }
                    value.setDdTargetAppId(ddTargetAppId);
                    value.setDdTargetAppName(configAdContent.getDdTargetAppName());
                    value.setGroupByType("ddTargetAppName");
                    value.setQueryDetail("0");
                    map.put(ddTargetAppId + "-" + value.getHourNum(), value);
                }
            }
        }
        return new ArrayList<>(map.values());
    }

    /**
     * 按广告位置分组查询
     *
     * @param list            基础数据
     * @param parameterObject 查询条件
     * @return 符合数据
     */
    private List<AdValue> selectAdPositionData(List<AdValue> list, JSONObject parameterObject) {
        //map用于过滤相同key进行同key次数求和求和
        Map<String, AdValue> map = new HashMap<String, AdValue>(16);
        for (AdValue value : list) {
            //parameterObject 过滤查询条件
            if (parameterObject != null) {
                String appId = parameterObject.getString("appId");
                if (StringUtils.isNotBlank(appId)) {
                    if (!appId.equals(value.getAppId())) {
                        continue;
                    }
                }
//                String appPlatform = parameterObject.getString("appPlatform");
//                if (StringUtils.isNotBlank(appPlatform)) {
//                    if(!appPlatform.equals(value.getAppPlatform())){
//                        continue;
//                    }
//                } 查询平台下个版本补全
                String adPosition = parameterObject.getString("adPosition");
                if (StringUtils.isNotBlank(adPosition)) {
                    if (!adPosition.equals(String.valueOf(value.getAdPositionId()))) {
                        continue;
                    }
                }
                String adSpace = parameterObject.getString("adSpace");
                if (StringUtils.isNotBlank(adSpace)) {
                    if (!adSpace.equals(String.valueOf(value.getAdSpaceId()))) {
                        continue;
                    }
                }
                String adContent = parameterObject.getString("adContent");
                if (StringUtils.isNotBlank(adContent)) {
                    if (!adContent.equals(String.valueOf(value.getAdContentId()))) {
                        continue;
                    }
                }
                String targetAppId = parameterObject.getString("ddTargetAppId");
                if (StringUtils.isNotBlank(targetAppId)) {
                    ConfigAdContent configAdContent = configAdContentMap.get(String.valueOf(value.getAdContentId()));
                    if (configAdContent != null) {
                        if (!targetAppId.equals(configAdContent.getDdTargetAppId())) {
                            continue;
                        }
                    }
                }
            }
            //按广告位置分组，求和每天相同广告位置数据
            AdValue adValue = map.get(value.getAdPositionId() + "-" + value.getHourNum());
            ConfigAdPosition configAdPosition = configAdPosMap.get(String.valueOf(value.getAdPositionId()));
            if (adValue != null) {
                adValue.setPositionName(configAdPosition != null ? configAdPosition.getDdId() + "-" + configAdPosition.getDdName() : "微信");
                adValue.setShowNum(adValue.getShowNum() + value.getShowNum());
                adValue.setClickNum(adValue.getClickNum() + value.getClickNum());
                adValue.setPromoteShowNum(adValue.getPromoteShowNum() + value.getPromoteShowNum());
                adValue.setPromoteClickNum(adValue.getPromoteClickNum() + value.getPromoteClickNum());
                adValue.setTargetShowNum(adValue.getTargetShowNum() + value.getTargetShowNum());
            } else {
                //parameterObject 判断防止返回脏数据影响查询详情条件
                if (parameterObject != null) {
                    String appId = parameterObject.getString("appId");
                    if (StringUtils.isNotBlank(appId)) {
                        value.setAppId(appId);
                    } else {
                        value.setAppId("");
                    }
                    String adPosition = parameterObject.getString("adPosition");
                    if (StringUtils.isNotBlank(adPosition)) {
                        value.setAdPositionId(Integer.parseInt(adPosition));
                    }
                    String adSpace = parameterObject.getString("adSpace");
                    if (StringUtils.isNotBlank(adSpace)) {
                        value.setAdSpaceId(Integer.parseInt(adSpace));
                    } else {
                        value.setAdSpaceId(0);
                    }
                    String adContent = parameterObject.getString("adContent");
                    if (StringUtils.isNotBlank(adContent)) {
                        value.setAdContentId(Integer.parseInt(adContent));
                    } else {
                        value.setAdContentId(0);
                    }
                    String targetAppId = parameterObject.getString("ddTargetAppId");
                    if (StringUtils.isNotBlank(targetAppId)) {
                        List<Integer> contentIds = configAdContentService.adContentMapper.selectContentIdByDdTargetAppId(targetAppId);
                        if (contentIds.size() > 0) {
                            value.setContentIds(StringUtils.join(contentIds.toArray(), ","));
                        }
                    }
                }
                value.setPositionName(configAdPosition != null ? configAdPosition.getDdId() + "-" + configAdPosition.getDdName() : "微信");
                value.setGroupByType("adPosition");
                value.setQueryDetail("0");
                map.put(value.getAdPositionId() + "-" + value.getHourNum(), value);
            }
        }
        return new ArrayList<>(map.values());
    }

    /**
     * 按照产品名称分组查询
     *
     * @param list            基础数据
     * @param parameterObject 查询条件
     * @return 符合数据
     */
    private List<AdValue> selectProductNameData(List<AdValue> list, JSONObject parameterObject) {
        //map用于过滤相同key进行同key次数求和求和
        Map<String, AdValue> map = new HashMap<String, AdValue>(16);
        for (AdValue value : list) {
            //parameterObject 筛选查询条件
            if (parameterObject != null) {
                String appId = parameterObject.getString("appId");
                if (StringUtils.isNotBlank(appId)) {
                    if (!appId.equals(value.getAppId())) {
                        continue;
                    }
                }
//                String appPlatform = parameterObject.getString("appPlatform");
//                if (StringUtils.isNotBlank(appPlatform)) {
//                    if(!appPlatform.equals(value.getAppPlatform())){
//                        continue;
//                    }
//                } 平台类型后期补全
                String adPosition = parameterObject.getString("adPosition");
                if (StringUtils.isNotBlank(adPosition)) {
                    if (!adPosition.equals(String.valueOf(value.getAdPositionId()))) {
                        continue;
                    }
                }
                String adSpace = parameterObject.getString("adSpace");
                if (StringUtils.isNotBlank(adSpace)) {
                    if (!adSpace.equals(String.valueOf(value.getAdSpaceId()))) {
                        continue;
                    }
                }
                String adContent = parameterObject.getString("adContent");
                if (StringUtils.isNotBlank(adContent)) {
                    if (!adContent.equals(String.valueOf(value.getAdContentId()))) {
                        continue;
                    }
                }
                String targetAppId = parameterObject.getString("ddTargetAppId");
                if (StringUtils.isNotBlank(targetAppId)) {
                    ConfigAdContent configAdContent = configAdContentMap.get(String.valueOf(value.getAdContentId()));
                    if (configAdContent != null) {
                        if (!targetAppId.equals(configAdContent.getDdTargetAppId())) {
                            continue;
                        }
                    }
                }
            }
            AdValue adValue = map.get(String.valueOf(value.getAppId()));
            WxConfig wxConfig = wxConfigMap.get(value.getAppId());
            if (adValue != null) {
                adValue.setAppName(wxConfig != null ? wxConfig.getProductName() : "未知产品");
                adValue.setShowNum(adValue.getShowNum() + value.getShowNum());
                adValue.setClickNum(adValue.getClickNum() + value.getClickNum());
                adValue.setPromoteShowNum(adValue.getPromoteShowNum() + value.getPromoteShowNum());
                adValue.setPromoteClickNum(adValue.getPromoteClickNum() + value.getPromoteClickNum());
                adValue.setTargetShowNum(adValue.getTargetShowNum() + value.getTargetShowNum());
            } else {
                //parameterObject 判断防止返回脏数据影响查询详情条件
                if (parameterObject != null) {
                    String appId = parameterObject.getString("appId");
                    if (StringUtils.isNotBlank(appId)) {
                        value.setAppId(appId);
                    }
                    String adPosition = parameterObject.getString("adPosition");
                    if (StringUtils.isNotBlank(adPosition)) {
                        value.setAdPositionId(Integer.parseInt(adPosition));
                    } else {
                        value.setAdPositionId(0);
                    }
                    String adSpace = parameterObject.getString("adSpace");
                    if (StringUtils.isNotBlank(adSpace)) {
                        value.setAdSpaceId(Integer.parseInt(adSpace));
                    } else {
                        value.setAdSpaceId(0);
                    }
                    String adContent = parameterObject.getString("adContent");
                    if (StringUtils.isNotBlank(adContent)) {
                        value.setAdContentId(Integer.parseInt(adContent));
                    } else {
                        value.setAdContentId(0);
                    }
                    String targetAppId = parameterObject.getString("ddTargetAppId");
                    if (StringUtils.isNotBlank(targetAppId)) {
                        List<Integer> contentIds = configAdContentService.adContentMapper.selectContentIdByDdTargetAppId(targetAppId);
                        if (contentIds.size() > 0) {
                            value.setContentIds(StringUtils.join(contentIds.toArray(), ","));
                        }
                    }
                }
                value.setAppName(wxConfig != null ? wxConfig.getProductName() : "未知产品");
                value.setGroupByType("productName");
                value.setQueryDetail("0");
                map.put(value.getAppId(), value);
            }
        }
        return new ArrayList<>(map.values());

    }

    /**
     * 按广告位分组查询
     *
     * @param list            基础数据
     * @param parameterObject 查询条件
     * @return 符合数据
     */
    private List<AdValue> selectAdSpaceData(List<AdValue> list, JSONObject parameterObject) {
        //map用于过滤相同key进行同key次数求和求和
        HashMap<String, AdValue> map = new HashMap<String, AdValue>(16);
        for (AdValue value : list) {
            if (parameterObject != null) {
                String appId = parameterObject.getString("appId");
                if (StringUtils.isNotBlank(appId)) {
                    if (!appId.equals(value.getAppId())) {
                        continue;
                    }
                }
//                String appPlatform = parameterObject.getString("appPlatform");
//                if (StringUtils.isNotBlank(appPlatform)) {
//                    if(!appPlatform.equals(value.getAppPlatform())){
//                        continue;
//                    }
//                }
                String adPosition = parameterObject.getString("adPosition");
                if (StringUtils.isNotBlank(adPosition)) {
                    if (!adPosition.equals(String.valueOf(value.getAdPositionId()))) {
                        continue;
                    }
                }
                String adSpace = parameterObject.getString("adSpace");
                if (StringUtils.isNotBlank(adSpace)) {
                    if (!adSpace.equals(String.valueOf(value.getAdSpaceId()))) {
                        continue;
                    }
                }
                String adContent = parameterObject.getString("adContent");
                if (StringUtils.isNotBlank(adContent)) {
                    if (!adContent.equals(String.valueOf(value.getAdContentId()))) {
                        continue;
                    }
                }
                String targetAppId = parameterObject.getString("ddTargetAppId");
                if (StringUtils.isNotBlank(targetAppId)) {
                    ConfigAdContent configAdContent = configAdContentMap.get(String.valueOf(value.getAdContentId()));
                    if (configAdContent != null) {
                        if (!targetAppId.equals(configAdContent.getDdTargetAppId())) {
                            continue;
                        }
                    }
                }
            }
            AdValue adValue = map.get(value.getAdSpaceId() + "-" + value.getHourNum());
            ConfigAdSpace configAdSpace = configAdSpaceMap.get(String.valueOf(value.getAdSpaceId()));
            if (adValue != null) {
                adValue.setSpaceName(configAdSpace != null ? configAdSpace.getDdName() : "未知位置");
                adValue.setShowNum(adValue.getShowNum() + value.getShowNum());
                adValue.setClickNum(adValue.getClickNum() + value.getClickNum());
                adValue.setPromoteShowNum(adValue.getPromoteShowNum() + value.getPromoteShowNum());
                adValue.setPromoteClickNum(adValue.getPromoteClickNum() + value.getPromoteClickNum());
                adValue.setTargetShowNum(adValue.getTargetShowNum() + value.getTargetShowNum());
            } else {
                if (parameterObject != null) {
                    String appId = parameterObject.getString("appId");
                    if (StringUtils.isNotBlank(appId)) {
                        value.setAppId(appId);
                    } else {
                        value.setAppId("");
                    }
                    String adPosition = parameterObject.getString("adPosition");
                    if (StringUtils.isNotBlank(adPosition)) {
                        value.setAdPositionId(Integer.parseInt(adPosition));
                    } else {
                        value.setAdPositionId(0);
                    }
                    String adSpace = parameterObject.getString("adSpace");
                    if (StringUtils.isNotBlank(adSpace)) {
                        value.setAdSpaceId(Integer.parseInt(adSpace));
                    }
                    String adContent = parameterObject.getString("adContent");
                    if (StringUtils.isNotBlank(adContent)) {
                        value.setAdContentId(Integer.parseInt(adContent));
                    } else {
                        value.setAdContentId(0);
                    }
                    String targetAppId = parameterObject.getString("ddTargetAppId");
                    if (StringUtils.isNotBlank(targetAppId)) {
                        List<Integer> contentIds = configAdContentService.adContentMapper.selectContentIdByDdTargetAppId(targetAppId);
                        if (contentIds.size() > 0) {
                            value.setContentIds(StringUtils.join(contentIds.toArray(), ","));
                        }
                    }
                }
                value.setSpaceName(configAdSpace != null ? configAdSpace.getDdName() : "未知位置");
                value.setGroupByType("adSpace");
                value.setQueryDetail("0");
                map.put(value.getAdSpaceId() + "-" + value.getHourNum(), value);
            }
        }
        ArrayList<AdValue> adValues = new ArrayList<>(map.values());
        return adValues;

    }

    /**
     * 按广告内容分组查询
     *
     * @param list            基础数据
     * @param parameterObject 查询条件
     * @return 符合数据
     */
    private List<AdValue> selectAdContentData(List<AdValue> list, JSONObject parameterObject) {
        ConcurrentHashMap<String, ConfigAdType> configAdTypeMap = configAdTypeService.getAll(ConfigAdType.class);
        //map用于过滤相同key进行同key次数求和求和
        Map<String, AdValue> map = new HashMap<String, AdValue>(16);
        for (AdValue value : list) {
            if (parameterObject != null) {
                String appId = parameterObject.getString("appId");
                if (StringUtils.isNotBlank(appId)) {
                    if (!appId.equals(value.getAppId())) {
                        continue;
                    }
                }
//                String appPlatform = parameterObject.getString("appPlatform");
//                if (StringUtils.isNotBlank(appPlatform)) {
//                    if(!appPlatform.equals(value.getAppPlatform())){
//                        continue;
//                    }
//                }
                String adPosition = parameterObject.getString("adPosition");
                if (StringUtils.isNotBlank(adPosition)) {
                    if (!adPosition.equals(String.valueOf(value.getAdPositionId()))) {
                        continue;
                    }
                }
                String adSpace = parameterObject.getString("adSpace");
                if (StringUtils.isNotBlank(adSpace)) {
                    if (!adSpace.equals(String.valueOf(value.getAdSpaceId()))) {
                        continue;
                    }
                }
                String adContent = parameterObject.getString("adContent");
                if (StringUtils.isNotBlank(adContent)) {
                    if (!adContent.equals(String.valueOf(value.getAdContentId()))) {
                        continue;
                    }
                }
                String targetAppId = parameterObject.getString("ddTargetAppId");
                if (StringUtils.isNotBlank(targetAppId)) {
                    ConfigAdContent configAdContent = configAdContentMap.get(String.valueOf(value.getAdContentId()));
                    if (configAdContent != null) {
                        if (!targetAppId.equals(configAdContent.getDdTargetAppId())) {
                            continue;
                        }
                    }
                }
            }
            AdValue adValue = map.get(value.getAdContentId() + "-" + value.getHourNum());
            ConfigAdContent configAdContent = configAdContentMap.get(String.valueOf(value.getAdContentId()));
            if (adValue != null) {
                adValue.setContentName(configAdContent != null ? configAdContent.getDdId() + "-" + configAdContent.getDdTargetAppName() : "微信");
                adValue.setShowNum(adValue.getShowNum() + value.getShowNum());
                adValue.setClickNum(adValue.getClickNum() + value.getClickNum());
                adValue.setPromoteShowNum(adValue.getPromoteShowNum() + value.getPromoteShowNum());
                adValue.setPromoteClickNum(adValue.getPromoteClickNum() + value.getPromoteClickNum());
                adValue.setTargetShowNum(adValue.getTargetShowNum() + value.getTargetShowNum());
            } else {
                if (parameterObject != null) {
                    String appId = parameterObject.getString("appId");
                    if (StringUtils.isNotBlank(appId)) {
                        value.setAppId(appId);
                    } else {
                        value.setAppId("");
                    }
                    String adPosition = parameterObject.getString("adPosition");
                    if (StringUtils.isNotBlank(adPosition)) {
                        value.setAdPositionId(Integer.parseInt(adPosition));
                    } else {
                        value.setAdPositionId(0);
                    }
                    String adSpace = parameterObject.getString("adSpace");
                    if (StringUtils.isNotBlank(adSpace)) {
                        value.setAdSpaceId(Integer.parseInt(adSpace));
                    } else {
                        value.setAdSpaceId(0);
                    }
                    String adContent = parameterObject.getString("adContent");
                    if (StringUtils.isNotBlank(adContent)) {
                        value.setAdContentId(Integer.parseInt(adContent));
                    }
                    String targetAppId = parameterObject.getString("ddTargetAppId");
                    if (StringUtils.isNotBlank(targetAppId)) {
                        List<Integer> contentIds = configAdContentService.adContentMapper.selectContentIdByDdTargetAppId(targetAppId);
                        if (contentIds.size() > 0) {
                            value.setContentIds(StringUtils.join(contentIds.toArray(), ","));
                        }
                    }
                }

                value.setAdTypeName(configAdTypeMap.get(String.valueOf(configAdContent.getDdAdType())).getDdName());
                value.setContentName(configAdContent != null ? configAdContent.getDdId() + "-" + configAdContent.getDdTargetAppName() : "微信");
                value.setGroupByType("adContent");
                value.setQueryDetail("0");
                map.put(value.getAdContentId() + "-" + value.getHourNum(), value);
            }
        }
        ArrayList<AdValue> adValues = new ArrayList<>(map.values());
        return adValues;
    }

    /**
     * 按全部明细分组查询
     *
     * @param list            基础数据
     * @param parameterObject 查询条件
     * @return 符合数据
     */
    private List<AdValue> selectAll(List<AdValue> list, JSONObject parameterObject) {
        List<AdValue> adValues = new ArrayList<>();
        for (AdValue value : list) {
            //过滤查询条件
            if (parameterObject != null) {
                String appId = parameterObject.getString("appId");
                if (StringUtils.isNotBlank(appId)) {
                    if (!appId.equals(value.getAppId())) {
                        continue;
                    }
                }
//                String appPlatform = parameterObject.getString("appPlatform");
//                if (StringUtils.isNotBlank(appPlatform)) {
//                    if(!appPlatform.equals(value.getAppPlatform())){
//                        continue;
//                    }
//                }
                String adPosition = parameterObject.getString("adPosition");
                if (StringUtils.isNotBlank(adPosition)) {
                    if (!adPosition.equals(String.valueOf(value.getAdPositionId()))) {
                        continue;
                    }
                }
                String adSpace = parameterObject.getString("adSpace");
                if (StringUtils.isNotBlank(adSpace)) {
                    if (!adSpace.equals(String.valueOf(value.getAdSpaceId()))) {
                        continue;
                    }
                }
                String adContent = parameterObject.getString("adContent");
                if (StringUtils.isNotBlank(adContent)) {
                    if (!adContent.equals(String.valueOf(value.getAdContentId()))) {
                        continue;
                    }
                }
                String targetAppId = parameterObject.getString("ddTargetAppId");
                if (StringUtils.isNotBlank(targetAppId)) {
                    ConfigAdContent configAdContent = configAdContentMap.get(String.valueOf(value.getAdContentId()));
                    if (configAdContent != null) {
                        if (!targetAppId.equals(configAdContent.getDdTargetAppId())) {
                            continue;
                        }
                    }
                }
            }
            if (value.getAppId() != null) {
                WxConfig wxConfig = wxConfigMap.get(value.getAppId());
                if (wxConfig != null) {
                    value.setAppName(wxConfig.getProductName());
                    value.setAppPlatform(wxConfig.getDdAppPlatform());
                }
            }
            ConfigAdPosition configAdPosition = configAdPosMap.get(String.valueOf(value.getAdPositionId()));
            if (configAdPosition != null) {
                value.setPositionName(value.getAdPositionId() + "-" + configAdPosition.getDdName());
            } else {
                value.setPositionName(value.getAdPositionId() + " - 未匹配");
            }
            if (value.getAdSpaceId() > 0) {
                ConfigAdSpace configAdSpace = configAdSpaceMap.get(String.valueOf(value.getAdSpaceId()));
                if (configAdSpace != null) {
                    value.setSpaceName(value.getAdSpaceId() + "-" + configAdSpace.getDdName());
                } else {
                    value.setSpaceName(value.getAdSpaceId() + " - 未匹配");
                }
                ConfigAdContent configAdContent = configAdContentMap.get(String.valueOf(value.getAdContentId()));
                if (configAdContent != null) {
                    value.setContentName(value.getAdContentId() + "-" + configAdContent.getDdTargetAppName());
                    value.setDdTargetAppId(configAdContent.getDdTargetAppId());
                    value.setDdTargetAppName(configAdContent.getDdTargetAppName());
                } else {
                    value.setContentName(value.getAdContentId() + " - 未匹配");
                }
            } else {
                value.setSpaceName("微信");
            }
            if (value.getAdContentId() > 0) {
                ConfigAdContent configAdContent = configAdContentMap.get(String.valueOf(value.getAdContentId()));
                if (configAdContent != null) {
                    value.setContentName(value.getAdContentId() + "-" + configAdContent.getDdTargetAppName());
                    value.setDdTargetAppId(configAdContent.getDdTargetAppId());
                    value.setDdTargetAppName(configAdContent.getDdTargetAppName());
                } else {
                    value.setContentName(value.getAdContentId() + " - 未匹配");
                }
            } else {
                value.setContentName("微信");
            }
            adValues.add(value);
        }
        return adValues;
    }

}
