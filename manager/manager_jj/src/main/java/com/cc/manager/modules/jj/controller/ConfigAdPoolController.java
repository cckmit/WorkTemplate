package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.service.ConfigAdContentService;
import com.cc.manager.modules.jj.service.ConfigAdPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author cf
 * @since 2020-06-16
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/jj/configAdPool")
public class ConfigAdPoolController implements BaseCrudController {

    private ConfigAdPoolService configAdPoolService;

    @Override
    public CrudObjectResult getObjectById(String id) {
        return null;
    }

    @Override
    public CrudObjectResult getObject(String getObjectParam) {
        return null;
    }

    @Override
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return configAdPoolService.getPage(crudPageParam);
    }

    @Override
    public PostResult post(String requestParam) {
        return null;
    }

    @Override
    public PostResult put(String requestParam) {
        return null;
    }

    @Override
    public PostResult delete(String requestParam) {
        return null;
    }

    @Override
    public JSONObject getSelectArray(String requestParam) {
        return null;
    }
    @Autowired
    public void setConfigAdPoolService(ConfigAdPoolService configAdPoolService) {
        this.configAdPoolService = configAdPoolService;
    }
}

