package com.fish.service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存 Service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-10 17:38
 */
public abstract class CacheService<T> {

    /**
     * 数据缓存对象
     */
    ConcurrentHashMap<Class<T>, ConcurrentHashMap<String, T>> cacheMap = new ConcurrentHashMap<>();

    /**
     * 更新整个缓存
     */
    public ConcurrentHashMap<String, T> updateAllCache(Class<T> clazz) {
        ConcurrentHashMap<String, T> map = new ConcurrentHashMap<>();
        updateAllCache(map);
        cacheMap.put(clazz, map);
        return map;
    }

    /**
     * 讲数据按照当前业务逻辑更新到缓存map中
     *
     * @param map 缓存map
     */
    abstract void updateAllCache(ConcurrentHashMap<String, T> map);

    /**
     * 更新指定缓存
     */
    public void updateCache(Class<T> clazz, String key, T value) {
        ConcurrentHashMap<String, T> map = cacheMap.get(clazz);
        if (map == null) {
            map = new ConcurrentHashMap<>();
            cacheMap.put(clazz, map);
        }
        map.put(key, value);
    }

    /**
     * 获取指定类型的全部缓存数据
     *
     * @param clazz 类型
     * @return 全部缓存数据
     */
    public ConcurrentHashMap<String, T> getAll(Class<T> clazz) {
        ConcurrentHashMap<String, T> map = cacheMap.get(clazz);
        if (map == null) {
            map = updateAllCache(clazz);
        }
        return map;
    }

    /**
     * 获取缓存对象
     *
     * @param clazz class 类型
     * @param key   缓存key
     * @return 缓存对象
     */
    public T getEntity(Class<T> clazz, String key) {
        ConcurrentHashMap<String, T> map = cacheMap.get(clazz);
        if (map == null) {
            map = updateAllCache(clazz);
        }
        if (map != null) {
            T t = map.get(key);
            if (t != null) {
                return t;
            } else {
                t = queryEntity(clazz, key);
                if (t != null) {
                    map.put(key, t);
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * 查询指定缓存
     *
     * @param clazz class 类型
     * @param key   缓存key
     * @return 查询结果
     */
    abstract T queryEntity(Class<T> clazz, String key);

    /**
     * 移除指定缓存
     *
     * @param clazz
     * @param key   缓存key
     */
    public void removeEntity(Class<T> clazz, String key) {
        ConcurrentHashMap<String, T> map = cacheMap.get(clazz);
        if (map != null) {
            map.remove(key);
        }
    }

}
