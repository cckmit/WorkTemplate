package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.ConfigAdContentMapper;
import com.fish.dao.second.mapper.ConfigAdSpaceMapper;
import com.fish.dao.second.model.ConfigAdContent;
import com.fish.dao.second.model.ConfigAdSpace;
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
public class ConfigAdSpaceService implements BaseService<ConfigAdSpace> {

    @Autowired
    ConfigAdSpaceMapper adSpaceMapper;

    @Autowired
    ConfigAdContentMapper adContentMapper;

    @Autowired
    CacheService cacheService;

    @Autowired
    BaseConfig baseConfig;

    @Override
    public void setDefaultSort(GetParameter parameter) {
    }

    @Override
    public Class getClassInfo() {
        return ConfigAdSpace.class;
    }

    @Override
    public boolean removeIf(ConfigAdSpace configAdSpace, JSONObject searchData) {
        return false;
    }

    @Override
    public List<ConfigAdSpace> selectAll(GetParameter parameter) {
        // 数据查询
        ConfigAdSpace adSpace = new ConfigAdSpace();
        if (StringUtils.isNotBlank(parameter.getSearchData())) {
            JSONObject searchObject = JSONObject.parseObject(parameter.getSearchData());
            String adType = searchObject.getString("adType");
            adSpace.setDdAdType(StringUtils.isNotBlank(adType) ? Integer.parseInt(adType) : 0);
            adSpace.setDdName(searchObject.getString("name"));
        }
        List<ConfigAdSpace> configAdSpaces = this.adSpaceMapper.selectAll(adSpace);

        // 数据处理
        for (ConfigAdSpace configAdSpace : configAdSpaces) {
            // 根据广告内容ID，拼接广告名称
            if (StringUtils.isNotBlank(configAdSpace.getDdAdContents())) {
                String ddAdContents = null;
                String[] splitContents = configAdSpace.getDdAdContents().split(",");
                //处理广告内容数据
                for (String contentId : splitContents) {
                    ConfigAdContent configAdContents = this.cacheService.getConfigAdContents(
                            Integer.parseInt(contentId));
                    //防止表内没有对应ID数据异常
                    if (configAdContents != null) {
                        String contentName = configAdContents.getDdTargetAppName();
                        if (StringUtils.isBlank(ddAdContents)) {
                            ddAdContents = contentId + "-" + contentName;
                        } else {
                            ddAdContents += ", " + contentId + "-" + contentName;
                        }
                    }
                }
                configAdSpace.setAdContentNames(ddAdContents);
            }
        }
        return configAdSpaces;
    }

    /**
     * 新增广告内容
     *
     * @param adSpace
     * @return
     */
    public PostResult insert(ConfigAdSpace adSpace) {
        PostResult result = new PostResult();
        int id = this.adSpaceMapper.insert(adSpace);
        if (id <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，新增广告内容失败！");
        } else {
            ReadJsonUtil.flushTable("config_ad_space", this.baseConfig.getFlushCache());
        }
        cacheService.updateAllConfigAdSpaces();
        return result;
    }

    /**
     * 修改广告内容
     *
     * @param adSpace
     * @return
     */
    public PostResult update(ConfigAdSpace adSpace) {
        PostResult result = new PostResult();
        int update = this.adSpaceMapper.update(adSpace);
        if (update <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        } else {
            ReadJsonUtil.flushTable("config_ad_space", this.baseConfig.getFlushCache());
        }
        cacheService.updateAllConfigAdSpaces();
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
        int delete = this.adSpaceMapper.delete(deleteIds);
        if (delete <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        } else {
            ReadJsonUtil.flushTable("config_ad_space", this.baseConfig.getFlushCache());
        }
        cacheService.updateAllConfigAdSpaces();
        return result;
    }


    /**
     * select组件数据
     *
     * @return
     */
    public List<ConfigAdSpace> selectAllSpace() {
        return this.adSpaceMapper.selectAll(new ConfigAdSpace());
    }

    /**
     * 通过ID查询查询指定广告内容
     *
     * @param id 广告位置ID
     * @return 广告内容
     */
    public ConfigAdSpace getConfigAdSpace(int id) {
        return id == 0 ? null : this.adSpaceMapper.select(id);
    }

    /**
     * 通过广告位查询广告内容列表
     *
     * @param spaceId
     * @return
     */
    public List<ConfigAdContent> selectContentBySpaceId(int spaceId) {
        ConfigAdSpace configAdSpace = this.adSpaceMapper.select(spaceId);
        if (configAdSpace != null && StringUtils.isNotBlank(configAdSpace.getDdAdContents())) {
            System.out.println("configAdSpace.getDdAdContents():" + configAdSpace.getDdAdContents());
            List<ConfigAdContent> adContentList = this.adContentMapper.selectContentBySpaceId(
                    configAdSpace.getDdAdContents());
            System.out.println(adContentList.size());
            return adContentList;
        }
        return null;
    }
}
