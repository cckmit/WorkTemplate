package com.fish.controller;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.WxGroupHistoryService;
import com.fish.utils.BaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/manage")
public class WxGroupHistoryController {

    @Autowired
    WxGroupHistoryService wxGroupHistoryService;

    @ResponseBody
    @GetMapping(value = "/history")
    public GetResult getWxGroupManager(GetParameter parameter){
        GetResult result = wxGroupHistoryService.findAll(parameter);
        return result;
    }

}
