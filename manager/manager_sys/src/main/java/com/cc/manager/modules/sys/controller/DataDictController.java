package com.cc.manager.modules.sys.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.sys.entity.DataDict;
import com.cc.manager.modules.sys.service.DataDictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-03 23:07
 */
@RestController
@RequestMapping(value = "/sys/dataDict")
public class DataDictController implements BaseCrudController {

    private DataDictService dataDictService;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.dataDictService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.dataDictService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.dataDictService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        return this.dataDictService.post(requestParam);
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.dataDictService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.dataDictService.delete(requestParam);
    }

    @Override
    @GetMapping(value = "/getSelectArray/{requestParam}")
    public JSONObject getSelectArray(@PathVariable String requestParam) {
        return this.dataDictService.getSelectArray(DataDict.class, requestParam);
    }

    /**
     * 获取某个类型的数据列表，用于下拉框和赋值
     *
     * @return 数据列表
     */
    @GetMapping(value = "/getDataArrayByType")
    public JSONArray getDataArrayByType(String dataType) {
        JSONArray returnArray = new JSONArray();
        if (StringUtils.isNotBlank(dataType)) {
            List<DataDict> list = this.dataDictService.getCacheEntityList(DataDict.class);
            list.forEach(dataDict -> {
                if (StringUtils.equals(dataType, dataDict.getDataType())) {
                    JSONObject dictObject = new JSONObject();
                    dictObject.put("dataKey", dataDict.getDataKey());
                    dictObject.put("dataValue", dataDict.getDataValue());
                    returnArray.add(dictObject);
                }
            });
        }
        return returnArray;
    }

    @Autowired
    public void setDataDictService(DataDictService dataDictService) {
        this.dataDictService = dataDictService;
    }


}
