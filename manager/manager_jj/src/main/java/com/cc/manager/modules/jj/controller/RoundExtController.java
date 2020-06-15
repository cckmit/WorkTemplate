package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.config.JjConfig;
import com.cc.manager.modules.jj.entity.RoundExt;
import com.cc.manager.modules.jj.service.RoundExtService;
import com.cc.manager.modules.jj.utils.PersieServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 游戏赛制配置
 *
 * @author cf
 * @since 2020-05-11
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/roundExt")
public class RoundExtController implements BaseCrudController {

    private RoundExtService roundExtService;
    private JjConfig jjConfig;
    private PersieServerUtils persieServerUtils;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.roundExtService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.roundExtService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.roundExtService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        PostResult postResult = this.roundExtService.post(requestParam);
        if (postResult.getCode() == 1) {
            this.persieServerUtils.refreshTable("round_ext");
        }
        return postResult;
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        PostResult putResult = this.roundExtService.put(requestParam);
        if (putResult.getCode() == 1) {
            this.persieServerUtils.refreshTable("round_ext");
        }
        return putResult;
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        PostResult deleteResult = this.roundExtService.delete(requestParam);
        if (deleteResult.getCode() == 1) {
            this.persieServerUtils.refreshTable("round_ext");
        }
        return deleteResult;
    }

    @Override
    @GetMapping("/getSelectArray/{requestParam}")
    public JSONObject getSelectArray(@PathVariable String requestParam) {
        return this.roundExtService.getSelectArray(RoundExt.class, requestParam);
    }

    @Autowired
    public void setRoundExtService(RoundExtService roundExtService) {
        this.roundExtService = roundExtService;
    }

    @Autowired
    public void setJjConfig(JjConfig jjConfig) {
        this.jjConfig = jjConfig;
    }

    @Autowired
    public void setPersieServerUtils(PersieServerUtils persieServerUtils) {
        this.persieServerUtils = persieServerUtils;
    }
}

