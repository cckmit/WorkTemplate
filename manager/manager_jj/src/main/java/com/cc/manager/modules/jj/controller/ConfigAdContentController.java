package com.cc.manager.modules.jj.controller;

import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.ConfigAdContent;
import com.cc.manager.modules.jj.service.ConfigAdContentService;
import com.cc.manager.modules.jj.utils.PersieServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-07 15:41
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/jj/configAdContent")
public class ConfigAdContentController implements BaseCrudController {

    private ConfigAdContentService configAdContentService;
    private PersieServerUtils persieServerUtils;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.configAdContentService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.configAdContentService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.configAdContentService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        PostResult postResult = this.configAdContentService.post(requestParam);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_content");
//        }
        return postResult;
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        PostResult postResult = this.configAdContentService.put(requestParam);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_content");
//        }
        return postResult;
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        PostResult postResult = this.configAdContentService.delete(requestParam);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_content");
//        }
        return postResult;
    }

    @Override
    @GetMapping(value = "/getSelectArray/{requestParam}")
    public JSONObject getSelectArray(@PathVariable String requestParam) {
        return this.configAdContentService.getSelectArray(ConfigAdContent.class, null);
    }

    /**
     * 查询推广APP列表
     *
     * @return 推广APP列表
     */
    @GetMapping(value = "/getTargetAppArray")
    public JSONObject getTargetAppArray() {
        return this.configAdContentService.getTargetAppArray();
    }

    @PutMapping(value = "/uploadImageUrlByUpload")
    public PostResult uploadImageUrlByUpload(@RequestBody String requestParam) {
        PostResult postResult = this.configAdContentService.uploadImageUrlByUpload(requestParam);
        if (postResult.getCode() == 1) {
            this.persieServerUtils.refreshTable("config_ad_content");
        }
        return postResult;
    }

    @Autowired
    public void setConfigAdContentService(ConfigAdContentService configAdContentService) {
        this.configAdContentService = configAdContentService;
    }

    @Autowired
    public void setPersieServerUtils(PersieServerUtils persieServerUtils) {
        this.persieServerUtils = persieServerUtils;
    }

}
