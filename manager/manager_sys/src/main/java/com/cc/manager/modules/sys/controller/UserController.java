package com.cc.manager.modules.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.sys.entity.User;
import com.cc.manager.modules.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-23 21:19
 */
@RestController
@RequestMapping(value = "/sys/user")
public class UserController implements BaseCrudController {

    private UserService userService;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.userService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.userService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.userService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        return this.userService.post(requestParam);
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.userService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.userService.delete(requestParam);
    }

    @Override
    @GetMapping(value = "/getSelectArray/{requestParam}")
    public JSONObject getSelectArray(@PathVariable String requestParam) {
        return this.userService.getSelectArray(User.class, null);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
