package com.fish.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.model.ConfigAdContent;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.ConfigAdContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 广告内容管理
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-28 20:40
 */
@Controller
@RequestMapping(value = "/manage")
public class ConfigAdContentController {

    @Autowired
    ConfigAdContentService adContentService;

    /**
     * @param getParameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdContent")
    public GetResult getConfigAdSource(GetParameter getParameter) {
        return this.adContentService.findAll(getParameter);
    }

    /**
     * 新增
     *
     * @param configAdContent
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdContent/new")
    public PostResult newConfigAdSource(@RequestBody ConfigAdContent configAdContent) {
        return this.adContentService.insert(configAdContent);
    }

    /**
     * 更新
     *
     * @param configAdContent
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdContent/edit")
    public PostResult updateConfigAdSource(@RequestBody ConfigAdContent configAdContent) {
        return this.adContentService.update(configAdContent);
    }

    /**
     * 复制
     *
     * @param configAdContent
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdContent/copy")
    public PostResult copyConfigAdSource(@RequestBody ConfigAdContent configAdContent) {
        return this.adContentService.copy(configAdContent);
    }
    /**
     * 删除
     *
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdContent/delete")
    public PostResult delete(@RequestBody JSONObject jsonObject) {
        return this.adContentService.delete(jsonObject.getString("deleteIds"));
    }


    /**
     * 获取广告内容下拉框
     *
     * @param getParameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdContent/select")
    public List<ConfigAdContent> getConfigAdContentSelect(GetParameter getParameter) {
        return this.adContentService.selectAllContent(getParameter);
    }

    /**
     * @param id 广告位置ID
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdContent/get")
    public ConfigAdContent getConfigAdContent(Integer id) {
        return this.adContentService.select(id);
    }

    /**
     * 查询指定类型的广告内容
     *
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdContent/selectByType")
    public JSONArray getAdContentJsonByType(String adType) {
        return this.adContentService.getAdContentJsonByType(adType);
    }

}
