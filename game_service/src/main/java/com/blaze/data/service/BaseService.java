package com.blaze.data.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blaze.data.entity.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 数据库操作的mybatis plus封装类<br/>
 * 继承的类是拦截IService接口必须实现的方法，为避免出现在业务Service中但是并不会使用
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-09 20:35
 */
public abstract class BaseService<E extends BaseEntity<E>, M extends BaseMapper<E>> implements IService<E> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseService.class);

    /**
     * 持久层对象
     */
    protected M baseMapper;

    /**
     * 更新全部缓存默认方法，如果想修改数据存储格式，请自行修改，如果重构的数据非当前对象，请存储到 objectCacheMap
     */
    public ConcurrentHashMap<Serializable, E> updateAllCacheEntity(Class<E> clazz) {
        EntityCache<E> entityCache = new EntityCache<>();
        List<E> list = this.list();
        if (list != null) {
            for (E entity : list) {
                entityCache.entityCacheMap.put(entity.getCacheKey(), entity);
            }
        }
        EntityCacheMap.ALL_ENTITY_CACHE_MAP.put(clazz, entityCache);
        return entityCache.entityCacheMap;
    }

    /**
     * 通过缓存key获取缓存对象，注意如果存储的非当前对象，强制类型转换会有异常
     *
     * @param clazz 缓存类型
     * @param key   key
     * @return 缓存对象
     */
    public E getCacheEntity(Class<E> clazz, Serializable key) {
        return this.getCacheEntityMap(clazz).get(key);
    }

    /**
     * 获取全部缓存数据
     *
     * @param clazz 缓存类
     * @return 缓存map
     */
    @SuppressWarnings("unchecked")
    public ConcurrentHashMap<Serializable, E> getCacheEntityMap(Class<E> clazz) {
        EntityCache<E> entityCache = (EntityCache<E>) EntityCacheMap.ALL_ENTITY_CACHE_MAP.get(clazz);
        if (Objects.isNull(entityCache)) {
            return this.updateAllCacheEntity(clazz);
        }
        return entityCache.entityCacheMap;
    }

    /**
     * 更新非entity类型缓存
     *
     * @param clazz 缓存类
     * @return 缓存对象
     */
    public ConcurrentHashMap<Serializable, Object> updateAllCacheObject(Class<E> clazz) {
        EntityCache<E> entityCache = new EntityCache<>();
        return entityCache.objectCacheMap;
    }

    /**
     * 自定义缓存时，存储的可能是其它类型
     *
     * @param clazz 缓存类型
     * @param key   key
     * @return 缓存对象
     */
    public Object getCacheObject(Class<E> clazz, Serializable key) {
        return this.getCacheObjectMap(clazz).get(key);
    }

    /**
     * 获取全部缓存数据
     *
     * @param clazz 缓存类
     * @return 缓存map
     */
    @SuppressWarnings("unchecked")
    public ConcurrentHashMap<Serializable, Object> getCacheObjectMap(Class<E> clazz) {
        EntityCache<E> entityCache = (EntityCache<E>) EntityCacheMap.ALL_ENTITY_CACHE_MAP.get(clazz);
        if (Objects.isNull(entityCache)) {
            return this.updateAllCacheObject(clazz);
        }
        return entityCache.objectCacheMap;
    }

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
        return this.baseMapper.selectOne(queryWrapper);
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
        return this.baseMapper;
    }

    @Autowired
    public void setBaseMapper(M baseMapper) {
        this.baseMapper = baseMapper;
    }

}

/**
 * entity缓存存储类
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-09 21:14
 */
class EntityCacheMap<E extends BaseEntity<E>> {

    /**
     * 缓存map
     */
    static ConcurrentHashMap<Class<?>, EntityCache<?>> ALL_ENTITY_CACHE_MAP = new ConcurrentHashMap<>();

}

/**
 * 缓存对象数据
 */
class EntityCache<E extends BaseEntity<E>> {

    /**
     * Entity缓存存储map
     */
    ConcurrentHashMap<Serializable, E> entityCacheMap = new ConcurrentHashMap<>();
    /**
     * Object缓存存储map，用于存储重构的数据
     */
    ConcurrentHashMap<Serializable, Object> objectCacheMap = new ConcurrentHashMap<>();

}
