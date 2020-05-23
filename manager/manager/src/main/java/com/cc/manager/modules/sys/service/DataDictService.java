package com.cc.manager.modules.sys.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.GetPageParam;
import com.cc.manager.modules.sys.entity.DataDict;
import com.cc.manager.modules.sys.mapper.DataDictMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-03 21:49
 */
@Service
public class DataDictService extends BaseCrudService<DataDict, DataDictMapper> {

    /**
     * 如果getPage中的分组和排序方式不能满足您的要求，请先调用entityQueryWrapper.clear()方法，然后重写自己的wrapper
     *
     * @param getPageParam 分页请求参数
     * @param queryWrapper 查询wrapper
     */
    @Override
    protected void updateGetPageWrapper(GetPageParam getPageParam, QueryWrapper<DataDict> queryWrapper) {
        queryWrapper.orderByAsc("data_type").orderByAsc("data_sort");
        if (StringUtils.isNotBlank(getPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(getPageParam.getQueryData());
            String dataType = queryObject.getString("dataType");
            queryWrapper.like(StringUtils.isNotBlank(dataType), "data_type", dataType);
        }
    }

    /**
     * 如有需要，根据提交的数据更新插入数据库实体对象
     *
     * @param requestParam 请求参数
     * @param entity       数据对象
     */
    @Override
    protected void updateInsertEntity(String requestParam, DataDict entity) {

    }

    /**
     * 根据提交的数据更新数据对象和wrapper
     *
     * @param requestParam  提交的参数
     * @param entity        数据对象
     * @param updateWrapper 更新wrapper
     */
    @Override
    protected boolean update(String requestParam, DataDict entity, UpdateWrapper<DataDict> updateWrapper) {
        return this.updateById(entity);
    }

    /**
     * 更新删除数据Wrapper
     *
     * @param requestParam  请求的参数
     * @param deleteWrapper 删除Wrapper
     * @return 删除结果
     */
    @Override
    protected boolean delete(String requestParam, UpdateWrapper<DataDict> deleteWrapper) {
        return false;
    }

}
