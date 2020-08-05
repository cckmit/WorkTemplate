package com.blaze.logic.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blaze.logic.Constant;
import com.blaze.common.PostResult;
import com.blaze.logic.service.MyRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 排行榜控制器
 *
 * @author Sky
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/rank")
public class MyRankController {

    @Autowired
    private MyRankService myRankService;

    @GetMapping("/flush")
    public String flush(@RequestBody JSONObject data) {
        return "success";
    }

    @PostMapping("/user")
    public PostResult queryUsers(@RequestBody JSONObject userInfo) {
        PostResult postResult = new PostResult();
        JSONArray users = userInfo.getJSONArray(Constant.USERS);
        JSONArray result = myRankService.getUserInfo(users);
        if (result != null) {
            postResult.setData(result);
        }
        return postResult;
    }

    @PostMapping("/addScore")
    public PostResult addScore(@RequestBody JSONObject record) {
        PostResult postResult = new PostResult();
        JSONObject user = myRankService.addOnceRecord(record);
        //获取最新记录
        postResult.setData(user);
        return postResult;
    }

    @PostMapping("/queryMatch")
    public PostResult queryRecord(@RequestBody JSONObject search) {
        PostResult postResult = new PostResult();
        JSONObject result = myRankService.getOnceMatchRecord(search);
        postResult.setData(result);
        return postResult;
    }
}
