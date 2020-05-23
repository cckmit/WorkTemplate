package com.cc.manager.common.mvc;

import com.cc.manager.common.result.GetPageParam;
import com.cc.manager.common.result.GetPageResult;
import com.cc.manager.common.result.GetResult;
import com.cc.manager.common.result.PostResult;

/**
 * 按照Restful规则定义的CRUD模块Controller抽象类，并提供缓存查询功能</br>
 * 如果想要修改某个功能的实现，请自行Override
 * 关于如何实现自动缓存，请参考 BaseCrudEntity
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-22 20:21
 */
public interface BaseCrudController {

    /**
     * 根据ID查询对象
     * 方法注解： @GetMapping(value = "/id/{id}")
     * 参数注解：@PathVariable
     *
     * @param id ID
     * @return 查询结果
     */
    GetResult getObjectById(String id);

    /**
     * 自定义参数查询对象
     * 方法注解：@GetMapping(value = "/getObject/{getObjectParam}")
     * 参数注解：@PathVariable
     *
     * @param getObjectParam 查询参数
     * @return 查询结果
     */
    GetResult getObject(String getObjectParam);

    /**
     * 分页查询
     * 方法注解：@GetMapping(value = "/getPage/{getPageParam}")
     * 参数注解：@PathVariable
     *
     * @param getPageParam 分页查询参数
     * @return 分页数据结果
     */
    GetPageResult getPage(GetPageParam getPageParam);

    /**
     * 新增数据接口
     * 方法注解：@PostMapping
     * 参数注解：@RequestBody
     *
     * @param requestParam 数据Json字符串
     * @return 新增结果
     */
    PostResult post(String requestParam);

    /**
     * 更新数据接口
     * 方法注解：@PutMapping
     * 参数注解：@RequestBody
     *
     * @param requestParam 数据Json字符串
     * @return 更新结果
     */
    PostResult put(String requestParam);

    /**
     * 删除数据接口
     * 方法注解：@DeleteMapping
     * 参数注解：@RequestBody
     *
     * @param requestParam 数据Json字符串
     * @return 删除结果
     */
    PostResult delete(String requestParam);

}
