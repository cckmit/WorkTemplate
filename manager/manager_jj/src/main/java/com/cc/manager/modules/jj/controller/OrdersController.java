package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.*;
import com.cc.manager.modules.jj.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 充值订单查询
 *
 * @author cf
 * @since 2020-05-13
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/orders")
public class OrdersController implements BaseStatsController {

    private OrdersService ordersService;
    @Override
    public StatsListResult getList(StatsListParam statsListParam) {
        return null;
    }


    @Override
    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return ordersService.getPage(statsListParam);
    }
    /**
     * 补发订单
     *
     * @param singleOrder singleOrder
     * @return PostResult
     */
    @ResponseBody
    @PostMapping(value = "/supplement")
    public PostResult supplementOrders(@RequestBody JSONObject singleOrder) {
        PostResult result = new PostResult();
        int count = 0;
        String appId = singleOrder.getString("appId");
        String uid = singleOrder.getString("uid");
        String orderId = singleOrder.getString("orderid");
        count = ordersService.supplementOrders(appId, uid, orderId);
        if (count == 200) {
            result.setCode(1);
            result.setMsg("操作成功");
            return result;
        } else {
            result.setCode(2);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }

    }

    @Autowired
    public void setOrdersService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }


}

