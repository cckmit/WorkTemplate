package com.blaze.data.entity;

import java.io.Serializable;

/**
 * 基础数据实体类
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-09 21:26
 */
public interface BaseEntity<E> extends Serializable {

    /**
     * 自动缓存数据的key
     * 如果当前实体类需要缓存，请实现此方法返回一个不为空的值
     *
     * @return 缓存key
     */
    Serializable getCacheKey();

}
