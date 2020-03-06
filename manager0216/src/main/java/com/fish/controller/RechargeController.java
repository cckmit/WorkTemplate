package com.fish.controller;

import com.fish.dao.second.model.Recharge;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 提现记录查询
 * RechargeController
 *
 * @author
 * @date
 */
@RestController
@RequestMapping(value = "/manage")
public class RechargeController {
    @Autowired
    RechargeService rechargeService;

    /**
     * 查询提现情况
     *
     * @param parameter
     * @return
     */
    @GetMapping(value = "/recharge")
    public GetResult getRecharge(GetParameter parameter) {
        return rechargeService.findAll(parameter);
    }

    /**
     * 审核提现接口
     *
     * @param productInfo
     * @return
     */
    @PostMapping(value = "/recharge/audit")
    public PostResult auditRecharge(@RequestBody List<Recharge> productInfo) {
        PostResult result = new PostResult();
        int cash = rechargeService.getCash(productInfo);
        if (cash == 200) {
            result.setMsg("操作成功");
            return result;
        } else {
            result.setSuccessed(false);
            result.setMsg("未完全提现成功，请联系管理员");
            return result;
        }
    }

}
