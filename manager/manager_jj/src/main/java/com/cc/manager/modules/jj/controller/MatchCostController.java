package com.cc.manager.modules.jj.controller;


import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.service.MatchCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cf
 * @since 2020-05-23
 */
@RestController
@RequestMapping(value = "/jj/matchCost")
public class MatchCostController implements BaseStatsController {

    private MatchCostService matchCostService;

    @Override
    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return this.matchCostService.getPage(statsListParam);
    }

    @Autowired
    public void setMatchCostService(MatchCostService matchCostService) {
        this.matchCostService = matchCostService;
    }

}




