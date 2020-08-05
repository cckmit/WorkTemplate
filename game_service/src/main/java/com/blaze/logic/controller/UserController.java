package com.blaze.logic.controller;

import com.alibaba.fastjson.JSONObject;
import com.blaze.common.PostResult;
import com.blaze.logic.RequestConst;
import com.blaze.logic.service.BaseUserService;
import com.blaze.logic.service.TtUserServiceImpl;
import com.blaze.logic.service.TxUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户信息处理接口，提供登录、绑定等功能
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-09 15:37
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/user")
public class UserController {

    private TtUserServiceImpl ttUserService;
    private TxUserServiceImpl txUserService;

    /**
     * 用户登录接口
     *
     * @param loginInfo 登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    public PostResult login(@RequestBody JSONObject loginInfo) {
        String platform = loginInfo.getString(RequestConst.PLATFORM);
        return this.getBaseUserService(platform).login(loginInfo);
    }


    /**
     * 绑定用户信息
     *
     * @param asyncInfo 绑定信息
     * @return 绑定结果
     */
    @PostMapping("/asyncUserInfo")
    public PostResult asyncUserInfo(@RequestBody JSONObject asyncInfo) {
        String platform = asyncInfo.getString(RequestConst.PLATFORM);
        return this.getBaseUserService(platform).asyncUserInfo(asyncInfo);
    }

    /**
     * 根据平台调用不同的Service
     *
     * @param platform 平台
     * @return 执行service
     */
    private BaseUserService getBaseUserService(String platform) {
        BaseUserService baseUserService = null;
        switch (platform) {
            case "tt":
                baseUserService = this.ttUserService;
                break;
            case "weixin":
            case "q":
                baseUserService = this.txUserService;
            case "oppo":
            default:
                break;
        }
        return baseUserService;
    }

    @Autowired
    public void setTtUserService(TtUserServiceImpl ttUserService) {
        this.ttUserService = ttUserService;
    }

    @Autowired
    public void setTxUserService(TxUserServiceImpl txUserService) {
        this.txUserService = txUserService;
    }

}
