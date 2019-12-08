package com.fish.controller;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/statistics/")
public class StatisticsController
{
    @Autowired
    StatisticsPayService payService;
    @Autowired
    StatisticsUserDayService userDayService;
    @Autowired
    StatisticsUserMonthService userMonthService;
    @Autowired
    StatisticsUserWeekService userWeekService;
    @Autowired
    StatisticsRetentionService retentionService;

    @ResponseBody
    @GetMapping(value = "user")
    public GetResult selectUser(GetParameter parameter)
    {
        switch (parameter.getDatagrid())
        {
            case "day":
                return userDayService.findAll(parameter);
            case "week":
                return userWeekService.findAll(parameter);
            case "month":
                return userMonthService.findAll(parameter);
            default:
                break;
        }
        GetResult result = new GetResult();
        result.setCode(400);
        result.setMsg("无效请求");
        return result;
    }

    @ResponseBody
    @GetMapping(value = "retention")
    public GetResult selectRetention(GetParameter parameter)
    {
        return retentionService.findAll(parameter);
    }

    @ResponseBody
    @GetMapping(value = "pay")
    public GetResult selectPay(GetParameter parameter)
    {
        return payService.findAll(parameter);
    }
}
