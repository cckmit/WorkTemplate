package com.blaze.data.service;

import com.blaze.data.entity.WxConfig;
import com.blaze.data.mapper.WxConfigMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-09 21:10
 */
@Service
public class WxConfigService extends BaseService<WxConfig, WxConfigMapper> {

    @Override
    public ConcurrentHashMap<Serializable, WxConfig> updateAllCacheEntity(Class<WxConfig> clazz) {
        EntityCache<WxConfig> entityCache = new EntityCache<>();
        List<WxConfig> list = this.list();
        if (list != null) {
            for (WxConfig entity : list) {
                if (StringUtils.isNotBlank(entity.getBanner()) && StringUtils.contains(entity.getBanner(), "-")) {
                    entity.setBanner(StringUtils.split(entity.getBanner(), "-")[1]);
                }
                if (StringUtils.isNotBlank(entity.getInit()) && StringUtils.contains(entity.getInit(), "-")) {
                    entity.setInit(StringUtils.split(entity.getInit(), "-")[1]);
                }
                entityCache.entityCacheMap.put(entity.getCacheKey(), entity);
            }
        }
        EntityCacheMap.ALL_ENTITY_CACHE_MAP.put(clazz, entityCache);
        return entityCache.entityCacheMap;
    }
}
