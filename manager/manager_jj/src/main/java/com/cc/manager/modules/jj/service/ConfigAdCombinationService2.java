package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.*;
import com.cc.manager.modules.jj.mapper.ConfigAdCombinationMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-15 20:35
 */
@Service
@DS("jj")
public class ConfigAdCombinationService2 extends BaseCrudService<ConfigAdCombination, ConfigAdCombinationMapper> {

    private ConfigAdPositionService configAdPositionService;
    private ConfigAdSpaceService configAdSpaceService;
    private ConfigAdContentService configAdContentService;
    private ConfigAdStrategyService configAdStrategyService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<ConfigAdCombination> queryWrapper) {
        queryWrapper.eq("ddType",2);
    }


    @Override
    protected boolean delete(String requestParam, UpdateWrapper<ConfigAdCombination> deleteWrapper) {
        if (StringUtils.isNotBlank(requestParam)) {
            List<String> idList = JSONObject.parseArray(requestParam, String.class);
            return this.removeByIds(idList);
        }
        return false;
    }

    /**
     * 只修改名称
     *
     * @param requestParam 修改参数
     * @return 修改结果
     */
    public PostResult updateName(String requestParam) {
        PostResult postResult = new PostResult();
        JSONObject requestObject = JSONObject.parseObject(requestParam);
        UpdateWrapper<ConfigAdCombination> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set(true, "ddName", requestObject.getString("name"));
        updateWrapper.eq(true, "ddId", requestObject.getString("id"));
        if (!this.update(updateWrapper)) {
            postResult.setCode(2);
            postResult.setMsg("名称修改失败！");
        }
        return postResult;
    }

    /**
     * 获取广告合计树形表Json，在方法中，return开头的变量表示是放置到返回值中的变量
     *
     * @param adCombination 广告合集ID
     * @return 广告合计树形表Json
     */
    public JSONObject getAdCombinationTable(String adCombination) {
        JSONObject returnObject = new JSONObject();
        returnObject.put("code", 2);

        ConfigAdCombination configAdCombination = this.getById(adCombination);
        if (Objects.nonNull(configAdCombination) && StringUtils.isNotBlank(configAdCombination.getPositionIds())) {
            JSONArray combinationArray = JSONArray.parseArray(configAdCombination.getPositionIds());
            JSONArray returnPositionArray = new JSONArray();
            int id = 1;
            for (int i = 0; i < combinationArray.size(); i++) {
                id++;
                JSONObject positionObject = combinationArray.getJSONObject(i);
                String positionId = positionObject.getString("positionId");
                ConfigAdPosition configAdPosition = this.configAdPositionService.getCacheEntity(ConfigAdPosition.class, positionId);

                JSONObject resultPositionObject = new JSONObject();
                resultPositionObject.put("id", id);
                resultPositionObject.put("operator", "addAdSpace");
                resultPositionObject.put("combinationId", adCombination);
                resultPositionObject.put("positionId", positionId);
                resultPositionObject.put("name", configAdPosition.getCacheValue());
                resultPositionObject.put("adType", configAdPosition.getAdTypeNames());


                // 解析广告位数据
                JSONArray spaceArray = positionObject.getJSONArray("spaces");
                JSONArray returnSpaceArray = new JSONArray();
                for (int j = 0; j < spaceArray.size(); j++) {
                    id++;
                    JSONObject spaceObject = spaceArray.getJSONObject(j);
                    String spaceId = spaceObject.getString("spaceId");
                    ConfigAdSpace configAdSpace = this.configAdSpaceService.getCacheEntity(ConfigAdSpace.class, spaceId);

                    JSONObject returnSpaceObject = new JSONObject();
                    returnSpaceObject.put("id", id);
                    returnSpaceObject.put("operator", "addAdContent");
                    returnSpaceObject.put("combinationId", adCombination);
                    returnSpaceObject.put("positionId", positionId);
                    returnSpaceObject.put("spaceId", spaceId);
                    returnSpaceObject.put("name", configAdSpace.getCacheValue());
                    returnSpaceObject.put("adType", configAdSpace.getAdTypeName());
                    returnSpaceObject.put("orderNum", j + 1);


                    returnSpaceArray.add(returnSpaceObject);
                }
                resultPositionObject.put("children", returnSpaceArray);
                returnPositionArray.add(resultPositionObject);
            }
            returnObject.put("data", returnPositionArray);
            returnObject.put("code", 1);
        }
        return returnObject;
    }
    public JSONObject getAdCombinationIconPool(String adCombination) {
        JSONObject returnObject = new JSONObject();
        returnObject.put("code", 2);

        ConfigAdCombination configAdCombination = this.getById(adCombination);
        if (Objects.nonNull(configAdCombination) && StringUtils.isNotBlank(configAdCombination.getContentIds())) {
            JSONArray iconPoolArray = JSONArray.parseArray(configAdCombination.getContentIds());
            JSONArray returnContentArray = new JSONArray();
            int id = 1;
            for (int k = 0; k < iconPoolArray.size(); k++) {
                id++;
                String contentId = iconPoolArray.getString(k);
                ConfigAdContent configAdContent = this.configAdContentService.getCacheEntity(ConfigAdContent.class, contentId);

                JSONObject returnContentObject = new JSONObject();
                returnContentObject.put("id", id);
                returnContentObject.put("combinationId", adCombination);
                returnContentObject.put("operator", "addAdPool");
                returnContentObject.put("contentId", contentId);
                returnContentObject.put("name", configAdContent.getCacheValue());
                returnContentObject.put("adType", configAdContent.getAdTypeName());
                returnContentObject.put("orderNum", k + 1);
                returnContentArray.add(returnContentObject);
            }
            returnObject.put("data", returnContentArray);
            returnObject.put("code", 1);
        }
        return returnObject;
    }


    public JSONObject getAdCombinationBannerPool(String adCombination) {
        JSONObject returnObject = new JSONObject();
        returnObject.put("code", 2);

        ConfigAdCombination configAdCombination = this.getById(adCombination);
        if (Objects.nonNull(configAdCombination) && StringUtils.isNotBlank(configAdCombination.getContentIds())) {
            JSONArray bannerPoolArray = JSONArray.parseArray(configAdCombination.getContentIds());
            JSONArray returnContentArray = new JSONArray();
            int id = 1;
            for (int k = 0; k < bannerPoolArray.size(); k++) {
                id++;
                String contentId = bannerPoolArray.getString(k);
                ConfigAdContent configAdContent = this.configAdContentService.getCacheEntity(ConfigAdContent.class, contentId);

                JSONObject returnContentObject = new JSONObject();
                returnContentObject.put("id", id);
                returnContentObject.put("combinationId", adCombination);
                returnContentObject.put("operator", "addAdPool");
                returnContentObject.put("contentId", contentId);
                returnContentObject.put("name", configAdContent.getCacheValue());
                returnContentObject.put("adType", configAdContent.getAdTypeName());
                returnContentObject.put("orderNum", k + 1);
                returnContentArray.add(returnContentObject);
            }
            returnObject.put("data", returnContentArray);
            returnObject.put("code", 1);
        }
        return returnObject;
    }
    /**
     * 新增广告位置
     *
     * @param addPositionObject 新增数据对象
     * @return 新增结果
     */
    public PostResult addPosition(JSONObject addPositionObject) {
        PostResult postResult = new PostResult();
        JSONArray combinationArray = this.getAdCombinationArray(addPositionObject.getString("id"));
        List<Integer> addPositionIdList = JSON.parseArray(addPositionObject.getString("positionIds"), Integer.class);

        for (int i = 0; i < combinationArray.size(); i++) {
            JSONObject positionObject = combinationArray.getJSONObject(i);
            addPositionIdList.remove(positionObject.getInteger("positionId"));
        }

        // 如果不存在已配置的广告位置，新增广告配置
        addPositionIdList.forEach(addPositionId -> {
            JSONObject adPositionObject = new JSONObject();
            adPositionObject.put("positionId", addPositionId);
            adPositionObject.put("spaces", new JSONArray());
            combinationArray.add(adPositionObject);
        });

        // 保存数据对象
        ConfigAdCombination configAdCombination = new ConfigAdCombination();
        configAdCombination.setId(addPositionObject.getInteger("id"));
        configAdCombination.setType(2);
        configAdCombination.setPositionIds(combinationArray.toJSONString());
        this.updateById(configAdCombination);
        return postResult;
    }

    /**
     * 批量删除广告位置
     * {"id":"1","positionId":1}
     *
     * @param deleteAdPositionsObject 批量删除广告位置
     * @return 新增结果
     */
    public PostResult deleteAdPosition(@RequestBody JSONObject deleteAdPositionsObject) {
        JSONArray combinationArray = this.getAdCombinationArray(deleteAdPositionsObject.getString("id"));
        combinationArray.removeIf(obj -> {
            JSONObject positionObject = (JSONObject) obj;
            return StringUtils.equals(positionObject.getString("positionId"), deleteAdPositionsObject.getString("positionId"));
        });

        // 保存数据对象
        ConfigAdCombination configAdCombination = new ConfigAdCombination();
        configAdCombination.setId(deleteAdPositionsObject.getInteger("id"));
        configAdCombination.setType(2);
        configAdCombination.setPositionIds(combinationArray.toJSONString());
        this.updateById(configAdCombination);
        return new PostResult();
    }


    /**
     * 新增广告位
     * {"spaceId":1,"positionId":"1","id":"1"}
     *
     * @param addAdSpaceObject 新增数据对象
     * @return 新增结果
     */
    public PostResult addAdSpace(@RequestBody JSONObject addAdSpaceObject) {
        PostResult postResult = new PostResult();
        JSONArray combinationArray = this.getAdCombinationArray(addAdSpaceObject.getString("id"));
        List<Integer> addSpaceIdList = JSON.parseArray(addAdSpaceObject.getString("spaceIds"), Integer.class);
        for (int i = 0; i < combinationArray.size(); i++) {
            JSONObject positionObject = combinationArray.getJSONObject(i);
            String positionId = positionObject.getString("positionId");
            if (StringUtils.equals(positionId, addAdSpaceObject.getString("positionId"))) {
                JSONArray spaceArray = positionObject.getJSONArray("spaces");

                addSpaceIdList.forEach(addSpaceId -> {
                    JSONObject spaceObject = new JSONObject();
                    spaceObject.put("spaceId", addSpaceId);
                    spaceArray.add(spaceObject);
                });
                break;
            }
        }

        // 保存数据对象
        ConfigAdCombination configAdCombination = new ConfigAdCombination();
        configAdCombination.setId(addAdSpaceObject.getInteger("id"));
        configAdCombination.setType(2);
        configAdCombination.setPositionIds(combinationArray.toJSONString());
        this.updateById(configAdCombination);
        return postResult;
    }

    /**
     * 删除广告位
     * {"positionId":"1","deleteSpaceIdArray":[],"id":"1"}
     *
     * @param deleteAdSpacesObject 删除数据
     * @return 删除结果
     */
    public PostResult deleteAdSpace(JSONObject deleteAdSpacesObject) {
        JSONArray combinationArray = this.getAdCombinationArray(deleteAdSpacesObject.getString("id"));
        for (int i = 0; i < combinationArray.size(); i++) {
            JSONObject positionObject = combinationArray.getJSONObject(i);
            String positionId = positionObject.getString("positionId");
            if (StringUtils.equals(positionId, deleteAdSpacesObject.getString("positionId"))) {
                JSONArray spaceArray = positionObject.getJSONArray("spaces");
                spaceArray.removeIf(obj -> {
                    JSONObject spaceObject = (JSONObject) obj;
                    return StringUtils.equals(spaceObject.getString("spaceId"), deleteAdSpacesObject.getString("spaceId"));
                });
                break;
            }
        }

        // 保存数据对象
        ConfigAdCombination configAdCombination = new ConfigAdCombination();
        configAdCombination.setId(deleteAdSpacesObject.getInteger("id"));
        configAdCombination.setType(2);
        configAdCombination.setPositionIds(combinationArray.toJSONString());
        this.updateById(configAdCombination);
        return new PostResult();
    }

    /**
     * 新增广告内容配置
     * {"spaceId":"1","positionId":"1","id":"1","contentIds":[50]}
     *
     * @param addAdContentObject 新增数据对象
     * @return 新增结果
     */
    public PostResult addAdContent(JSONObject addAdContentObject) {
        // 解析出要添加的广告内容ID
        JSONArray combinationArray = this.getAdCombinationArray(addAdContentObject.getString("id"));
        List<Integer> newContentIdList = JSONArray.parseArray(addAdContentObject.getString("contentIds"), Integer.class);

        combinationArrayForEach:
        for (int i = 0; i < combinationArray.size(); i++) {
            JSONObject positionObject = combinationArray.getJSONObject(i);
            String positionId = positionObject.getString("positionId");
            if (StringUtils.equals(positionId, addAdContentObject.getString("positionId"))) {
                JSONArray spaceArray = positionObject.getJSONArray("spaces");
                for (int j = 0; j < spaceArray.size(); j++) {
                    JSONObject spaceObject = spaceArray.getJSONObject(j);
                    String spaceId = spaceObject.getString("spaceId");
                    if (StringUtils.equals(spaceId, addAdContentObject.getString("spaceId"))) {
                        List<Integer> contentIdList = JSONArray.parseArray(spaceObject.getString("contentIds"), Integer.class);
                        for (int newContentId : newContentIdList) {
                            if (!contentIdList.contains(newContentId)) {
                                contentIdList.add(newContentId);
                            }
                        }
                        spaceObject.put("contentIds", JSONArray.parseArray(JSON.toJSONString(contentIdList)));
                        break combinationArrayForEach;
                    }
                }
            }
        }

        // 保存数据对象
        ConfigAdCombination configAdCombination = new ConfigAdCombination();
        configAdCombination.setId(addAdContentObject.getInteger("id"));
        configAdCombination.setType(2);
        configAdCombination.setPositionIds(combinationArray.toJSONString());
        this.updateById(configAdCombination);
        return new PostResult();
    }

    /**
     * <p>保存广告内容序号</p></br>
     * <p>{"spaceId":"2","contentIndexArray":[{"contentId":"1","showIndex":0},{"contentId":"5","showIndex":1}],"positionId":"2","id":"5"}</p>
     *
     * @param saveAdContentOrderNumObject 保存数据对象
     * @return 保存结果
     */
    public PostResult saveAdContentOrderNum(JSONObject saveAdContentOrderNumObject) {
        Map<String, LinkedHashMap<Integer, Integer>> map = new HashMap<>();
        JSONArray contentOrderNumArray = saveAdContentOrderNumObject.getJSONArray("adContentOrderNumArray");
        for (int i = 0; i < contentOrderNumArray.size(); i++) {
            JSONObject indexObject = contentOrderNumArray.getJSONObject(i);
            String key = indexObject.getString("positionId") + "_" + indexObject.getString("spaceId");
            LinkedHashMap<Integer, Integer> contentIndexMap = map.get(key);
            if (contentIndexMap == null) {
                contentIndexMap = new LinkedHashMap<>();
            }
            contentIndexMap.put(indexObject.getInteger("contentId"), indexObject.getInteger("orderNum"));
            map.put(key, contentIndexMap);
        }

        // 对当前所有广告位进行排序
        JSONArray combinationArray = this.getAdCombinationArray(saveAdContentOrderNumObject.getString("id"));
        for (int i = 0; i < combinationArray.size(); i++) {
            JSONObject positionObject = combinationArray.getJSONObject(i);
            String positionId = positionObject.getString("positionId");
            // 解析广告位数据
            JSONArray spaceArray = positionObject.getJSONArray("spaces");
            JSONArray returnSpaceArray = new JSONArray();
            for (int j = 0; j < spaceArray.size(); j++) {
                JSONObject spaceObject = spaceArray.getJSONObject(j);
                String spaceId = spaceObject.getString("spaceId");
                LinkedHashMap<Integer, Integer> contentOrderNumMap = map.get(positionId + "_" + spaceId);
                if (contentOrderNumMap != null) {
                    LinkedHashMap<Integer, Integer> sortedContentOrderNumMap = new LinkedHashMap<>();
                    // 排序
                    contentOrderNumMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
                            .collect(Collectors.toList()).forEach(ele -> sortedContentOrderNumMap.put(ele.getKey(), ele.getValue()));
                    spaceObject.put("contentIds", JSON.parseArray(JSON.toJSONString(sortedContentOrderNumMap.keySet())));
                }
            }
        }

        // 保存数据对象
        ConfigAdCombination configAdCombination = new ConfigAdCombination();
        configAdCombination.setId(saveAdContentOrderNumObject.getInteger("id"));
        configAdCombination.setType(2);
        configAdCombination.setContentIds(combinationArray.toJSONString());
        this.updateById(configAdCombination);
        return new PostResult();
    }

    /**
     * 批量删除广告内容
     * {"spaceId":"1","positionId":"1","deleteContentIdArray":["10","2"],"id":"1"}
     *
     * @param deleteAdContentsObject 删除数据对象
     * @return 删除结果
     */
    public PostResult deleteAdContent(JSONObject deleteAdContentsObject) {
        JSONArray combinationArray = this.getAdCombinationArray(deleteAdContentsObject.getString("id"));
        combinationArrayForEach:
        for (int i = 0; i < combinationArray.size(); i++) {
            JSONObject positionObject = combinationArray.getJSONObject(i);
            String positionId = positionObject.getString("positionId");
            if (StringUtils.equals(positionId, deleteAdContentsObject.getString("positionId"))) {
                JSONArray spaceArray = positionObject.getJSONArray("spaces");
                for (int j = 0; j < spaceArray.size(); j++) {
                    JSONObject spaceObject = spaceArray.getJSONObject(j);
                    String spaceId = spaceObject.getString("spaceId");
                    if (StringUtils.equals(spaceId, deleteAdContentsObject.getString("spaceId"))) {
                        JSONArray contentArray = spaceObject.getJSONArray("contentIds");
                        contentArray.removeIf(contentId ->
                                StringUtils.equals(String.valueOf(contentId), deleteAdContentsObject.getString("contentId"))
                        );
                        break combinationArrayForEach;
                    }
                }
            }
        }

        // 保存数据对象
        ConfigAdCombination configAdCombination = new ConfigAdCombination();
        configAdCombination.setId(deleteAdContentsObject.getInteger("id"));
        configAdCombination.setType(2);
        configAdCombination.setContentIds(combinationArray.toJSONString());
        this.updateById(configAdCombination);
        return new PostResult();
    }

    /**
     * @param id 广告合集ID
     * @return 广告合集数组
     */
    private JSONArray getAdCombinationArray(String id) {
        ConfigAdCombination configAdCombination = this.getById(id);
        if (Objects.nonNull(configAdCombination) && StringUtils.isNotBlank(configAdCombination.getContentIds())) {
            return JSONArray.parseArray(configAdCombination.getContentIds());
        }
        return new JSONArray();
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
    public void setConfigAdStrategyService(ConfigAdStrategyService configAdStrategyService) {
        this.configAdStrategyService = configAdStrategyService;
    }


}
