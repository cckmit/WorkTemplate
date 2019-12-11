package com.fish.controller;

import com.fish.dao.primary.model.GameDayInfo;
import com.fish.dao.primary.model.OnlineStatistics;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.GameDayInfoService;
import com.fish.service.OnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/manage")
public class OnlineStatisticsController {

    @Autowired
    OnlineService onlineService;

    //查询展示所有赛制配置
    @ResponseBody
    @GetMapping(value = "/online")
    public GetResult getGameDayIfon(GetParameter parameter) {

        GetResult onlineStatistics = onlineService.findAll(parameter);

        return onlineStatistics;
    }



}
