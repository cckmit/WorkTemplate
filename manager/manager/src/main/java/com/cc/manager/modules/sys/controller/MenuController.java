package com.cc.manager.modules.sys.controller;

import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.GetPageParam;
import com.cc.manager.common.result.GetPageResult;
import com.cc.manager.common.result.GetResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.sys.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 菜单管理
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-23 21:19
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/menu")
public class MenuController implements BaseCrudController {

    private MenuService menuService;

    /**
     * 根据ID查询对象
     * 方法注解： @GetMapping(value = "/id/{id}")
     * 参数注解：@PathVariable
     *
     * @param id ID
     * @return 查询结果
     */
    @Override
    @GetMapping(value = "/id/{id}")
    public GetResult getObjectById(@PathVariable String id) {
        return this.menuService.getObjectById(id);
    }

    /**
     * 自定义参数查询对象
     * 方法注解：@GetMapping(value = "/getObject/{getObjectParam}")
     * 参数注解：@PathVariable
     *
     * @param getObjectParam 查询参数
     * @return 查询结果
     */
    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public GetResult getObject(@PathVariable String getObjectParam) {
        return this.menuService.getObject(getObjectParam);
    }

    /**
     * 分页查询
     * 方法注解：@GetMapping(value = "/getPage/{getPageParam}")
     * 参数注解：@PathVariable
     *
     * @param getPageParam 分页查询参数
     * @return 分页数据结果
     */
    @Override
    @GetMapping(value = "/getPage")
    public GetPageResult getPage(@PathVariable GetPageParam getPageParam) {
        return this.menuService.getPage(getPageParam);
    }

    /**
     * 更新数据接口
     * 方法注解：@PutMapping
     * 参数注解：@RequestBody
     *
     * @param requestParam 数据Json字符串
     * @return 更新结果
     */
    @Override
    @PostMapping
    public PostResult post(String requestParam) {
        return this.menuService.post(requestParam);
    }

    /**
     * 修改数据接口
     * 方法注解：@PutMapping(value = "/")
     *
     * @param requestParam 删除条件Json字符串
     * @return 删除结果
     */
    @Override
    @PutMapping
    public PostResult put(String requestParam) {
        return this.menuService.put(requestParam);
    }

    /**
     * 删除数据接口
     * 方法注解：@DeleteMapping
     * 参数注解：@RequestBody
     *
     * @param requestParam 数据Json字符串
     * @return 删除结果
     */
    @Override
    @DeleteMapping
    public PostResult delete(String requestParam) {
        return this.menuService.delete(requestParam);
    }

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

}
