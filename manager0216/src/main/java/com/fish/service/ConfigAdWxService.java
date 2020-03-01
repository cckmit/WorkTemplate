package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.ConfigAdContentMapper;
import com.fish.dao.second.mapper.ConfigAdWxMapper;
import com.fish.dao.second.model.ConfigAdWx;
import com.fish.dao.second.model.ConfigAdWx;
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
public class ConfigAdWxService implements BaseService<ConfigAdWx> {

    @Autowired
    ConfigAdWxMapper adWxMapper;

    @Override
    public void setDefaultSort(GetParameter parameter) { }

    @Override
    public Class getClassInfo() { return ConfigAdWx.class; }

    @Override
    public boolean removeIf(ConfigAdWx configAdContent, JSONObject searchData) { return false; }

    @Override
    public List selectAll(GetParameter parameter) { return this.adWxMapper.selectAll(); }

    /**
     * 新增广告内容
     *
     * @param adContent
     * @return
     */
    public PostResult insert(ConfigAdWx adContent) {
        PostResult result = new PostResult();
        int id = this.adWxMapper.insert(adContent);
        if (id <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，新增广告内容失败！");
        }
        return result;
    }

    /**
     * 修改广告内容
     *
     * @param adContent
     * @return
     */
    public PostResult update(ConfigAdWx adContent) {
        PostResult result = new PostResult();
        int update = this.adWxMapper.update(adContent);
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
        int delete = this.adWxMapper.delete(id);
        if (delete <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        }
        return result;
    }

}
