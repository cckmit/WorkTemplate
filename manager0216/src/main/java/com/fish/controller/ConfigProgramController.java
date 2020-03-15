package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.model.ConfigProgram;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.ConfigProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 合集及版本号配置
 * AppConfigController
 *
 * @author
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class ConfigProgramController {

    @Autowired
    ConfigProgramService configProgramService;

    /**
     * 查询合集跳转信息
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configProgram")
    public GetResult getConfigProgram(GetParameter parameter) {
        return configProgramService.findAll(parameter);
    }

    /**
     * 新增
     *
     * @param productInfo
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configProgram/new")
    public PostResult insertConfigProgram(@RequestBody ConfigProgram productInfo) {
        return this.configProgramService.insert(productInfo);

    }

    /**
     * 更新
     *
     * @param productInfo
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configProgram/edit")
    public PostResult modifyConfigProgram(@RequestBody ConfigProgram productInfo) {
        return this.configProgramService.updateByPrimaryKeySelective(productInfo);
    }

    /**
     * @param ddAppId 下拉数据返回
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configProgram/get")
    public ConfigProgram getConfigProgram(String ddAppId, String ddMinVer) {
        return this.configProgramService.select(ddAppId, ddMinVer);
    }

    /**
     * 删除
     *
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configProgram/delete")
    public PostResult delete(@RequestBody JSONObject jsonObject) {
        return this.configProgramService.delete(jsonObject.getString("deleteIds"),jsonObject.getString("ddMinVer"));
    }
}
