package com.cc.manager.modules.tt.controller;


import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.*;
import com.cc.manager.modules.tt.service.TtDailyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cf
 * @since 2020-05-08
 */
@CrossOrigin
@RestController
@RequestMapping("/tt/ttDailyValue")
public class TtDailyValueController implements BaseStatsController {

    private TtDailyValueService ttDailyValueService;

    @PostMapping(value = "/getData")
    public PostResult getData(@RequestBody JSONObject jsonObject) {
        return this.ttDailyValueService.getData(jsonObject);
    }

    @GetMapping(value = "/getPage")
    @Override
    public StatsListResult getPage(StatsListParam statsListParam) {
        return ttDailyValueService.getPage(statsListParam);
    }

    @GetMapping(value = "/getList")
    public StatsListResult getList(StatsListParam statsListParam) {
        return ttDailyValueService.getList(statsListParam);
    }

    @Autowired
    public void setTtDailyValueService(TtDailyValueService ttDailyValueService) {
        this.ttDailyValueService = ttDailyValueService;
    }

}

