package com.cc.manager.modules.fc.controller;


import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.*;
import com.cc.manager.modules.fc.service.MinitjWxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cf
 * @since 2020-05-13
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/dataCollect")
public class DataCollectController implements BaseStatsController {

    private MinitjWxService minitjWxService;

    @Override
    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return this.minitjWxService.getPage(statsListParam);
    }

    @Autowired
    public void setMinitjWxService(MinitjWxService minitjWxService) {
        this.minitjWxService = minitjWxService;
    }

}

