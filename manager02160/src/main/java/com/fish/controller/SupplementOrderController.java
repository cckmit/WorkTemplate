package com.fish.controller;

import com.fish.dao.primary.model.SupplementOrder;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.SupplementOrderService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 手动补单
 * SupplementOrderController
 *
 * @author
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class SupplementOrderController {

    @Autowired
    SupplementOrderService supplementOrderService;
    @Autowired
    BaseConfig baseConfig;

    /**
     * 查询补单数据
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/supplementorder")
    public GetResult getSupplementOrder(GetParameter parameter) {
        return supplementOrderService.findAll(parameter);
    }

    /**
     * 补单
     *
     * @param productInfo
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/supplementorder/new")
    public PostResult insertSupplementOrder(@RequestBody SupplementOrder productInfo) {
        PostResult result = new PostResult();
        int count = supplementOrderService.insert(productInfo);
        if (count == 1) {
            //刷新业务表结构
            String res = ReadJsonUtil.flushTable("user_value", baseConfig.getFlushCache());
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    /**
     * 查询用户当前金币
     */
    @ResponseBody
    @GetMapping(value = "/supplementorder/query")
    public SupplementOrder selectCurrentCoinByUid(@RequestParam("uid") String uid) {
        return supplementOrderService.selectCurrentCoin(uid);
    }


}
