package com.cc.manager.modules.jj.controller;

import com.alibaba.fastjson.JSONArray;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.service.WxConfigService;
import com.cc.manager.modules.jj.utils.PersieServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-04 18:11
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/wxConfig")
public class WxConfigController implements BaseCrudController {

    private WxConfigService wxConfigService;

    private PersieServerUtils persieServerUtils;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.wxConfigService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.wxConfigService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.wxConfigService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        PostResult postResult = this.wxConfigService.post(requestParam);
        if (postResult.getCode() == 1) {
            postResult = this.persieServerUtils.refreshTable("wx_config");
        }
        return postResult;
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        PostResult put = this.wxConfigService.put(requestParam);
        if (put.getCode() == 1) {
            put = this.persieServerUtils.refreshTable("wx_config");
        }
        return put;
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        PostResult deleteResult = this.wxConfigService.delete(requestParam);
        if (deleteResult.getCode() == 1) {
            deleteResult = this.persieServerUtils.refreshTable("wx_config");
        }
        return deleteResult;
    }

    @Override
    @GetMapping(value = "/getSelectArray/{requestParam}")
    public JSONArray getSelectArray(@PathVariable String requestParam) {
        return this.wxConfigService.getSelectArray(WxConfig.class, requestParam);
    }

    /**
     * 获取资源图
     *
     * @param parameter parameter
     * @return PostResult
     */
    @PostMapping(value = "/flushPicture")
    public PostResult flushResource(@RequestBody JSONArray parameter) {
        PostResult postResult = wxConfigService.flushResource(parameter);
        //刷新业务表结构
        postResult = this.persieServerUtils.refreshTable("wx_config");
        return postResult;
    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

    @Autowired
    public void setPersieServerUtils(PersieServerUtils persieServerUtils) {
        this.persieServerUtils = persieServerUtils;
    }
}
