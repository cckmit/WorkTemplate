package com.cc.manager.modules.tt.controller;


import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.tt.service.TtDataCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cf
 * @since 2020-05-08
 */
@CrossOrigin
@RestController
@RequestMapping("/tt/ttDataCollect")
public class TtDataCollectController implements BaseStatsController {

    private TtDataCollectService ttDataCollectService;

    @Override
    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return ttDataCollectService.getPage(statsListParam);
    }

    @Autowired
    public void setTtDataCollectService(TtDataCollectService TtDataCollectService) {
        this.ttDataCollectService = TtDataCollectService;
    }

}

