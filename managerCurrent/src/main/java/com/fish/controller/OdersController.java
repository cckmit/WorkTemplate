package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.model.Orders;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 充值订单
 * OdersController
 *
 * @author
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class OdersController {
    @Autowired
    OrdersService ordersService;

    //查询订单信息
    @ResponseBody
    @GetMapping(value = "/orders")
    public GetResult getOders(GetParameter parameter) {
        return ordersService.findAll(parameter);
    }

    @ResponseBody
    @PostMapping(value = "/orders/single")
    public PostResult getSingleOders(@RequestBody JSONObject singleOrder) {
        PostResult result = new PostResult();
        int count = 404;
        //JSONObject jsonObject = JSON.parseObject(singleOrder);
        String appId = singleOrder.getString("appId");
        String uid = singleOrder.getString("uid");
        String orderId = singleOrder.getString("orderid");
        count = ordersService.singleOrder(appId, uid, orderId);
        if (count == 200) {
            result.setMsg("操作成功");
            return result;
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }

    }

    //新增订单信息
    @ResponseBody
    @PostMapping(value = "/orders/new")
    public PostResult insertOders(@RequestBody Orders productInfo) {
        PostResult result = new PostResult();

        int count = ordersService.insert(productInfo);
        // count =1;
        if (count == 1) {
            result.setMsg("操作成功");
            return result;
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
    }

    //修改订单信息
    @ResponseBody
    @PostMapping(value = "/orders/edit")
    public PostResult modifyOders(@RequestBody Orders productInfo) {
        PostResult result = new PostResult();
        //  int count =1;
        int count = ordersService.updateByPrimaryKeySelective(productInfo);
        if (count != 0) {
            result.setMsg("操作成功");
            return result;
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }

    }

}
