package com.fish.controller;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户信息
 * UserInfoController
 *
 * @author
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;

    /**
     * 查询用户信息
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/userinfo")
    public GetResult getUserInfo(GetParameter parameter) {
        return userInfoService.findAll(parameter);
    }

}
