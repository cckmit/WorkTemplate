package com.cc.manager.modules.fc.controller;

import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.service.AdValueWxAdUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cf
 * @since 2020-05-21
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/jj/adValueWxAdUnit")
public class AdValueWxAdUnitController implements BaseStatsController {

    private AdValueWxAdUnitService adValueWxAdUnitService;

    @Override
    @GetMapping(value = "/getList")
    public StatsListResult getList(StatsListParam statsListParam) {
        return this.adValueWxAdUnitService.getList(statsListParam);
    }

    @Autowired
    public void setAdValueService(AdValueWxAdUnitService adValueWxAdUnitService) {
        this.adValueWxAdUnitService = adValueWxAdUnitService;
    }

}
