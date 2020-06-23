package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.ConfigAdContent;
import com.cc.manager.modules.jj.entity.ConfigAdContentPool;
import com.cc.manager.modules.jj.mapper.ConfigAdContentPoolMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 广告内容合集
 *
 * @author cf
 * @since 2020-06-19
 */
@Service
public class ConfigAdContentPoolService extends BaseCrudService<ConfigAdContentPool, ConfigAdContentPoolMapper> {

    private static final String BANNER_TYPE = "1";
    private static final String INIT_TYPE = "3";
    private static final String ICON_TYPE = "4";
    private ConfigAdContentService configAdContentService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<ConfigAdContentPool> queryWrapper) {

    }

    @Override
    protected void updateInsertEntity(String requestParam, ConfigAdContentPool entity) {
        JSONObject contentIdsObject = new JSONObject();
        contentIdsObject.put(BANNER_TYPE, new JSONArray().toJSONString());
        contentIdsObject.put(INIT_TYPE, new JSONArray().toJSONString());
        contentIdsObject.put(ICON_TYPE, new JSONArray().toJSONString());
        entity.setContentIds(contentIdsObject.toJSONString());
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<ConfigAdContentPool> deleteWrapper) {
        if (StringUtils.isNotBlank(requestParam)) {
            List<String> idList = JSONObject.parseArray(requestParam, String.class);
            return this.removeByIds(idList);
        }
        return false;
    }

    /**
     * 修改名称
     *
     * @param requestParam 修改参数
     * @return 修改结果
     */
    public PostResult updateName(String requestParam) {
        PostResult postResult = new PostResult();
        JSONObject requestObject = JSONObject.parseObject(requestParam);
        UpdateWrapper<ConfigAdContentPool> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set(true, "ddName", requestObject.getString("name"));
        updateWrapper.eq(true, "ddId", requestObject.getString("id"));
        if (!this.update(updateWrapper)) {
            postResult.setCode(2);
            postResult.setMsg("名称修改失败！");
        }
        return postResult;
    }

    /**
     * 获取广告内容显示表
     *
     * @param adCombination 广告合集ID
     * @return 广告内容JSONObject
     */
    public JSONObject getAdContentTable(String adCombination) {
        JSONObject returnObject = new JSONObject();
        ConfigAdContentPool configAdContentPool = this.getById(adCombination);
        if (Objects.nonNull(configAdContentPool) && StringUtils.isNotBlank(configAdContentPool.getContentIds())) {
            JSONObject contentObject = JSON.parseObject(configAdContentPool.getContentIds());
            JSONArray bannerList = JSONArray.parseArray(contentObject.getString(BANNER_TYPE));
            JSONArray initList = JSONArray.parseArray(contentObject.getString(INIT_TYPE));
            JSONArray iconList = JSONArray.parseArray(contentObject.getString(ICON_TYPE));
            JSONArray returnPositionArray = new JSONArray();
            dealPositionObject(bannerList, returnPositionArray);
            dealPositionObject(initList, returnPositionArray);
            dealPositionObject(iconList, returnPositionArray);
            returnObject.put("data", returnPositionArray);
            returnObject.put("code", 1);
        } else {
            returnObject.put("data", new JSONArray());
            returnObject.put("code", 1);
        }

        return returnObject;
    }

    /**
     * 处理不同类型List
     *
     * @param list                list
     * @param returnPositionArray 返回数组对象
     */
    private void dealPositionObject(JSONArray list, JSONArray returnPositionArray) {
        for (int i = 0; i < list.size(); i++) {
            Object contentId = list.get(i);
            ConfigAdContent configAdContent = this.configAdContentService.getCacheEntity(ConfigAdContent.class, contentId.toString());
            JSONObject resultPositionObject = new JSONObject();
            resultPositionObject.put("id", contentId.toString());
            resultPositionObject.put("name", configAdContent.getAdTypeName());
            resultPositionObject.put("index", i + 1);
            resultPositionObject.put("targetAppId", configAdContent.getTargetAppId());
            resultPositionObject.put("targetAppName", configAdContent.getTargetAppName());
            resultPositionObject.put("imageUrl", configAdContent.getImageUrl());
            returnPositionArray.add(resultPositionObject);
        }
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
        JSONObject contentIdsObject = this.getAdContentIdsObject(addAdContentObject.getString("id"));
        List<Integer> newContentIdList = JSONArray.parseArray(addAdContentObject.getString("contentIds"), Integer.class);
        for (Integer contentId : newContentIdList) {
            ConfigAdContent configAdContent = this.configAdContentService.getCacheEntity(ConfigAdContent.class, contentId.toString());
            int adType = configAdContent.getAdType();
            if (adType == Integer.parseInt(BANNER_TYPE)) {
                JSONArray bannerList = JSONArray.parseArray(contentIdsObject.getString(BANNER_TYPE));
                bannerList.add(contentId);
                contentIdsObject.put(BANNER_TYPE, bannerList);
            }
            if (adType == Integer.parseInt(INIT_TYPE)) {
                JSONArray initList = JSONArray.parseArray(contentIdsObject.getString(INIT_TYPE));
                initList.add(contentId);
                contentIdsObject.put(INIT_TYPE, initList);
            }
            if (adType == Integer.parseInt(ICON_TYPE)) {
                JSONArray iconList = JSONArray.parseArray(contentIdsObject.getString(ICON_TYPE));
                iconList.add(contentId);
                contentIdsObject.put(ICON_TYPE, iconList);
            }
        }
        // 保存数据对象
        ConfigAdContentPool configAdContentPool = new ConfigAdContentPool();
        configAdContentPool.setId(addAdContentObject.getInteger("id"));
        configAdContentPool.setContentIds(contentIdsObject.toJSONString());
        this.updateById(configAdContentPool);
        return new PostResult();
    }

    /**
     * 批量删除广告内容
     * {"positionId":"1","deleteContentIdArray":["10","2"],"id":"1"}
     *
     * @param deleteAdContentsObject 删除数据对象
     * @return 删除结果
     */
    public PostResult deleteAdContent(JSONObject deleteAdContentsObject) {
        Map<String, String> bannerMap = new LinkedHashMap<>(16);
        Map<String, Integer> bannerChangeMap = new LinkedHashMap<>(16);
        Map<String, String> initMap = new LinkedHashMap<>(16);
        Map<String, Integer> initChangeMap = new LinkedHashMap<>(16);
        Map<String, String> iconMap = new LinkedHashMap<>(16);
        Map<String, Integer> iconChangeMap = new LinkedHashMap<>(16);
        JSONObject contentIdsObject = this.getAdContentIdsObject(deleteAdContentsObject.getString("id"));
        JSONArray contentIdsArray = deleteAdContentsObject.getJSONArray("contentId");
        for (int i = contentIdsArray.size() - 1; i >= 0; i--) {
            JSONObject jsonObject = contentIdsArray.getJSONObject(i);
            ConfigAdContent configAdContent = this.configAdContentService.getCacheEntity(ConfigAdContent.class, jsonObject.getString("id"));
            if (configAdContent.getAdType() == Integer.parseInt(BANNER_TYPE)) {
                // 添加需要删除的banner数据
                bannerMap.put(jsonObject.getString("index"), jsonObject.getString("id"));
            }
            if (configAdContent.getAdType() == Integer.parseInt(INIT_TYPE)) {
                // 添加需要删除的init数据
                initMap.put(jsonObject.getString("index"), jsonObject.getString("id"));
            }
            if (configAdContent.getAdType() == Integer.parseInt(ICON_TYPE)) {
                // 添加需要删除的icon数据
                iconMap.put(jsonObject.getString("index"), jsonObject.getString("id"));
            }
        }
        JSONArray bannerList = JSONArray.parseArray(contentIdsObject.getString(BANNER_TYPE));
        changeMap(bannerChangeMap, bannerList);
        changeMapRemove(bannerMap, bannerChangeMap);
        contentIdsObject.put(BANNER_TYPE, bannerChangeMap.values());
        JSONArray initList = JSONArray.parseArray(contentIdsObject.getString(INIT_TYPE));
        changeMap(initChangeMap, initList);
        changeMapRemove(initMap, initChangeMap);
        contentIdsObject.put(INIT_TYPE, initChangeMap.values());
        JSONArray iconList = JSONArray.parseArray(contentIdsObject.getString(ICON_TYPE));
        changeMap(iconChangeMap, iconList);
        changeMapRemove(iconMap, iconChangeMap);
        contentIdsObject.put(ICON_TYPE, iconChangeMap.values());
        // 保存数据对象
        ConfigAdContentPool configAdContentPool = new ConfigAdContentPool();
        configAdContentPool.setId(deleteAdContentsObject.getInteger("id"));
        configAdContentPool.setContentIds(contentIdsObject.toJSONString());
        this.updateById(configAdContentPool);
        return new PostResult();
    }

    /**
     * 删除目标数据
     *
     * @param paramMap  paramMap
     * @param changeMap changeMap
     */
    private void changeMapRemove(Map<String, String> paramMap, Map<String, Integer> changeMap) {
        for (String key : paramMap.keySet()) {
            changeMap.remove(key);
        }
    }

    /**
     * 原数据赋值index
     *
     * @param changeMap changeMap
     * @param list      list
     */
    private void changeMap(Map<String, Integer> changeMap, JSONArray list) {
        for (int i = 0; i < list.size(); i++) {
            changeMap.put(String.valueOf(i + 1), Integer.parseInt(list.get(i).toString()));
        }
    }

    /**
     * 复制广告池内容
     *
     * @param combinationIdObject combinationIdObject
     * @return 复制结果
     */
    public PostResult copyContent(JSONObject combinationIdObject) {
        PostResult postResult = new PostResult();
        int copyCount = 0;
        JSONObject contentIdsObject = this.getAdContentIdsObject(combinationIdObject.getString("id"));
        JSONArray combinationIdList = JSONArray.parseArray(combinationIdObject.getString("combinationIdList"));
        for (Object combinationId : combinationIdList) {
            UpdateWrapper<ConfigAdContentPool> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("ddId", combinationId);
            updateWrapper.set("ddContentIds", contentIdsObject.toJSONString());
            boolean update = this.update(updateWrapper);
            if (update) {
                copyCount = copyCount + 1;
            }
        }
        if (copyCount != combinationIdList.size()) {
            postResult.setCode(2);
            postResult.setMsg("复制失败！");
        }
        return postResult;
    }

    /**
     * 保存广告内容序号
     *
     * @param saveAdContentOrderNumObject 保存数据对象
     * @return 保存结果
     */
    public PostResult saveAdContentOrderNum(JSONObject saveAdContentOrderNumObject) {
        String adContentOrderNumArray = "adContentOrderNumArray";
        if (saveAdContentOrderNumObject.getJSONArray(adContentOrderNumArray).size() == 0) {
            PostResult postResult = new PostResult();
            postResult.setCode(0);
            postResult.setMsg("无内容无法进行排序");
            return postResult;
        }
        JSONObject contentIdsObject = this.getAdContentIdsObject(saveAdContentOrderNumObject.getString("id"));
        Map<String, Integer> bannerMap = new LinkedHashMap<>(16);
        Map<String, Integer> initMap = new LinkedHashMap<>(16);
        Map<String, Integer> iconMap = new LinkedHashMap<>(16);
        JSONArray contentOrderNumArray = saveAdContentOrderNumObject.getJSONArray(adContentOrderNumArray);
        for (int i = 0; i < contentOrderNumArray.size(); i++) {
            JSONObject contentObject = contentOrderNumArray.getJSONObject(i);
            Integer contentId = contentObject.getInteger("id");
            String index = contentObject.getString("index");
            ConfigAdContent configAdContent = this.configAdContentService.getCacheEntity(ConfigAdContent.class, contentId.toString());
            if (configAdContent.getAdType() == Integer.parseInt(BANNER_TYPE)) {
                bannerMap.put(index, contentId);
            } else if (configAdContent.getAdType() == Integer.parseInt(INIT_TYPE)) {
                initMap.put(index, contentId);
            } else if (configAdContent.getAdType() == Integer.parseInt(ICON_TYPE)) {
                iconMap.put(index, contentId);
            }
        }
        saveContentIndex(contentIdsObject, bannerMap, BANNER_TYPE);
        saveContentIndex(contentIdsObject, initMap, INIT_TYPE);
        saveContentIndex(contentIdsObject, iconMap, ICON_TYPE);
        // 保存数据对象
        ConfigAdContentPool configAdContentPool = new ConfigAdContentPool();
        configAdContentPool.setId(saveAdContentOrderNumObject.getInteger("id"));
        configAdContentPool.setContentIds(contentIdsObject.toJSONString());
        this.updateById(configAdContentPool);
        return new PostResult();
    }

    private void saveContentIndex(JSONObject contentIdsObject, Map<String, Integer> bannerMap, String type) {
        LinkedHashMap<String, Integer> sortedContentOrderMap = new LinkedHashMap<>();
        // 排序
        bannerMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList()).forEach(ele -> sortedContentOrderMap.put(ele.getKey(), ele.getValue()));

        contentIdsObject.put(type, sortedContentOrderMap.values());
    }

    /**
     * @param id 广告合集ID
     * @return 广告内容数组
     */
    private JSONObject getAdContentIdsObject(String id) {
        ConfigAdContentPool configAdContentPool = this.getById(id);
        if (Objects.nonNull(configAdContentPool) && StringUtils.isNotBlank(configAdContentPool.getContentIds())) {
            return JSON.parseObject(configAdContentPool.getContentIds());
        }
        return new JSONObject();
    }

    @Autowired
    public void setConfigAdContentService(ConfigAdContentService configAdContentService) {
        this.configAdContentService = configAdContentService;
    }

}
