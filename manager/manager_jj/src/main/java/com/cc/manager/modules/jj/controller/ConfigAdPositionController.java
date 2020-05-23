package com.cc.manager.modules.jj.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.ConfigAdPosition;
import com.cc.manager.modules.jj.service.ConfigAdPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-07 15:42
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/jj/configAdPosition")
public class ConfigAdPositionController implements BaseCrudController {

    private ConfigAdPositionService configAdPositionService;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.configAdPositionService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.configAdPositionService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.configAdPositionService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        return this.configAdPositionService.post(requestParam);
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.configAdPositionService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.configAdPositionService.delete(requestParam);
    }

    @Override
    @GetMapping(value = "/getSelectArray/{requestParam}")
    public JSONArray getSelectArray(@PathVariable String requestParam) {
        return this.configAdPositionService.getSelectArray(ConfigAdPosition.class, null);
    }

    @PutMapping("/statusSwitch")
    public PostResult statusSwitch(@RequestBody JSONObject switchObject) {
        return this.configAdPositionService.statusSwitch(switchObject);
    }

    @Autowired
    public void setConfigAdPositionService(ConfigAdPositionService configAdPositionService) {
        this.configAdPositionService = configAdPositionService;
    }

}
