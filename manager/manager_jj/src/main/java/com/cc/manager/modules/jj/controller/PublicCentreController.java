package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONArray;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.service.PublicCentreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author cf
 * @since 2020-05-13
 */

@CrossOrigin
@RestController
@RequestMapping("/jj/publicCentre")
public class PublicCentreController implements BaseCrudController {

    private PublicCentreService publicCentreService;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.publicCentreService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.publicCentreService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.publicCentreService.getPage(crudPageParam);
    }

    /**
     * 修改完毕公众号中心内容提交JSON文件到客户端
     *
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/public/submitJson")
    public PostResult submitPublicCentre() {
        PostResult result = new PostResult();
        publicCentreService.selectAllForJson();
        return result;

    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        return this.publicCentreService.post(requestParam);
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.publicCentreService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.publicCentreService.delete(requestParam);
    }

    @Override
    public JSONArray getSelectArray(String requestParam) {
        return null;
    }

    @Autowired
    public void setOrdersService(PublicCentreService publicCentreService) {
        this.publicCentreService = publicCentreService;
    }
}

