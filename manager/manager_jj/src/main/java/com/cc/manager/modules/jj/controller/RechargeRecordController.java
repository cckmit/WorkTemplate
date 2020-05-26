package com.cc.manager.modules.jj.controller;


import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.service.RechargeRecordService;
import com.cc.manager.modules.jj.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cf
 * @since 2020-05-09
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/rechargeRecord")
public class RechargeRecordController implements BaseStatsController {


    private RechargeRecordService rechargeRecordService;

    @Override
    public StatsListResult getList(StatsListParam statsListParam) {
        return null;
    }

    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return this.rechargeRecordService.getPage(statsListParam);
    }

    @Autowired
    public void setRechargeRecordService(RechargeRecordService rechargeRecordService) {
        this.rechargeRecordService = rechargeRecordService;
    }

}

