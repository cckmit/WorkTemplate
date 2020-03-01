package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.ConfigAdContentMapper;
import com.fish.dao.second.model.ConfigAdContent;
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
public class ConfigAdContentService implements BaseService<ConfigAdContent> {

    @Autowired
    ConfigAdContentMapper adContentMapper;

    @Override
    public void setDefaultSort(GetParameter parameter) { }

    @Override
    public Class getClassInfo() { return ConfigAdContent.class; }

    @Override
    public boolean removeIf(ConfigAdContent configAdContent, JSONObject searchData) { return false; }

    @Override
    public List selectAll(GetParameter parameter) { return this.adContentMapper.selectAll(); }

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
        int delete = this.adContentMapper.delete(id);
        if (delete <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        }
        return result;
    }

}
