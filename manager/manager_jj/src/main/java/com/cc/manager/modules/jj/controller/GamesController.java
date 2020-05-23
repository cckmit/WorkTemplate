package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONArray;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.Games;
import com.cc.manager.modules.jj.service.GamesService;
import com.cc.manager.modules.jj.utils.PersieServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 游戏列表
 *
 * @author cf
 * @since 2020-05-08
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/games")
public class GamesController implements BaseCrudController {

    private GamesService gamesService;

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
        PostResult postResult = this.gamesService.post(requestParam);
        if (postResult.getCode() == 1) {
            postResult = this.persieServerUtils.refreshTable("games");
        }
        return postResult;
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        PostResult putResult = this.gamesService.put(requestParam);
        if (putResult.getCode() == 1) {
            putResult = this.persieServerUtils.refreshTable("games");
        }
        return putResult;
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.gamesService.delete(requestParam);
    }

    @Override
    @GetMapping("/getSelectArray/{requestParam}")
    public JSONArray getSelectArray(@PathVariable String requestParam) {
        return this.gamesService.getSelectArray(Games.class, requestParam);
    }

    /**
     * 刷新游戏配置资源
     *
     * @param parameter parameter
     * @return PostResult
     */
    @CrossOrigin
    @PostMapping(value = "/refreshResource")
    public PostResult getGamesResources(@RequestBody JSONArray parameter) {
        PostResult postResult = gamesService.flushGamesResources(parameter);
        //刷新业务表结构
        if (postResult.getCode() == 1) {
            postResult = this.persieServerUtils.refreshTable("games");
        }
        return postResult;
    }

    @Autowired
    public void setGamesService(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    @Autowired
    public void setPersieServerUtils(PersieServerUtils persieServerUtils) {
        this.persieServerUtils = persieServerUtils;
    }

}

