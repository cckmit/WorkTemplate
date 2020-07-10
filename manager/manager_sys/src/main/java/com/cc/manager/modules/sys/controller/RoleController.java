package com.cc.manager.modules.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.sys.entity.Role;
import com.cc.manager.modules.sys.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 角色配置Controller
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-23 22:59
 */
@RestController
@RequestMapping(value = "/sys/role")
public class RoleController implements BaseCrudController {

    private RoleService roleService;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.roleService.getObjectById(id);
    }

    @Override
    public CrudObjectResult getObject(String getObjectParam) {
        return this.roleService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.roleService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        return this.roleService.post(requestParam);
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.roleService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.roleService.delete(requestParam);
    }

    @Override
    @GetMapping(value = "/getSelectArray/{requestParam}")
    public JSONObject getSelectArray(@PathVariable String requestParam) {
        return this.roleService.getSelectArray(Role.class, requestParam);
    }

    /**
     * 更新角色菜单ID
     *
     * @param requestParam 请求参数，包含角色ID和角色菜单ID列表
     * @return 操作结果
     */
    @PutMapping("/updateMenuIds")
    public PostResult updateMenuIds(@RequestBody String requestParam) {
        return this.roleService.updateMenuIds(requestParam);
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

}
