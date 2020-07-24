package com.cc.manager.modules.tt.controller;


import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.tt.service.QqDailyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cf
 * @since 2020-05-08
 */
@CrossOrigin
@RestController
@RequestMapping("/qq/qqDailyValue")
public class QqDailyValueController implements BaseStatsController {

    private QqDailyValueService qqDailyValueService;

    @PostMapping(value = "/getData")
    public PostResult getData(@RequestBody JSONObject jsonObject) {
        return this.qqDailyValueService.getData(jsonObject);
    }

    @GetMapping(value = "/getPage")
    @Override
    public StatsListResult getPage(StatsListParam statsListParam) {
        return qqDailyValueService.getPage(statsListParam);
    }

    @GetMapping(value = "/getList")
    public StatsListResult getList(StatsListParam statsListParam) {
        return qqDailyValueService.getList(statsListParam);
    }

    @Autowired
    public void setQqDailyValueService(QqDailyValueService qqDailyValueService) {
        this.qqDailyValueService = qqDailyValueService;
    }

}

