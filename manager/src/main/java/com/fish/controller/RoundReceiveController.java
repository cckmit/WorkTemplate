package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.RoundReceiveService;
import com.fish.utils.BaseConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 获奖记录查询
 * RoundReceiveController
 *
 * @author
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class RoundReceiveController {

    @Autowired
    RoundReceiveService roundReceiveService;
    @Autowired
    BaseConfig baseConfig;

    /**
     * 查询获奖记录查询
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/roundreceive")
    public GetResult getRoundreceive(GetParameter parameter) {
        return roundReceiveService.findAll(parameter);
    }

    @ResponseBody
    @GetMapping(value = "/roundreceive/result")
    public GetResult searchRoundReceive(HttpServletRequest request, GetParameter parameter) {
        JSONObject search = new JSONObject();
        String times = request.getParameter("times");
        String userName = request.getParameter("userName");
        String gameCode = request.getParameter("gameCode");
        String roundCode = request.getParameter("roundCode");
        String ddGroup = request.getParameter("ddGroup");
        if (StringUtils.isNotBlank(times)) {
            search.put("times", times);
        }
        if (StringUtils.isNotBlank(userName)) {
            search.put("userName", userName);
        }
        if (StringUtils.isNotBlank(gameCode)) {
            search.put("gameCode", gameCode);
        }
        if (StringUtils.isNotBlank(roundCode)) {
            search.put("roundCode", roundCode);
        }
        if (StringUtils.isNotBlank(ddGroup)) {
            search.put("ddGroup", ddGroup);
        }
        if (search.size() > 0) {
            parameter.setSearchData(search.toJSONString());
        }
        return roundReceiveService.findAll(parameter);
    }

}
