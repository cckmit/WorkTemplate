package com.cc.manager.modules.fc.controller;


import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.fc.service.MinitjWxActiveResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cf
 * @since 2020-05-22
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/jj/userActiveResource")
public class MinitjWxActiveResourceCollectController implements BaseCrudController {

    private MinitjWxActiveResourceService minitjWxActiveResourceService;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.minitjWxActiveResourceService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.minitjWxActiveResourceService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.minitjWxActiveResourceService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        return this.minitjWxActiveResourceService.post(requestParam);
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.minitjWxActiveResourceService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.minitjWxActiveResourceService.delete(requestParam);
    }

    @Override
    public JSONObject getSelectArray(String requestParam) {
        return null;
    }

    @Autowired
    public void setMinitjWxActiveResourceService(MinitjWxActiveResourceService minitjWxActiveResourceService) {
        this.minitjWxActiveResourceService = minitjWxActiveResourceService;
    }

}

