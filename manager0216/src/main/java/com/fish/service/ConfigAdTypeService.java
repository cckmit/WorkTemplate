package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.model.ManageUser;
import com.fish.dao.second.mapper.ConfigAdTypeMapper;
import com.fish.dao.second.model.ConfigAdType;
import com.fish.protocols.GetParameter;
import com.fish.protocols.PostResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 广告类型Service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-26 19:57
 */
@Service
public class ConfigAdTypeService extends CacheService<ConfigAdType> implements BaseService<ConfigAdType> {

    @Autowired
    ConfigAdTypeMapper configAdTypeMapper;

    @Override
    public void setDefaultSort(GetParameter parameter) {
    }

    @Override
    public Class<ConfigAdType> getClassInfo() {
        return ConfigAdType.class;
    }

    @Override
    public boolean removeIf(ConfigAdType configAdType, JSONObject searchData) {
        return false;
    }

    @Override
    public List<ConfigAdType> selectAll(GetParameter parameter) {
        return this.configAdTypeMapper.selectAll();
    }

    public ConfigAdType selectByPrimaryKey(int id) {
        return this.configAdTypeMapper.select(id);
    }

    /**
     * 新增广告类型
     *
     * @param configAdType
     * @return
     */
    public PostResult insert(ConfigAdType configAdType) {
        PostResult postResult = new PostResult();
        int id = this.configAdTypeMapper.insert(configAdType);
        if (id <= 0) {
            postResult.setSuccessed(false);
            postResult.setMsg("操作失败，请联系管理员！");
        }

        //cacheService.updateAllConfigAdTypes();
        return postResult;
    }

    /**
     * 更新广告类型
     *
     * @param configAdType
     * @return
     */
    public PostResult update(ConfigAdType configAdType) {
        PostResult postResult = new PostResult();
        int id = this.configAdTypeMapper.update(configAdType);
        if (id <= 0) {
            postResult.setSuccessed(false);
            postResult.setMsg("操作失败，请联系管理员！");
        }
        return postResult;
    }

    /**
     * select组件数据
     *
     * @param getParameter
     * @return
     */
    public List<ConfigAdType> selectAllAdType(GetParameter getParameter) {
        List<ConfigAdType> configAdTypes = configAdTypeMapper.selectAll();
        for (ConfigAdType configAdType : configAdTypes) {
            Integer ddId = configAdType.getDdId();
            String ddName = configAdType.getDdName();
            configAdType.setDdName(ddId + "-" + ddName);
        }
        return configAdTypes;
    }

    @Override
    void updateAllCache(ConcurrentHashMap<String, ConfigAdType> map) {
        List<ConfigAdType> configAdTypes = this.configAdTypeMapper.selectAll();
        configAdTypes.forEach(configAdType -> {
            map.put(String.valueOf(configAdType.getDdId()), configAdType);
        });
    }

    @Override
    ConfigAdType queryEntity(Class<ConfigAdType> clazz, String key) {
        return this.configAdTypeMapper.select(Integer.valueOf(key));
    }
}
