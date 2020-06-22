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
import com.cc.manager.modules.jj.mapper.ConfigAdCombinationMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-15 20:35
 */
@Service
@DS("jj")
public class ConfigAdCombinationService2 extends BaseCrudService<ConfigAdCombination, ConfigAdCombinationMapper> {

    private static final String BANNER_TYPE = "1";
    private static final String INIT_TYPE = "3";
    private static final String ICON_TYPE = "4";
    private ConfigAdPositionService configAdPositionService;
    private ConfigAdContentService configAdContentService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<ConfigAdCombination> queryWrapper) {
        queryWrapper.eq("ddType", 2);
    }

    @Override
    protected void updateInsertEntity(String requestParam, ConfigAdCombination entity) {
//        entity.setType(2);
//        entity.setPositionIds(new JSONArray().toJSONString());
//        JSONObject contentIdsObject = new JSONObject();
//        contentIdsObject.put(BANNER_TYPE, new JSONArray().toJSONString());
//        contentIdsObject.put(INIT_TYPE, new JSONArray().toJSONString());
//        contentIdsObject.put(ICON_TYPE, new JSONArray().toJSONString());
//        entity.setContentIds(contentIdsObject.toJSONString());
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
     * 修改名称
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
     * 获取广告位置显示表
     *
     * @param adCombination 广告合集ID
     * @return 广告位置JSONObject
     */
    public JSONObject getAdPositionTable(String adCombination) {
        JSONObject returnObject = new JSONObject();
//        returnObject.put("code", 2);
//        ConfigAdCombination configAdCombination = this.getById(adCombination);
//        if (Objects.nonNull(configAdCombination) && StringUtils.isNotBlank(configAdCombination.getPositionIds())) {
//            JSONArray positionArray = JSONArray.parseArray(configAdCombination.getPositionIds());
//            JSONArray returnPositionArray = new JSONArray();
//            for (Object positionId : positionArray) {
//                ConfigAdPosition configAdPosition = this.configAdPositionService.getCacheEntity(ConfigAdPosition.class, positionId.toString());
//                JSONObject resultPositionObject = new JSONObject();
//                resultPositionObject.put("id", positionId.toString());
//                resultPositionObject.put("name", configAdPosition.getName());
//                resultPositionObject.put("adTypeNames", configAdPosition.getAdTypeNames());
//                returnPositionArray.add(resultPositionObject);
//            }
//            returnObject.put("data", returnPositionArray);
//            returnObject.put("code", 1);
//        } else {
//            returnObject.put("data", new JSONArray());
//            returnObject.put("code", 1);
//        }
        return returnObject;
    }

    /**
     * 获取广告内容显示表
     *
     * @param adCombination 广告合集ID
     * @return 广告内容JSONObject
     */
    public JSONObject getAdContentTable(String adCombination) {
        JSONObject returnObject = new JSONObject();
//        ConfigAdCombination configAdCombination = this.getById(adCombination);
//        if (Objects.nonNull(configAdCombination) && StringUtils.isNotBlank(configAdCombination.getContentIds())) {
//            JSONObject contentObject = JSON.parseObject(configAdCombination.getContentIds());
//            JSONArray bannerList = JSONArray.parseArray(contentObject.getString(BANNER_TYPE));
//            JSONArray initList = JSONArray.parseArray(contentObject.getString(INIT_TYPE));
//            JSONArray iconList = JSONArray.parseArray(contentObject.getString(ICON_TYPE));
//            JSONArray returnPositionArray = new JSONArray();
//            for (int i = 0; i < bannerList.size(); i++) {
//                Object contentId = bannerList.get(i);
//                ConfigAdContent configAdContent = this.configAdContentService.getCacheEntity(ConfigAdContent.class, contentId.toString());
//                JSONObject resultPositionObject = new JSONObject();
//                resultPositionObject.put("id", contentId.toString());
//                resultPositionObject.put("name", configAdContent.getAdTypeName());
//                resultPositionObject.put("index", i + 1);
//                resultPositionObject.put("targetAppId", configAdContent.getTargetAppId());
//                resultPositionObject.put("targetAppName", configAdContent.getTargetAppName());
//                resultPositionObject.put("imageUrl", configAdContent.getImageUrl());
//                returnPositionArray.add(resultPositionObject);
//            }
//            for (int i = 0; i < initList.size(); i++) {
//                Object contentId = initList.get(i);
//                ConfigAdContent configAdContent = this.configAdContentService.getCacheEntity(ConfigAdContent.class, contentId.toString());
//                JSONObject resultPositionObject = new JSONObject();
//                resultPositionObject.put("id", contentId.toString());
//                resultPositionObject.put("name", configAdContent.getAdTypeName());
//                resultPositionObject.put("index", i + 1);
//                resultPositionObject.put("targetAppId", configAdContent.getTargetAppId());
//                resultPositionObject.put("targetAppName", configAdContent.getTargetAppName());
//                resultPositionObject.put("imageUrl", configAdContent.getImageUrl());
//                returnPositionArray.add(resultPositionObject);
//            }
//            for (int i = 0; i < iconList.size(); i++) {
//                Object contentId = iconList.get(i);
//                ConfigAdContent configAdContent = this.configAdContentService.getCacheEntity(ConfigAdContent.class, contentId.toString());
//                JSONObject resultPositionObject = new JSONObject();
//                resultPositionObject.put("id", contentId.toString());
//                resultPositionObject.put("name", configAdContent.getAdTypeName());
//                resultPositionObject.put("index", i + 1);
//                resultPositionObject.put("targetAppId", configAdContent.getTargetAppId());
//                resultPositionObject.put("targetAppName", configAdContent.getTargetAppName());
//                resultPositionObject.put("imageUrl", configAdContent.getImageUrl());
//                returnPositionArray.add(resultPositionObject);
//            }
//            returnObject.put("data", returnPositionArray);
//            returnObject.put("code", 1);
//        } else {
//            returnObject.put("data", new JSONArray());
//            returnObject.put("code", 1);
//        }

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
//        JSONArray positionIdsArray = this.getAdPositionIdsArray(addPositionObject.getString("id"));
//        List<Integer> addPositionIdList = JSON.parseArray(addPositionObject.getString("positionIds"), Integer.class);
//        for (int i = positionIdsArray.size() - 1; i >= 0; i--) {
//            for (Integer integer : addPositionIdList) {
//                if (StringUtils.equals(integer.toString(), positionIdsArray.get(i).toString())) {
//                    postResult.setCode(2);
//                    postResult.setMsg("广告配置配置重复");
//                    return postResult;
//                }
//            }
//        }
//        positionIdsArray.addAll(addPositionIdList);
//        // 保存数据对象
//        ConfigAdCombination configAdCombination = new ConfigAdCombination();
//        configAdCombination.setId(addPositionObject.getInteger("id"));
//        configAdCombination.setType(2);
//        configAdCombination.setPositionIds(positionIdsArray.toJSONString());
//        this.updateById(configAdCombination);
        return postResult;
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
//        JSONObject contentIdsObject = this.getAdContentIdsObject(addAdContentObject.getString("id"));
//        List<Integer> newContentIdList = JSONArray.parseArray(addAdContentObject.getString("contentIds"), Integer.class);
//        for (Integer contentId : newContentIdList) {
//            ConfigAdContent configAdContent = this.configAdContentService.getCacheEntity(ConfigAdContent.class, contentId.toString());
//            int adType = configAdContent.getAdType();
//            if (adType == 1) {
//                String banner = contentIdsObject.getString(BANNER_TYPE);
//                JSONArray bannerList = JSONArray.parseArray(banner);
//                bannerList.add(contentId);
//                contentIdsObject.put(BANNER_TYPE, bannerList);
//            }
//            if (adType == 3) {
//                String init = contentIdsObject.getString(INIT_TYPE);
//                JSONArray initList = JSONArray.parseArray(init);
//                initList.add(contentId);
//                contentIdsObject.put(INIT_TYPE, initList);
//            }
//            if (adType == 4) {
//                String icon = contentIdsObject.getString(ICON_TYPE);
//                JSONArray iconList = JSONArray.parseArray(icon);
//                iconList.add(contentId);
//                contentIdsObject.put(ICON_TYPE, iconList);
//            }
//        }
//        // 保存数据对象
//        ConfigAdCombination configAdCombination = new ConfigAdCombination();
//        configAdCombination.setId(addAdContentObject.getInteger("id"));
//        configAdCombination.setType(2);
//        configAdCombination.setContentIds(contentIdsObject.toJSONString());
//        this.updateById(configAdCombination);
        return new PostResult();
    }

    /**
     * 批量删除广告位置
     * {"id":"1","positionId":1}
     *
     * @param deleteAdPositionsObject 批量删除广告位置
     * @return 新增结果
     */
    public PostResult deleteAdPosition(@RequestBody JSONObject deleteAdPositionsObject) {
//        JSONArray positionArray = this.getAdPositionIdsArray(deleteAdPositionsObject.getString("id"));
//        JSONArray positionIds = JSONArray.parseArray(deleteAdPositionsObject.getString("positionIds"));
//        for (Object positionId : positionIds) {
//            for (int i = positionArray.size() - 1; i >= 0; i--) {
//                if (StringUtils.equals(positionArray.get(i).toString(), positionId.toString())) {
//                    positionArray.remove(i);
//                }
//            }
//        }
//        // 保存数据对象
//        ConfigAdCombination configAdCombination = new ConfigAdCombination();
//        configAdCombination.setId(deleteAdPositionsObject.getInteger("id"));
//        configAdCombination.setType(2);
//        configAdCombination.setPositionIds(positionArray.toJSONString());
//        this.updateById(configAdCombination);
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
//        Map<String, String> bannerMap = new LinkedHashMap<>(16);
//        Map<String, String> bannerChangeMap = new LinkedHashMap<>(16);
//        Map<String, String> initMap = new LinkedHashMap<>(16);
//        Map<String, String> initChangeMap = new LinkedHashMap<>(16);
//        Map<String, String> iconMap = new LinkedHashMap<>(16);
//        Map<String, String> iconChangeMap = new LinkedHashMap<>(16);
//        JSONObject contentIdsObject = this.getAdContentIdsObject(deleteAdContentsObject.getString("id"));
//        JSONArray contentIdsArray = deleteAdContentsObject.getJSONArray("contentId");
//        for (int i = contentIdsArray.size() - 1; i >= 0; i--) {
//            JSONObject jsonObject = contentIdsArray.getJSONObject(i);
//            ConfigAdContent configAdContent = this.configAdContentService.getCacheEntity(ConfigAdContent.class, jsonObject.getString("id"));
//            if (configAdContent.getAdType() == Integer.parseInt(BANNER_TYPE)) {
//                // 添加需要删除的banner数据
//                bannerMap.put(jsonObject.getString("index"), jsonObject.getString("id"));
//            }
//            if (configAdContent.getAdType() == Integer.parseInt(INIT_TYPE)) {
//                // 添加需要删除的init数据
//                initMap.put(jsonObject.getString("index"), jsonObject.getString("id"));
//            }
//            if (configAdContent.getAdType() == Integer.parseInt(ICON_TYPE)) {
//                // 添加需要删除的icon数据
//                iconMap.put(jsonObject.getString("index"), jsonObject.getString("id"));
//            }
//        }
//        JSONArray bannerList = JSONArray.parseArray(contentIdsObject.getString(BANNER_TYPE));
//        for (int i = 0; i < bannerList.size(); i++) {
//            bannerChangeMap.put(String.valueOf(i + 1), bannerList.get(i).toString());
//        }
//        for (String key : bannerMap.keySet()) {
//            bannerChangeMap.remove(key);
//        }
//        contentIdsObject.put(BANNER_TYPE, JSON.toJSONString(bannerChangeMap.values()));
//        JSONArray initList = JSONArray.parseArray(contentIdsObject.getString(INIT_TYPE));
//        for (int i = 0; i < initList.size(); i++) {
//            initChangeMap.put(String.valueOf(i + 1), initList.get(i).toString());
//        }
//        for (String key : initMap.keySet()) {
//            initChangeMap.remove(key);
//        }
//        contentIdsObject.put(INIT_TYPE, JSON.toJSONString(initChangeMap.values()));
//        JSONArray iconList = JSONArray.parseArray(contentIdsObject.getString(ICON_TYPE));
//        for (int i = 0; i < iconList.size(); i++) {
//            iconChangeMap.put(String.valueOf(i + 1), iconList.get(i).toString());
//        }
//        for (String key : iconMap.keySet()) {
//            iconChangeMap.remove(key);
//        }
//        contentIdsObject.put(ICON_TYPE, JSON.toJSONString(iconChangeMap.values()));
//        // 保存数据对象
//        ConfigAdCombination configAdCombination = new ConfigAdCombination();
//        configAdCombination.setId(deleteAdContentsObject.getInteger("id"));
//        configAdCombination.setType(2);
//        configAdCombination.setContentIds(contentIdsObject.toJSONString());
//        this.updateById(configAdCombination);
        return new PostResult();
    }

    /**
     * 复制广告位置内容
     *
     * @param combinationIdObject combinationIdObject
     * @return 复制结果
     */
    public PostResult copyPosition(JSONObject combinationIdObject) {
        PostResult postResult = new PostResult();
        postResult.setMsg("复制成功，请注意查看");
        int copyCount = 0;
        JSONArray positionIdsArray = this.getAdPositionIdsArray(combinationIdObject.getString("id"));
        JSONArray combinationIdList = JSONArray.parseArray(combinationIdObject.getString("combinationIdList"));
        for (Object combinationId : combinationIdList) {
            UpdateWrapper<ConfigAdCombination> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("ddId", combinationId);
            updateWrapper.set("ddPositionIds", positionIdsArray.toJSONString());
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
            UpdateWrapper<ConfigAdCombination> updateWrapper = new UpdateWrapper<>();
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
//        String adContentOrderNumArray = "adContentOrderNumArray";
//        if (saveAdContentOrderNumObject.getJSONArray(adContentOrderNumArray).size() == 0) {
//            PostResult postResult = new PostResult();
//            postResult.setCode(0);
//            postResult.setMsg("无内容无法进行排序");
//            return postResult;
//        }
//        JSONObject contentIdsObject = this.getAdContentIdsObject(saveAdContentOrderNumObject.getString("id"));
//        Map<String, String> bannerMap = new LinkedHashMap<>(16);
//        Map<String, String> initMap = new LinkedHashMap<>(16);
//        Map<String, String> iconMap = new LinkedHashMap<>(16);
//        JSONArray contentOrderNumArray = saveAdContentOrderNumObject.getJSONArray(adContentOrderNumArray);
//        for (int i = 0; i < contentOrderNumArray.size(); i++) {
//            JSONObject contentObject = contentOrderNumArray.getJSONObject(i);
//            String contentId = contentObject.getString("id");
//            String index = contentObject.getString("index");
//            ConfigAdContent configAdContent = this.configAdContentService.getCacheEntity(ConfigAdContent.class, contentId);
//            if (configAdContent.getAdType() == Integer.parseInt(BANNER_TYPE)) {
//                bannerMap.put(index, contentId);
//            } else if (configAdContent.getAdType() == Integer.parseInt(INIT_TYPE)) {
//                initMap.put(index, contentId);
//            } else if (configAdContent.getAdType() == Integer.parseInt(ICON_TYPE)) {
//                iconMap.put(index, contentId);
//            }
//        }
//        LinkedHashMap<String, String> sortedContentOrderBannerMap = new LinkedHashMap<>();
//        // banner排序
//        bannerMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
//                .collect(Collectors.toList()).forEach(ele -> sortedContentOrderBannerMap.put(ele.getKey(), ele.getValue()));
//        System.out.println(JSON.toJSONString(sortedContentOrderBannerMap.values()));
//        contentIdsObject.put(BANNER_TYPE, JSON.toJSONString(sortedContentOrderBannerMap.values()));
//        LinkedHashMap<String, String> sortedContentOrderInitMap = new LinkedHashMap<>();
//        // init排序
//        initMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
//                .collect(Collectors.toList()).forEach(ele -> sortedContentOrderInitMap.put(ele.getKey(), ele.getValue()));
//        System.out.println(JSON.toJSONString(sortedContentOrderInitMap.values()));
//        contentIdsObject.put(INIT_TYPE, JSON.toJSONString(sortedContentOrderInitMap.values()));
//
//        LinkedHashMap<String, String> sortedContentOrderIconMap = new LinkedHashMap<>();
//        // icon排序
//        iconMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
//                .collect(Collectors.toList()).forEach(ele -> sortedContentOrderIconMap.put(ele.getKey(), ele.getValue()));
//        System.out.println(JSON.toJSONString(sortedContentOrderIconMap.values()));
//        contentIdsObject.put(ICON_TYPE, JSON.toJSONString(sortedContentOrderIconMap.values()));
//
//        // 保存数据对象
//        ConfigAdCombination configAdCombination = new ConfigAdCombination();
//        configAdCombination.setId(saveAdContentOrderNumObject.getInteger("id"));
//        configAdCombination.setType(2);
//        configAdCombination.setContentIds(contentIdsObject.toJSONString());
//        this.updateById(configAdCombination);
        return new PostResult();
    }

    /**
     * @param id 广告合集ID
     * @return 广告位置数组
     */
    private JSONArray getAdPositionIdsArray(String id) {
//        ConfigAdCombination configAdCombination = this.getById(id);
//        if (Objects.nonNull(configAdCombination) && StringUtils.isNotBlank(configAdCombination.getPositionIds())) {
//            return JSONArray.parseArray(configAdCombination.getPositionIds());
//        }
        return new JSONArray();
    }

    /**
     * @param id 广告合集ID
     * @return 广告内容数组
     */
    private JSONObject getAdContentIdsObject(String id) {
//        ConfigAdCombination configAdCombination = this.getById(id);
//        if (Objects.nonNull(configAdCombination) && StringUtils.isNotBlank(configAdCombination.getContentIds())) {
//            return JSON.parseObject(configAdCombination.getContentIds());
//        }
        return new JSONObject();
    }

    @Autowired
    public void setConfigAdPositionService(ConfigAdPositionService configAdPositionService) {
        this.configAdPositionService = configAdPositionService;
    }

    @Autowired
    public void setConfigAdContentService(ConfigAdContentService configAdContentService) {
        this.configAdContentService = configAdContentService;
    }

}
