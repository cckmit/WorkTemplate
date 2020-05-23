package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONArray;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.*;
import com.cc.manager.modules.jj.service.PayStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 付费实时统计
 *
 * @author cf
 * @since 2020-05-13
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/payStatistic")
public class PayStatisticController implements BaseStatsController {
    private PayStatisticService payStatisticService;


    @Override
    public StatsListResult getList(StatsListParam statsListParam) {
        return null;
    }

    @Override
    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return payStatisticService.getPage(statsListParam);
    }

    @Autowired
    public void setPayStatisticService(PayStatisticService payStatisticService) {
        this.payStatisticService = payStatisticService;
    }


}

