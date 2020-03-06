package com.fish.controller;

import com.fish.dao.second.model.ConfigAdStrategy;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.ConfigAdStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 广告内容管理
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-28 20:40
 */
@Controller
@RequestMapping(value = "/manage")
public class ConfigAdStrategyController {

    @Autowired
    ConfigAdStrategyService adStrategyService;

    /**
     * @param getParameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdStrategy")
    public GetResult getConfigAdSource(GetParameter getParameter) {
        return this.adStrategyService.findAll(getParameter);
    }

    /**
     * 新增
     *
     * @param configAdStrategy
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdStrategy/new")
    public PostResult newConfigAdSource(@RequestBody ConfigAdStrategy configAdStrategy) {
        return this.adStrategyService.insert(configAdStrategy);
    }

    /**
     * 更新
     *
     * @param configAdStrategy
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdStrategy/edit")
    public PostResult updateConfigAdSource(@RequestBody ConfigAdStrategy configAdStrategy) {
        return this.adStrategyService.update(configAdStrategy);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdStrategy/delete")
    public PostResult delete(@RequestBody int id) {
        return this.adStrategyService.delete(id);
    }

    /**
     * @param getParameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdStrategy/select")
    public List<ConfigAdStrategy> getConfigAdStrategySelect(GetParameter getParameter) {
        return this.adStrategyService.selectAllAdStrategy(getParameter);
    }
}
