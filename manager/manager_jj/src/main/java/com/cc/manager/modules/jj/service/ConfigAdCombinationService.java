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
import com.cc.manager.modules.jj.entity.ConfigAdCombination;
import com.cc.manager.modules.jj.entity.ConfigAdContent;
import com.cc.manager.modules.jj.entity.ConfigAdPosition;
import com.cc.manager.modules.jj.entity.ConfigAdSpace;
import com.cc.manager.modules.jj.mapper.ConfigAdCombinationMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-07 15:35
 */
@Service
@DS("jj")
public class ConfigAdCombinationService extends BaseCrudService<ConfigAdCombination, ConfigAdCombinationMapper> {

    private ConfigAdPositionService configAdPositionService;

    private ConfigAdSpaceService configAdSpaceService;

    private ConfigAdContentService configAdContentService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<ConfigAdCombination> queryWrapper) {

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<ConfigAdCombination> deleteWrapper) {
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
     * <div>通过广告合计ID获取当前合集下的广告位置列表</div>
     * <div>作为返回值的JSON对象都以return为前缀</div>
     *
     * @param id 广告合集
     * @return 广告位置列表
     */
    public JSONArray getAdPositionList(String id) {
        JSONArray returnPositionArray = new JSONArray();
        ConfigAdCombination configAdCombination = this.getById(id);
        if (Objects.nonNull(configAdCombination) && StringUtils.isNotBlank(configAdCombination.getCombinationJson())) {
            JSONArray combinationArray = JSONArray.parseArray(configAdCombination.getCombinationJson());
            for (int i = 0; i < combinationArray.size(); i++) {
                JSONObject positionObject = combinationArray.getJSONObject(i);
                String positionId = positionObject.getString("positionId");
                ConfigAdPosition configAdPosition = this.configAdPositionService.getCacheEntity(ConfigAdPosition.class, positionId);

                JSONObject returnPositionObject = new JSONObject();
                returnPositionObject.put("id", id);
                returnPositionObject.put("positionId", positionId);
                returnPositionObject.put("positionType", configAdPosition.getAdTypeNames());
                returnPositionObject.put("positionName", configAdPosition.getName());
                returnPositionArray.add(returnPositionObject);
            }
        }
        return returnPositionArray;
    }

    /**
     * 新增广告位置
     *
     * @param addPositionObject 新增数据对象
     * @return 新增结果
     */
    public PostResult addPosition(JSONObject addPositionObject) {
        PostResult postResult = new PostResult();
        String newAdPositionId = addPositionObject.getString("positionId");
        ConfigAdCombination configAdCombination = this.getById(addPositionObject.getString("id"));
        JSONArray combinationArray = null;
        if (StringUtils.isNotBlank(configAdCombination.getCombinationJson())) {
            combinationArray = JSONArray.parseArray(configAdCombination.getCombinationJson());
            for (int i = 0; i < combinationArray.size(); i++) {
                JSONObject positionObject = combinationArray.getJSONObject(i);
                String positionId = positionObject.getString("positionId");
                if (StringUtils.equals(newAdPositionId, positionId)) {
                    postResult.setCode(2);
                    postResult.setMsg("新增失败：所选广告位置已存在！");
                    break;
                }
            }
        } else {
            combinationArray = new JSONArray();
        }

        // 如果不存在已配置的广告位置，新增广告配置
        if (postResult.getCode() == 1) {
            JSONObject adPositionObject = new JSONObject();
            adPositionObject.put("positionId", addPositionObject.getString("positionId"));
            adPositionObject.put("strategyId", 0);
            adPositionObject.put("strategyValue", "");
            adPositionObject.put("spaces", new JSONArray());
            combinationArray.add(adPositionObject);
            // 将json对象存入数据库
            configAdCombination.setCombinationJson(JSONArray.toJSONString(combinationArray));
            this.updateById(configAdCombination);
        }
        return postResult;
    }

    /**
     * @param id           广告合计ID
     * @param adPositionId 广告位置ID
     * @return 广告位置策略+广告位列表
     */
    public JSONObject getAdSpaceList(String id, String adPositionId) {
        JSONObject returnPositionObject = new JSONObject();
        JSONArray returnSpaceArray = new JSONArray();
        ConfigAdCombination configAdCombination = this.getById(id);
        if (Objects.nonNull(configAdCombination) && StringUtils.isNotBlank(configAdCombination.getCombinationJson())) {
            JSONArray combinationArray = JSONArray.parseArray(configAdCombination.getCombinationJson());
            for (int i = 0; i < combinationArray.size(); i++) {
                JSONObject positionObject = combinationArray.getJSONObject(i);
                String positionId = positionObject.getString("positionId");
                if (StringUtils.equals(positionId, adPositionId)) {
                    JSONArray spaceArray = positionObject.getJSONArray("spaces");
                    returnPositionObject.put("strategyId", positionObject.getString("strategyId"));
                    returnPositionObject.put("strategyValue", positionObject.getString("strategyValue"));
                    for (int j = 0; j < spaceArray.size(); j++) {
                        JSONObject spaceObject = spaceArray.getJSONObject(j);
                        String spaceId = spaceObject.getString("spaceId");
                        ConfigAdSpace configAdSpace = this.configAdSpaceService.getCacheEntity(ConfigAdSpace.class, spaceId);

                        JSONObject returnSpaceObject = new JSONObject();
                        returnSpaceObject.put("id", id);
                        returnSpaceObject.put("positionId", adPositionId);
                        returnSpaceObject.put("spaceId", spaceId);
                        returnSpaceObject.put("spaceType", configAdSpace.getAdTypeName());
                        returnSpaceObject.put("spaceName", configAdSpace.getName());
                        returnSpaceArray.add(returnSpaceObject);
                    }
                    break;
                }
            }
        }
        returnPositionObject.put("spaceArray", returnSpaceArray);
        return returnPositionObject;
    }

    /**
     * 批量删除广告位置
     * {"id":"1","deletePositionIdArray":["1"]}
     *
     * @param deleteAdPositionsObject 批量删除广告位置
     * @return 新增结果
     */
    public PostResult deleteAdPositions(@RequestBody JSONObject deleteAdPositionsObject) {
        List<Integer> deletePositionIdList = JSONArray.parseArray(deleteAdPositionsObject.getString("deletePositionIdArray"), Integer.class);

        JSONArray combinationArray = this.getAdCombinationArray(deleteAdPositionsObject.getString("id"));
        combinationArray.removeIf(obj -> {
            JSONObject positionObject = (JSONObject) obj;
            return deletePositionIdList.contains(positionObject.getInteger("positionId"));
        });

        // 保存数据对象
        ConfigAdCombination configAdCombination = new ConfigAdCombination();
        configAdCombination.setId(deleteAdPositionsObject.getInteger("id"));
        configAdCombination.setCombinationJson(combinationArray.toJSONString());
        this.updateById(configAdCombination);
        return new PostResult();
    }

    /**
     * 更新广告位置策略
     * {"positionId":"1","strategyId":"4","id":"1","strategyValue":""}
     *
     * @param updateStrategyObject 更新数据对象
     * @return 更新结果
     */
    public PostResult updateAdStrategy(@RequestBody JSONObject updateStrategyObject) {
        JSONArray combinationArray = this.getAdCombinationArray(updateStrategyObject.getString("id"));
        for (int i = 0; i < combinationArray.size(); i++) {
            JSONObject positionObject = combinationArray.getJSONObject(i);
            if (StringUtils.equals(positionObject.getString("positionId"), updateStrategyObject.getString("positionId"))) {
                positionObject.put("strategyId", updateStrategyObject.getInteger("strategyId"));
                positionObject.put("strategyValue", updateStrategyObject.getString("strategyValue"));
                break;
            }
        }

        // 保存数据对象
        ConfigAdCombination configAdCombination = new ConfigAdCombination();
        configAdCombination.setId(updateStrategyObject.getInteger("id"));
        configAdCombination.setCombinationJson(combinationArray.toJSONString());
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
        combinationArrayForEach:
        for (int i = 0; i < combinationArray.size(); i++) {
            JSONObject positionObject = combinationArray.getJSONObject(i);
            String positionId = positionObject.getString("positionId");
            if (StringUtils.equals(positionId, addAdSpaceObject.getString("positionId"))) {
                JSONArray spaceArray = positionObject.getJSONArray("spaces");
                for (int j = 0; j < spaceArray.size(); j++) {
                    JSONObject spaceObject = spaceArray.getJSONObject(j);
                    if (StringUtils.equals(spaceObject.getString("spaceId"), addAdSpaceObject.getString("spaceId"))) {
                        postResult.setCode(2);
                        postResult.setMsg("操作失败：已存在选择的广告位！");
                        break combinationArrayForEach;
                    }
                }
                JSONObject spaceObject = new JSONObject();
                spaceObject.put("spaceId", addAdSpaceObject.getInteger("spaceId"));
                spaceObject.put("contentIds", new JSONArray());
                spaceArray.add(spaceObject);
                break;
            }
        }

        // 保存数据对象
        ConfigAdCombination configAdCombination = new ConfigAdCombination();
        configAdCombination.setId(addAdSpaceObject.getInteger("id"));
        configAdCombination.setCombinationJson(combinationArray.toJSONString());
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
    public PostResult deleteAdSpaces(JSONObject deleteAdSpacesObject) {
        // 解析出要删除的的广告位ID
        List<Integer> deleteSpaceIdList = JSONArray.parseArray(deleteAdSpacesObject.getString("deleteSpaceIdArray"), Integer.class);

        JSONArray combinationArray = this.getAdCombinationArray(deleteAdSpacesObject.getString("id"));
        for (int i = 0; i < combinationArray.size(); i++) {
            JSONObject positionObject = combinationArray.getJSONObject(i);
            String positionId = positionObject.getString("positionId");
            if (StringUtils.equals(positionId, deleteAdSpacesObject.getString("positionId"))) {
                JSONArray spaceArray = positionObject.getJSONArray("spaces");
                spaceArray.removeIf(obj -> {
                    JSONObject spaceObject = (JSONObject) obj;
                    return deleteSpaceIdList.contains(spaceObject.getInteger("spaceId"));
                });
                break;
            }
        }

        // 保存数据对象
        ConfigAdCombination configAdCombination = new ConfigAdCombination();
        configAdCombination.setId(deleteAdSpacesObject.getInteger("id"));
        configAdCombination.setCombinationJson(combinationArray.toJSONString());
        this.updateById(configAdCombination);
        return new PostResult();
    }

    public JSONArray getAdContentList(String id, String adPositionId, String adSpaceId) {
        JSONArray returnContentArray = new JSONArray();
        ConfigAdCombination configAdCombination = this.getById(id);
        if (Objects.nonNull(configAdCombination) && StringUtils.isNotBlank(configAdCombination.getCombinationJson())) {
            JSONArray combinationArray = JSONArray.parseArray(configAdCombination.getCombinationJson());
            for (int i = 0; i < combinationArray.size(); i++) {
                JSONObject positionObject = combinationArray.getJSONObject(i);
                String positionId = positionObject.getString("positionId");
                if (StringUtils.equals(positionId, adPositionId)) {
                    JSONArray spaceArray = positionObject.getJSONArray("spaces");
                    for (int j = 0; j < spaceArray.size(); j++) {
                        JSONObject spaceObject = spaceArray.getJSONObject(j);
                        String spaceId = spaceObject.getString("spaceId");
                        if (StringUtils.equals(spaceId, adSpaceId)) {
                            JSONArray contentArray = spaceObject.getJSONArray("contentIds");
                            for (int k = 0; k < contentArray.size(); k++) {
                                String contentId = contentArray.getString(k);
                                ConfigAdContent configAdContent = this.configAdContentService.getCacheEntity(ConfigAdContent.class, contentId);

                                JSONObject returnContentObject = new JSONObject();
                                returnContentObject.put("id", id);
                                returnContentObject.put("positionId", adPositionId);
                                returnContentObject.put("spaceId", spaceId);
                                returnContentObject.put("contentId", contentId);
                                returnContentObject.put("showIndex", k);
                                returnContentObject.put("contentType", configAdContent.getAdTypeName());
                                returnContentObject.put("contentName", configAdContent.getTargetAppName());
                                returnContentArray.add(returnContentObject);
                            }
                        }
                    }
                    break;
                }
            }
        }
        return returnContentArray;
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
        List<Integer> newContentIdList = JSONArray.parseArray(addAdContentObject.getString("contentIds"), Integer.class);

        JSONArray combinationArray = this.getAdCombinationArray(addAdContentObject.getString("id"));
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
        configAdCombination.setCombinationJson(combinationArray.toJSONString());
        this.updateById(configAdCombination);
        return new PostResult();
    }

    /**
     * <p>保存广告内容序号</p></br>
     * <p>{"spaceId":"2","contentIndexArray":[{"contentId":"1","showIndex":0},{"contentId":"5","showIndex":1}],"positionId":"2","id":"5"}</p>
     *
     * @param saveAdContentIndexObject 保存数据对象
     * @return 保存结果
     */
    public PostResult saveAdContentIndex(JSONObject saveAdContentIndexObject) {
        // 解析得到排序后的广告内容数组
        JSONArray contentIndexArray = saveAdContentIndexObject.getJSONArray("contentIndexArray");
        LinkedHashMap<Integer, Integer> contentIndexMap = new LinkedHashMap<>();
        LinkedHashMap<Integer, Integer> sortedContentIndexMap = new LinkedHashMap<>();
        for (int i = 0; i < contentIndexArray.size(); i++) {
            JSONObject contentIndexObject = contentIndexArray.getJSONObject(i);
            contentIndexMap.put(contentIndexObject.getInteger("contentId"), contentIndexObject.getInteger("showIndex"));
        }
        // 根据value排序
        contentIndexMap.entrySet().stream().sorted((p1, p2) -> p1.getValue().compareTo(p2.getValue()))
                .collect(Collectors.toList()).forEach(ele -> sortedContentIndexMap.put(ele.getKey(), ele.getValue()));
        JSONArray contentIds = JSONArray.parseArray(JSON.toJSONString(sortedContentIndexMap.keySet()));

        // 将排序后的内容更新掉原始数据区域
        JSONArray combinationArray = this.getAdCombinationArray(saveAdContentIndexObject.getString("id"));
        combinationArrayForEach:
        for (int i = 0; i < combinationArray.size(); i++) {
            JSONObject positionObject = combinationArray.getJSONObject(i);
            String positionId = positionObject.getString("positionId");
            if (StringUtils.equals(positionId, saveAdContentIndexObject.getString("positionId"))) {
                JSONArray spaceArray = positionObject.getJSONArray("spaces");
                for (int j = 0; j < spaceArray.size(); j++) {
                    JSONObject spaceObject = spaceArray.getJSONObject(j);
                    String spaceId = spaceObject.getString("spaceId");
                    if (StringUtils.equals(spaceId, saveAdContentIndexObject.getString("spaceId"))) {
                        spaceObject.put("contentIds", contentIds);
                        break combinationArrayForEach;
                    }
                }
            }
        }

        // 保存数据对象
        ConfigAdCombination configAdCombination = new ConfigAdCombination();
        configAdCombination.setId(saveAdContentIndexObject.getInteger("id"));
        configAdCombination.setCombinationJson(combinationArray.toJSONString());
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
    public PostResult deleteAdContents(JSONObject deleteAdContentsObject) {
        // 解析出要删除的广告内容ID
        List<String> deleteContentIdList = JSONArray.parseArray(deleteAdContentsObject.getString("deleteContentIdArray"), String.class);

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
                        contentArray.removeIf(contentId -> deleteContentIdList.contains(String.valueOf(contentId)));
                        break combinationArrayForEach;
                    }
                }
            }
        }

        // 保存数据对象
        ConfigAdCombination configAdCombination = new ConfigAdCombination();
        configAdCombination.setId(deleteAdContentsObject.getInteger("id"));
        configAdCombination.setCombinationJson(combinationArray.toJSONString());
        this.updateById(configAdCombination);
        return new PostResult();
    }

    /**
     * @param id 广告合集ID
     * @return 广告合集数组
     */
    private JSONArray getAdCombinationArray(String id) {
        ConfigAdCombination configAdCombination = this.getById(id);
        if (Objects.nonNull(configAdCombination) && StringUtils.isNotBlank(configAdCombination.getCombinationJson())) {
            return JSONArray.parseArray(configAdCombination.getCombinationJson());
        }
        return new JSONArray();
    }

    /**
     * 获取指定的广告位置参数
     *
     * @param id         广告合集ID
     * @param positionId 广告位置ID
     * @return 广告位置参数
     */
    private JSONObject getAdPosition(String id, String positionId) {
        JSONArray adCombinationArray = this.getAdCombinationArray(id);
        for (int i = 0; i < adCombinationArray.size(); i++) {
            JSONObject positionObject = adCombinationArray.getJSONObject(i);
            String adPositionId = positionObject.getString("positionId");
            if (StringUtils.equals(positionId, adPositionId)) {
                return positionObject;
            }
        }
        return new JSONObject();
    }

    /**
     * @param id         广告合集ID
     * @param positionId 广告位置ID
     * @param spaceId    广告位ID
     * @return 广告位参数
     */
    private JSONObject getAdSpace(String id, String positionId, String spaceId) {
        JSONObject adPositionObject = this.getAdPosition(id, positionId);
        JSONArray spaceArray = adPositionObject.getJSONArray("spaces");
        for (int i = 0; i < spaceArray.size(); i++) {
            JSONObject adSpaceObject = spaceArray.getJSONObject(i);
            String adSpaceId = adSpaceObject.getString("spaceId");
            if (StringUtils.equals(spaceId, adSpaceId)) {
                return adSpaceObject;
            }
        }
        return new JSONObject();
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


}
