package com.cc.manager.modules.tt.controller;


import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.tt.service.TtDailyValueService;
import com.cc.manager.web.ProjectServletListener;
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
    public CrudObjectResult getData(@RequestBody JSONObject jsonObject) {
        // 验证参数，验证通过
        ProjectServletListener.scheduler.execute(()->{
             this.ttDailyValueService.getAllData(jsonObject);
        });
        return  new CrudObjectResult();
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

