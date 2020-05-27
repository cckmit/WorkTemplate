package com.cc.manager.modules.fc.controller;


import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.service.MinitjWxActiveResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cf
 * @since 2020-05-22
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/fc/userResource")
public class MinitjWxUserResourceController implements BaseStatsController {

    private MinitjWxActiveResourceService minitjWxActiveResourceService;

    @Override
    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return this.minitjWxActiveResourceService.getPage(statsListParam);
    }

    @Autowired
    public void setMinitjWxActiveResourceService(MinitjWxActiveResourceService minitjWxActiveResourceService) {
        this.minitjWxActiveResourceService = minitjWxActiveResourceService;
    }

}

