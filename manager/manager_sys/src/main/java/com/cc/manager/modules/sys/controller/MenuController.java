package com.cc.manager.modules.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.sys.entity.Menu;
import com.cc.manager.modules.sys.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 菜单管理
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-23 21:19
 */
@RestController
@RequestMapping(value = "/sys/menu")
public class MenuController implements BaseCrudController {

    private MenuService menuService;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.menuService.getObjectById(id);
    }

    @Override
    public CrudObjectResult getObject(String getObjectParam) {
        return null;
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return null;
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        return this.menuService.post(requestParam);
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.menuService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.menuService.delete(requestParam);
    }

    @Override
    @GetMapping(value = "/getSelectArray/{requestParam}")
    public JSONObject getSelectArray(@PathVariable String requestParam) {
        return this.menuService.getSelectArray(Menu.class, requestParam);
    }

    /**
     * 获取菜单表
     *
     * @return 菜单表
     */
    @GetMapping(value = "/getMenuTable")
    public JSONObject getMenuTable() {
        return this.menuService.getMenuTable();
    }

    @PutMapping(value = "/tableEdit")
    public PostResult tableEdit(@RequestBody String requestParam){
        return this.menuService.tableEdit(requestParam);
    }

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

}
