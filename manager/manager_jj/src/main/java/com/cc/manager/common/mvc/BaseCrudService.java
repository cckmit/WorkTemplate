package com.cc.manager.common.mvc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;

/**
 * <div>CRUD操作基础类，封装了常用的功能接口</div>
 * <div>当前接口只是进行了一些简单的封装，代码非常简单，使用之前请仔细阅读，根据具体业务实现对应的抽象方法即可</div>
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-22 18:07
 */
public abstract class BaseCrudService<E extends BaseCrudEntity<E>, M extends BaseMapper<E>> implements IService<E> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseCrudService.class);

    /**
     * 持久层对象
     */
    protected M mapper;

    /**
     * 根据ID查询
     *
     * @param id id
     * @return JSONObject对象
     */
    public CrudObjectResult getObjectById(String id) {
        CrudObjectResult crudObjectResult = new CrudObjectResult();
        try {
            E entity = this.getById(id);
            if (Objects.nonNull(entity)) {
                this.rebuildSelectedEntity(entity);
                crudObjectResult.setData(JSONObject.parseObject(JSONObject.toJSONString(entity)));
            }
        } catch (Exception e) {
            crudObjectResult.setCode(2);
            crudObjectResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return crudObjectResult;
    }

    /**
     * 根据请求参数查询
     *
     * @param requestParam 请求参数
     * @return JSONObject对象
     */
    public CrudObjectResult getObject(String requestParam) {
        CrudObjectResult crudObjectResult = new CrudObjectResult();
        try {
            QueryWrapper<E> queryWrapper = new QueryWrapper<>();
            this.updateGetObjectWrapper(requestParam, queryWrapper);
            // 参考文档：https://mp.baomidou.com/guide/crud-interface.html#get
            queryWrapper.last("LIMIT 1");
            E entity = this.getOne(queryWrapper);
            if (Objects.nonNull(entity)) {
                this.rebuildSelectedEntity(entity);
                crudObjectResult.setData(JSONObject.parseObject(JSONObject.toJSONString(entity)));
            }
        } catch (Exception e) {
            crudObjectResult.setCode(2);
            crudObjectResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return crudObjectResult;
    }

    /**
     * 根据参数更新wrapper，用于查询一条数据
     *
     * @param requestParam 请求参数
     * @param queryWrapper 查询wrapper
     */
    protected void updateGetObjectWrapper(String requestParam, QueryWrapper<E> queryWrapper) {
    }

    /**
     * 重构查询结果
     *
     * @param entity 查询结果数据对象
     */
    protected void rebuildSelectedEntity(E entity) {
    }

    /**
     * 分页查询
     *
     * @param crudPageParam 分页请求参数
     */
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        CrudPageResult pageResult = new CrudPageResult();
        try {
            Page<E> page = new Page<>(crudPageParam.getPage(), crudPageParam.getLimit());
            QueryWrapper<E> queryWrapper = new QueryWrapper<>();
            // 更新查询排序条件
            if (StringUtils.isNotBlank(crudPageParam.getOrderBy())) {
                JSONObject orderByObject = JSONObject.parseObject(crudPageParam.getOrderBy());
                orderByObject.forEach((orderByColumn, orderByType) -> {
                    String typeStr = orderByType.toString();
                    if ("ASC".equalsIgnoreCase(typeStr)) {
                        queryWrapper.orderBy(true, true, orderByColumn);
                    } else if ("DESC".equalsIgnoreCase(typeStr)) {
                        queryWrapper.orderBy(true, false, orderByColumn);
                    }
                });
            }
            this.updateGetPageWrapper(crudPageParam, queryWrapper);
            IPage<E> entityPages = this.page(page, queryWrapper);
            if (Objects.nonNull(entityPages)) {
                pageResult.setCount(entityPages.getTotal());
                List<E> entityList = entityPages.getRecords();
                this.rebuildSelectedList(crudPageParam, entityList);
                pageResult.setData(JSONArray.parseArray(JSON.toJSONString(entityList)));
            }
        } catch (Exception e) {
            pageResult.setCode(1);
            pageResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return pageResult;
    }

    /**
     * 如果getPage中的分组和排序方式不能满足您的要求，请先调用entityQueryWrapper.clear()方法，然后重写自己的wrapper
     *
     * @param crudPageParam 分页请求参数
     * @param queryWrapper  查询wrapper
     */
    protected abstract void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<E> queryWrapper);

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<E> entityList) {
        entityList.forEach(this::rebuildSelectedEntity);
    }

    /**
     * 新增数据
     *
     * @param requestParam 提交的参数
     * @return 新增结果
     */
    public PostResult post(String requestParam) {
        PostResult postResult = new PostResult();
        if (StringUtils.isNotBlank(requestParam)) {
            try {
                Class<E> entityClass = this.getEntityClass();
                E entity = JSONObject.parseObject(requestParam, entityClass);
                this.updateInsertEntity(requestParam, entity);
                if (this.save(entity)) {
                    // 给需要缓存的数据更新缓存
                    this.updateCacheEntity(entity);
                } else {
                    postResult.setCode(2);
                    postResult.setMsg("新增数据失败：数据插入数据库失败！");
                }
            } catch (Exception e) {
                postResult.setCode(2);
                postResult.setMsg("新增数据失败：提交参数解析异常！");
            }
        } else {
            postResult.setCode(2);
            postResult.setMsg("新增数据失败：提交参数为空！");
        }
        return postResult;
    }

    /**
     * 如有需要，根据提交的数据更新插入数据库实体对象
     *
     * @param requestParam 请求参数
     * @param entity       数据对象
     */
    protected void updateInsertEntity(String requestParam, E entity) {
    }

    /**
     * 修改数据
     *
     * @param requestParam 提交的参数
     * @return 修改结果
     */
    public PostResult put(String requestParam) {
        PostResult postResult = new PostResult();
        if (StringUtils.isNotBlank(requestParam)) {
            try {
                Class<E> entityClass = this.getEntityClass();
                E entity = JSONObject.parseObject(requestParam, entityClass);
                UpdateWrapper<E> updateWrapper = new UpdateWrapper<>();
                if (this.update(requestParam, entity, updateWrapper)) {
                    // 给需要缓存的数据更新缓存
                    this.updateCacheEntity(entity);
                } else {
                    postResult.setCode(2);
                    postResult.setMsg("更新数据失败：数据插入数据库失败！");
                }
            } catch (Exception e) {
                postResult.setCode(2);
                postResult.setMsg("更新数据失败，参数解析异常！");
            }
        } else {
            postResult.setCode(2);
            postResult.setMsg("更新数据失败，提交参数为空！");
        }
        return postResult;
    }

    /**
     * 根据前端请求的参数更新数据，整理自己的数据，执行更新</br>
     * 更新方法请参考IService源码或者
     * https://mp.baomidou.com/guide/crud-interface.html
     *
     * @param requestParam  提交的参数
     * @param entity        数据对象
     * @param updateWrapper 更新wrapper
     * @return 数据更新结果
     */
    protected boolean update(String requestParam, E entity, UpdateWrapper<E> updateWrapper) {
        return this.updateById(entity);
    }

    /**
     * 删除数据，当前删除方法暂时只适合务理删除，如果需要逻辑删除，请通过更新逻辑处理或者自定义方法删除
     *
     * @param requestParam 根据条件删除数据
     * @return 提交结果
     */
    @SuppressWarnings("unchecked")
    public PostResult delete(String requestParam) {
        PostResult postResult = new PostResult();
        if (StringUtils.isNotBlank(requestParam)) {
            UpdateWrapper<E> deleteWrapper = new UpdateWrapper<>();
            if (this.delete(requestParam, deleteWrapper)) {
                // 如果当前数据有缓存，更新全部缓存
                Class<E> entityClass = this.getEntityClass();
                if (CrudEntityCacheMap.ALL_ENTITY_CACHE_MAP.containsKey(entityClass)) {
                    this.updateAllCacheEntity(entityClass);
                }
            } else {
                postResult.setCode(2);
                postResult.setMsg("删除数据失败：不符合删除条件，服务器阻止了删除操作！");
            }
        } else {
            postResult.setCode(2);
            postResult.setMsg("删除数据失败：提交参数为空！");
        }
        return postResult;
    }

    /**
     * <div>更新删除数据Wrapper</div>
     * <div>删除比较敏感，请自行实现删除逻辑</div>
     *
     * @param requestParam  请求的参数
     * @param deleteWrapper 删除Wrapper
     * @return 删除结果
     */
    protected abstract boolean delete(String requestParam, UpdateWrapper<E> deleteWrapper);

    /**
     * 查询下拉框选项Json数组，默认是key-value形式的Json对象，如果要自定义，请自行重写
     *
     * @param clazz        查询下拉框数据类
     * @param requestParam 过滤参数
     * @return 下拉框选项数据组
     */
    public JSONArray getSelectArray(Class<E> clazz, String requestParam) {
        JSONArray selectOptionArray = new JSONArray();
        List<E> list = this.getCacheEntityList(clazz);
        list.forEach(entity -> {
            JSONObject option = new JSONObject();
            option.put("key", entity.getCacheKey());
            option.put("value", entity.getCacheValue());
            selectOptionArray.add(option);
        });
        return selectOptionArray;
    }

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    @SuppressWarnings("unchecked")
    private Class<E> getEntityClass() {
        ParameterizedType parameterClass = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = parameterClass.getActualTypeArguments();
        return (Class<E>) actualTypeArguments[0];
    }

    //------------------------------ 以上是日常查询相关方法 ----------------------------//

    //------------------------------ 以下是缓存相关方法 ----------------------------//

    /**
     * 获取缓存对象
     *
     * @param clazz 缓存类
     * @param key   缓存key
     * @return 缓存对象
     */
    @SuppressWarnings("unchecked")
    public E getCacheEntity(Class<E> clazz, String key) {
        return (E) this.getEntityCache(clazz).entityCacheMap.get(key);
    }

    /**
     * 获取缓存值
     *
     * @param clazz 缓存类
     * @param key   缓存key
     * @return 缓存值
     */
    public String getCacheValue(Class<E> clazz, String key) {
        E entity = this.getCacheEntity(clazz, key);
        if (Objects.nonNull(entity)) {
            return entity.getCacheValue();
        }
        return null;
    }

    /**
     * 查询缓存数据列表
     *
     * @param clazz 缓存类
     * @return 缓存列表
     */
    @SuppressWarnings("unchecked")
    public List<String> getCacheValueList(Class<E> clazz) {
        CrudEntityCache<E> entityCache = getEntityCache(clazz);
        List<String> list = new ArrayList<>(Lists.newArrayListWithCapacity(entityCache.entityKeyList.size()));
        for (String entityKey : entityCache.entityKeyList) {
            E entity = (E) getEntityCache(clazz).entityCacheMap.get(entityKey);
            list.add(entity.getCacheValue());
        }
        return list;
    }

    /**
     * 查询缓存数据列表
     *
     * @param clazz 缓存类
     * @return 缓存列表
     */
    @SuppressWarnings("unchecked")
    public List<E> getCacheEntityList(Class<E> clazz) {
        CrudEntityCache<E> entityCache = getEntityCache(clazz);
        List<E> list = new ArrayList<>(Lists.newArrayListWithCapacity(entityCache.entityKeyList.size()));
        for (String entityKey : entityCache.entityKeyList) {
            list.add((E) entityCache.entityCacheMap.get(entityKey));
        }
        return list;
    }

    /**
     * 初始化对象缓存，初始化时，更新全部缓存
     */
    @SuppressWarnings("unchecked")
    private CrudEntityCache<E> getEntityCache(Class<E> clazz) {
        CrudEntityCache<E> entityCache = (CrudEntityCache<E>) CrudEntityCacheMap.ALL_ENTITY_CACHE_MAP.get(clazz);
        if (Objects.isNull(entityCache)) {
            entityCache = this.updateAllCacheEntity(clazz);
        }
        return entityCache;
    }

    /**
     * 更新自定义了缓存key的数据对象缓存
     *
     * @param entity 数据对象
     */
    @SuppressWarnings("unchecked")
    private void updateCacheEntity(E entity) {
        if (!StringUtils.equals(BaseCrudEntity.NO_CACHE, entity.getCacheKey())) {
            CrudEntityCache<E> entityCache = this.getEntityCache((Class<E>) entity.getClass());
            entityCache.entityCacheMap.put(entity.getCacheKey(), entity);
            entityCache.entityKeyList.add(entity.getCacheKey());
        }
    }

    /**
     * 更新全部缓存
     */
    private CrudEntityCache<E> updateAllCacheEntity(Class<E> clazz) {
        CrudEntityCache<E> entityCache = new CrudEntityCache<>();
        List<E> list = this.list();
        if (list != null) {
            for (E entity : list) {
                this.rebuildSelectedEntity(entity);
                entityCache.entityCacheMap.put(entity.getCacheKey(), entity);
                entityCache.entityKeyList.add(entity.getCacheKey());
            }
        }
        CrudEntityCacheMap.ALL_ENTITY_CACHE_MAP.put(clazz, entityCache);
        return entityCache;
    }

    //------------------------------ 以上是缓存相关方法 ----------------------------//

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
