package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONArray;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.common.utils.ReduceJsonUtil;
import com.cc.manager.modules.jj.config.JjConfig;
import com.cc.manager.modules.jj.service.RoundMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author cf
 * @since 2020-05-08
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/roundMatch")
public class RoundMatchController implements BaseCrudController {

    private RoundMatchService roundMatchService;
    private JjConfig jjConfig;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.roundMatchService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.roundMatchService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.roundMatchService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        PostResult post = this.roundMatchService.post(requestParam);
        if (post.getCode() == 1) {
            ReduceJsonUtil.flushTable("round_match", this.jjConfig.getFlushCache());
        }
        return post;
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        PostResult put = this.roundMatchService.put(requestParam);
        if (put.getCode() == 1) {
            ReduceJsonUtil.flushTable("round_match", this.jjConfig.getFlushCache());
        }
        return put;
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        PostResult delete = this.roundMatchService.delete(requestParam);
        if (delete.getCode() == 1) {
            ReduceJsonUtil.flushTable("round_match", this.jjConfig.getFlushCache());
        }
        return this.roundMatchService.delete(requestParam);
    }

    @Override
    public JSONArray getSelectArray(String requestParam) {
        return null;
    }

    @Autowired
    public void setRoundExtService(RoundMatchService roundMatchService) {
        this.roundMatchService = roundMatchService;
    }
}

