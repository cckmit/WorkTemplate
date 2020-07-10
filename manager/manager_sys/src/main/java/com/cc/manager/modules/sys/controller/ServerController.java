package com.cc.manager.modules.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.sys.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * TODO
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-02 12:18
 */
@RestController
@RequestMapping(value = "/sys/server")
public class ServerController implements BaseCrudController {

    private ServerService serverService;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.serverService.getObjectById(id);
    }

    @Override
    public CrudObjectResult getObject(String getObjectParam) {
        return null;
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.serverService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        return this.serverService.post(requestParam);
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.serverService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.serverService.delete(requestParam);
    }

    @Override
    public JSONObject getSelectArray(String requestParam) {
        return null;
    }

    @Autowired
    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }

}
