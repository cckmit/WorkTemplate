package com.fish.controller;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.WxGroupHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 微信群管理记录
 * WxGroupHistoryController
 *
 * @author Host-0311
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class WxGroupHistoryController {

    @Autowired
    WxGroupHistoryService wxGroupHistoryService;

    @ResponseBody
    @GetMapping(value = "/history")
    public GetResult getWxGroupManager(GetParameter parameter) {
        return wxGroupHistoryService.findAll(parameter);
    }

}
