package com.cc.manager.modules.fc.controller;

import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.service.AdValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-05 16:21
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/fc/adValue")
public class AdValueController implements BaseStatsController {

    private AdValueService adValueService;

    @Override
    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return this.adValueService.getPage(statsListParam);
    }

    @Autowired
    public void setAdValueService(AdValueService adValueService) {
        this.adValueService = adValueService;
    }

}
