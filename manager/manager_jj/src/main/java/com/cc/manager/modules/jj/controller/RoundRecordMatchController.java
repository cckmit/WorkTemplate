package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.RoundRecord;
import com.cc.manager.modules.jj.service.RoundRecordMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 小程序比赛结果
 *
 * @author cf
 * @since 2020-05-08
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/roundRecordMatch")
public class RoundRecordMatchController implements BaseCrudController {

    private RoundRecordMatchService roundRecordMatchService;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.roundRecordMatchService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.roundRecordMatchService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.roundRecordMatchService.getPage(crudPageParam);
    }

    /**
     * 导出Excel小程序结果
     *
     * @param roundRecord roundRecord
     */
    @GetMapping(value = "/exportResult")
    public void getRankingResult(RoundRecord roundRecord, HttpServletResponse response) {
        roundRecordMatchService.exportResult(roundRecord, response);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        return null;
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return null;
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return null;
    }

    @Override
    public JSONObject getSelectArray(String requestParam) {
        return null;
    }

    @Autowired
    public void setRoundRecordMatchService(RoundRecordMatchService roundRecordMatchService) {
        this.roundRecordMatchService = roundRecordMatchService;
    }

}

