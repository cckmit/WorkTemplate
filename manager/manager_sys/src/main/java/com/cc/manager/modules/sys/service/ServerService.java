package com.cc.manager.modules.sys.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.sys.entity.Server;
import com.cc.manager.modules.sys.mapper.ServerMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-02 12:15
 */
@Service
public class ServerService extends BaseCrudService<Server, ServerMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<Server> queryWrapper) {

    }

    /**
     * 获取系统配置服务器
     * @return 服务器配置
     */
    public JSONObject getServerJson( ) {
        JSONArray serverArray = new JSONArray();
        List<Server> list = this.list();
        list.forEach(entity -> {
            JSONObject option = new JSONObject();
            option.put("key", entity.getServerKey());
            option.put("url", entity.getServerUrl());
            option.put("dir", entity.getServerDir());
            serverArray.add(option);
        });

        JSONObject resultObject = new JSONObject();
        resultObject.put("code", 1);
        resultObject.put("data", serverArray);
        return resultObject;
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<Server> deleteWrapper) {
        if (StringUtils.isNotBlank(requestParam)) {
            List<String> idList = JSONObject.parseArray(requestParam, String.class);
            if (!idList.isEmpty()) {
                return this.removeByIds(idList);
            }
        }
        return false;
    }


}
