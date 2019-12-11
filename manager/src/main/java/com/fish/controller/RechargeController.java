package com.fish.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.model.ArcadeGames;
import com.fish.dao.primary.model.CheckBoxData;
import com.fish.dao.second.model.Recharge;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.GamesService;
import com.fish.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/manage")
public class RechargeController {

    @Autowired
    RechargeService rechargeService;


    //查询展示提现情况
    @GetMapping(value = "/recharge")
    public GetResult getRecharge(GetParameter parameter) {
        return rechargeService.findAll(parameter);
    }


    //新增游戏信息
    @PostMapping(value = "/recharge/new")
    public PostResult insertRecharge(@RequestBody Recharge productInfo) {
        System.out.println("---------------------");
        PostResult result = new PostResult();
        int count = rechargeService.insert(productInfo);
        if (count == 1) {
            result.setCode(200);
            result.setMsg("操作成功");
            return result;
        } else {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
    }

    @PostMapping(value = "/recharge/delete")
    public PostResult deleteRecharge(@RequestBody Recharge productInfo) {
        PostResult result = new PostResult();
        int count = 1;
        if (count == 1) {
            result.setCode(200);
            result.setMsg("操作成功");
            return result;
        } else {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
    }

    //修改游戏信息
    @PostMapping(value = "/recharge/edit")
    public PostResult modifyRecharge(@RequestBody Recharge productInfo) {
        PostResult result = new PostResult();
        int count = rechargeService.updateByPrimaryKeySelective(productInfo);
        if (count != 0) {
            result.setCode(200);
            result.setMsg("操作成功");
            return result;
        } else {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }

    }

}
