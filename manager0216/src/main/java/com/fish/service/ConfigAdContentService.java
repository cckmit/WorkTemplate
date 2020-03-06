package com.fish.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.ConfigAdContentMapper;
import com.fish.dao.second.model.ConfigAdContent;
import com.fish.protocols.GetParameter;
import com.fish.protocols.PostResult;
import com.fish.service.cache.CacheService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-28 19:07
 */
@Service
public class ConfigAdContentService implements BaseService<ConfigAdContent> {

    @Autowired
    ConfigAdContentMapper adContentMapper;

    @Autowired
    CacheService cacheService;

    @Autowired
    BaseConfig baseConfig;

    @Override
    public void setDefaultSort(GetParameter parameter) { }

    @Override
    public Class getClassInfo() { return ConfigAdContent.class; }

    @Override
    public boolean removeIf(ConfigAdContent configAdContent, JSONObject searchData) { return false; }

    @Override
    public List<ConfigAdContent> selectAll(GetParameter parameter) {
        ConfigAdContent configAdContent = new ConfigAdContent();
        if (StringUtils.isNotBlank(parameter.getSearchData())) {
            JSONObject searchObject = JSONObject.parseObject(parameter.getSearchData());
            String adType = searchObject.getString("adType");
            configAdContent.setDdAdType(StringUtils.isNotBlank(adType) ? Integer.parseInt(adType) : 0);
            configAdContent.setDdTargetAppId(searchObject.getString("targetAppId"));
            configAdContent.setDdTargetAppName(searchObject.getString("targetAppName"));
            configAdContent.setDdPromoteAppId(searchObject.getString("promoteAppId"));
            configAdContent.setDdPromoteAppName(searchObject.getString("promoteAppName"));
        }
        return this.adContentMapper.selectAll(configAdContent);
    }

    /**
     * 新增广告内容
     *
     * @param adContent
     * @return
     */
    public PostResult insert(ConfigAdContent adContent) {
        PostResult result = new PostResult();
        int id = this.adContentMapper.insert(adContent);
        if (id <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，新增广告内容失败！");
        }
        String res = ReadJsonUtil.flushTable("config_ad_content", this.baseConfig.getFlushCache());
        cacheService.updateAllConfigAdContents();
        return result;
    }

    /**
     * 修改广告内容
     *
     * @param adContent
     * @return
     */
    public PostResult update(ConfigAdContent adContent) {
        PostResult result = new PostResult();
        int update = this.adContentMapper.update(adContent);
        if (update <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        }
        String res = ReadJsonUtil.flushTable("config_ad_content", this.baseConfig.getFlushCache());
        cacheService.updateAllConfigAdContents();
        return result;
    }

    /**
     * 根据ID删除广告内容
     *
     * @param deleteIds
     * @return
     */
    public PostResult delete(String deleteIds) {
        PostResult result = new PostResult();
        int delete = this.adContentMapper.delete(deleteIds);
        if (delete <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        }
        String res = ReadJsonUtil.flushTable("config_ad_content", this.baseConfig.getFlushCache());
        cacheService.updateAllConfigAdContents();
        return result;
    }

    /**
     * 上传时更新图片地址
     *
     * @param imageUrl
     * @param id
     */
    public void updateImageUrl(String imageUrl, String id) {
        try {
            this.adContentMapper.updateImageUrl(imageUrl, Integer.parseInt(id));
        } catch (Exception e) {
            System.out.println("图片名称格式错误！");
        }
    }

    /**
     * select组件数据
     *
     * @param getParameter
     * @return
     */
    public List<ConfigAdContent> selectAllContent(GetParameter getParameter) {
        List<ConfigAdContent> configAdContents = this.adContentMapper.selectAll(new ConfigAdContent());
        for (ConfigAdContent configAdContent : configAdContents) {
            Integer ddId = configAdContent.getDdId();
            String ddName = configAdContent.getDdTargetAppName();
            configAdContent.setAdTypeName(ddId + "-" + ddName);
        }
        return configAdContents;
    }

    /**
     * 通过ID查询广告内容
     *
     * @param id
     * @return
     */
    public ConfigAdContent select(Integer id) {
        return this.adContentMapper.select(id);
    }

    /**
     * 通过广告类型查询广告内容
     *
     * @param adType
     * @return
     */
    public JSONArray getAdContentJsonByType(String adType) {
        if (StringUtils.isBlank(adType)) {
            return null;
        }
        List<ConfigAdContent> list = this.adContentMapper.selectAllByType(Integer.parseInt(adType));
        JSONArray selectArray = new JSONArray();
        for (ConfigAdContent configAdContent : list) {
            JSONObject selectJson = new JSONObject();
            //{name: val.ddName, value: val.ddId, selected: false};
            selectJson.put("name", configAdContent.getDdId() + "-" + configAdContent.getDdTargetAppName());
            selectJson.put("value", configAdContent.getDdId());
            selectJson.put("selected", false);
            selectArray.add(selectJson);
        }
        return selectArray;
    }
}
