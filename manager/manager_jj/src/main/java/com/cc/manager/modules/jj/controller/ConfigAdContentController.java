package com.cc.manager.modules.jj.controller;

import com.alibaba.fastjson.JSONArray;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.ConfigAdContent;
import com.cc.manager.modules.jj.service.ConfigAdContentService;
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
        return this.configAdContentService.post(requestParam);
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.configAdContentService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.configAdContentService.delete(requestParam);
    }

    @Override
    @GetMapping(value = "/getSelectArray/{requestParam}")
    public JSONArray getSelectArray(@PathVariable String requestParam) {
        return this.configAdContentService.getSelectArray(ConfigAdContent.class,null);
    }

    @Autowired
    public void setConfigAdContentService(ConfigAdContentService configAdContentService) {
        this.configAdContentService = configAdContentService;
    }

}
