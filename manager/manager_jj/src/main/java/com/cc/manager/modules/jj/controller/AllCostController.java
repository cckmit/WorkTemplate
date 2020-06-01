package com.cc.manager.modules.jj.controller;


import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.service.AdValueService;
import com.cc.manager.modules.jj.service.AllCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author cf
 * @since 2020-05-23
 */
@RestController
@RequestMapping(value = "/jj/allCost")
public class AllCostController implements BaseStatsController {

    private  AllCostService  allCostService;

    @Override
    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return this.allCostService.getPage(statsListParam);
    }

    @Autowired
    public void setAllCostService(AllCostService allCostService) {
        this.allCostService = allCostService;
    }

}




