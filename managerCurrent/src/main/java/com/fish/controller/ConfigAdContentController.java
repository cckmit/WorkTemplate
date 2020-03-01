package com.fish.controller;

import com.fish.dao.second.model.ConfigAdContent;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.ConfigAdContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * 删除
     *
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdContent/delete")
    public PostResult delete(@RequestBody int id) {
        return this.adContentService.delete(id);
    }

}
