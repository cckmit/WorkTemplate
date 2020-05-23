package com.cc.manager.common.mvc;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CRUD操作的缓存存储类
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-25 19:49
 */
class CrudEntityCacheMap<E extends BaseCrudEntity<E>> {

    /**
     * 缓存map
     */
    static ConcurrentHashMap<Class<?>, CrudEntityCache<?>> ALL_ENTITY_CACHE_MAP = new ConcurrentHashMap<>();

}

/**
 * 缓存对象数据
 */
class CrudEntityCache<E extends BaseCrudEntity<E>> {
    /**
     * 每个缓存类的缓存数据列表
     */
    ConcurrentHashMap<String, Object> entityCacheMap = new ConcurrentHashMap<>();

    /**
     * 每个缓存类的换数数据序列
     */
    Vector<String> entityKeyList = new Vector<>();
}



