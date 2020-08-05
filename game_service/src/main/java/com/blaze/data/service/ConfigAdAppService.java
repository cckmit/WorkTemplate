package com.blaze.data.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blaze.data.entity.ConfigAdApp;
import com.blaze.data.mapper.ConfigAdAppMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-17 17:04
 */
@Service
public class ConfigAdAppService extends BaseService<ConfigAdApp, ConfigAdAppMapper> {

    /**
     * 重构缓存查询方法
     *
     * @param clazz 缓存对象
     * @return 缓存内容
     */
    @Override
    @SuppressWarnings("unchecked")
    public ConcurrentHashMap<Serializable, Object> updateAllCacheObject(Class<ConfigAdApp> clazz) {
        EntityCache<ConfigAdApp> entityCache = new EntityCache<>();
        // 当前工程只支持4.0.0以上版本
        QueryWrapper<ConfigAdApp> queryWrapper = new QueryWrapper();
        queryWrapper.ge("ddMinVersion", "4");
        List<ConfigAdApp> list = this.list(queryWrapper);
        if (list != null) {
            list.forEach(configAdApp -> {
                TreeMap<String, ConfigAdApp> map = (TreeMap<String, ConfigAdApp>) entityCache.objectCacheMap.get(configAdApp.getAppId());
                if (map == null) {
                    map = new TreeMap<>();
                }
                // default表示默认
                if (StringUtils.contains(configAdApp.getAppId(), "default")) {
                    map.put("default", configAdApp);
                } else {
                    map.put(configAdApp.getMinVersion(), configAdApp);
                }
                entityCache.objectCacheMap.put(configAdApp.getAppId(), map);
            });
        }
        EntityCacheMap.ALL_ENTITY_CACHE_MAP.put(clazz, entityCache);
        return entityCache.objectCacheMap;
    }

}
