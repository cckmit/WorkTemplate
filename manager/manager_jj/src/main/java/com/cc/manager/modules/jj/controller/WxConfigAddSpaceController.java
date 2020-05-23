package com.cc.manager.modules.jj.controller;

import com.alibaba.fastjson.JSONArray;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.service.WxConfigAddSpaceService;
import com.cc.manager.modules.jj.service.WxConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 广告位配置
 * @author cf
 * @since 2020-05-08
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/jj/wxConfigAddSpace")
public class WxConfigAddSpaceController implements BaseCrudController {

    private WxConfigAddSpaceService wxConfigAddSpaceService;
    private WxConfigService wxConfigService;
    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.wxConfigAddSpaceService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.wxConfigAddSpaceService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.wxConfigAddSpaceService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        return this.wxConfigAddSpaceService.post(requestParam);
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.wxConfigAddSpaceService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.wxConfigAddSpaceService.delete(requestParam);
    }

    @Override
    @GetMapping(value = "/getSelectArray/{requestParam}")
    public JSONArray getSelectArray(@PathVariable String requestParam) {
        return this.wxConfigService.getSelectArray(WxConfig.class, requestParam);
    }

    @Autowired
    public void setWxConfigAddSpaceService(WxConfigAddSpaceService wxConfigAddSpaceService) {
        this.wxConfigAddSpaceService = wxConfigAddSpaceService;
    }
    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

}
