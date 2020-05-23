package com.cc.manager.modules.jj.controller;


import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息
 *
 * @author cf
 * @since 2020-05-09
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/userInfo")
public class UserInfoController implements BaseStatsController {

    private UserInfoService userInfoService;

    @Override
    public StatsListResult getList(StatsListParam statsListParam) {
        return null;
    }

    @Override
    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return userInfoService.getPage(statsListParam);
    }

    @Autowired
    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

}

