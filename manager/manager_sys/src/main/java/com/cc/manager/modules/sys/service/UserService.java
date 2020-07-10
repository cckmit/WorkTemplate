package com.cc.manager.modules.sys.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.sys.entity.Role;
import com.cc.manager.modules.sys.entity.User;
import com.cc.manager.modules.sys.mapper.UserMapper;
import com.cc.manager.shiro.AccountUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户操作逻辑
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-23 21:21
 */
@Service
public class UserService extends BaseCrudService<User, UserMapper> {

    private RoleService roleService;

    /**
     * 用户登录，利用用户名查询用户信息，然后比较密码是否匹配
     *
     * @param username 用户名
     * @return 登录成功用户信息
     */
    public AccountUser login(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = this.mapper.selectOne(queryWrapper);
        if (user != null) {
            return JSONObject.parseObject(JSONObject.toJSONString(user), AccountUser.class);
        }
        return null;
    }

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<User> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String username = queryObject.getString("username");
            queryWrapper.like(StringUtils.isNotBlank(username), "username", username);
            String nickName = queryObject.getString("nickName");
            queryWrapper.like(StringUtils.isNotBlank(nickName), "nickName", nickName);
        }
    }

    @Override
    protected void rebuildSelectedEntity(User entity) {
        entity.setPassword(null);
        if (StringUtils.isNotBlank(entity.getRoleIds())) {
            List<String> roleIdList = JSONObject.parseArray(entity.getRoleIds(), String.class);
            JSONArray roleNameArray = new JSONArray();
            roleIdList.forEach(roleId -> {
                roleNameArray.add(this.roleService.getCacheValue(Role.class, roleId));
            });
            entity.setRoleNames(roleNameArray.toJSONString());
        }
    }


    @Override
    protected void updateInsertEntity(String requestParam, User entity) {
        // 对密码进行加密
        entity.setPassword(DigestUtil.md5Hex(entity.getUsername() + entity.getPassword()));
    }

    @Override
    protected boolean update(String requestParam, User entity, UpdateWrapper<User> updateWrapper) {
        // 管理员总是管理员角色
        if (entity.getId() == 1) {
            entity.setRoleIds("[1]");
        }
        // 只有在密码和用户名全不为空才对密码进行加密
        if (StringUtils.isNotBlank(entity.getUsername()) && StringUtils.isNotBlank(entity.getPassword())) {
            entity.setPassword(DigestUtil.md5Hex(entity.getUsername() + entity.getPassword()));
        } else {
            entity.setPassword(null);
        }
        // 更新数据
        return this.updateById(entity);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<User> deleteWrapper) {
        if (StringUtils.isNotBlank(requestParam)) {
            List<String> idList = JSONObject.parseArray(requestParam, String.class);
            // 删除时移除管理员账号ID
            idList.remove("1");
            if (!idList.isEmpty()) {
                return this.removeByIds(idList);
            }
        }
        return false;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

}
