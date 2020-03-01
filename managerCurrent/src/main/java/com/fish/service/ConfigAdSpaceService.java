package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.ConfigAdSpaceMapper;
import com.fish.dao.second.model.ConfigAdSpace;
import com.fish.protocols.GetParameter;
import com.fish.protocols.PostResult;
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

    @Override
    public void setDefaultSort(GetParameter parameter) { }

    @Override
    public Class getClassInfo() { return ConfigAdSpace.class; }

    @Override
    public boolean removeIf(ConfigAdSpace configAdSpace, JSONObject searchData) { return false; }

    @Override
    public List selectAll(GetParameter parameter) { return this.adSpaceMapper.selectAll(); }

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
        }
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
        }
        return result;
    }

    /**
     * 根据ID删除广告内容
     *
     * @param id
     * @return
     */
    public PostResult delete(int id) {
        PostResult result = new PostResult();
        int delete = this.adSpaceMapper.delete(id);
        if (delete <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        }
        return result;
    }

}
