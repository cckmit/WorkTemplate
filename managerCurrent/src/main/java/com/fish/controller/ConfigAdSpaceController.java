package com.fish.controller;

import com.fish.dao.second.model.ConfigAdContent;
import com.fish.dao.second.model.ConfigAdSpace;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.ConfigAdContentService;
import com.fish.service.ConfigAdSpaceService;
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
public class ConfigAdSpaceController {

    @Autowired
    ConfigAdSpaceService adSpaceService;

    /**
     * @param getParameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdSpace")
    public GetResult getConfigAdSource(GetParameter getParameter) {
        return this.adSpaceService.findAll(getParameter);
    }

    /**
     * 新增
     *
     * @param configAdSpace
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdSpace/new")
    public PostResult newConfigAdSource(@RequestBody ConfigAdSpace configAdSpace) {
        return this.adSpaceService.insert(configAdSpace);
    }

    /**
     * 更新
     *
     * @param configAdSpace
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdSpace/edit")
    public PostResult updateConfigAdSource(@RequestBody ConfigAdSpace configAdSpace) {
        return this.adSpaceService.update(configAdSpace);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdSpace/delete")
    public PostResult delete(@RequestBody int id) {
        return this.adSpaceService.delete(id);
    }

}
