package com.cc.manager.modules.jj.controller;

import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.service.ConfigAdAppService;
import com.cc.manager.modules.jj.utils.PersieServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-07 15:40
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/jj/configAdApp")
public class ConfigAdAppController implements BaseCrudController {

    private ConfigAdAppService configAdAppService;
    private PersieServerUtils persieServerUtils;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.configAdAppService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.configAdAppService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.configAdAppService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        PostResult postResult = this.configAdAppService.post(requestParam);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_app");
//        }
        return postResult;
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        PostResult postResult = this.configAdAppService.put(requestParam);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_app");
//        }
        return postResult;
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        PostResult postResult = this.configAdAppService.delete(requestParam);
        if (postResult.getCode() == 1) {
            this.persieServerUtils.refreshTable("config_ad_app");
        }
        return postResult;
    }

    @Override
    @GetMapping(value = "/getSelectArray/{requestParam}")
    public JSONObject getSelectArray(@PathVariable String requestParam) {
        return null;
    }

    @PutMapping("/switchStatus")
    public PostResult switchStatus(@RequestBody String requestParam) {
        PostResult postResult = this.configAdAppService.switchStatus(requestParam);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_app");
//        }
        return postResult;
    }

    @Autowired
    public void setConfigAdAppService(ConfigAdAppService configAdAppService) {
        this.configAdAppService = configAdAppService;
    }

    @Autowired
    public void setPersieServerUtils(PersieServerUtils persieServerUtils) {
        this.persieServerUtils = persieServerUtils;
    }

}
