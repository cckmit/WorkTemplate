package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.ConfigAdPositionMapper;
import com.fish.dao.second.mapper.ConfigAdSpaceMapper;
import com.fish.dao.second.model.ConfigAdPosition;
import com.fish.dao.second.model.ConfigAdSpace;
import com.fish.dao.second.model.ConfigAdStrategy;
import com.fish.protocols.GetParameter;
import com.fish.protocols.PostResult;
import com.fish.service.cache.CacheService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-28 19:07
 */
@Service
public class ConfigAdPositionService implements BaseService<ConfigAdPosition> {

    @Autowired
    ConfigAdPositionMapper adPositionMapper;

    @Autowired
    ConfigAdSpaceMapper adSpaceMapper;

    @Autowired
    CacheService cacheService;

    @Autowired
    BaseConfig baseConfig;

    @Autowired
    ConfigAdStrategyService adStrategyService;

    @Override
    public void setDefaultSort(GetParameter parameter) {
    }

    @Override
    public Class getClassInfo() {
        return ConfigAdPositionService.class;
    }

    @Override
    public boolean removeIf(ConfigAdPosition configAdPosition, JSONObject searchData) {
        return false;
    }

    @Override
    public List<ConfigAdPosition> selectAll(GetParameter parameter) {
        ConfigAdPosition adPosition = new ConfigAdPosition();
        if (StringUtils.isNotBlank(parameter.getSearchData())) {
            JSONObject searchObject = JSONObject.parseObject(parameter.getSearchData());
            adPosition.setDdAdTypes(searchObject.getString("adType"));
            adPosition.setDdName(searchObject.getString("name"));
        }
        List<ConfigAdPosition> list = this.adPositionMapper.selectAll(adPosition);

        ConcurrentHashMap<String, ConfigAdStrategy> sMap = this.adStrategyService.getAll(ConfigAdStrategy.class);
        for (ConfigAdPosition configAdPosition : list) {
            // 处理广告类型
            if (StringUtils.isNotBlank(configAdPosition.getDdAdTypes())) {
                String[] adTypeArray = configAdPosition.getDdAdTypes().split(",");
                String adTypeNames = "";
                for (String typeId : adTypeArray) {
                    String adTypeName = this.cacheService.getConfigAdTypes(Integer.parseInt(typeId)).getDdName();
                    if (StringUtils.isBlank(adTypeNames)) {
                        adTypeNames = typeId + "-" + adTypeName;
                    } else {
                        adTypeNames += "," + typeId + "-" + adTypeName;
                    }
                }
                configAdPosition.setAdTypeNames(adTypeNames);
            }

            // 处理广告内容
            if (StringUtils.isNotBlank(configAdPosition.getDdAdSpaces())) {
                String[] spaceIdArray = configAdPosition.getDdAdSpaces().split(",");
                String adSpaceNames = "";
                for (String spaceId : spaceIdArray) {
                    String adSpaceName = this.cacheService.getConfigAdSpaces(Integer.parseInt(spaceId)).getDdName();
                    if (StringUtils.isBlank(adSpaceNames)) {
                        adSpaceNames = spaceId + "-" + adSpaceName;
                    } else {
                        adSpaceNames += "," + spaceId + "-" + adSpaceName;
                    }
                }
                configAdPosition.setAdSpaceNames(adSpaceNames);
            }

            // 处理策略ID
            configAdPosition.setAdStrategyNames(
                    sMap.get(String.valueOf(configAdPosition.getDdStrategyId())).getDdName());
        }
        return list;
    }

    /**
     * 新增广告位置
     *
     * @param adPosition
     * @return
     */
    public PostResult insert(ConfigAdPosition adPosition) {
        PostResult result = new PostResult();
        int id = this.adPositionMapper.insert(adPosition);
        if (id <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，新增广告内容失败！");
        } else {
            ReadJsonUtil.flushTable("config_ad_position", this.baseConfig.getFlushCache());
        }
        return result;
    }

    /**
     * 修改广告内容
     *
     * @param adContent
     * @return
     */
    public PostResult update(ConfigAdPosition adContent) {
        PostResult result = new PostResult();
        int update = this.adPositionMapper.update(adContent);
        if (update <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        } else {
            ReadJsonUtil.flushTable("config_ad_position", this.baseConfig.getFlushCache());
        }
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
        int delete = this.adPositionMapper.delete(deleteIds);
        if (delete <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        } else {
            ReadJsonUtil.flushTable("config_ad_position", this.baseConfig.getFlushCache());
        }
        return result;
    }

    /**
     * 通过ID查询广告位置参数
     *
     * @param id 广告位置ID
     * @return 广告位置配置
     */
    public ConfigAdPosition getConfigAdPosition(int id) {
        if (id == 0) {
            return null;
        }
        return this.adPositionMapper.select(id);
    }

    /**
     * 查询广告位置查询下拉框选项
     *
     * @return
     */
    public List<ConfigAdPosition> selectAll() {
        return this.adPositionMapper.selectAll(new ConfigAdPosition());
    }

    /**
     * 根据广告位置查询广告位内容
     *
     * @param positionId
     * @return
     */
    public List<ConfigAdSpace> selectSpaceByPositionId(int positionId) {
        ConfigAdPosition configAdPosition = this.adPositionMapper.select(positionId);
        if (configAdPosition != null && StringUtils.isNotBlank(configAdPosition.getDdAdSpaces())) {
            return this.adSpaceMapper.selectAllByIds(configAdPosition.getDdAdSpaces());
        }
        return null;
    }

    /**
     * 更新状态
     *
     * @param jsonObject
     * @return
     */
    public PostResult changeStatus(JSONObject jsonObject) {
        int count = 0;
        PostResult result = new PostResult();
        Integer ddId = jsonObject.getInteger("ddId");
        Boolean ddAllowedOperation = jsonObject.getBoolean("ddAllowedOperation");
        Boolean ddShowWxAd = jsonObject.getBoolean("ddShowWxAd");
        Boolean ddShowWxReVideoAd = jsonObject.getBoolean("ddShowWxReVideoAd");
        // 更新运营状态
        if (ddAllowedOperation != null) {
            count = this.adPositionMapper.changeDdAllowedOperation(ddId, ddAllowedOperation);
        }
        // 更细是否显示微信
        if (ddShowWxAd != null) {
            count = this.adPositionMapper.changeDdShowWxAd(ddId, ddShowWxAd);
        }
        // 更新是否显示激励视频
        if (ddShowWxReVideoAd != null) {
            count = this.adPositionMapper.changeDdShowWxReVideoAd(ddId, ddShowWxReVideoAd);
        }
        if (count <= 0) {
            result.setSuccessed(false);
            result.setMsg("更新失败");
        } else {
            ReadJsonUtil.flushTable("config_ad_position", this.baseConfig.getFlushCache());
        }
        return result;
    }

}
