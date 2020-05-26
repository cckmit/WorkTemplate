package com.cc.manager.common.mvc;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * CURD操作基础Entity接口，并提供简单实现的自动缓存功能，如果需要实现，请自定义缓存KEY
 * 同时，cacheKey和cacheValue还会兼做下拉框查询默认值
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-26 22:20
 */
public interface BaseCrudEntity<E> extends Serializable {

    /**
     * 无需缓存标识
     */
    String NO_CACHE = "NO_CACHE";

    /**
     * 如果当前Entity的数据对象或者值放入缓存，请实现此接口<br/>
     * 返回缓存的Key，可以实现公共的下拉框选项接口、radio接口、checkBox接口，数据字典等等
     *
     * @return 缓存KEY
     */
    @JSONField(serialize = false)
    default String getCacheKey() {
        return NO_CACHE;
    }

    /**
     * 如果需要返回缓存的部分属性字符串，请实现此接口<br/>
     * 返回缓存的Value，可以实现公共的下拉框选项接口、radio接口、checkBox接口，数据字典等等
     *
     * @return 缓存Value
     */
    @JSONField(serialize = false)
    default String getCacheValue() {
        return null;
    }
}
