package com.blaze.logic.controller;

import com.alibaba.fastjson.JSONObject;
import com.blaze.common.PostResult;
import com.blaze.logic.service.UserGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 游戏节点
 *
 * @author Sky
 */
@RestController
@RequestMapping(value = "/game")
public class GameController {

    @Autowired
    UserGameService service;

    @PostMapping("/asyncUserData")
    public PostResult asyncUserData(@RequestBody JSONObject asyncInfo) {
        return service.asyncUserData(asyncInfo);
    }
}
