package com.cc.manager.modules.jj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.ConfigStatus;
import com.cc.manager.modules.jj.mapper.ConfigStatusMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-03 12:10
 */
@Service
public class ConfigStatusService extends BaseCrudService<ConfigStatus, ConfigStatusMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<ConfigStatus> queryWrapper) {

    }

    @Override
    protected boolean update(String requestParam, ConfigStatus entity, UpdateWrapper<ConfigStatus> updateWrapper) {
        // 只允许修改包含下列关键字配置
        List<String> allowedUpdateKeywordList = Lists.newArrayList("config", "goods");
        for (String allowedUpdateKeyword : allowedUpdateKeywordList) {
            if (StringUtils.contains(entity.getId(), allowedUpdateKeyword)) {
                return this.updateById(entity);
            }
        }
        return false;
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<ConfigStatus> deleteWrapper) {
        return false;
    }

}
