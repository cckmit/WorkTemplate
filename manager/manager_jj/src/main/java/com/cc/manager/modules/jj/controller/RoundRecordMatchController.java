package com.cc.manager.modules.jj.controller;


import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.entity.RoundRecord;
import com.cc.manager.modules.jj.service.RoundRecordMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 小程序比赛结果
 *
 * @author cf
 * @since 2020-05-08
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/roundRecordMatch")
public class RoundRecordMatchController implements BaseStatsController {

    private RoundRecordMatchService roundRecordMatchService;

    @Override
    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return this.roundRecordMatchService.getPage(statsListParam);
    }

    /**
     * 导出Excel小程序结果
     *
     * @param roundRecord roundRecord
     */
    @GetMapping(value = "/exportResult")
    public void getRankingResult(RoundRecord roundRecord, HttpServletResponse response) {
        roundRecordMatchService.exportResult(roundRecord, response);
    }

    @Autowired
    public void setRoundRecordMatchService(RoundRecordMatchService roundRecordMatchService) {
        this.roundRecordMatchService = roundRecordMatchService;
    }


}

