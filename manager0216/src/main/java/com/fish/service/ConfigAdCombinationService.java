package com.fish.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.ConfigAdCombinationMapper;
import com.fish.dao.second.model.ConfigAdCombination;
import com.fish.dao.second.model.ConfigAdPosition;
import com.fish.dao.second.model.ConfigAdSpace;
import com.fish.protocols.GetParameter;
import com.fish.protocols.PostResult;
import com.fish.service.cache.CacheService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-13 17:19
 */
@Service
public class ConfigAdCombinationService implements BaseService<ConfigAdCombination> {

    @Autowired
    ConfigAdCombinationMapper adCombinationMapper;

    @Autowired
    CacheService cacheService;

    @Autowired
    ConfigAdPositionService adPositionService;

    @Autowired
    ConfigAdSpaceService adSpaceService;

    @Autowired
    ConfigAdContentService adContentService;

    @Autowired
    BaseConfig baseConfig;

    @Override
    public void setDefaultSort(GetParameter parameter) { }

    @Override
    public Class<ConfigAdCombination> getClassInfo() { return ConfigAdCombination.class; }

    @Override
    public boolean removeIf(ConfigAdCombination configAdCombination, JSONObject searchData) { return false; }

    @Override
    public List<ConfigAdCombination> selectAll(GetParameter parameter) {
        getDefaultJson();
        return this.adCombinationMapper.selectAll();
    }

    /**
     * 查询指定的
     *
     * @param id
     * @return
     */
    public JSONArray getEditJson(int id) {
        ConfigAdCombination configAdCombination = this.adCombinationMapper.select(id);
        if (configAdCombination != null && StringUtils.isNotBlank(configAdCombination.getDdJson())) {
            JSONArray combinationArray = JSONArray.parseArray(configAdCombination.getDdJson());
            for (int positionIndex = 0; positionIndex < combinationArray.size(); positionIndex++) {
                JSONObject positionObject = combinationArray.getJSONObject(positionIndex);
                // 更新广告位置名称
                int positionId = positionObject.getInteger("positionId");
                positionObject.put("positionName",
                        positionId + "-" + this.cacheService.getConfigAdPositions(positionId).getDdName());
                // 更新广告策略名称
                int strategyId = positionObject.getInteger("strategyId");
                positionObject.put("strategyName",
                        strategyId + "-" + this.cacheService.getConfigAdStrategys(strategyId).getDdName());
                // 处理广告位
                JSONArray spaceArray = positionObject.getJSONArray("spaces");
                for (int spaceIndex = 0; spaceIndex < spaceArray.size(); spaceIndex++) {
                    JSONObject spaceObject = spaceArray.getJSONObject(spaceIndex);
                    // 处理广告位名称
                    int spaceId = spaceObject.getInteger("spaceId");
                    ConfigAdSpace configAdSpace = this.cacheService.getConfigAdSpaces(spaceId);
                    spaceObject.put("spaceName", spaceId + "-" + configAdSpace.getDdName());
                    // 将广告内容名拼接起来
                    JSONArray contentArray = spaceObject.getJSONArray("contentIds");
                    String contentNames = "";
                    for (int contentIndex = 0; contentIndex < contentArray.size(); contentIndex++) {
                        int contentId = contentArray.getInteger(contentIndex);
                        String contentName = contentId + "-" + this.cacheService.getConfigAdContents(
                                contentId).getDdTargetAppName();
                        if (StringUtils.isBlank(contentNames)) {
                            contentNames = contentName;
                        } else {
                            contentNames += ", " + contentName;
                        }
                    }
                    spaceObject.put("contentNames", contentNames);
                }
            }
            System.out.println(combinationArray);
            return combinationArray;
        }
        return null;
    }

    /**
     * 通过以前的配置生成默认的新配置方法，先放在这里用以测试，后续可以删除
     *
     * @return
     */
    private void getDefaultJson() {
        JSONArray jsonArray = new JSONArray();
        List<ConfigAdPosition> positionList = this.adPositionService.selectAll();
        for (ConfigAdPosition adPosition : positionList) {
            JSONObject positionObject = new JSONObject();
            positionObject.put("positionId", adPosition.getDdId());
            if (StringUtils.isNotBlank(adPosition.getDdAdSpaces())) {
                JSONArray spaceArray = new JSONArray();
                for (String adSpaceId : adPosition.getDdAdSpaces().split(",")) {
                    ConfigAdSpace configAdSpace = this.adSpaceService.getConfigAdSpace(Integer.parseInt(adSpaceId));
                    JSONObject spaceObject = new JSONObject();
                    spaceObject.put("spaceId", Integer.parseInt(adSpaceId));
                    JSONArray contentIdArray = new JSONArray();
                    if (StringUtils.isNotBlank(configAdSpace.getDdAdContents())) {
                        for (String adContentId : configAdSpace.getDdAdContents().split(",")) {
                            contentIdArray.add(Integer.parseInt(adContentId));
                        }
                    }
                    spaceObject.put("contentIds", contentIdArray);
                    spaceArray.add(spaceObject);
                }
                positionObject.put("spaces", spaceArray);
            }
            positionObject.put("strategyId", adPosition.getDdStrategyId());
            positionObject.put("strategyValue", adPosition.getDdStrategyValue());
            jsonArray.add(positionObject);
        }
        System.out.println(jsonArray);
    }


    /**
     * 新增
     *
     * @param configAdCombination
     * @return
     */
    public PostResult insert(ConfigAdCombination configAdCombination) {
        PostResult postResult = new PostResult();
        int insert = this.adCombinationMapper.insert(configAdCombination);
        if (insert <= 0) {
            postResult.setSuccessed(false);
            postResult.setMsg("操作失败，新增组合配置内容失败！");
        } else {
            ReadJsonUtil.flushTable("config_ad_combination", this.baseConfig.getFlushCache());
        }
        return postResult;
    }

    /**
     * 更新名称，不更新配置Json
     *
     * @param configAdCombination
     * @return
     */
    public PostResult update(ConfigAdCombination configAdCombination) {
        PostResult postResult = new PostResult();
        int update = this.adCombinationMapper.update(configAdCombination);
        if (update <= 0) {
            postResult.setSuccessed(false);
            postResult.setMsg("操作失败，修改组合配置内容失败！");
        } else {
            ReadJsonUtil.flushTable("config_ad_combination", this.baseConfig.getFlushCache());
        }
        return postResult;
    }

    /**
     * 保存配置Json，不更新名称等内容
     * // TODO 这里处理有些复杂，数据结构不合理，有空将页面提交的数据优化
     *
     * @param jsonObject
     * @return
     */
    public PostResult saveCombinationJson(JSONObject jsonObject) {
        PostResult postResult = new PostResult();
        if (jsonObject != null) {
            // 先取出组合配置ID
            int combinationId = jsonObject.getInteger("combinationId");

            // 从Json中循环取出广告位置和广告位，其它数据就好处理了
            // 广告位置列表
            List<Integer> positionList = new ArrayList<>();
            // 广告位列表Map
            Map<Integer, List<Integer>> positionSpaceMap = new HashMap<>();
            for (String key : jsonObject.keySet()) {
                // 广告位置
                if (key.startsWith("position_")) {
                    positionList.add(Integer.parseInt(key.replace("position_", "")));
                } else if (key.startsWith("space_")) {
                    String[] spaceIdArray = key.split("_");
                    int positionId = Integer.parseInt(spaceIdArray[1]);
                    int spaceId = Integer.parseInt(spaceIdArray[2]);
                    List<Integer> spaceList = positionSpaceMap.computeIfAbsent(positionId, k -> new ArrayList<>());
                    spaceList.add(spaceId);
                }
            }

            JSONArray jsonArray = new JSONArray();
            //循环广告位置List
            Collections.sort(positionList);
            positionList.forEach(positionId -> {
                JSONObject positionObject = new JSONObject();
                // 广告位置ID
                positionObject.put("positionId", positionId);
                // 广告位置策略ID
                String strategy = jsonObject.getString("strategy_id_" + positionId);
                positionObject.put("strategyId", Integer.parseInt(strategy.split("-")[0]));
                // 广告策略参数
                positionObject.put("strategyValue", jsonObject.getString("strategy_value_" + positionId));
                // 广告位数组
                JSONArray spaceArray = new JSONArray();
                List<Integer> spaceList = positionSpaceMap.get(positionId);
                if (spaceList != null) {
                    spaceList.forEach(spaceId -> {
                        JSONObject spaceObject = new JSONObject();
                        spaceObject.put("spaceId", spaceId);

                        String contentNames = jsonObject.getString("content_" + positionId + "_" + spaceId);
                        JSONArray contentArray = new JSONArray();
                        if (StringUtils.isNotBlank(contentNames)) {
                            String[] contentNameArray = contentNames.split(",");
                            for (String contentName : contentNameArray) {
                                contentArray.add(Integer.parseInt(contentName.trim().split("-")[0]));
                            }
                        }
                        spaceObject.put("contentIds", contentArray);
                        spaceArray.add(spaceObject);
                    });
                }
                positionObject.put("spaces", spaceArray);
                jsonArray.add(positionObject);
            });

            // 保存数据
            ConfigAdCombination adCombination = new ConfigAdCombination();
            adCombination.setDdId(combinationId);
            adCombination.setDdJson(jsonArray.toJSONString());
            int update = this.adCombinationMapper.saveCombinationJson(adCombination);
            if (update <= 0) {
                postResult.setSuccessed(false);
                postResult.setMsg("操作失败，广告组合配置保存失败，请联系管理员！");
            } else {
                ReadJsonUtil.flushTable("config_ad_combination", this.baseConfig.getFlushCache());
            }
        }
        return postResult;
    }

    public PostResult delete(String deleteIds) {
        PostResult postResult = new PostResult();
        int delete = this.adCombinationMapper.delete(deleteIds);
        if (delete <= 0) {
            postResult.setSuccessed(false);
            postResult.setMsg("操作失败，修改组合配置内容失败！");
        } else {
            ReadJsonUtil.flushTable("config_ad_combination", this.baseConfig.getFlushCache());
        }
        return postResult;
    }

}
