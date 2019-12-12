package com.fish.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.WxConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/manage")
public class WxConfigController {

    @Autowired
    WxConfigService wxConfigService;

    //查询展示所有wxconfig信息
    @ResponseBody
    @GetMapping(value = "/wxconfig")
    public GetResult getWxConfig(GetParameter parameter) {
        return wxConfigService.findAll(parameter);
    }

    //新增游戏appid、secret信息
    @ResponseBody
    @PostMapping(value = "/wxconfig/new")
    public PostResult insertWxConfig(@RequestBody WxConfig productInfo) {
        PostResult result = new PostResult();
        int count = wxConfigService.insert(productInfo);
        if (count == 1) {
//           JSONObject paramMap = new JSONObject();
//            JSONObject paramMapWx = new JSONObject();
//            paramMap.put("name","wx_config");
//            String resApp= HttpUtil.post("https://sgame.qinyougames.com/persieService/flush/logic", paramMap.toJSONString());
//            paramMapWx.put("name","app_config");
//            String resWx= HttpUtil.post("https://sgame.qinyougames.com/persieService/flush/logic", paramMapWx.toJSONString());
//            System.out.println("我是res返回值 : "+resWx);
//            System.out.println("我是res返回值 : "+resApp);
            result.setCode(200);
            result.setMsg("操作成功");
            return result;
        }else if(count == 3){
            result.setCode(408);
            result.setMsg("AppId重复，操作失败");
            return result;
        } else if(count == 4){
            result.setCode(409);
            result.setMsg("产品名称重复，操作失败");
            return result;
        }else {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
    }

    //修改游戏appid、secret信息
    @ResponseBody
    @PostMapping(value = "/wxconfig/edit")
    public PostResult modifyWxConfig(@RequestBody WxConfig productInfo) {
        PostResult result = new PostResult();
        int count = wxConfigService.updateByPrimaryKeySelective(productInfo);
        if (count == 1) {
//            JSONObject paramMap = new JSONObject();
//            JSONObject paramMapWx = new JSONObject();
//            paramMap.put("name","wx_config");
//            String resApp= HttpUtil.post("https://sgame.qinyougames.com/persieService/flush/logic", paramMap.toJSONString());
//            paramMapWx.put("name","app_config");
//            String resWx= HttpUtil.post("https://sgame.qinyougames.com/persieService/flush/logic", paramMapWx.toJSONString());
//            System.out.println("我是res返回值 : "+resWx);
//            System.out.println("我是res返回值 : "+resApp);
            result.setCode(200);
            result.setMsg("操作成功");
            return result;
        }else if(count == 3){
            result.setCode(408);
            result.setMsg("AppId重复，操作失败");
            return result;
        } else if(count == 4){
            result.setCode(409);
            result.setMsg("产品名称重复，操作失败");
            return result;
        } else {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }

    }

}
