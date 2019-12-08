package com.fish.controller;

import com.fish.dao.primary.model.WxInput;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/admin/wechat")
public class WechatController
{
    @Autowired
    WechatService wechatService;

    @ResponseBody
    @PostMapping(value ={"/wxInput/new","/wxInput/edit"} )
    public PostResult wxInput(@RequestBody WxInput wxInput)
    {
        PostResult result = new PostResult();
        wechatService.saveWxInput(wxInput);
        result.setCode(200);
        result.setMsg("操作成功");
        return result;
    }

    @ResponseBody
    @GetMapping(value = "/wxInput")
    public GetResult wxInput(GetParameter parameter)
    {
        return wechatService.findWxInput(parameter);
    }

    @ResponseBody
    @GetMapping(value = "/select")
    public GetResult select(GetParameter parameter)
    {
        return wechatService.select(parameter);
    }
}
