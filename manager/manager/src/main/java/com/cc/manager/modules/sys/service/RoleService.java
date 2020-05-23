package com.cc.manager.modules.sys.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.GetPageParam;
import com.cc.manager.modules.sys.mapper.RoleMapper;
import com.cc.manager.modules.sys.entity.Role;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单 Service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-23 23:17
 */
@Service
public class RoleService extends BaseCrudService<Role, RoleMapper> {

    @Override
    protected void updateGetPageWrapper(GetPageParam getPageParam, QueryWrapper<Role> queryWrapper) {
        if (StringUtils.isNotBlank(getPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(getPageParam.getQueryData());
            String roleName = queryObject.getString("roleName");
            queryWrapper.like(StringUtils.isNotBlank(roleName), "role_name", roleName);
        }
    }

    @Override
    protected void updateInsertEntity(String requestParam, Role entity) {

    }

    @Override
    protected boolean update(String requestParam, Role entity, UpdateWrapper<Role> updateWrapper) {
        return this.updateById(entity);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<Role> deleteWrapper) {
        if (StringUtils.isNotBlank(requestParam)) {
            List<String> idList = Lists.newArrayList(StringUtils.split(","));
            // 删除时移除管理员账号角色
            idList.remove("1");
            return this.removeByIds(idList);
        }
        return false;
    }

}
