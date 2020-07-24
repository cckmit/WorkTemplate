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
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
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

    public StatsListResult getPage(StatsListParam statsListParam) {
        StatsListResult statsListResult = new StatsListResult();
        // 判断请求参数是否为空
        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            statsListParam.setQueryObject(JSONObject.parseObject(statsListParam.getQueryData()));
        }
        if (Objects.isNull(statsListParam.getQueryObject())) {
            statsListParam.setQueryObject(new JSONObject());
        }
        try {
            // 初始化查询wrapper
            QueryWrapper<E> queryWrapper = new QueryWrapper<>();
            this.updateGetListWrapper(statsListParam, queryWrapper, statsListResult);
            Page<E> page = new Page<>(statsListParam.getPage(), statsListParam.getLimit());
            IPage<E> entityPages = this.page(page, queryWrapper);
            if (Objects.nonNull(entityPages)) {
                List<E> entityList = entityPages.getRecords();
                JSONObject totalRow = this.rebuildStatsListResult(statsListParam, entityList, statsListResult);
                statsListResult.setData(JSONArray.parseArray(JSON.toJSONString(entityList)));
                statsListResult.setTotalRow(totalRow);
                statsListResult.setCount(entityPages.getTotal());
            }
        } catch (Exception e) {
            statsListResult.setCode(1);
            statsListResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return statsListResult;
    }

    /**
     * 统计数据不分页查询
     *
     * @param statsListParam 查询请求参数
     * @return 统计数据列表查询返回值封装对象
     */
    public StatsListResult getList(StatsListParam statsListParam) {
        StatsListResult statsListResult = new StatsListResult();
        // 判断请求参数是否为空
        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            statsListParam.setQueryObject(JSONObject.parseObject(statsListParam.getQueryData()));
        }
        if (Objects.isNull(statsListParam.getQueryObject())) {
            statsListParam.setQueryObject(new JSONObject());
        }
        try {
            // 初始化查询wrapper
            QueryWrapper<E> queryWrapper = new QueryWrapper<>();
            this.updateGetListWrapper(statsListParam, queryWrapper, statsListResult);
            List<E> entityList = this.list(queryWrapper);
            if (Objects.nonNull(entityList)) {
                JSONObject totalRow = this.rebuildStatsListResult(statsListParam, entityList, statsListResult);
                int page = statsListParam.getPage();
                int rows = statsListParam.getLimit();
                List<E> sendData = new Vector<>();
                for (int i = (page - 1) * rows; i < page * rows; i++)
                {
                    if (entityList.size() > i)
                    {
                        sendData.add(entityList.get(i));
                    }
                }
                statsListResult.setData(JSONArray.parseArray(JSON.toJSONString(sendData)));
                statsListResult.setTotalRow(totalRow);
                statsListResult.setCount(entityList.size());
            }
        } catch (Exception e) {
            statsListResult.setCode(1);
            statsListResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return statsListResult;
    }

    /**
     * 更新查询列表参数
     *
     * @param statsListParam  查询参数
     * @param queryWrapper    查询wrapper
     * @param statsListResult 查询返回值封装对象
     */
    protected abstract void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<E> queryWrapper, StatsListResult statsListResult);

    /**
     * 重构查询数据，并进行数据合计
     *
     * @param statsListParam  查询参数
     * @param entityList      查询数据对象列表
     * @param statsListResult 响应结果
     * @return 合计数据
     */
    protected abstract JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<E> entityList, StatsListResult statsListResult);

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
        List<E> list = this.list(queryWrapper);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
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
