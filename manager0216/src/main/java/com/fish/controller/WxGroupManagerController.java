package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.model.WxGroupManager;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.WxGroupManagerService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 微信群管理
 * WxGroupManagerController
 *
 * @author Host-0311
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class WxGroupManagerController {

    @Autowired
    WxGroupManagerService wxGroupManagerService;

    @Autowired
    BaseConfig baseConfig;

    /**
     * 查询
     *
     * @param parameter parameter
     * @return 查询结果
     */
    @ResponseBody
    @GetMapping(value = "/wxgroup")
    public GetResult getWxGroupManager(GetParameter parameter) {
        return wxGroupManagerService.findAll(parameter);
    }

    /**
     * 修改
     *
     * @param productInfo productInfo
     * @return 修改结果
     */
    @ResponseBody
    @PostMapping(value = "/wxgroup/edit")
    public PostResult modifyWxConfig(@RequestBody WxGroupManager productInfo) {
        PostResult result = new PostResult();

        // 修改数据首先把历史数据存到历史表里面
        wxGroupManagerService.insertHistoryDate(productInfo);
        // 判断是否需要更新上传二维码时间
        if (wxGroupManagerService.isUpdateOperateTime(productInfo)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String updateRqCodeTime = format.format(new Date());
            productInfo.setUpdateQrCodeTime(updateRqCodeTime);
        }
        int count = wxGroupManagerService.updateWxGroupManager(productInfo);
        if (count == 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败");
        } else {
            wxGroupManagerService.updateConfigConfirm(productInfo);
            //刷新业务表结构
            ReadJsonUtil.flushTable("config_confirm", baseConfig.getFlushCache());
        }
        return result;
    }

    @ResponseBody
    @PostMapping(value = "/wxgroup/change")
    public PostResult changeStatus(@RequestBody JSONObject jsonObject) {
        PostResult result = new PostResult();
        boolean status = jsonObject.getBoolean("ddStatus");
        Integer ddStatus = 0;
        if (status) {
            ddStatus = 1;
        }
        int count = wxGroupManagerService.changeStatus(ddStatus);
        if (count <= 0) {
            result.setSuccessed(false);
            result.setMsg("更新失败");
        }
        //刷新业务表结构
        ReadJsonUtil.flushTable("config_confirm", baseConfig.getFlushCache());
        return result;
    }
}
