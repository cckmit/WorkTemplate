package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.service.WxGroupManagerService;
import com.cc.manager.modules.jj.utils.PersieServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 微信群管理
 *
 * @author cf
 * @since 2020-05-09
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/wxGroupManager")
public class WxGroupManagerController implements BaseCrudController {

    private WxGroupManagerService wxGroupManagerService;

    private PersieServerUtils persieServerUtils;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.wxGroupManagerService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.wxGroupManagerService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.wxGroupManagerService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        PostResult postResult = this.wxGroupManagerService.post(requestParam);
        if (postResult.getCode() == 1) {
            this.persieServerUtils.refreshTable("config_confirm");
        }
        return postResult;
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.wxGroupManagerService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.wxGroupManagerService.delete(requestParam);
    }

    /**
     * 页面开关更新开关状态
     *
     * @param jsonObject jsonObject
     * @return PostResult
     */
    @PutMapping(value = "/switchStatus")
    public PostResult updateShowId(@RequestBody JSONObject jsonObject) {
        PostResult postResult = this.wxGroupManagerService.switchStatus(jsonObject);
        if (postResult.getCode() == 1) {
            this.persieServerUtils.refreshTable("config_confirm");
        }
        return postResult;
    }

    @Override
    public JSONObject getSelectArray(String requestParam) {
        return null;
    }

    @Autowired
    public void setWxGroupManagerService(WxGroupManagerService wxGroupManagerService) {
        this.wxGroupManagerService = wxGroupManagerService;
    }

    @Autowired
    public void setPersieServerUtils(PersieServerUtils persieServerUtils) {
        this.persieServerUtils = persieServerUtils;
    }

}

