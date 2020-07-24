package com.cc.manager.modules.tt.controller;


import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.tt.service.QqDailyAdValueService;
import com.cc.manager.modules.tt.service.TtDailyAdValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cf
 * @since 2020-05-08
 */
@CrossOrigin
@RestController
@RequestMapping("/qq/qqDailyAdValue")
public class QqDailyAdValueController implements BaseCrudController {

    private QqDailyAdValueService qqDailyAdValueService;

    @PostMapping(value = "/getAdData")
    public PostResult getAddData(@RequestBody JSONObject jsonObject) {
        return this.qqDailyAdValueService.getAdData(jsonObject);
    }

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(String id) {
        return null;
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(String getObjectParam) {
        return null;
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return qqDailyAdValueService.getPage(crudPageParam);
    }

    @Override
    public PostResult post(String requestParam) {
        return null;
    }

    @Override
    public PostResult put(String requestParam) {
        return null;
    }

    @Override
    public PostResult delete(String requestParam) {
        return null;
    }

    @Override
    public JSONObject getSelectArray(String requestParam) {
        return null;
    }

    @Autowired
    public void setQqDailyAdValueService(QqDailyAdValueService qqDailyAdValueService) {
        this.qqDailyAdValueService = qqDailyAdValueService;
    }

}

