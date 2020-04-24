package com.fish.controller;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.DataCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 数据汇总
 * Fc_DataCollectController
 *
 * @author
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class Fc_DataCollectController {

    @Autowired
    DataCollectService fcDataCollectService;

    /**
     * 查询数据汇总信息
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/datacollect")
    public GetResult getDataCollect(GetParameter parameter) {
        return fcDataCollectService.findAll(parameter);
    }

}
