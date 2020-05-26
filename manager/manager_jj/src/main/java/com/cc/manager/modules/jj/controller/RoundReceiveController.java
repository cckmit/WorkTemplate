package com.cc.manager.modules.jj.controller;


import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.service.RoundReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获奖记录查询
 *
 * @author cf
 * @since 2020-05-08
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/roundReceive")
public class RoundReceiveController implements BaseStatsController {

    private RoundReceiveService roundReceiveService;


    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return this.roundReceiveService.getPage(statsListParam);
    }

    @Override
    public StatsListResult getList(StatsListParam statsListParam) {
        return null;
    }

    @Autowired
    public void setRoundReceiveService(RoundReceiveService roundReceiveService) {
        this.roundReceiveService = roundReceiveService;
    }

}

