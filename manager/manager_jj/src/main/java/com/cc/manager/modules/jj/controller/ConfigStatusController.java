package com.cc.manager.modules.jj.controller;

import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.ConfigStatus;
import com.cc.manager.modules.jj.service.ConfigStatusService;
import com.cc.manager.modules.jj.utils.PersieServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-03 12:27
 */
@RestController
@RequestMapping(value = "/jj/configStatus")
public class ConfigStatusController implements BaseCrudController {

    private ConfigStatusService configStatusService;
    private PersieServerUtils persieServerUtils;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.configStatusService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.configStatusService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.configStatusService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        PostResult postResult = this.configStatusService.post(requestParam);
        if (postResult.getCode() == 1) {
            this.persieServerUtils.refreshTable("config_status");
        }
        return postResult;
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        PostResult postResult = this.configStatusService.put(requestParam);
        if (postResult.getCode() == 1) {
            this.persieServerUtils.refreshTable("config_status");
        }
        return postResult;
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        PostResult postResult = this.configStatusService.delete(requestParam);
        if (postResult.getCode() == 1) {
            this.persieServerUtils.refreshTable("config_status");
        }
        return postResult;
    }

    @Override
    @GetMapping(value = "/getSelectArray/{requestParam}")
    public JSONObject getSelectArray(@PathVariable String requestParam) {
        return this.configStatusService.getSelectArray(ConfigStatus.class, null);
    }


    @Autowired
    public void setConfigStatusService(ConfigStatusService configStatusService) {
        this.configStatusService = configStatusService;
    }

    @Autowired
    public void setPersieServerUtils(PersieServerUtils persieServerUtils) {
        this.persieServerUtils = persieServerUtils;
    }

}
