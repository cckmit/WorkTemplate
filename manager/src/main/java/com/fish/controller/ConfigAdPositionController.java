package com.fish.controller;

import com.fish.dao.second.model.ConfigAdContent;
import com.fish.dao.second.model.ConfigAdPosition;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.ConfigAdContentService;
import com.fish.service.ConfigAdPositionService;
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
public class ConfigAdPositionController {

    @Autowired
    ConfigAdPositionService adPositionService;

    /**
     * @param getParameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdPosition")
    public GetResult getConfigAdSource(GetParameter getParameter) {
        return this.adPositionService.findAll(getParameter);
    }

    /**
     * 新增
     *
     * @param configAdPosition
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdPosition/new")
    public PostResult newConfigAdSource(@RequestBody ConfigAdPosition configAdPosition) {
        return this.adPositionService.insert(configAdPosition);
    }

    /**
     * 更新
     *
     * @param configAdPosition
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdPosition/edit")
    public PostResult updateConfigAdSource(@RequestBody ConfigAdPosition configAdPosition) {
        return this.adPositionService.update(configAdPosition);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdPosition/delete")
    public PostResult delete(@RequestBody int id) {
        return this.adPositionService.delete(id);
    }

}
