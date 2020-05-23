package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONArray;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.common.utils.ExcelUtils;
import com.cc.manager.modules.jj.entity.RoundRecord;
import com.cc.manager.modules.jj.service.RoundReceiveService;
import com.cc.manager.modules.jj.service.RoundRecordGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author cf
 * @since 2020-05-08
 */
@CrossOrigin
@RestController
@RequestMapping("/roundRecordGame")
public class RoundRecordGameController implements BaseCrudController {
    private RoundRecordGameService roundRecordGameService;
    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.roundRecordGameService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.roundRecordGameService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.roundRecordGameService.getPage(crudPageParam);
    }
    /**
     * 导出Excel结果
     *
     * @param roundRecord
     * @return
     */
    @GetMapping(value = "/export")
    public void getRankingResult(RoundRecord roundRecord, HttpServletResponse response) {
        roundRecordGameService.exportResult(roundRecord,response);
    }
    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        return this.roundRecordGameService.post(requestParam);
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.roundRecordGameService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.roundRecordGameService.delete(requestParam);
    }

    @Override
    public JSONArray getSelectArray(String requestParam) {
        return null;
    }


    @Autowired
    public void setRoundRecordGameService(RoundRecordGameService roundRecordGameService) {
        this.roundRecordGameService = roundRecordGameService;
    }
}

