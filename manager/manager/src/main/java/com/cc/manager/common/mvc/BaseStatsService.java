package com.cc.manager.common.mvc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cc.manager.common.result.GetPageParam;
import com.cc.manager.common.result.GetStatsPageResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * 统计数据查询，当前Service只实现一个主要功能：按您所需查询，分页查询数据
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-01 18:35
 */
public abstract class BaseStatsService<E extends BaseStatsEntity<E>, M extends BaseMapper<E>> implements IService<E> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseStatsService.class);

    /**
     * 持久层对象
     */
    protected M mapper;

    /**
     * 分页查询
     *
     * @param getPageParam 分页请求参数
     */
    public GetStatsPageResult getPage(GetPageParam getPageParam) {
        GetStatsPageResult statsPageResult = new GetStatsPageResult();
        try {
            Page<E> page = new Page<>(getPageParam.getPage(), getPageParam.getLimit());
            QueryWrapper<E> queryWrapper = new QueryWrapper<>();
            // 更新查询排序条件
            if (StringUtils.isNotBlank(getPageParam.getOrderBy())) {
                JSONObject orderByObject = JSONObject.parseObject(getPageParam.getOrderBy());
                orderByObject.forEach((orderByColumn, orderByType) -> {
                    String typeStr = orderByType.toString();
                    if ("ASC".equalsIgnoreCase(typeStr)) {
                        queryWrapper.orderBy(true, true, orderByColumn);
                    } else if ("DESC".equalsIgnoreCase(typeStr)) {
                        queryWrapper.orderBy(true, false, orderByColumn);
                    }
                });
            }
            this.updateGetPageWrapper(getPageParam, queryWrapper);
            IPage<E> entityPages = this.page(page, queryWrapper);
            if (Objects.nonNull(entityPages)) {
                statsPageResult.setCount(entityPages.getTotal());
                List<E> entityList = entityPages.getRecords();
                this.rebuildSelectedList(getPageParam, entityList);
                statsPageResult.setData(JSONArray.parseArray(JSON.toJSONString(entityList)));
            }
        } catch (Exception e) {
            statsPageResult.setCode(1);
            statsPageResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return statsPageResult;
    }

    /**
     * 如果getPage中的分组和排序方式不能满足您的要求，请先调用entityQueryWrapper.clear()方法，然后重写自己的wrapper
     *
     * @param getPageParam 分页请求参数
     * @param queryWrapper 查询wrapper
     */
    protected abstract void updateGetPageWrapper(GetPageParam getPageParam, QueryWrapper<E> queryWrapper);

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param getPageParam 查询参数
     * @param entityList   查询数据对象列表
     */
    protected void rebuildSelectedList(GetPageParam getPageParam, List<E> entityList) {
    }

    //--- 以下是拦截IService接口必须实现的方法，我认为有些无须实现，但是避免出现在业务Service中，如果业务需要，请自行重写 -------//

    /**
     * 批处理存储数据
     *
     * @param entityList 数据列表
     * @param batchSize  数据量
     * @return 存储结果
     */
    @Override
    public boolean saveBatch(Collection<E> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<E> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<E> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(E entity) {
        return false;
    }

    @Override
    public E getOne(Wrapper<E> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<E> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<E> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public BaseMapper<E> getBaseMapper() {
        return this.mapper;
    }

    @Autowired
    public void setMapper(M mapper) {
        this.mapper = mapper;
    }

}
