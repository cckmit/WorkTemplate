package com.fish.controller;

import com.fish.dao.second.model.ConfigAdSource;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.ConfigAdSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 广告来源
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-26 20:37
 */
@Controller
@RequestMapping(value = "/manage")
public class ConfigAdSourceController {

    @Autowired
    ConfigAdSourceService configAdSourceService;

    /**
     * @param getParameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdSource")
    public GetResult getConfigAdSource(GetParameter getParameter) {
        return this.configAdSourceService.findAll(getParameter);
    }

    /**
     * 新增
     *
     * @param configAdSource
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdSource/new")
    public PostResult newConfigAdSource(@RequestBody ConfigAdSource configAdSource) {
        return this.configAdSourceService.insert(configAdSource);
    }

    /**
     * 更新
     *
     * @param configAdSource
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdSource/edit")
    public PostResult updateConfigAdSource(@RequestBody ConfigAdSource configAdSource) {
        return this.configAdSourceService.update(configAdSource);
    }

}
