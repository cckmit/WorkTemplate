package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONArray;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.common.utils.ReduceJsonUtil;
import com.cc.manager.config.BaseConfig;
import com.cc.manager.modules.jj.config.JjConfig;
import com.cc.manager.modules.jj.service.RoundGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author cf
 * @since 2020-05-08
 */

@CrossOrigin
@RestController
@RequestMapping("/jj/roundGame")
public class RoundGameController implements BaseCrudController {

    private RoundGameService roundGameService;
    private JjConfig jjConfig;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.roundGameService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.roundGameService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.roundGameService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        PostResult post = this.roundGameService.post(requestParam);
        if (post.getCode() == 1) {
            ReduceJsonUtil.flushTable("round_game", this.jjConfig.getFlushCache());
        }
        return post;
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        PostResult put = this.roundGameService.put(requestParam);
        if (put.getCode() == 1) {
            ReduceJsonUtil.flushTable("round_game", this.jjConfig.getFlushCache());
        }
        return put;
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        PostResult delete = this.roundGameService.delete(requestParam);
        if (delete.getCode() == 1) {
            ReduceJsonUtil.flushTable("round_game", this.jjConfig.getFlushCache());
        }
        return this.roundGameService.delete(requestParam);
    }

    @Override
    public JSONArray getSelectArray(String requestParam) {
        return null;
    }

    @Autowired
    public void setRoundExtService(RoundGameService roundGameService) {
        this.roundGameService = roundGameService;
    }

}

