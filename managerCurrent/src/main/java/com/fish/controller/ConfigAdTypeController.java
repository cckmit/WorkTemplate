package com.fish.controller;

import com.fish.dao.second.model.ConfigAdType;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.ConfigAdTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 广告类型
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-26 20:53
 */
@Controller
@RequestMapping(value = "/manage")
public class ConfigAdTypeController {

    @Autowired
    ConfigAdTypeService configAdTypeService;

    /**
     * @param getParameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdType")
    public GetResult getConfigAdSource(GetParameter getParameter) {
        return this.configAdTypeService.findAll(getParameter);
    }


    /**
     * @param getParameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdType/select")
    public List<ConfigAdType> getConfigAdSourceSelect(GetParameter getParameter) {
        return this.configAdTypeService.selectAllAdType(getParameter);
    }
    /**
     * 新增
     *
     * @param configAdType
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdType/new")
    public PostResult newConfigAdType(@RequestBody ConfigAdType configAdType) {
        return this.configAdTypeService.insert(configAdType);
    }

    /**
     * 更新
     *
     * @param configAdType
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdType/edit")
    public PostResult updateConfigAdSource(@RequestBody ConfigAdType configAdType) {
        return this.configAdTypeService.update(configAdType);
    }

}
