package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.model.ConfigAdPosition;
import com.fish.dao.second.model.ConfigAdSpace;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.ConfigAdPositionService;
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
public class ConfigAdPositionController {

    @Autowired
    ConfigAdPositionService adPositionService;

    /**
     * 查询
     *
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
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdPosition/delete")
    public PostResult delete(@RequestBody JSONObject jsonObject) {
        return this.adPositionService.delete(jsonObject.getString("deleteIds"));
    }

    /**
     * @param id 广告位置ID
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdPosition/get")
    public ConfigAdPosition getConfigAdSpace(int id) {
        return this.adPositionService.getConfigAdPosition(id);
    }

    /**
     * 查询广告位置列表
     *
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdPosition/selectAll")
    public List<ConfigAdPosition> select() {
        return this.adPositionService.selectAll();
    }

    /**
     * 根据广告位置ID查询广告位列表
     *
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdPosition/selectSpaceByPositionId")
    public List<ConfigAdSpace> selectSpaceByPositionId(int positionId) {
        return this.adPositionService.selectSpaceByPositionId(positionId);
    }

}
