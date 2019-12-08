package com.fish.controller;

import com.fish.dao.second.model.GoodValueInfo;
import com.fish.dao.second.model.UserInfo;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.BasicUserBookService;
import com.fish.service.BasicUserPayInfoService;
import com.fish.service.BasicUserPayService;
import com.fish.service.BasicUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/basic")
public class BasicUserController
{
    @Autowired
    BasicUserService userService;
    @Autowired
    BasicUserPayService payService;
    @Autowired
    BasicUserPayInfoService payInfoService;
    @Autowired
    BasicUserBookService bookService;

    @ResponseBody
    @GetMapping(value = "/userInfo")
    public GetResult userInfo(GetParameter parameter)
    {
        return userService.findAll(parameter);
    }

    @ResponseBody
    @GetMapping(value = "/payUser")
    public GetResult payUser(GetParameter parameter)
    {
        return payService.findAll(parameter);
    }

    @ResponseBody
    @GetMapping(value = "/info")
    public UserInfo userInfo(@RequestParam(name = "userId") String userId)
    {
        return userService.selectByUserId(userId);
    }

    @ResponseBody
    @GetMapping(value = "/payInfo")
    public GetResult payInfo(GetParameter parameter)
    {
        return payInfoService.findAll(parameter);
    }
    @ResponseBody
    @GetMapping(value = "/payInfo/goodsInfo")
    public GetResult goodsInfo(@RequestParam(name = "goodsId") Integer goodsId)
    {
        GetResult result = new GetResult();
        GoodValueInfo goodValueInfo = payInfoService.selectByGoodsId(goodsId);
            List<GoodValueInfo> lists = new ArrayList<GoodValueInfo>();
        lists.add(goodValueInfo);
        result.setData(lists);
        result.setCode(200);
        result.setMsg("查询成功");
        return result;
    }

    @ResponseBody
    @GetMapping(value = "/payInfo/goods")
    public GetResult goodsAll()
    {
        GetResult<GoodValueInfo> result = new GetResult<>();
        List<GoodValueInfo> lists = payInfoService.selectAllGoods();
        result.setData(lists);
        result.setCode(200);
        result.setMsg("查询成功");
        return result;
    }

    @ResponseBody
    @GetMapping(value = "/bookInfo")
    public GetResult bookInfo(GetParameter parameter)
    {
        return bookService.findAll(parameter);
    }

}
