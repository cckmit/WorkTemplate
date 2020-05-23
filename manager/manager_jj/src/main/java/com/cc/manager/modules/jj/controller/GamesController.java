package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.common.utils.ReduceJsonUtil;
import com.cc.manager.config.BaseConfig;
import com.cc.manager.modules.jj.config.JjConfig;
import com.cc.manager.modules.jj.service.GamesService;
import com.cc.manager.modules.jj.utils.PersieServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author cf
 * @since 2020-05-08
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/jj/games")
public class GamesController implements BaseCrudController {

    private GamesService gamesService;
    private JjConfig jjConfig;
    private PersieServerUtils persieServerUtils;
    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.gamesService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.gamesService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.gamesService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        return this.gamesService.post(requestParam);
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.gamesService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.gamesService.delete(requestParam);
    }

    @Override
    public JSONArray getSelectArray(String requestParam) {
        return null;
    }
    /**
     * 刷新资源图
     *
     * @param parameter parameter
     * @return PostResult
     */
    @ResponseBody
    @PostMapping(value = "/games/flushGames")
    public PostResult getGamesResources(@RequestBody JSONObject parameter) {
        PostResult postResult = new PostResult();
        int i = gamesService.flushGamesResources(parameter);
        if (i != 0) {
            //刷新业务表结构
            postResult = this.persieServerUtils.refreshTable("games");
        } else {
            postResult.setCode(2);
            postResult.setMsg("操作失败，请联系管理员");
            return postResult;
        }
        return postResult;
    }
    @Autowired
    public void setGamesService(GamesService gamesService) {
        this.gamesService = gamesService;
    }
    @Autowired
    public void setJjConfig(JjConfig jjConfig) {
        this.jjConfig = jjConfig;
    }
    @Autowired
    public void setPersieServerUtils(PersieServerUtils persieServerUtils) {
        this.persieServerUtils = persieServerUtils;
    }

}

