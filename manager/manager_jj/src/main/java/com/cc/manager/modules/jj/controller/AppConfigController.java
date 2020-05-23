package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONArray;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.common.utils.ReduceJsonUtil;
import com.cc.manager.config.BaseConfig;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.service.AppConfigService;
import com.cc.manager.modules.jj.service.WxConfigService;
import com.cc.manager.modules.jj.utils.PersieServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 审核配置
 *
 * @author cf
 * @since 2020-05-09
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/appConfig")
public class AppConfigController implements BaseCrudController {

    private WxConfigService wxConfigService;

    private AppConfigService appConfigService;

    private PersieServerUtils persieServerUtils;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.appConfigService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.appConfigService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.appConfigService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        PostResult postResult = this.appConfigService.post(requestParam);
        if (postResult.getCode() == 1) {
            postResult = this.persieServerUtils.refreshTable("app_config");
        }
        return postResult;
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        PostResult postResult = this.appConfigService.put(requestParam);
        if (postResult.getCode() == 1) {
            postResult = this.persieServerUtils.refreshTable("app_config");
        }
        return postResult;
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        PostResult postResult =  this.appConfigService.delete(requestParam);
        if (postResult.getCode() == 1) {
            postResult = this.persieServerUtils.refreshTable("app_config");
        }
        return postResult;
    }

    @Override
    @GetMapping(value = "/getSelectArray/{requestParam}")
    public JSONArray getSelectArray(@PathVariable String requestParam) {
        return this.wxConfigService.getSelectArray(WxConfig.class, requestParam);
    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }
    @Autowired
    public void setAppConfigService(AppConfigService appConfigService) {
        this.appConfigService = appConfigService;
    }

    @Autowired
    public void setPersieServerUtils(PersieServerUtils persieServerUtils) {
        this.persieServerUtils = persieServerUtils;
    }
}

