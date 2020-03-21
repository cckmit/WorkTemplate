package com.fish.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.model.ConfigAdCombination;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.ConfigAdCombinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 广告组合配置Controller
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-14 21:03
 */
@Controller
@RequestMapping(value = "/manage")
public class ConfigAdCombinationController {

    @Autowired
    ConfigAdCombinationService adCombinationService;

    /**
     * @param getParameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdCombination")
    public GetResult getConfigAdSource(GetParameter getParameter) {
        return this.adCombinationService.findAll(getParameter);
    }

    /**
     * 新增
     *
     * @param configAdCombination
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdCombination/new")
    public PostResult newConfigAdSource(@RequestBody ConfigAdCombination configAdCombination) {
        return this.adCombinationService.insert(configAdCombination);
    }

    /**
     * 更新
     *
     * @param configAdCombination
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdCombination/edit")
    public PostResult updateConfigAdSource(@RequestBody ConfigAdCombination configAdCombination) {
        return this.adCombinationService.update(configAdCombination);
    }

    /**
     * 更新
     *
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdCombination/saveCombinationJson")
    public PostResult saveCombinationJson(@RequestBody JSONObject jsonObject) {
        return this.adCombinationService.saveCombinationJson(jsonObject);
    }

    /**
     * 删除
     *
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdCombination/delete")
    public PostResult delete(@RequestBody JSONObject jsonObject) {
        return this.adCombinationService.delete(jsonObject.getString("deleteIds"));
    }

    /**
     * @param getParameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdCombination/select")
    public List<ConfigAdCombination> getConfigAdCombinationSelect(GetParameter getParameter) {
        return this.adCombinationService.selectAll(getParameter);
    }

    @ResponseBody
    @GetMapping(value = "/configAdCombination/getEditJson")
    public JSONArray getEditJson(int id) {
        return this.adCombinationService.getEditJson(id);
    }

}
