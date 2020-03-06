package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.ConfigAdSourceMapper;
import com.fish.dao.second.model.ConfigAdSource;
import com.fish.protocols.GetParameter;
import com.fish.protocols.PostResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 广告来源Service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-26 19:57
 */
@Service
public class ConfigAdSourceService implements BaseService<ConfigAdSource> {

    @Autowired
    ConfigAdSourceMapper configAdSourceMapper;

    @Override
    public void setDefaultSort(GetParameter parameter) { }

    @Override
    public Class<ConfigAdSource> getClassInfo() { return ConfigAdSource.class; }

    @Override
    public boolean removeIf(ConfigAdSource configAdSource, JSONObject searchData) { return false;}

    @Override
    public List<ConfigAdSource> selectAll(GetParameter parameter) { return this.configAdSourceMapper.selectAll(); }

    /**
     * 新增广告来源
     *
     * @param configAdSource
     * @return
     */
    public PostResult insert(ConfigAdSource configAdSource) {
        PostResult postResult = new PostResult();
        int id = this.configAdSourceMapper.insert(configAdSource);
        if (id <= 0) {
            postResult.setSuccessed(false);
            postResult.setMsg("操作失败，请联系管理员！");
        }
        return postResult;
    }

    /**
     * 更新广告来源
     *
     * @param configAdSource
     * @return
     */
    public PostResult update(ConfigAdSource configAdSource) {
        PostResult postResult = new PostResult();
        int id = this.configAdSourceMapper.update(configAdSource);
        if (id <= 0) {
            postResult.setSuccessed(false);
            postResult.setMsg("操作失败，请联系管理员！");
        }
        return postResult;
    }

}
