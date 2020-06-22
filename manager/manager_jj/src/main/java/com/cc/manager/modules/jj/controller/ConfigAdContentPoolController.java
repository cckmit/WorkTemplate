package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.ConfigAdContentPool;
import com.cc.manager.modules.jj.service.ConfigAdContentPoolService;
import com.cc.manager.modules.jj.utils.PersieServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cf
 * @since 2020-06-19
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/jj/configAdContentPool")
public class ConfigAdContentPoolController implements BaseCrudController {

    private ConfigAdContentPoolService configAdContentPoolService;
    private PersieServerUtils persieServerUtils;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.configAdContentPoolService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.configAdContentPoolService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.configAdContentPoolService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        PostResult postResult = this.configAdContentPoolService.post(requestParam);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_content_pool");
//        }
        return postResult;
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        PostResult postResult = this.configAdContentPoolService.put(requestParam);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_content_pool");
//        }
        return postResult;
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        PostResult postResult = this.configAdContentPoolService.delete(requestParam);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_content_pool");
//        }
        return postResult;
    }

    @Override
    @GetMapping(value = "/getSelectArray/{requestParam}")
    public JSONObject getSelectArray(@PathVariable String requestParam) {
        return this.configAdContentPoolService.getSelectArray(ConfigAdContentPool.class, requestParam);
    }

    /**
     * 修改广告合集名称
     *
     * @param requestParam 请求参数
     * @return 修改结果
     */
    @PostMapping(value = "/updateName")
    public PostResult updateName(@RequestBody String requestParam) {
        return this.configAdContentPoolService.updateName(requestParam);
    }

    @GetMapping(value = "/getAdContentTable/{id}")
    public JSONObject getAdContentTable(@PathVariable String id) {
        return this.configAdContentPoolService.getAdContentTable(id);
    }

    /**
     * 新增广告内容
     *
     * @param addAdContentObject 新增数据对象
     * @return 新增结果
     */
    @PostMapping(value = "/addAdContent")
    public PostResult addAdContent(@RequestBody JSONObject addAdContentObject) {
        PostResult postResult = this.configAdContentPoolService.addAdContent(addAdContentObject);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_content_pool");
//        }
        return postResult;
    }

    /**
     * 批量删除广告位
     *
     * @param deleteAdContentsObject 新增数据对象
     * @return 删除结果
     */
    @DeleteMapping(value = "/deleteAdContent")
    public PostResult deleteAdContent(@RequestBody JSONObject deleteAdContentsObject) {
        PostResult postResult = this.configAdContentPoolService.deleteAdContent(deleteAdContentsObject);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_content_pool");
//        }
        return postResult;
    }

    /**
     * 保存广告位序号
     *
     * @param saveAdContentOrderNumObject 数据保存对象
     * @return 删除结果
     */
    @PostMapping(value = "/saveAdContentOrderNum")
    public PostResult saveAdContentOrderNum(@RequestBody JSONObject saveAdContentOrderNumObject) {
        PostResult postResult = this.configAdContentPoolService.saveAdContentOrderNum(saveAdContentOrderNumObject);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_content_pool");
//        }
        return postResult;
    }

    @PostMapping(value = "/copyContent")
    public PostResult copyContent(@RequestBody JSONObject combinationIdObject) {
        PostResult postResult = this.configAdContentPoolService.copyContent(combinationIdObject);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_content_pool");
//        }
        return postResult;
    }

    @Autowired
    public void setConfigAdContentPoolService(ConfigAdContentPoolService configAdContentPoolService) {
        this.configAdContentPoolService = configAdContentPoolService;
    }

    @Autowired
    public void setPersieServerUtils(PersieServerUtils persieServerUtils) {
        this.persieServerUtils = persieServerUtils;
    }

}

