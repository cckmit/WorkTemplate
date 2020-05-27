package com.cc.manager.modules.jj.controller;


import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.entity.RoundRecord;
import com.cc.manager.modules.jj.service.RoundRecordGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 小游戏比赛结果
 *
 * @author cf
 * @since 2020-05-08
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/roundRecordGame")
public class RoundRecordGameController implements BaseStatsController {

    private RoundRecordGameService roundRecordGameService;


    @Override    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return this.roundRecordGameService.getPage(statsListParam);
    }

    /**
     * 导出Excel游戏结果
     *
     * @param roundRecord roundRecord
     */
    @GetMapping(value = "/exportResult")
    public void getGameRankingResult(RoundRecord roundRecord, HttpServletResponse response) {
        roundRecordGameService.exportResult(roundRecord, response);
    }




    @Autowired
    public void setRoundRecordGameService(RoundRecordGameService roundRecordGameService) {
        this.roundRecordGameService = roundRecordGameService;
    }

}

